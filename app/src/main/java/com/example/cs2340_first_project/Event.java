package com.example.cs2340_first_project;

public class Event {
    private String title;
    private String location;
    private String instructor;

    public Event() {
        this.title = "";
        this.location = "";
        this.instructor = "";
    }

    public Event(String title, String location, String instructor) {
        this.title = title;
        this.location = location;
        this.instructor = instructor;
    }

    public String getTitle() {
        return this.title;
    }
    public String getLocation() {
        return this.location;
    }
    public String getInstructor() {
        return this.instructor;
    }
}
