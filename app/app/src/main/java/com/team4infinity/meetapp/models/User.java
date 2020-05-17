package com.team4infinity.meetapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
@IgnoreExtraProperties
public class User {
    @Exclude
    public String uID;
    public String email;
    @Exclude
    public String password;
    public String firstName;
    public String lastName;
    public String gender;
    public String birthDate;
    public ArrayList<String> bookmarkedEventsID=new ArrayList<>();
    public ArrayList<String> ratedEventsID=new ArrayList<>();
    public ArrayList<String> visitedEventsID=new ArrayList<>();
    public ArrayList<String> createdEventsID=new ArrayList<>();
    public String FullName(){
        return firstName+" "+lastName;
    }

    public User() {
    }

    public User(String uID, String email, String password, String firstName, String lastName, String gender, String birthDate) {
        this.uID = uID;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
    }
}
