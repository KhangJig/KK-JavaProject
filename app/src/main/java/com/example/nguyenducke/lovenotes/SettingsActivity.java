package com.example.nguyenducke.lovenotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.nguyenducke.lovenotes.MainActivity.changeToolbarFont;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        InitUI();
        ActionBar();


    }

    private void InitUI() {

        toolbar=(Toolbar)findViewById(R.id.toolbar_Info);
        changeToolbarFont(toolbar,this);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
