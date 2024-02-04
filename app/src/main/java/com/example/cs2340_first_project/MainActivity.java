package com.example.cs2340_first_project;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340_first_project.databinding.ActivityMainBinding;

import java.util.ArrayList;

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

        currMonth = 1;
        refreshActivity();


        Button btnPreviousMonth = findViewById(R.id.btnPreviousMonth);
        Button btnNextMonth = findViewById(R.id.btnNextMonth);
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        cellWidth = screenWidth/7;

    }

    private void refreshActivity() {
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

        ArrayList<String> taskList = new ArrayList<>();

        // Populate the taskList here

        // Example:
        taskList.add("CS2340 - Object Oriented Design");

        for (String task : taskList) {
            TextView taskText = new TextView(this);
            taskText.setText(String.valueOf(task));

            taskText.setMaxWidth(cellWidth);
            taskText.setMaxLines(1);

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
        return newCell;

    }


}