package com.example.cs2340_first_project;

import static com.example.cs2340_first_project.EventSQLM.instanceOfEventDatabase;

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
    private Button deleteButton, eventTimeButton, eventDetailsBackButton, eventEndTimeButton;
    private Course selectedEvent;
    private String selectedTime, selectedEndTime;


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
        eventTimeButton = findViewById(R.id.eventTimeButton);
        deleteButton = findViewById(R.id.deleteEventButton);
        eventDetailsBackButton = findViewById(R.id.eventDetailsBackButton);
        eventEndTimeButton = findViewById(R.id.eventEndTimeButton);


        eventTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
                System.out.println("eventTime pressed");
            }
        });

        eventEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePickerDialog();
                System.out.println("eventTime pressed");
            }
        });

        eventDetailsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });


    }

    private void checkForEditEvent()
    {
        int passedEventID = getIntent().getIntExtra("id", -1); // fix this
        selectedEvent = (Course) Event.findEventByID(passedEventID);
        selectedTime = "00:00";
        selectedEndTime = "00:00";

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
            selectedEndTime = selectedEvent.getEndTime();
            eventTimeButton.setText("Start Time: " + selectedTime);
            eventEndTimeButton.setText("End Time: " + selectedEndTime);

        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveEvent(View view)
    {

        EventSQLM ESQLM = instanceOfEventDatabase(this);

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
            Course newCourse = new Course(title, instructor, daysOfWeek, selectedTime, section, location, selectedEndTime);
            //System.out.println(newCourse);
            Event.events.add(newCourse);
            ESQLM.addEventToDatabase(newCourse);
        }
        else
        {
            selectedEvent.setTitle(title);
            selectedEvent.setInstructor(instructor);
            selectedEvent.setDaysOfWeek(daysOfWeek);
            selectedEvent.setSection(section);
            selectedEvent.setLocation(location);
            selectedEvent.setTimeOfDay(selectedTime);
            selectedEvent.setEndTime(selectedEndTime);
            ESQLM.updateEventInDB(selectedEvent);
        }

        //EventNotificationScheduler.scheduleEventNotification(view.getContext(), selectedTime, daysOfWeek, selectedEvent);

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
                        eventTimeButton.setText("Start Time: " + selectedTime);
                    }
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }

    private void showEndTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedEndTime = String.format("%02d:%02d", hourOfDay, minute);
                        eventEndTimeButton.setText("End Time: " + selectedEndTime);
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
        EventSQLM ESQLM = instanceOfEventDatabase(this);
        ESQLM.deleteEvent(selectedEvent);
        setResult(RESULT_OK);
        finish();
    }
}