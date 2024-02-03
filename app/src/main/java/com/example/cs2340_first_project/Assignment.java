package com.example.cs2340_first_project;

public class Assignment extends OneTimeEvent {
    private Course parentCourse;
    private boolean isComplete;
    private boolean isExam;
    public Assignment() {
        super();
        parentCourse = new Course();
        isComplete = false;
        isExam = false;
    }
    public Assignment(String title, String location, String date, Course parentCourse, boolean isExam) {
        super(title, location, date);
        this.parentCourse = parentCourse;
        this.isComplete = false;
        this.isExam = isExam;
    }

    public void setParentCourse(Course parentCourse) {
        this.parentCourse = parentCourse;
    }
    public Course getParentCourse() {
        return parentCourse;
    }
    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }
    public boolean isComplete() {
        return isComplete;
    }
    public void setExam(boolean isExam) {
        this.isExam = isExam;
    }
    public boolean isExam() {
        return isExam;
    }
}
