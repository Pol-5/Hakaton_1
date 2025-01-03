package com.example.hakaton;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Recom_Events extends AppCompatActivity {

    private TextView text;
    private RecommendationDatabase recommendationDatabase;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom_events);

        text = findViewById(R.id.eventTextView);
        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entrance();  // Вызов метода входа
            }
        });

        // Подключаем базы данных
        Data_base eventsDbHelper = new Data_base(this);
        RecommendationDatabase recommendationDbHelper = new RecommendationDatabase(this);

        SQLiteDatabase recommendationDb = recommendationDbHelper.getReadableDatabase();

        // Получаем рекомендованные события
        List<Data_base.Event> recommendedEvents = eventsDbHelper.getRecommendedEvents(recommendationDb);

        // Формируем текст для отображения
        StringBuilder recommendationsText = new StringBuilder("Рекомендованные события:\n");

        if (recommendedEvents.isEmpty()) {
            recommendationsText.append("Нет рекомендованных событий.");
        } else {
            for (Data_base.Event event : recommendedEvents) {
                recommendationsText.append("\nДата: ").append(event.getDate())
                        .append("\nВремя: ").append(event.getTime())
                        .append("\nОписание: ").append(event.getDescription())
                        .append("\nКатегория: ").append(event.getCategory())
                        .append("\nМесто: ").append(event.getPlace())
                        .append("\n--------------------------");
            }
        }

        // Устанавливаем текст в TextView
        text.setText(recommendationsText.toString());
    }

    public void Entrance() {
        Intent intent = new Intent(Recom_Events.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
