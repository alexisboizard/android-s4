package com.iut.mobile.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iut.mobile.tp1.model.UserData;

public class DetailActivity extends AppCompatActivity {
    final static String KEY_NAME = "name";
    final static String KEY_DATA = "data";
    final static String KEY_FIRSTNAME = "firstname";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        UserData userData = (UserData) getIntent().getSerializableExtra(KEY_DATA);

        String name = userData.name;
        String firstname = userData.firstname;

        TextView tv_name = findViewById(R.id.text_view_name);
        TextView tv_firstname = findViewById(R.id.text_view_firstname);

        tv_name.setText(name);
        tv_firstname.setText(firstname);

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent();
                i.putExtra(KEY_NAME,name);
                i.putExtra(KEY_FIRSTNAME,firstname);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}