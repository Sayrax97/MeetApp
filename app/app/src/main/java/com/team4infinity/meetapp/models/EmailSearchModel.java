package com.team4infinity.meetapp.models;

import ir.mirrajabi.searchdialog.core.Searchable;

public class EmailSearchModel implements Searchable {
    private String mTitle;


    public EmailSearchModel(String mTitle) {
        this.mTitle = mTitle;

    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }


}
