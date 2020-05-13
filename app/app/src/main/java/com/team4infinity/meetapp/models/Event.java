package com.team4infinity.meetapp.models;

import java.util.ArrayList;

public class Event {
    public String title;
    public String description;
    public String address;
    public String dateTime;
    public String specialRequirement;
    public double lat;
    public double lon;
    public double rating;
    public double price;
    public int maxOccupancy;
    public ArrayList<String> galleryURIs;
    public ArrayList<String> attendeesID;
    public ArrayList<Integer> categoriesID;
}
