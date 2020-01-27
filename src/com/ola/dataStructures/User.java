package com.ola.dataStructures;

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
        return id > 0 &&
                name != null &&
                IsValidRole(role) &&
                FormatUtilities.IsValidEmail(email) &&
                FormatUtilities.IsValidPhoneNumber(phn);
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
