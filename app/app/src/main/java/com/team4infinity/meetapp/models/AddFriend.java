package com.team4infinity.meetapp.models;

public class AddFriend {
    String full_name;
    String receiver_key;

    public AddFriend(String full_name, String receiver_key) {
        this.full_name = full_name;
        this.receiver_key = receiver_key;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getReceiver_key() {
        return receiver_key;
    }

    public void setReceiver_key(String receiver_key) {
        this.receiver_key = receiver_key;
    }
}
