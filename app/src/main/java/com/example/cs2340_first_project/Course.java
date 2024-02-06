package com.example.cs2340_first_project;

import java.util.ArrayList;

public class Course extends WeeklyEvent {
    private String instructor;
    private String section;
    public Course() {
        super();
        instructor = "";
        section = "";
    }
    public Course(String title, String location, boolean[] daysOfWeek, String timeOfDay, String instructor, String section) {
        super(title, location, daysOfWeek, timeOfDay);
        this.instructor = instructor;
        this.section = section;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    public String getInstructor() {
        return instructor;
    }
    public void setSection(String section) {
        this.section = section;
    }
    public String getSection() {
        return section;
    }
}
