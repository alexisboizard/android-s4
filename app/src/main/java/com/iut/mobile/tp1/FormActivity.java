package com.iut.mobile.tp1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.textfield.TextInputEditText;
import com.iut.mobile.tp1.model.UserData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FormActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;

    private static final int GALLERY_REQ_CODE = 500;
    ImageView iv_avatar = null;
    RadioButton inputGender = null;
    TextInputEditText inputName = null;
    TextInputEditText inputFirstname = null;
    TextInputEditText inputBirthday = null;
    TextInputEditText inputPhone = null;
    TextInputEditText inputEmail = null;
    TextInputEditText inputCity = null;
    TextInputEditText inputZIP = null;
    MaterialButton submitButton = null;
    final static int REQUEST_CODE_DETAIL = 666;
    private int[]  inputID= {R.id.ti_name_content,R.id.ti_firstname_content,R.id.ti_birthday_content,R.id.ti_tel_content,R.id.ti_mail_content,R.id.ti_city_content,R.id.ti_zip_content};
    final static String KEY_DATA = "data";
    File avatar = null;
    UserData userData = null;
    Object avatar_object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        submitButton = findViewById(R.id.submit_button);
        userData = new UserData();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                appendToUserData(userData);
                saveUserAvatar((Uri) avatar_object);
                if(avatar != null){
                    userData.avatar_path = avatar.getAbsolutePath();
                }else{
                    userData.avatar_path = "";
                }
                Intent i = new Intent(getApplicationContext(), FormActivity.class);
                i.putExtra(KEY_DATA,userData);
                setResult(RESULT_OK,i);
                finish();
            }
        });

        iv_avatar = findViewById(R.id.avatar);

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(FormActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(i, "Select Picture"),GALLERY_REQ_CODE);
            }
        });

    }
    private void initField() {
        RadioGroup rg_sex = (RadioGroup) findViewById(R.id.radio_group_sex);
        int rb_id = rg_sex.getCheckedRadioButtonId();

        inputGender = (RadioButton) findViewById(rb_id);
        inputFirstname = findViewById(R.id.ti_firstname_content);
        inputName = findViewById(R.id.ti_name_content);
        inputBirthday = findViewById(R.id.ti_birthday_content);
        inputPhone = findViewById(R.id.ti_tel_content);
        inputEmail = findViewById(R.id.ti_mail_content);
        inputCity = findViewById(R.id.ti_city_content);
        inputZIP = findViewById(R.id.ti_zip_content);
    }
    private String selectedGender(RadioButton rb){
        String gender = null;
        switch ((String)rb.getText()){
            case "M":
                gender = "Homme";
                break;
            case "F":
                gender = "Femme";
                break;
            case "Autres":
                gender = "Autres";
            break;
        }
        return gender;
    }
    private void appendToUserData(UserData userData) {
        initField();
        userData.gender = selectedGender(inputGender);
        userData.firstname = inputFirstname.getText().toString();
        userData.name = inputName.getText().toString();
        userData.birthday = inputBirthday.getText().toString();
        userData.phone = inputPhone.getText().toString();
        userData.email = inputEmail.getText().toString();
        userData.city = inputCity.getText().toString();
        userData.zip = inputZIP.getText().toString();
    }

    private boolean isFieldFilled(int fieldID){
        String fieldContent = findViewById(fieldID).toString();
        if(fieldContent.isEmpty() && inputGender.isChecked()){
            return false;
        }
        return true;
    }

    private boolean allFieldFilled(int[] fieldsId){
        int fieldsCount = fieldsId.length;
        boolean filledFields = true;
        for (int i=0;i<fieldsCount;i++){
            if(isFieldFilled(fieldsId[i]) == false){
                filledFields = false;
            }
        }
        return filledFields;
    }

    private void getInputData(UserData data){

        if(allFieldFilled(inputID)){
            appendToUserData(data);
        }else{
            Toast.makeText(getApplicationContext(),"Merci de remplir tous les champs",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK && data != null){
            UserData userData = (UserData) data.getSerializableExtra(DetailActivity.KEY_DATA);
        }
        if(requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null){
            iv_avatar.setImageURI(data.getData());
            avatar_object = data.getData();
        }
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void saveUserAvatar(Uri uri){
        Bitmap bitmap = null;
        try {
            String path = getApplicationContext().getFilesDir().toString();
            avatar = new File(path, userData.firstname+"_" + userData.name+".png" );
            FileOutputStream out = new FileOutputStream(avatar);
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(getContentResolver(),avatar.getAbsolutePath(),avatar.getName(),avatar.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}