package com.ola;

import com.ola.databases.IdDb;
import com.ola.parsers.ReturnCsvParser;
import com.ola.utilities.FileUtilities;
import com.ola.utilities.PrintUtilities;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Return {
    private static String commandSyntex = "ret  -f [csv file path]";
    public static void Run(String[] args, DataProvider dataProvider, String checkoutFilePath){
        Options options = new Options();

        Option filePathOption = new Option("f", "file", true, "file with checkout details");
        filePathOption.setRequired(true);
        options.addOption(filePathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return;
        }
        var transactionDb = dataProvider.TransactionDb;
        var checkoutDb = dataProvider.CheckoutDb;

        try {
            cmd = parser.parse(options, args);
            var filePath = cmd.getOptionValue('f');
            if(!FileUtilities.Exists(filePath)){
                System.out.println("Specified file does not exist: "+filePath);
                return;
            }
            InputStream stream = new FileInputStream(filePath);
            var csvParser = new ReturnCsvParser(stream);
            for (var record: csvParser.GetReturnes()) {
                record = new com.ola.dataStructures.Return(record.BookId, record.DateTime);
                if(transactionDb.Return(record)){
                    PrintUtilities.PrintSuccessLine(record.BookId +" has been returned.");
                }
                else PrintUtilities.PrintWarningLine("Book return unsuccessful");
                if(checkoutDb.Return(record)){
                    PrintUtilities.PrintSuccessLine(record.BookId +" removed from checkouts");
                }
                else PrintUtilities.PrintWarningLine(record.BookId +" unable to remove from checkouts");
            }
            
            if(checkoutDb.HasReturns() && FileUtilities.Exists(checkoutFilePath)){
                var rewriteStream = new FileOutputStream(checkoutFilePath);
                checkoutDb.WriteReturns(rewriteStream, false);
            }
            else{
                PrintUtilities.PrintErrorLine("Unable to find checkout file. Returns were not recorded.");
            }

        }
        catch (ParseException | IOException e) {
            PrintUtilities.PrintLine(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }
}
