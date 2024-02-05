package com.example.cs2340_first_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
    }

    private void initWidgets() {
        todoListView = findViewById(R.id.todoListView);
    }
    private void loadFromDBToMemory(){
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateTodoListArray();
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

}