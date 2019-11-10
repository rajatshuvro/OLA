package com.ola.dataStructures;

import java.util.HashSet;

public class User {
    public final int Id;
    public final String Name;
    public final String Role;

    public User(int id, String name, String role){
        Id = id;
        Name = name;
        Role = role;
    }

    public static boolean IsValid(int id, String name, String role) {
        return id > 0 &&
                name != null &&
                IsValidRole(role);
    }

    public String toString(){
        return "Id:"+Integer.toString(Id) + "Name:"+ Name + "Role:" + Role;
    }

    //static fields
    public static final String StudentRoleTag = "Student";
    public static final String VolunteerRoleTag = "Volunteer";
    public static final String TeacherRoleTag = "Teacher";
    public static final String AdminRoleTag = "Administrator";

    public static final HashSet<String> RoleTags = new HashSet<>(){{
        add(StudentRoleTag);
        add(VolunteerRoleTag);
        add(TeacherRoleTag);
        add(AdminRoleTag);
    }};
    private static boolean IsValidRole(String role) {
        return RoleTags.contains(role);
    }
}
