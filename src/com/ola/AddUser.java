package com.ola;

import com.ola.databases.UserDb;
import org.apache.commons.cli.*;

public class AddUser
{
    private static String commandSyntex = "add-user  -n [Name] -r [Role] -e [Email] -p [Phone]";
    public static void Run(String[] args, UserDb userDb){
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
            var name = cmd.getOptionValue("name");
            var role = cmd.getOptionValue("role");
            var email = cmd.getOptionValue("email");
            var phone = cmd.getOptionValue("phone");

            int id = userDb.AddNewUser(name, role, email, phone);
            if(id != -1) System.out.println("New user added. Id: "+id);
            else System.out.println("Failed to add new user.");
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }
}
