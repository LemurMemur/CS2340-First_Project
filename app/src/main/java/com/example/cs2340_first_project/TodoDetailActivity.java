package com.example.cs2340_first_project;

import static com.example.cs2340_first_project.SQLiteManager.instanceOfDatabase;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TodoDetailActivity extends AppCompatActivity {
    private EditText titleEditText, descEditText, courseEditText, locationEditText, timeEditText;
    private Button deleteButton;
    private Todo selectedTodo;
    private Spinner categorySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        initWidgets();
        checkForEditTodo();
        setupCategorySpinner();
    }

    private void initWidgets() {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        courseEditText = findViewById(R.id.courseEditText);
        locationEditText = findViewById(R.id.locationEditText);
        deleteButton = findViewById(R.id.deleteTodoButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        timeEditText = findViewById(R.id.TimeEditText);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(timeEditText);
            }
        });
    }
    private void showDateTimeDialog(final EditText timeEditText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        // Format as mm/dd/yy-hh/mm
                        String formattedDateTime = String.format("%02d/%02d/%02d-%02d:%02d",
                                monthOfYear + 1, dayOfMonth, year % 100, hourOfDay, minute);
                        timeEditText.setText(formattedDateTime);
                    }
                };

                new TimePickerDialog(TodoDetailActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(TodoDetailActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void checkForEditTodo() {
        Intent previousIntent = getIntent();
        int passedTodoID = previousIntent.getIntExtra(Todo.TODO_EDIT_EXTRA, -1);
        selectedTodo = Todo.getTodoForID(passedTodoID);
        if (selectedTodo != null) {
            titleEditText.setText(selectedTodo.getTitle());
            descEditText.setText(selectedTodo.getDescription());
            courseEditText.setText(selectedTodo.getCourse());
            locationEditText.setText(selectedTodo.getLocation());
            timeEditText.setText(formatDateTimeForDisplay(selectedTodo.getDateTime()));
            // Set spinner value if needed
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }
    private String formatDateTimeForDisplay(String iso8601DateTime) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yy-HH/mm", Locale.getDefault());
        try {
            Date date = iso8601Format.parse(iso8601DateTime);
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void saveTodo(View view) {
        SQLiteManager sqLiteManager = instanceOfDatabase(this);
        String title = titleEditText.getText().toString();
        String desc = descEditText.getText().toString();
        String course = courseEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String dateTimeString = timeEditText.getText().toString(); // Ensure you retrieve the string from the EditText

        // Prepare the date formats
        SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yy-HH/mm", Locale.getDefault());
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String iso8601DateTime = "";
        try {
            // Parse the date from the custom format to a Date object
            Date date = displayFormat.parse(dateTimeString);
            if (date != null) {
                // Format the Date object to the ISO8601 string
                iso8601DateTime = iso8601Format.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace(); // Consider handling this case more gracefully
        }

        if (selectedTodo == null) {
            int id = Todo.todoArrayList.size();
            Todo newTodo = new Todo(id, title, desc, course, location, category, iso8601DateTime); // Assuming your Todo constructor is prepared to take the dateTime parameter
            Todo.todoArrayList.add(newTodo);
            sqLiteManager.addTodoToDatabase(newTodo);
        } else {
            selectedTodo.setTitle(title);
            selectedTodo.setDescription(desc);
            selectedTodo.setCourse(course);
            selectedTodo.setLocation(location);
            selectedTodo.setCategory(category);
            selectedTodo.setDateTime(dateTimeString);
            // Ensure Todo class has a method to set the dateTime
            sqLiteManager.updateTodoInDB(selectedTodo);
        }

        finish();
    }


    public void deleteTodoCall(View view) {
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm delete Todo?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTodo();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteTodo() {
        selectedTodo.setDeleted(new Date());
        SQLiteManager sqLiteManager = instanceOfDatabase(this);
        sqLiteManager.updateTodoInDB(selectedTodo);
        finish();
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set spinner to show the correct category when editing an existing Todo
        if (selectedTodo != null && selectedTodo.getCategory() != null) {
            int spinnerPosition = adapter.getPosition(selectedTodo.getCategory());
            categorySpinner.setSelection(spinnerPosition);
        }

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Optionally update the Todo object immediately if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}
