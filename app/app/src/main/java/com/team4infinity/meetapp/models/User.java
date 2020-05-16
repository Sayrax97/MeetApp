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
    public ArrayList<String> bookmarkedEventsID;
    public ArrayList<String> ratedEventsID;
    public ArrayList<String> visitedEventsID;
    public ArrayList<String> createdEventsID;
    public String FullName(){
        return firstName+" "+lastName;
    }
}
