package com.example.cs2340_first_project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class CalendarDetailActivity extends AppCompatActivity
{
    private EditText eventTitleEditText, eventInstructorEditText, eventSectionEditText,
            eventLocationEditText;
    private LinearLayout eventDaySelection;
    private Button deleteButton;
    private Course selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);

        initEventWidgets();
        checkForEditEvent();
    }

    private void initEventWidgets()
    {

        eventTitleEditText = findViewById(R.id.eventTitleEditText);
        eventInstructorEditText = findViewById(R.id.eventInstructorEditText);
        eventDaySelection = findViewById(R.id.daySelection);
        eventSectionEditText = findViewById(R.id.eventSectionEditText);
        eventLocationEditText = findViewById(R.id.eventLocationEditText);
        deleteButton = findViewById(R.id.deleteEventButton);

        //adsf

    }

    private void checkForEditEvent()
    {
        int passedEventID = getIntent().getIntExtra("id", -1); // fix this
        selectedEvent = (Course) Event.findEventByID(passedEventID);

        if (selectedEvent != null)
        {
            eventTitleEditText.setText(selectedEvent.getTitle());
            eventInstructorEditText.setText(selectedEvent.getInstructor());
            for (int i=0; i<6; ++i) {
                eventDaySelection.getChildAt(i).setActivated(selectedEvent.getDaysOfWeek()[i]);
            }
            eventSectionEditText.setText(selectedEvent.getSection());
            eventLocationEditText.setText(selectedEvent.getLocation());
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveEvent(View view)
    {

        String title = String.valueOf(eventTitleEditText.getText());
        String instructor = String.valueOf(eventInstructorEditText.getText());
        boolean[] daysOfWeek = new boolean[7];

        System.out.println(daysOfWeek);

        for (int i=0; i<6; ++i) {
            daysOfWeek[i] = ((CheckBox) eventDaySelection.getChildAt(i)).isChecked();
        }
        String section = String.valueOf(eventSectionEditText.getText());;
        String location = String.valueOf(eventLocationEditText.getText());;
        if(selectedEvent == null)
        {
            Course newCourse = new Course(title, instructor, daysOfWeek, section, location);
            //System.out.println(newCourse);
            Event.events.add(newCourse);
        }
        else
        {
            selectedEvent.setTitle(title);
            selectedEvent.setInstructor(instructor);
            selectedEvent.setDaysOfWeek(daysOfWeek);
            selectedEvent.setSection(section);
            selectedEvent.setLocation(location);
        }

        setResult(RESULT_OK);
        finish();
    }

    public void deleteEventCall(View view)
    {
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm delete event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteEvent();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ;
                    }
                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteEvent() {
        Event.events.remove(selectedEvent);
        setResult(RESULT_OK);
        finish();
    }
}