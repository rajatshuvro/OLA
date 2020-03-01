package com.ola;

import com.ola.utilities.PrintUtilities;
import org.apache.commons.cli.*;

import java.io.IOException;

public class AddUser
{
    private static String commandSyntex = "add-user  -n [Name] -r [Role] -e [Email] -p [Phone]";
    public static void Run(String[] args, DataProvider dataProvider){
        var name = GetName(args);
        Options options = new Options();

        Option nameOption = new Option("n", "name", true, "user name");
        nameOption.setRequired(true);
        options.addOption(nameOption);

        Option roleOption = new Option("r", "role", true, "user role (Teacher/Student/Volunteer/Administrator/Citizen)");
        roleOption.setRequired(true);
        options.addOption(roleOption);

        Option emailOption = new Option("e", "email", true, "valid email address");
        emailOption.setRequired(true);
        options.addOption(emailOption);

        Option phoneOption = new Option("p", "phone", true, "valid phone number");
        phoneOption.setRequired(true);
        options.addOption(phoneOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            var role = cmd.getOptionValue("role");
            var email = cmd.getOptionValue("email");
            var phone = cmd.getOptionValue("phone");

            var id = dataProvider.AddNewUser(name, role, email, phone);
            if(id != -1)
                PrintUtilities.PrintSuccessLine(name+" was added to the user database. Assigned Id: "+id);
            else PrintUtilities.PrintErrorLine("Failed to add new user.");

        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }

    //name componets will be split into components. Those need to be stitched back
    private static String GetName(String[] args) {
        var nameBuilder = new StringBuilder();
        int i=0;
        while (i < args.length ){
            if(args[i].equals("-n") || args[i].equals("--name")) break;
            i++;
        }

        i++;
        if (i == args.length) return null;

        while (i < args.length){
            if(args[i].startsWith("-")) break;
            if(nameBuilder.length()>0) nameBuilder.append(' ');
            nameBuilder.append(args[i++]);
        }
        return nameBuilder.toString();
    }
}
