package com.iut.mobile.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iut.mobile.tp1.model.UserData;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    final static String KEY_DATA = "data";
    UserData userData;

    int[] tv_id = {
            R.id.text_view_name,
            R.id.text_view_firstname,
            R.id.text_view_birthday,
            R.id.text_view_email,
            R.id.text_view_tel,
            R.id.text_view_city,
            R.id.text_view_zip
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        userData = (UserData) getIntent().getSerializableExtra(KEY_DATA);

        setTextView();

        ImageView iv_detail = findViewById(R.id.iv_detail);
        if(userData.avatar_path.isEmpty()){
            iv_detail.setImageResource(R.drawable.default_avatar);
        }else{
            iv_detail.setImageURI(Uri.parse(userData.avatar_path));
        }

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private List getInputsData(){
        List inputData = new ArrayList<String>();
        inputData.add(userData.name);
        inputData.add((userData.firstname));
        inputData.add((userData.birthday));
        inputData.add((userData.email));
        inputData.add((userData.phone));
        inputData.add((userData.city));
        inputData.add((userData.zip));

        return inputData;
    }

    private List intitializeTextView(int[] tv_id){
        int tv_count = tv_id.length;
        List textViews = new ArrayList<TextView>();
        for(int i=0;i<tv_count;i++){
            textViews.add(findViewById(tv_id[i]));
        }
        return textViews;
    }
    private void setTextView(){
        List textViews = intitializeTextView(tv_id);
        List inputsData = getInputsData();

        if(textViews.size() == inputsData.size()){
            for (int i = 0; i < textViews.size(); i++) {
                TextView tempTV = (TextView) textViews.get(i);
                tempTV.setText((CharSequence) inputsData.get(i));
            }
        }

    }
}