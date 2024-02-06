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
import java.util.ArrayList;
import java.util.Date;


public class EventSQLM extends SQLiteOpenHelper
{
    private static EventSQLM ESQLM;

    private static final String DATABASE_NAME = "EventDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Events";
    private static final String COUNTER = "counter";
    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String LOCATION_FIELD = "location";
    private static final String SECTION_FIELD = "section";
    private static final String INSTRUCTOR_FIELD = "instructor";
    private static final String DAYS_FIELD = "days";
    private static final String TIME_FIELD = "timeOfDay";
    private static final String END_TIME_FIELD = "endTime";

    public EventSQLM(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static EventSQLM instanceOfEventDatabase(Context context)
    {
        if(ESQLM == null)
            ESQLM = new EventSQLM(context);

        return ESQLM;
    }

    private static String boolArrtoString(boolean[] days) {
        String ret = "";
        for (boolean b : days) {
            if (b) ret += "1";
            else ret += "0";
        }
        return ret;
    }
    private static boolean[] stringtoBoolArr(String str) {
        boolean[] ret = new boolean[7];
        for (int i=0; i<7; ++i) {
            ret[i] = (str.charAt(i) == '1');
        }
        return ret;
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
                .append(LOCATION_FIELD)
                .append(" TEXT, ")
                .append(SECTION_FIELD)
                .append(" TEXT, ")
                .append(INSTRUCTOR_FIELD)
                .append(" TEXT, ")
                .append(DAYS_FIELD)
                .append(" TEXT, ")
                .append(TIME_FIELD)
                .append(" TEXT, ")
                .append(END_TIME_FIELD)
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

    public void addEventToDatabase(Event event)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        event = (Course) event;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, event.getID());
        contentValues.put(TITLE_FIELD, event.getTitle());
        contentValues.put(LOCATION_FIELD, event.getLocation());
        contentValues.put(SECTION_FIELD, ((Course) event).getSection());
        contentValues.put(INSTRUCTOR_FIELD, ((Course) event).getInstructor());
        contentValues.put(DAYS_FIELD, boolArrtoString(((Course) event).getDaysOfWeek()));
        contentValues.put(TIME_FIELD, ((Course) event).getTimeOfDay());
        contentValues.put(END_TIME_FIELD, ((Course) event).getEndTime());



        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateEventListArray()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null))
        {
            Event.events = new ArrayList<>();
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String location = result.getString(3);
                    String section = result.getString(4);
                    String instructor = result.getString(5);
                    boolean[] days = stringtoBoolArr(result.getString(6));
                    String time = result.getString(7);
                    Event event = new Course(title, location, days, time, instructor, section);
                    event.setID(id);
                    Event.events.add(event);
                    Event.idCount = Math.max(Event.idCount, id+1);
                }
            }
        }
    }

    public void updateEventInDB(Event event)
    {
        event = (Course) event;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, event.getID());
        contentValues.put(TITLE_FIELD, event.getTitle());
        contentValues.put(LOCATION_FIELD, event.getLocation());
        contentValues.put(SECTION_FIELD, ((Course) event).getSection());
        contentValues.put(INSTRUCTOR_FIELD, ((Course) event).getInstructor());
        contentValues.put(DAYS_FIELD, boolArrtoString(((Course) event).getDaysOfWeek()));
        contentValues.put(TIME_FIELD, ((Course) event).getTimeOfDay());
        contentValues.put(END_TIME_FIELD, ((Course) event).getEndTime());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(event.getID())});
    }

    public void deleteEvent(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, ID_FIELD + " = ?", new String[]{String.valueOf(event.getID())});

    }

}
