package com.example.cs2340_first_project;

public class Event {
    private String title;
    private String location;

    public Event() {
        this.title = "";
        this.location = "";
    }

    public Event(String title, String location) {
        this.title = title;
        this.location = location;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLocation() {
        return this.location;
    }
}
