package com.example.cs2340_first_project;

import static com.example.cs2340_first_project.EventSQLM.instanceOfEventDatabase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340_first_project.databinding.ActivityMainBinding;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currMonth = 0;
    private String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private int[] monthLengths = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int[] monthStartDays = {1, 4, 4, 0, 2, 5, 0, 3, 6, 1, 4, 6};
    private int cellWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

        currMonth = 1;


        Button btnPreviousMonth = findViewById(R.id.btnPreviousMonth);
        Button btnNextMonth = findViewById(R.id.btnNextMonth);
        Button todoListNavigationButton = findViewById(R.id.todoListNavigationButton);
        Button newEventButton = findViewById(R.id.newEventButton);
        btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currMonth--;
                if (currMonth < 0) {
                    currMonth += 12;
                }

                refreshActivity();
            }
        });

        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currMonth++;
                if (currMonth >= 12) {
                    currMonth -= 12;
                }
                refreshActivity();
            }
        });

        todoListNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTodo(view);
            }
        });
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEvent(view, -1);
                refreshActivity();
            }

        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        cellWidth = screenWidth/7;

        populateEventArrayList();

        refreshActivity();
    }

    private void refreshActivity() {

        //update event list


        GridLayout calendarGrid = findViewById(R.id.calendarGrid);
        calendarGrid.removeAllViews();

        // Set Month title
        TextView title = findViewById(R.id.monthTitle);
        title.setText(monthNames[currMonth]);

        // Add empty cells for the offset

        for (int i = 0; i < monthStartDays[currMonth]; i++) {

            TextView emptyCell = new TextView(this);
            emptyCell.setVisibility(View.INVISIBLE);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            emptyCell.setLayoutParams(params);

            calendarGrid.addView(emptyCell);

        }

        // Add day cells
        for (int day = 1; day <= monthLengths[currMonth]; day++) {
            calendarGrid.addView(newFunctionalCell(day));
        }
    }

    private View newFunctionalCell(int day) {
        LinearLayout newCell = new LinearLayout(this);
        newCell.setOrientation(LinearLayout.VERTICAL);

        TextView dayText = new TextView(this);
        dayText.setText(String.valueOf(day));
        dayText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        newCell.addView(dayText);


        // DONT USE taskList, INSTEAD USE THE events STATIC MEMBER
        ArrayList<String> taskList = new ArrayList<>();

        // Populate the cell

        for (Event event : Event.events) {
            boolean[] dOW = ((WeeklyEvent) event).getDaysOfWeek();
            if (!dOW[(day - 1 + monthStartDays[currMonth])%7]) {
                continue;
            }
            TextView taskText = new TextView(this);
            taskText.setText(String.valueOf(event.getTitle()));

            taskText.setMaxWidth(cellWidth);
            taskText.setMaxLines(1);


            taskText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newEvent(view, event.getID());
                }
            });

            newCell.addView(taskText);
        }

        newCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click on day
            }
        });

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        newCell.setLayoutParams(params);

        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(2, 0x88888888); // Set the border width and color

        newCell.setBackground(borderDrawable);

        return newCell;

    }
    public void goToTodo(View view){
        Intent todoIntent = new Intent(this, MainActivityTodo.class);
        startActivity(todoIntent);
    }

    public void newEvent(View view, int eventID){
        Intent newEventIntent = new Intent(this, CalendarDetailActivity.class);
        newEventIntent.putExtra("id", eventID);
        startActivityForResult(newEventIntent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Sort event list so it is ordered by time
            Collections.sort(Event.events, new Comparator<Event>() {
                @Override
                public int compare(Event ev1, Event ev2) {
                    return ((WeeklyEvent)ev1).getTimeOfDay().compareTo(((WeeklyEvent)ev2).getTimeOfDay());
                }
            });
            // Refresh Calendar View to include edits
            refreshActivity();
        }
    }

    private void populateEventArrayList() {
        EventSQLM ESQLM = instanceOfEventDatabase(this);
        ESQLM.populateEventListArray();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String name = "Notif Channel";
            String desc = "Channel for events";
            int impo = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(EventNotifications.channelID, name, impo);
            channel.setDescription(desc);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    }



}