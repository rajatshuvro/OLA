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

        var record =fobParser.GetNextRecord();
        while ( record != null){
            var user = GetUser(record);
            if (user != null)  users.add(user);

            record = fobParser.GetNextRecord();
        }
        fobParser.close();

        return users;
    }

    private User GetUser(HashMap<String, String> record){
        String id    = record.get(IdTag);
        String name  = record.get(NameTag);
        String role  = record.get(RoleTag);
        String email = record.get(EmailTag);
        String phnNo = record.get(PhoneTag);

        return User.Create(id, name, role, email, phnNo);
    }


}
