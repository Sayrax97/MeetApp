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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public void setSpecialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public ArrayList<String> getGalleryURIs() {
        return galleryURIs;
    }

    public void setGalleryURIs(ArrayList<String> galleryURIs) {
        this.galleryURIs = galleryURIs;
    }

    public ArrayList<String> getAttendeesID() {
        return attendeesID;
    }

    public void setAttendeesID(ArrayList<String> attendeesID) {
        this.attendeesID = attendeesID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
