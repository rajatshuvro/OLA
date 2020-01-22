package com.ola.unitTests.databases;

import java.io.*;
import java.util.ArrayList;

import com.ola.dataStructures.User;
import com.ola.databases.UserDb;
import com.ola.parsers.ParserUtilities;
import com.ola.parsers.UserParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class UserDbTests {
    @Test
    public void ParseUsersTest() throws IOException {
        var parser = new UserParser(TestStreams.GetUsersStream());
        var users = parser.GetUsers();

        assertEquals(8, users.size());
    }

    @Test
    public void CreateUserDbTest(){
        var users = new ArrayList<User>();
        users.add(new User(123, "Totini", User.StudentRoleTag, "totini.tonu@onkur.com", "732-666-7242"));
        users.add(new User(234, "Nabil", User.TeacherRoleTag, "saber.nabil@onkur.com", "858-345-1234"));
        users.add(new User(345, "Rajat", User.VolunteerRoleTag, "rajat.shuvro@onkur.com", "(732) 666-7242"));
        users.add(new User(456, "Zohir", User.AdminRoleTag, "zohir.choudhury@onkur.com","987-145-3456"));

        var userDb = new UserDb(users);
        assertEquals(4,userDb.size());

        assertEquals("Totini", userDb.GetUser(123).Name);
        assertNull(userDb.GetUser(999));
    }
}
