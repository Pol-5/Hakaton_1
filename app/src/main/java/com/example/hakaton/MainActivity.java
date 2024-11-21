package com.example.hakaton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView textViewEvents;
    private Data_base dbHelper;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button2);
        calendarView = findViewById(R.id.calendarView);
        textViewEvents = findViewById(R.id.eventTextView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entrance();  // Вызов метода входа
            }
        });

       dbHelper = new Data_base(this);
/*dbHelper.insertEvent("2024-11-21", "10:00", "Конференция в университете", "Наука","ВСГУТУ");
dbHelper.insertEvent("2024-11-25", "14:00", "Кросс нации", "Спорт","Спортиваная площадка ВСГУТУ");*/

        calendarView.setOnDateChangeListener((widget, year, month, dayOfMonth) -> {

            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;


            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = dbHelper.getEventsByDate(selectedDate); // Получаем события по дате

            StringBuilder eventsText = new StringBuilder("События на " + selectedDate + ":\n");

            if (cursor != null && cursor.moveToFirst()) {
                int columnDate = cursor.getColumnIndex(Data_base.COLUMN_DATE);
                int columnTime = cursor.getColumnIndex(Data_base.COLUMN_TIME);
                int columnDescription = cursor.getColumnIndex(Data_base.COLUMN_DESCRIPTION);
                int columnCategory = cursor.getColumnIndex(Data_base.COLUMN_CATEGORY);
                int columnPlace = cursor.getColumnIndex(Data_base.COLUMN_PLACE);


                if (columnDate >= 0 && columnTime >= 0 && columnDescription >= 0 && columnCategory >= 0) {
                    do {
                        String time = cursor.getString(columnTime);
                        String description = cursor.getString(columnDescription);
                        String category = cursor.getString(columnCategory);
                        String place = cursor.getString(columnPlace);


                        eventsText.append("\nВремя: ").append(time)
                                .append("\nОписание: ").append(description)
                                .append("\nКатегория: ").append(category)
                                .append("\nМесто: ").append(place)
                                .append("\n--------------------------");
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } else {
                eventsText.append("\nНет событий на этот день.");
            }


            textViewEvents.setText(eventsText.toString());
        });
    }
    public void Entrance()
    {
        Intent intent = new Intent(MainActivity.this,Recom_Events.class);
        startActivity(intent);
        finish();
    }

}
