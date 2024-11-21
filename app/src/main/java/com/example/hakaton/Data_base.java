package com.example.hakaton;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class Data_base extends SQLiteOpenHelper
{
    public class Event {
        private String date;
        private String time;
        private String description;
        private String category;
        private String place;

        public Event(String date,String time,String description, String category,String place) {
            this.date = date;
            this.description = description;
            this.category = category;
            this.time=time;
            this.place=place;
        }

        public String getDate() {
            return date;
        }
        public String getTime() {
            return time;
        }
        public String getDescription() {
            return description;
        }
        public String getCategory() {
            return category;
        }
        public String getPlace(){return place;}


    }



    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_EVENTS = "events";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY = "category";

    public static final String COLUMN_PLACE="place";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +COLUMN_TIME + " TEXT NOT NULL, "+
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_CATEGORY + " TEXT NOT NULL, " + COLUMN_PLACE + " TEXT NOT NULL " +
                    ");";

    public Data_base(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }
    public void addEvent(SQLiteDatabase db, String date,String time, String description, String category,String place) {
        String insertQuery = "INSERT INTO " + Data_base.TABLE_EVENTS + " (" +
                Data_base.COLUMN_DATE + ", " + Data_base.COLUMN_TIME + ", " +
                Data_base.COLUMN_DESCRIPTION + ", " +
                Data_base.COLUMN_CATEGORY + ", " +Data_base.COLUMN_PLACE+") VALUES (?, ?, ?,?,?)";
        db.execSQL(insertQuery, new Object[]{date,time ,description, category,place});
    }
    public List<Event> getAllEvents(SQLiteDatabase db) {
        List<Event> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Data_base.TABLE_EVENTS;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(Data_base.COLUMN_DATE));
                String time=cursor.getString(cursor.getColumnIndexOrThrow(Data_base.COLUMN_TIME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(Data_base.COLUMN_DESCRIPTION));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(Data_base.COLUMN_CATEGORY));
                String place=cursor.getString(cursor.getColumnIndexOrThrow(Data_base.COLUMN_PLACE));
                events.add(new Event (date,time, description, category,place));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }
    // EventDatabaseHelper.java
    // EventDatabaseHelper.java
    public long insertEvent(String date, String time, String description, String category,String place) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_PLACE, place);


        return db.insert(TABLE_EVENTS, null, values);
    }

    public Cursor getEventsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();


        return db.query(
                TABLE_EVENTS,
                null,
                COLUMN_DATE + "=?",
                new String[]{date},
                null,
                null,
                COLUMN_TIME + " ASC"
        );
    }
    public List<Data_base.Event> getRecommendedEvents(SQLiteDatabase recommendationDb) {
        List<Data_base.Event> recommendedEvents = new ArrayList<>();
        SQLiteDatabase eventsDb = this.getReadableDatabase();

        // Получаем категории из recommendation_db
        Cursor categoriesCursor = recommendationDb.rawQuery("SELECT category FROM recommendations", null);
        if (categoriesCursor.moveToFirst()) {
            do {
                String category = categoriesCursor.getString(categoriesCursor.getColumnIndexOrThrow("category"));

                // Для каждой категории ищем события в таблице events
                Cursor eventsCursor = eventsDb.rawQuery(
                        "SELECT * FROM " + TABLE_EVENTS + " WHERE " + COLUMN_CATEGORY + "=?",
                        new String[]{category}
                );

                if (eventsCursor.moveToFirst()) {
                    do {
                        String date = eventsCursor.getString(eventsCursor.getColumnIndexOrThrow(COLUMN_DATE));
                        String time = eventsCursor.getString(eventsCursor.getColumnIndexOrThrow(COLUMN_TIME));
                        String description = eventsCursor.getString(eventsCursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                        String place = eventsCursor.getString(eventsCursor.getColumnIndexOrThrow(COLUMN_PLACE));

                        recommendedEvents.add(new Data_base.Event(date, time, description, category, place));
                    } while (eventsCursor.moveToNext());
                }
                eventsCursor.close();

            } while (categoriesCursor.moveToNext());
        }
        categoriesCursor.close();

        return recommendedEvents;
    }

}

