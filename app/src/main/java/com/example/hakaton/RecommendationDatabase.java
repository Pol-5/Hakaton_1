package com.example.hakaton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecommendationDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recommendation_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TEST_RESULTS = "test_results";

    // Для таблицы test_results
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_PROGRAMMING = "programming";
    private static final String COLUMN_SPORTS = "sports";
    private static final String COLUMN_BUSINESS = "business";
    private static final String COLUMN_POLITICS = "politics";
    private static final String COLUMN_SOCIAL = "social";
    private static final String COLUMN_STUDY = "study";
    private static final String COLUMN_SCIENCE = "science";

    private static final String CREATE_TEST_RESULTS_TABLE =
            "CREATE TABLE " + TABLE_TEST_RESULTS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_PROGRAMMING + " INTEGER, " +
                    COLUMN_SPORTS + " INTEGER, " +
                    COLUMN_BUSINESS + " INTEGER, " +
                    COLUMN_POLITICS + " INTEGER, " +
                    COLUMN_SOCIAL + " INTEGER, " +
                    COLUMN_STUDY + " INTEGER," +
                    COLUMN_SCIENCE + " INTEGER" +
                    ");";

    public RecommendationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEST_RESULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_RESULTS);
        onCreate(db);
    }

    public boolean saveTestResults(int userId, boolean programming, boolean sports, boolean business,
                                   boolean politics, boolean social, boolean study, boolean science) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_PROGRAMMING, programming ? 1 : 0);
        values.put(COLUMN_SPORTS, sports ? 1 : 0);
        values.put(COLUMN_BUSINESS, business ? 1 : 0);
        values.put(COLUMN_POLITICS, politics ? 1 : 0);
        values.put(COLUMN_SOCIAL, social ? 1 : 0);
        values.put(COLUMN_STUDY, study ? 1 : 0);
        values.put(COLUMN_SCIENCE, science ? 1 : 0);

        long result = db.replace(TABLE_TEST_RESULTS, null, values);
        db.close();

        if (result == -1) {
            Log.e("DatabaseError", "Ошибка при сохранении тестовых данных для пользователя с ID: " + userId);
            return false;
        }

        Log.d("DatabaseSuccess", "Тестовые данные успешно сохранены для пользователя с ID: " + userId);
        return true;
    }

    public List<String> getRecommendedEvents(int userId) {
        List<String> recommendedEvents = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Шаг 1: Получаем предпочтения пользователя из таблицы test_results
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_TEST_RESULTS + " WHERE " + COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)}
        );

        // Шаг 2: Если данные для пользователя найдены, продолжаем
        if (cursor.moveToFirst()) {
            // Шаг 3: Проверяем, какие категории интересуют пользователя
            if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROGRAMMING)) == 1) {
                recommendedEvents.addAll(getEventsByCategory("Программирование"));
            }
            if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SPORTS)) == 1) {
                recommendedEvents.addAll(getEventsByCategory("Спорт"));
            }
            if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BUSINESS)) == 1) {
                recommendedEvents.addAll(getEventsByCategory("Бизнес"));
            }
            if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POLITICS)) == 1) {
                recommendedEvents.addAll(getEventsByCategory("Политика"));
            }
            if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SOCIAL)) == 1) {
                recommendedEvents.addAll(getEventsByCategory("Социальные проекты"));
            }
            if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDY)) == 1) {
                recommendedEvents.addAll(getEventsByCategory("Учеба"));
            }
            if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCIENCE)) == 1) {
                recommendedEvents.addAll(getEventsByCategory("Наука"));
            }
        }
        cursor.close();
        return recommendedEvents;
    }


    private List<String> getEventsByCategory(String category) {
        List<String> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Используем правильную таблицу для событий (events)
        Cursor cursor = db.rawQuery(
                "SELECT description FROM events WHERE category=?",
                new String[]{category});

        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            events.add(event);
        }
        cursor.close();
        return events;
    }
}
