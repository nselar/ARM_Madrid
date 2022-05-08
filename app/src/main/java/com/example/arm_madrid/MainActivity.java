package com.example.arm_madrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new CustomAdapter(this);

        RecyclerView list = findViewById(R.id.rList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

    }

    public void goToAddPlace(View view){

        Intent placeIntent = new Intent(getApplicationContext(),AddPlaceActivity.class);

        startActivity(placeIntent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.updateData();
    }
}