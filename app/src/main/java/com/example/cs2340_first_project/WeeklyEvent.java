package com.example.cs2340_first_project;

public class WeeklyEvent extends Event {
    private String dayOfWeek;
    public WeeklyEvent() {
        super();
        dayOfWeek = "";
    }
    public WeeklyEvent(String title, String location, String dayOfWeek) {
        super(title, location);
        this.dayOfWeek = dayOfWeek;
    }

    String getDayOfWeek() {
        return dayOfWeek;
    }
}
