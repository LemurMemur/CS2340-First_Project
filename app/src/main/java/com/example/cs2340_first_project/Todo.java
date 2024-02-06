package com.example.cs2340_first_project;

import java.util.ArrayList;
import java.util.Date;

public class Todo
{
    public static ArrayList<Todo> todoArrayList = new ArrayList<>();
    public static String TODO_EDIT_EXTRA =  "todoEdit";

    private int id;
    private String title;
    private String description;
    private Date deleted;
    private String timeOfDay;

    public Todo(int id, String title, String description, Date deleted, String timeOfDay)
    {
        this(id, title, description, deleted);
        this.timeOfDay = timeOfDay;
    }

    public Todo(int id, String title, String description, Date deleted)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deleted = deleted;
    }

    public Todo(int id, String title, String description)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        deleted = null;
    }

    public static Todo getTodoForID(int passedTodoID)
    {
        for (Todo todo : todoArrayList)
        {
            if(todo.getId() == passedTodoID)
                return todo;
        }

        return null;
    }

    public static ArrayList<Todo> nonDeletedTodos()
    {
        ArrayList<Todo> nonDeleted = new ArrayList<>();
        for(Todo todo : todoArrayList)
        {
            if(todo.getDeleted() == null)
                nonDeleted.add(todo);
        }

        return nonDeleted;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getDeleted()
    {
        return deleted;
    }

    public void setDeleted(Date deleted)
    {
        this.deleted = deleted;
    }

    public String getTime()
    {
        return timeOfDay;
    }

    public void setTime(String timeOfDay)
    {
        this.timeOfDay = timeOfDay;
    }
}
