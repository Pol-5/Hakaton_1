package com.example.hakaton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecommendationDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        db = new RecommendationDatabase(this);

        CheckBox cbProgramming = findViewById(R.id.checkBoxProgramming);
        CheckBox cbSports = findViewById(R.id.checkBoxSports);
        CheckBox cbBusiness = findViewById(R.id.checkBoxBusiness);
        CheckBox cbPolitics = findViewById(R.id.checkBoxPolitics);
        CheckBox cbSocial = findViewById(R.id.checkBoxSocial);
        CheckBox cbStudy = findViewById(R.id.checkBoxStudy);
        CheckBox cbScience = findViewById(R.id.checkBoxScience);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            boolean programming = cbProgramming.isChecked();
            boolean sports = cbSports.isChecked();
            boolean business = cbBusiness.isChecked();
            boolean politics = cbPolitics.isChecked();
            boolean social = cbSocial.isChecked();
            boolean study = cbStudy.isChecked();
            boolean science= cbScience.isChecked();

            int userId = 1;
            db.saveTestResults(userId, programming, sports, business, politics, social, study,science);

            Intent intent = new Intent(TestActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
