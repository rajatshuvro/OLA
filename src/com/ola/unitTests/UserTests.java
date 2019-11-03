package com.ola.unitTests;

import java.io.*;
import java.util.ArrayList;

import com.ola.dataStructures.User;
import com.ola.databases.UserDb;
import com.ola.parsers.UserTsvParser;
import org.junit.jupiter.api.Test;

import javax.print.MultiDocPrintService;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTests {
    public static InputStream GetUsersStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("Id\tName\tRole\n");
        writer.write("234\tSaber Nabil\tTeacher\n");
        writer.write("678\tRajat Shuvro\tVolunteer\n");
        writer.write("897\tZohir Chowdhury\tAdministrator\n");
        writer.write("564\tTotini Tonu\tStudent\n");
        writer.write("157\tIshal Khan\tStudent\n");
        writer.write("167\tAyrah Khan\tStudent\n");
        writer.write("169\tNoureen Chowdhury\tStudent\n");
        writer.write("456\tDarayush Ahmed\tStudent\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }
    @Test
    public void ParseUsersTest() throws IOException {
        var parser = new UserTsvParser(GetUsersStream());
        var users = parser.GetUsers();

        assertEquals(8, users.size());
    }

    @Test
    public void CreateUserDbTest(){
        var users = new ArrayList<User>();
        users.add(new User(123, "Totini", User.StudentRoleTag));
        users.add(new User(234, "Nabil", User.TeacherRoleTag));
        users.add(new User(345, "Rajat", User.VolunteerRoleTag));
        users.add(new User(456, "Zohir", User.AdminRoleTag));

        var userDb = new UserDb(users);
        assertEquals(4,userDb.size());

        assertEquals("Totini", userDb.GetUser(123).Name);
    }
}
