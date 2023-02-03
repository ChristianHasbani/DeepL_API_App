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

    public void initVariables(){
        translations = (ArrayList<Translation>) getIntent().getSerializableExtra("Translations");
        listView = findViewById(R.id.translationsListView);
    }

    public void createAdapter(){
        Context context = getApplicationContext();
        int layoutId = R.layout.translation_item;
        TranslationAdapter adapter = new TranslationAdapter(context,layoutId,translations);
        listView.setAdapter(adapter);
    }
}