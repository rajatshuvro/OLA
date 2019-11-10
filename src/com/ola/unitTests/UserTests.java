package com.ola.unitTests;

import java.io.*;
import java.util.ArrayList;

import com.ola.dataStructures.User;
import com.ola.databases.UserDb;
import com.ola.parsers.UserParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class UserTests {
    public static InputStream GetUsersStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("#Onkur library users\n");
        writer.write("***************************\n");
        writer.write("Name:\tSaber Nabil\n");
        writer.write("Id:\t\t234\n");
        writer.write("Role:\tTeacher\n");
        writer.write("***************************\n");
        writer.write("Name:\tRajat Shuvro Roy\n");
        writer.write("Id:\t\t678\n");
        writer.write("Role:\tVolunteer\n");
        writer.write("***************************\n");
        writer.write("Name:\tZohir Chowdhury\n");
        writer.write("Id:\t\t897\n");
        writer.write("Role:\tAdministrator\n");
        writer.write("***************************\n");
        writer.write("Name:\tTotini Tonu\n");
        writer.write("Id:\t\t564\n");
        writer.write("Role:\tStudent\n");
        writer.write("***************************\n");
        writer.write("Name:\tIshal Khan\n");
        writer.write("Id:\t\t157\n");
        writer.write("Role:\tStudent\n");
        writer.write("***************************\n");
        writer.write("Name:\tAyrah Khan\n");
        writer.write("Id:\t\t167\n");
        writer.write("Role:\tStudent\n");
        writer.write("***************************\n");
        writer.write("Name:\tNoureen Chowdhury\n");
        writer.write("Id:\t\t169\n");
        writer.write("Role:\tStudent\n");
        writer.write("***************************\n");
        writer.write("Name:\tDarayush Ahmed\n");
        writer.write("Id:\t\t456\n");
        writer.write("Role:\tStudent\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }
    @Test
    public void ParseUsersTest() throws IOException {
        var parser = new UserParser(GetUsersStream());
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
        assertNull(userDb.GetUser(999));
    }
}
