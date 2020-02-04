package com.ola.parsers;

import com.ola.dataStructures.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
        var fobParser = new FlatObjectParser(_inputStream, new String[]{
               IdTag, NameTag, RoleTag, EmailTag, PhoneTag
        });

        var nextSetOfValues =fobParser.GetNextRecord();
        while ( nextSetOfValues != null){
            var user = GetUser(nextSetOfValues);
            if (user != null)  users.add(user);

            nextSetOfValues = fobParser.GetNextRecord();
        }
        fobParser.close();

        return users;
    }

    private User GetUser(HashMap<String, String> keyValues){
        int id = 0;
        String name = null;
        String role = null;
        String email = null;
        String phnNo = null;
        for (var entry: keyValues.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

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
