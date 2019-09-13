package com.ola;
import org.apache.commons.cli.*;
public class Main {
//https://github.com/rajatshuvro/OLA/blob/master/README.md
    public static void main(String[] args) throws Exception{
        String subCommand = GetSubCommand(args);
        Options options = new Options();
        Option input = new Option("p", "problem", true, "problem name");
        input.setRequired(true);
        options.addOption(input);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String problemName = cmd.getOptionValue("problem");
            System.out.println("Running all unit tests for the "+problemName+" problem");
            }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("LeetJ [problem name]", options);

            System.exit(1);
        }
    }

    private static String GetSubCommand(String[] args) throws Exception {
        if (args.length < 2) throw new Exception("Missing sub-command");
        return args[1];
    }
}
