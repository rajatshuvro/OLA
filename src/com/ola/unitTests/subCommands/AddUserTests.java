package com.ola.unitTests.subCommands;
import com.ola.AddUser;
import com.ola.Appender;
import com.ola.DataProvider;
import com.ola.dataStructures.User;
import com.ola.databases.UserDb;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AddUserTests {
    private ArrayList<User> GetUsers(){
        var users= new ArrayList<User>();
        users.add(User.Create("123", "Radia Khondokar","Student", "radia.khon@onkur.com", "858-283-0192"));
        users.add(User.Create("234", "Titu Mitra","Student", "titu.mitra@onkur.com", "858-283-8192"));
        users.add(User.Create("345", "Nandan Das","Citizen", "nandan.das@onkur.com", "858-293-8192"));
        users.add(User.Create("456", "Bhobo Ghure","Pagol", "akasher@thikana.ear", "858-93-8192"));
        return users;
    }
    @Test
    public void AddUser(){
        var userDb = new UserDb(GetUsers());
        var appender = new Appender(null, new ByteArrayOutputStream(), null);
        var dataProvider = new DataProvider(null, userDb, null,appender);

        AddUser.Run(new String[]{"add-user", "-n","Shawroth","Shuvro", "-r", "Student", "-e", "shawroth.shuvro@onkur.com", "-p", "858-666-7242"}, dataProvider);
        var user = userDb.GetUser("shawroth.shuvro");
        assertNotNull(user);
        assertEquals(User.StudentRoleTag,user.Role);
    }
}
