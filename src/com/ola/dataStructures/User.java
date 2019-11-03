package com.ola.dataStructures;

public class User {
    public static final String StudentRoleTag = "Student";
    public static final String VolunteerRoleTag = "Volunteer";
    public static final String TeacherRoleTag = "Teacher";
    public static final String AdminRoleTag = "Administrator";

    public final int Id;
    public final String Name;
    public final String Role;

    public User(int id, String name, String role){
        Id = id;
        Name = name;
        Role = role;
    }

    public String toString(){
        return "Id:"+Integer.toString(Id) + "Name:"+ Name + "Role:" + Role;
    }
}
