package com.ola;
import com.ola.dataStructures.Book;
import com.ola.dataStructures.User;
import com.ola.databases.BookDb;
import com.ola.databases.UserDb;
import com.ola.parsers.BookParser;
import com.ola.parsers.UserCsvParser;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Add {
    private static String commandSyntex = "add  -b [books CSV file] -u [users CSV file]";
    public static void Run(String[] args, BookDb bookDb, UserDb userDb){
        Options options = new Options();

        Option bookOption = new Option("b", "book", true, "file with book details");
        bookOption.setRequired(false);
        options.addOption(bookOption);

        Option userOption = new Option("u", "user", true, "file with user details");
        userOption.setRequired(false);
        options.addOption(userOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("book")){
                var filePath = cmd.getOptionValue("book");
                InputStream stream = new FileInputStream(filePath);
                var bookParser = new BookParser(stream);
                var count = AddNewBook(bookParser.GetBooks(), bookDb);

                System.out.println("Number of new books added: "+count);
            }
            if(cmd.hasOption("user")){
                var filePath = cmd.getOptionValue("book");
                InputStream stream = new FileInputStream(filePath);
                var userParser = new UserCsvParser(stream);
                var count = AddNewUsers(userParser.GetUsers(), userDb);

                System.out.println("Number of new books added: "+count);
            }

        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }

    private static int AddNewUsers(ArrayList<User> users, UserDb userDb) {
        var count =0;
        for(var user: users){
            var id = userDb.AddNewUser(user.Name, user.Role, user.Email, user.Phone);
            if(id != -1) {
                System.out.println(user.Name +" was added to the user database. Assigned Id: "+id);
                count++;
            }
            else System.out.println("Failed to add new user "+user.Name);
        }
        return count;
    }

    public static int AddNewBook(ArrayList<Book> books, BookDb bookDb) throws IOException {
        for (Book book: books) {
            var displayId = bookDb.AddNew(book);
            System.out.println("New book added: "+displayId);
        }
        return books.size();
    }
}
