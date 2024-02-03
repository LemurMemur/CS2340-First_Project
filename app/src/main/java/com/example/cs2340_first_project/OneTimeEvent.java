package com.example.cs2340_first_project;

public class OneTimeEvent extends Event {
    private String date;
    public OneTimeEvent() {
        super();
        date = "";
    }
    public OneTimeEvent(String title, String location, String date) {
        super(title, location);
        this.date = date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }
}
