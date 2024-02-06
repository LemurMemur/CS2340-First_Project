package com.example.cs2340_first_project;

import static com.example.cs2340_first_project.SQLiteManager.instanceOfDatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class TodoDetailActivity extends AppCompatActivity
{
    private EditText titleEditText, descEditText, courseEditText, locationEditText;
    private Button deleteButton;
    private Todo selectedTodo;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        initWidgets();
        checkForEditTodo();
        setupCategorySpinner();
    }

    private void initWidgets()
    {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        courseEditText = findViewById(R.id.courseEditText);
        locationEditText = findViewById(R.id.locationEditText);
        deleteButton = findViewById(R.id.deleteTodoButton);
        categorySpinner = findViewById(R.id.categorySpinner);
    }

    private void checkForEditTodo()
    {
        Intent previousIntent = getIntent();

        int passedTodoID = previousIntent.getIntExtra(com.example.cs2340_first_project.Todo.TODO_EDIT_EXTRA, -1);
        selectedTodo = Todo.getTodoForID(passedTodoID);

        if (selectedTodo != null)
        {
            titleEditText.setText(selectedTodo.getTitle());
            descEditText.setText(selectedTodo.getDescription());
            courseEditText.setText(selectedTodo.getCourse());
            locationEditText.setText(selectedTodo.getLocation());
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveTodo(View view)
    {
        SQLiteManager sqLiteManager = instanceOfDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());
        String course = String.valueOf(courseEditText.getText());
        String location = String.valueOf(locationEditText.getText());

        if(selectedTodo == null)
        {
            int id = Todo.todoArrayList.size();
            Todo newTodo = new Todo(id, title, desc, course, location);
            com.example.cs2340_first_project.Todo.todoArrayList.add(newTodo);
            sqLiteManager.addTodoToDatabase(newTodo);
        }
        else
        {
            selectedTodo.setTitle(title);
            selectedTodo.setDescription(desc);
            selectedTodo.setCourse(course);
            selectedTodo.setLocation(location);
            sqLiteManager.updateTodoInDB(selectedTodo);
        }

        finish();
    }

    public void deleteTodoCall(View view)
    {
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
                        ;
                    }
                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteTodo()
    {
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

        // Optionally, handle item selections
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Your code here for handling item selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Your code here for handling no selection
            }
        });
    }
}