package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.databases.TransactionDb;
import com.ola.parsers.ReturnCsvParser;
import com.ola.utilities.FileUtilities;
import com.ola.utilities.PrintUtilities;
import com.ola.utilities.TimeUtilities;
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
            for (var bookId: csvParser.GetReturnedBookIds()) {
                if(transactionDb.Return(bookId)){
                    PrintUtilities.PrintSuccessLine(bookId +" has been returned.");
                }
                else PrintUtilities.PrintWarningLine("Book return unsuccessful");
                if(checkoutDb.Return(bookId)){
                    PrintUtilities.PrintSuccessLine(bookId +" removed from checkouts");
                }
                else PrintUtilities.PrintWarningLine(bookId +" unable to remove from checkouts");
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
