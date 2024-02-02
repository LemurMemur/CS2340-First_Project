package com.example.cs2340_first_project;

import java.util.ArrayList;

public class CalendarDbHelper {
    private ArrayList<Event> events;

    public CalendarDbHelper() {
        events = new ArrayList<Event>();
    }

    public void addEvent(Event newEvent) {
        events.add(newEvent);
    }
    public void removeEvent(Event newEvent) {
        events.remove(newEvent);
    }
    public ArrayList<Event> getEventList() {
        return events;
    }

}
