package com.example.cs2340_first_project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SQLiteManager extends SQLiteOpenHelper
{
    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME = "TodoDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Todo";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String DESC_FIELD = "desc";
    private static final String COURSE_FIELD = "course";
    private static final String LOCATION_FIELD = "location";
    private static final String DUEDATE_FIELD = "duedate";
    private static final String CATEGORY_FIELD = "category";
    private static final String COMPLETE_FIELD = "complete";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context)
    {
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT, ")
                .append(COURSE_FIELD)
                .append(" TEXT, ")
                .append(LOCATION_FIELD)
                .append(" TEXT, ")
                .append(CATEGORY_FIELD)
                .append(" TEXT, ")
                .append(DUEDATE_FIELD)
                .append(" TEXT, ")
                .append(COMPLETE_FIELD)
                .append(" TEXT,")
                .append(DELETED_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
 /*       switch (oldVersion)
        {
            case 1:
                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COURSE_FIELD + " TEXT");
                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + LOCATION_FIELD + " TEXT");
            case 2:
               // sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NEW_COLUMN + " TEXT");
                break;
         }
    */ }

    public void addTodoToDatabase(Todo todo)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, todo.getId());
        contentValues.put(TITLE_FIELD, todo.getTitle());
        contentValues.put(DESC_FIELD, todo.getDescription());
        contentValues.put(COURSE_FIELD, todo.getCourse());
        contentValues.put(LOCATION_FIELD, todo.getLocation());
        contentValues.put(CATEGORY_FIELD, todo.getCategory());
        contentValues.put(DUEDATE_FIELD, todo.getDuedate());
        contentValues.put(COMPLETE_FIELD, todo.getComplete());

        contentValues.put(DELETED_FIELD, getStringFromDate(todo.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateTodoListArray()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null))
        {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String desc = result.getString(3);
                    String course = result.getString(4);
                    String location = result.getString(5);
                    String category = result.getString(6);
                    String duedate = result.getString(7);
                    String complete = result.getString(8);
                    String stringDeleted = result.getString(9);
                    Date deleted = getDateFromString(stringDeleted);
                    Todo todo = new Todo(id,title,desc,course, location, category, duedate, complete, deleted);
                    Todo.todoArrayList.add(todo);
                }
            }
        }
    }

    public void updateTodoInDB(Todo todo)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, todo.getId());
        contentValues.put(TITLE_FIELD, todo.getTitle());
        contentValues.put(DESC_FIELD, todo.getDescription());
        contentValues.put(COURSE_FIELD, todo.getCourse());
        contentValues.put(LOCATION_FIELD, todo.getLocation());
        contentValues.put(CATEGORY_FIELD, todo.getCategory());
        contentValues.put(DUEDATE_FIELD, todo.getDuedate());
        contentValues.put(COMPLETE_FIELD, todo.getComplete());
        contentValues.put(DELETED_FIELD, getStringFromDate(todo.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(todo.getId())});
    }

    private String getStringFromDate(Date date)
    {
        if(date == null)
            return null;
        return dateFormat.format(date);
    }

    private Date getDateFromString(String string)
    {
        try
        {
            return dateFormat.parse(string);
        }
        catch (ParseException | NullPointerException e)
        {
           return null;
        }
    }
}

















