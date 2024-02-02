package com.example.cs2340_first_project;

public class OneTimeEvent extends Event {
    private String date;
    public OneTimeEvent() {
        super();
        date = "";
    }
    public OneTimeEvent(String title, String location, String instructor, String date) {
        super(title, location, instructor);
        this.date = date;
    }

    String getDate() {
        return date;
    }
}
