package com.example.cs2340_first_project;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CalendarDetailActivity extends AppCompatActivity
{
    private EditText eventTitleEditText, eventInstructorEditText, eventSectionEditText,
            eventLocationEditText;
    private LinearLayout eventDaySelection;
    private Button deleteButton, eventTimeButton;
    private Course selectedEvent;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);

        initEventWidgets();
        checkForEditEvent();


        eventTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
                System.out.println("eventTime pressed");
            }
        });

    }

    private void initEventWidgets()
    {

        eventTitleEditText = findViewById(R.id.eventTitleEditText);
        eventInstructorEditText = findViewById(R.id.eventInstructorEditText);
        eventDaySelection = findViewById(R.id.daySelection);
        eventSectionEditText = findViewById(R.id.eventSectionEditText);
        eventLocationEditText = findViewById(R.id.eventLocationEditText);
        eventTimeButton = findViewById(R.id.eventTimeButton);
        deleteButton = findViewById(R.id.deleteEventButton);


    }

    private void checkForEditEvent()
    {
        int passedEventID = getIntent().getIntExtra("id", -1); // fix this
        selectedEvent = (Course) Event.findEventByID(passedEventID);
        selectedTime = "00:00";

        if (selectedEvent != null)
        {
            eventTitleEditText.setText(selectedEvent.getTitle());
            eventInstructorEditText.setText(selectedEvent.getInstructor());
            for (int i=0; i<7; ++i) {
                ((CheckBox) eventDaySelection.getChildAt(i)).setChecked(selectedEvent.getDaysOfWeek()[i]);
            }
            eventSectionEditText.setText(selectedEvent.getSection());
            eventLocationEditText.setText(selectedEvent.getLocation());
            selectedTime = selectedEvent.getTimeOfDay();
            eventTimeButton.setText("Selected Time: " + selectedTime);

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

        for (int i=0; i<7; ++i) {
            daysOfWeek[i] = ((CheckBox) eventDaySelection.getChildAt(i)).isChecked();
        }
        String section = String.valueOf(eventSectionEditText.getText());;
        String location = String.valueOf(eventLocationEditText.getText());;
        if(selectedEvent == null)
        {
            Course newCourse = new Course(title, instructor, daysOfWeek, selectedTime, section, location);
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
            selectedEvent.setTimeOfDay(selectedTime);
        }

        setResult(RESULT_OK);
        finish();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        eventTimeButton.setText("Selected Time: " + selectedTime);
                    }
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
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