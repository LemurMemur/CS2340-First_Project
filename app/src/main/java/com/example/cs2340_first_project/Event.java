package com.example.cs2340_first_project;

import java.util.ArrayList;

public class Event {

    public static ArrayList<Event> events = new ArrayList<>();
    public static int idCount = 0;
    private int id;
    private String title;
    private String location;

    public static Event findEventByID(int ID) {
        for (Event e : events) {
            if (e.id == ID) {
                return e;
            }
        }
        return null;
    }

    public Event() {
        this.id = idCount++;
        this.title = "";
        this.location = "";
    }

    public Event(String title, String location) {
        this.id = idCount++;
        this.title = title;
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setID(int id) {
        this.id = id;
    }
    public int getID() {
        return this.id;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return this.location;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
