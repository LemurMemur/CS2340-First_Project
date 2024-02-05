package com.example.cs2340_first_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class TodoDetailActivity extends AppCompatActivity
{
    private EditText titleEditText, descEditText;
    private Button deleteButton;
    private Todo selectedTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        initWidgets();
        checkForEditTodo();
    }

    private void initWidgets()
    {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        deleteButton = findViewById(R.id.deleteTodoButton);
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
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveTodo(View view)
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());

        if(selectedTodo == null)
        {
            int id = com.example.cs2340_first_project.Todo.todoArrayList.size();
            Todo newTodo = new Todo(id, title, desc);
            com.example.cs2340_first_project.Todo.todoArrayList.add(newTodo);
            sqLiteManager.addTodoToDatabase(newTodo);
        }
        else
        {
            selectedTodo.setTitle(title);
            selectedTodo.setDescription(desc);
            sqLiteManager.updateTodoInDB(selectedTodo);
        }

        finish();
    }

    public void deleteTodo(View view)
    {
        selectedTodo.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateTodoInDB(selectedTodo);
        finish();
    }
}