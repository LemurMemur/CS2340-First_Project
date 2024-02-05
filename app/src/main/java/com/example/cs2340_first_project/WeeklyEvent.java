package com.example.cs2340_first_project;


public class WeeklyEvent extends Event {
    private boolean[] daysOfWeek;
    private String timeOfDay;
    public WeeklyEvent() {
        super();
        daysOfWeek = new boolean[7];
    }
    public WeeklyEvent(String title, String location, boolean[] daysOfWeek) {
        super(title, location);
        this.daysOfWeek = daysOfWeek;
    }

    public void setDaysOfWeek(boolean[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
    public boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }
}
