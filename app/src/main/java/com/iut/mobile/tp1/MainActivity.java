package com.iut.mobile.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    
    TextView inputName = null;
    Button submitButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity", "onCreate");
        inputName = findViewById(R.id.text_input_name);
        submitButton = findViewById(R.id.submit_button);
        
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkName();
                Log.e("onClick","click");
            }
        });
        
    }

    private void checkName() {
        String text = inputName.getText().toString();
        Log.e("checkName",text);

        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(text);
        boolean match = matcher.matches();
        if(text.isEmpty()){
            Toast.makeText(getApplicationContext(),"Nom manquant",Toast.LENGTH_LONG).show();
        } else if (match) {
            // si nom contient des chiffres
            Toast.makeText(getApplicationContext(),"Nom ne peux pas contenir de chiffre",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity", "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity", "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", "onDestroy");

    }
}