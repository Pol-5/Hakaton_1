package com.example.hakaton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    EditText login;
    EditText password;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        login=findViewById(R.id.editTextText);
        password= findViewById(R.id.editTextTextPassword);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entrance();  // Вызов метода входа
            }
        });
    }

protected void Entrance()
{

    String enteredLogin = login.getText().toString();
    String enteredPassword = password.getText().toString();


    if (enteredLogin.isEmpty() || enteredPassword.isEmpty()) {
        Toast.makeText(this, "Please enter both login and password", Toast.LENGTH_SHORT).show();
        return;
    }

    String hashedEnteredPassword = SecurityUtils.hashPassword(enteredPassword);

    UserDataBase dbUser = new UserDataBase(this);


    boolean isLoginSuccessful = dbUser.checkLogin(enteredLogin, hashedEnteredPassword);

    if (isLoginSuccessful) {

        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        if(dbUser.checkTest(enteredLogin));
        {Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
        finish();
        }
        if(!dbUser.checkTest(enteredLogin))
        {
            Intent intent = new Intent(MainActivity2.this, TestActivity.class);
            startActivity(intent);
            finish();
        }
    }
    else {

        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }
}

}