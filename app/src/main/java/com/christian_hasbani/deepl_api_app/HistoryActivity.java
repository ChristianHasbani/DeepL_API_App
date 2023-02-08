package com.christian_hasbani.deepl_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    //ArrayList of all the last 10 translations
    private ArrayList<Translation>  translations;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initVariables();
        createAdapter();
    }

    //Method to initialize variables
    public void initVariables(){
        translations = (ArrayList<Translation>) getIntent().getSerializableExtra("Translations");
        listView = findViewById(R.id.translationsListView);
    }

    //Method to create an adapter for the list view
    public void createAdapter(){
        Context context = getApplicationContext();
        int layoutId = R.layout.translation_item;
        //Create adapter for the list view
        TranslationAdapter adapter = new TranslationAdapter(context,layoutId,translations);
        listView.setAdapter(adapter);
    }
}