package com.example.cs2340_first_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import android.widget.PopupMenu;


public class MainActivityTodo extends AppCompatActivity {

    private ListView todoListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_todo);

        initWidgets();
        loadFromDBToMemory();
        setTodoAdapter();
        setOnClickListener();
        Button CalenderNavigationButton = findViewById(R.id.CalenderNavigationButton);
        CalenderNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCalender(view);
            }
        });
    }

    private void initWidgets() {
        todoListView = findViewById(R.id.todoListView);
        Button filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopupMenu(v);
            }
        });
    }

    private static boolean firstRun = true;

    private void loadFromDBToMemory(){
        if(firstRun){
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.populateTodoListArray();
        }
        firstRun = false;
    }
    private void setTodoAdapter(){
        TodoAdapter todoAdapter = new TodoAdapter(getApplicationContext(), Todo.nonDeletedTodos());
        todoListView.setAdapter(todoAdapter);
    }
    private void setOnClickListener(){
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Todo selectedTodo = (Todo) todoListView.getItemAtPosition(position);
                Intent editTodoIntent = new Intent(getApplicationContext(), TodoDetailActivity.class);
                editTodoIntent.putExtra(Todo.TODO_EDIT_EXTRA, selectedTodo.getId());
                startActivity(editTodoIntent);
            }
        });
    }
    private void showFilterPopupMenu(View v) {
        PopupMenu filterMenu = new PopupMenu(MainActivityTodo.this, v);
        filterMenu.getMenu().add("Assignment");
        filterMenu.getMenu().add("Exam");
        filterMenu.getMenu().add("Other");

        filterMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                filterTodosByCategory(item.getTitle().toString());
                return true;
            }
        });

        filterMenu.show();
    }
    private void filterTodosByCategory(String category) {
        List<Todo> filteredTodos = new ArrayList<>();
        for (Todo todo : Todo.nonDeletedTodos()) {
            if (todo.getCategory().equalsIgnoreCase(category)) {
                filteredTodos.add(todo);
            }
        }
        // If you want to add an option to show all todos again, you might need to modify this logic
        TodoAdapter todoAdapter = new TodoAdapter(getApplicationContext(), filteredTodos);
        todoListView.setAdapter(todoAdapter);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        setTodoAdapter();
    }

    public void newTodo(View view){
        Intent newTodoIntent = new Intent(this, TodoDetailActivity.class);
        startActivity(newTodoIntent);
    }
    public void goToCalender(View view){
        Intent CalenderIntent = new Intent(this, MainActivity.class);
        startActivity(CalenderIntent);
    }

}