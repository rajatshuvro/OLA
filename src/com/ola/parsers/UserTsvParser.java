package com.ola.parsers;

import com.ola.dataStructures.User;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.io.InputStream;
import java.util.Arrays;

public class UserTsvParser {
    private InputStream _inputStream;
    private int IdIndex = -1;
    private int NameIndex = -1;
    private int RoleIndex = -1;

    private final String IdTag = "Id";
    private final String NameTag = "Name";
    private final String RoleTag = "Role";

    public UserTsvParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    //Id	Name	Role
    //234	Saber Nabil	Teacher
    private void SetColumnIndices(String headerLine){
        var splits = headerLine.split("\t");
        var splitList = Arrays.asList(splits);

        IdIndex = splitList.indexOf(IdTag);
        NameIndex = splitList.indexOf(NameTag);
        RoleIndex = splitList.indexOf(RoleTag);
    }

    private User GetUser(String line){
        var splits = line.split("\t");

        var id = Integer.parseInt(splits[IdIndex]);
        var name = splits[NameIndex];
        var role = splits[RoleIndex];

        if (!IsValidRole(role)) throw new DataValidationException("Invalid role observed in line:\n"+ line);
        return new User(id, name, role);
    }

    private boolean IsValidRole(String role) {
        return role.equals("Student")
                || role.equals("Teacher")
                || role.equals("Volunteer")
                || role.equals("Administrator");
    }
}
