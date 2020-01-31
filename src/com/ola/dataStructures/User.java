package com.ola.dataStructures;

import com.ola.parsers.ParserUtilities;
import com.ola.utilities.FormatUtilities;

import java.util.HashSet;

public class User {
    public final int Id;
    public final String Name;
    public final String Role;
    public final String Email;
    public final String Phone;

    public User(int id, String name, String role, String email, String phn){
        Id = id;
        Name = name;
        Role = role;
        Email = email;
        Phone = phn;
    }
    public boolean IsValid(){
        return IsValid(Id, Name, Role, Email, Phone);
    }

    public static boolean IsValid(int id, String name, String role, String email, String phn) {
        if(ParserUtilities.IsNullOrEmpty(name)){
            System.out.println("User name cannot be empty");
            return false;
        }
        if(!IsValidRole(role)){
            System.out.println("Role has to be one of:"+ String.join("/",RoleTags));
            return false;
        }
        if(!FormatUtilities.IsValidEmail(email)){
            System.out.println("Please provide a valid email address. e.g. user@onkur.com");
            return false;
        }
        if(!FormatUtilities.IsValidPhoneNumber(phn)){
            System.out.println("Please provide a valid phone number. e.g. XXX-XXX-XXXX");
            return false;
        }
        return id > 0;
    }

    public String toString(){
        return    "Id:       "+ Id +
                "\nName:     "+ Name +
                "\nRole:     "+ Role +
                "\nEmail:    "+ Email+
                "\nPhone:    "+ Phone;
    }

    //static fields
    public static final String StudentRoleTag = "Student";
    public static final String CitizenRoleTag = "Citizen";
    public static final String VolunteerRoleTag = "Volunteer";
    public static final String TeacherRoleTag = "Teacher";
    public static final String AdminRoleTag = "Administrator";

    public static final HashSet<String> RoleTags = new HashSet<>(){{
        add(StudentRoleTag);
        add(CitizenRoleTag);
        add(VolunteerRoleTag);
        add(TeacherRoleTag);
        add(AdminRoleTag);
    }};
    private static boolean IsValidRole(String role) {
        return RoleTags.contains(role);
    }


}
