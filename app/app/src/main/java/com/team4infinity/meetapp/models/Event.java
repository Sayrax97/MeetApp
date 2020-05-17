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
    public ArrayList<String> galleryURIs=new ArrayList<>();
    public ArrayList<String> attendeesID=new ArrayList<>();
    public String category;

    public Event() {
    }

    public Event(String title, String description, String address, String dateTime, String specialRequirement, double lat, double lon, double rating, double price, int maxOccupancy) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.dateTime = dateTime;
        this.specialRequirement = specialRequirement;
        this.lat = lat;
        this.lon = lon;
        this.rating = rating;
        this.price = price;
        this.maxOccupancy = maxOccupancy;
    }
}
