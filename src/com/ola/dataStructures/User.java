package com.ola.dataStructures;

import com.ola.luceneIndex.ISearchDocument;
import com.ola.parsers.ParserUtilities;
import com.ola.utilities.FormatUtilities;

import java.util.HashSet;

public class User implements ISearchDocument {
    public final int Id;
    public final String Name;
    public final String Role;
    public final String Email;
    public final String Phone;

    private User(int id, String name, String role, String email, String phn){
        Id = id;
        Name = name;
        Role = role;
        Email = email;
        Phone = phn;
    }

    public String GetId(){
        return Integer.toString(Id);
    }
    public static User Create(int id, String name, String role, String email, String phn){
        if(!IsValid(id, name, role, email, phn)) return null;
        return new User(id, name, role, email, phn);
    }

    private static boolean IsValid(int id, String name, String role, String email, String phn) {
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

    @Override
    public String toString(){
        return    "Id:       "+ Id +
                "\nName:     "+ Name +
                "\nRole:     "+ Role +
                "\nEmail:    "+ Email+
                "\nPhone:    "+ Phone;
    }

    public String GetContent(){
        return   Id +"\n"+ Name +"\n"+ Role +"\n"+ Email+"\n"+ Phone;
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
