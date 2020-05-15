package com.team4infinity.meetapp;

import com.team4infinity.meetapp.models.Category;
import com.team4infinity.meetapp.models.Event;


import java.util.ArrayList;

public class Categories {
    ArrayList<Category> categories;

    public Categories() {
        categories=new ArrayList<Category>();

        Category c1=new Category();
        c1.name="jebemliga";
        Category c2=new Category();
        c1.name="jebemliga1";
        Category c3=new Category();
        c1.name="jebemliga2";

        categories.add(c1);
        categories.add(c2);
        categories.add(c3);
    }

    private static class ChamberOfSecrets{
        public static final Categories instance= new Categories();
    }
    public static Categories getInstance() {
        return ChamberOfSecrets.instance;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

}
