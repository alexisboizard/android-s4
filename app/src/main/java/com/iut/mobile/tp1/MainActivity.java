package com.iut.mobile.tp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.iut.mobile.tp1.model.UserData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE_DETAIL = 666;
    File file = null;
    RecyclerView rv;
    ContactAdapter adapter;
    ArrayList<UserData> contactList = new ArrayList<>();

    TextView contact_counter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        file = new File(getApplicationContext().getFilesDir(), "data.json");
        if(!file.exists()) {
            createJson();
        }

        adapter = new ContactAdapter(this,buildContactListFromJSON(contactList));
        rv = findViewById(R.id.rv_contact);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        contact_counter = findViewById(R.id.tv_contact_counter);

        contact_counter.setText(String.valueOf( contactList.size() + " Contact" + (contactList.size()>1?"s":"")));

        MaterialButton createContact = findViewById(R.id.btn_new_contact);
        createContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),FormActivity.class);
                startActivityForResult(i,REQUEST_CODE_DETAIL);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if(direction == ItemTouchHelper.LEFT){
                //delete element
                contactList.remove(position);
                rv.getAdapter().notifyItemRemoved(position);
                buildJsonFromContactList();
                contact_counter.setText(contactList.size() + " Contact" + (contactList.size() > 1 ? "s" : ""));

            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK && data != null){
            UserData userData = (UserData) data.getSerializableExtra(FormActivity.KEY_DATA);
                //writeJSONFile("data.json",userData);
                contactList.add(userData);
                buildJsonFromContactList();
                adapter.notifyDataSetChanged();
                contact_counter.setText(contactList.size() + " Contact" + (contactList.size() > 1 ? "s" : ""));
        }
    }

    public String getJSONFile(){
        String json;
        try {
            InputStream in = new FileInputStream(file);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public void createJson() {
        String fileInit = "{\"contact\":[]}";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(fileInit);
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<UserData> buildContactListFromJSON(ArrayList<UserData> contactList){
        try {
            JSONObject obj = new JSONObject(getJSONFile());
            JSONArray array = obj.getJSONArray("contact");
            for(int i=0;i<array.length();i++){
                UserData userData = new UserData();
                JSONObject o = array.getJSONObject(i);
                userData.gender = o.getString("gender");
                userData.name = o.getString("last_name");
                userData.firstname = o.getString("first_name");
                userData.email = o.getString("email");
                userData.phone = o.getString("phone");
                userData.city = o.getString("city");
                userData.zip = o.getString("zip");
                //userData.birthday = o.getString("birthday");
                userData.avatar_path = o.getString("avatar_path");
                contactList.add(userData);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return contactList;
    }
    public void buildJsonFromContactList(){
        createJson();
        final String[] jsonContent = {"{\"contact\":["};
        contactList.forEach((contact)->
                        jsonContent[0] = jsonContent[0]
                                + "{\"gender\":" + '"' +contact.gender + "\","
                                + "\"first_name\":" + '"' + contact.firstname+ "\","
                                + "\"last_name\":" + '"' + contact.name+ "\","
                                + "\"email\":" + '"' + contact.email+ "\","
                                + "\"phone\":" + '"' + contact.phone+ "\","
                                + "\"city\":" + '"' + contact.city+ "\","
                                + "\"zip\":" + '"' + contact.zip+ "\","
                                + "\"avatar_path\":" + '"' + contact.avatar_path+ "\""
                                + "},"
        );
        jsonContent[0] = jsonContent[0].substring(0,jsonContent[0].length() -1) + "]} ";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonContent[0]);
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

// TODO rajouter la date de naissance dans le JSON