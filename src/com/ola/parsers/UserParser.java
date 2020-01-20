package com.ola.parsers;

import com.ola.dataStructures.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static com.ola.parsers.ParserUtilities.GetNextRecordLines;

public class UserParser {
    private InputStream _inputStream;

    private final String IdTag = "Id";
    private final String NameTag = "Name";
    private final String RoleTag = "Role";
    private final String EmailTag = "Email";
    private final String PhoneTag = "Phone";

    public UserParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<User> GetUsers() throws IOException {
        ArrayList<User> users = new ArrayList<>();

        try (Scanner scanner =  new Scanner(_inputStream)){
            while (scanner.hasNextLine()){
                String[] lines = GetNextRecordLines(scanner,"*");
                if(lines.length == 0) continue;
                var user = GetUser(lines);

                if(user == null) {
                    System.out.println("Invalid User record:");
                    for (var line : lines) {
                        System.out.println(line);
                    }
                    continue;
                }
                users.add(user);

            }
        }
        return users;
    }

    private User GetUser(String[] lines){
        int id = 0;
        String name = null;
        String role = null;
        String email = null;
        String phnNo = null;
        for (String line: lines) {
            var splits = line.split(":",2);
            var key = splits[0].strip();
            var value = splits[1].strip();

            switch (key){
                case IdTag:
                    id = Integer.parseInt(value);
                    break;
                case NameTag:
                    name = value;
                    break;
                case RoleTag:
                    role = value;
                    break;
                case EmailTag:
                    email = value;
                    break;
                case PhoneTag:
                    phnNo = value;
                    break;
            }
        }

        if (!User.IsValid(id, name, role, email, phnNo)) {

            return null;
        };
        return new User(id, name, role, email, phnNo);
    }


}
