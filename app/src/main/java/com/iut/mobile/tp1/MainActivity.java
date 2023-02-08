package com.iut.mobile.tp1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iut.mobile.tp1.model.UserData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    
    TextInputEditText inputName = null;
    TextInputEditText inputFirstname = null;
    TextInputEditText inputBirthday = null;
    TextInputEditText inputPhone = null;
    TextInputEditText inputEmail = null;
    TextInputEditText inputCity = null;
    TextInputEditText inputZIP = null;
    Button submitButton = null;
    final static int REQUEST_CODE_DETAIL = 666;

    UserData userData = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity", "onCreate");

        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initField();
                appendToUserData();
                openDetail(userData);
                //checkName();
            }
        });
    }

    private void appendToUserData() {
        userData.firstname = inputFirstname.getText().toString();
        userData.name = inputName.getText().toString();
        userData.birthday = inputBirthday.getText().toString();
        userData.phone = inputPhone.getText().toString();
        userData.email = inputEmail.getText().toString();
        userData.city = inputCity.getText().toString();
        userData.zip = inputZIP.getText().toString();

    }

    private void initField() {
        inputName = findViewById(R.id.ti_name_content);
        inputFirstname = findViewById(R.id.ti_firstname_content);
        inputBirthday = findViewById(R.id.ti_birthday_content);
        inputPhone = findViewById(R.id.ti_tel_content);
        inputEmail = findViewById(R.id.ti_mail_content);
        inputCity = findViewById(R.id.ti_city_content);
        inputZIP = findViewById(R.id.ti_zip_content);

    }

    private boolean isFieldFilled(int fieldID){
        String fieldContent = findViewById(fieldID).toString();
        if(fieldContent.isEmpty()){
            return false;
        }
        return true;
    }

    private void openDetail(UserData data){

        Intent i = new Intent(getApplicationContext(),DetailActivity.class);

        i.putExtra(DetailActivity.KEY_DATA, data);

        startActivityForResult(i,REQUEST_CODE_DETAIL);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK && data != null){
            UserData userData = (UserData) data.getSerializableExtra(DetailActivity.KEY_DATA);
            if(userData.name == inputName.toString() && userData.firstname == inputFirstname.toString()){
                Toast.makeText(getApplicationContext(),"Les données reçu sont bonne !",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Les données reçu sont incorrect !",Toast.LENGTH_LONG).show();
            }
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