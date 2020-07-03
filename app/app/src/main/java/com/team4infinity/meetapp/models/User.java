package com.team4infinity.meetapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
@IgnoreExtraProperties
public class User implements Serializable {
    @Exclude
    public String uID;
    public String email;
    @Exclude
    public String password;
    public String firstName;
    public String lastName;
    public String gender;
    public String birthDate;
    public double locLat;
    public double locLon;
    public double points;
    public ArrayList<String> bookmarkedEventsID=new ArrayList<>();
    public ArrayList<String> ratedEventsID=new ArrayList<>();
    public ArrayList<String> attendedEventsID =new ArrayList<>();
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

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public double getLocLat() {
        return locLat;
    }

    public void setLocLat(double locLat) {
        this.locLat = locLat;
    }

    public double getLocLon() {
        return locLon;
    }

    public void setLocLon(double locLon) {
        this.locLon = locLon;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public ArrayList<String> getBookmarkedEventsID() {
        return bookmarkedEventsID;
    }

    public void setBookmarkedEventsID(ArrayList<String> bookmarkedEventsID) {
        this.bookmarkedEventsID = bookmarkedEventsID;
    }

    public ArrayList<String> getRatedEventsID() {
        return ratedEventsID;
    }

    public void setRatedEventsID(ArrayList<String> ratedEventsID) {
        this.ratedEventsID = ratedEventsID;
    }

    public ArrayList<String> getAttendedEventsID() {
        return attendedEventsID;
    }

    public void setAttendedEventsID(ArrayList<String> attendedEventsID) {
        this.attendedEventsID = attendedEventsID;
    }

    public ArrayList<String> getCreatedEventsID() {
        return createdEventsID;
    }

    public void setCreatedEventsID(ArrayList<String> createdEventsID) {
        this.createdEventsID = createdEventsID;
    }

    @Override
    public String toString() {
        return "User{" +
                "uID='" + uID + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", locLat=" + locLat +
                ", locLon=" + locLon +
                ", points=" + points +
                ", bookmarkedEventsID=" + bookmarkedEventsID +
                ", ratedEventsID=" + ratedEventsID +
                ", attendedEventsID=" + attendedEventsID +
                ", createdEventsID=" + createdEventsID +
                '}';
    }
}
