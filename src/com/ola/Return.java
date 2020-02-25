package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.databases.TransactionDb;
import com.ola.utilities.PrintUtilities;
import com.ola.utilities.TimeUtilities;
import org.apache.commons.cli.*;

import java.io.IOException;

public class Return {
    private static String commandSyntex = "ret  -b [book id]";
    public static void Run(String[] args, TransactionDb transactionDb){
        Options options = new Options();

        Option bookIdOption = new Option("b", "book", true, "book id");
        bookIdOption.setRequired(true);
        options.addOption(bookIdOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            var bookId = cmd.getOptionValue('b');
            if(transactionDb.Return(bookId)){
                PrintUtilities.PrintSuccessLine(bookId +" has been returned.");
            }
            else PrintUtilities.PrintWarningLine("Book return unsuccessful");
        }
        catch (ParseException | IOException e) {
            PrintUtilities.PrintLine(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }
}
