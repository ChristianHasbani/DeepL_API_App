package com.christian_hasbani.deepl_api_app;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    //Components Used
    private EditText originalText,translatedText;
    private Spinner originalLang,translatedLang;
    private String originalLangSelected;
    private String targetLangSelected;

    //List of Languages available on DeepL
    private ArrayList<Language> languages = new ArrayList<>();

    //Translation manager for saving preferences
    private TranslationManager translationManager;

    //Authentication key
    private final String AUTH_KEY = "DeepL-Auth-Key 6c3cec34-e521-c80e-f6c2-ff924debf1d9:fx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();
        AndroidNetworking.initialize(this);
        getLanguages();
        translationManager = new TranslationManager(getApplicationContext());
    }

    //Method to be called when the user clicks on the translate button
    public void onClickTranslate(View view){
        //Checks if the user entered text in the edit text before the function calls the API
        if(!originalText.getText().toString().isEmpty()){
            String textToTranslate = originalText.getText().toString();
            String targetLangCode = getSelectedLangCode(targetLangSelected);
            String sourceLangCode = getSelectedLangCode(originalLangSelected);

            //dl is code for Detect Language which I added manually in the spinners which is not a language
            if(!sourceLangCode.equals("dl")){
                translateWithSource(textToTranslate,sourceLangCode,targetLangCode);
            }else{
                translateWithoutSourceLang(textToTranslate,targetLangCode);
            }
        }else{
            Toast.makeText(this,"Please enter text before pressing translate",Toast.LENGTH_SHORT).show();
        }
    }

    //Method to initialize variables
    public void initializeVariables(){
        originalText = findViewById(R.id.originalTextEditText);
        translatedText = findViewById(R.id.translatedTextEditText);

        originalLang = findViewById(R.id.originalLanguagesSpinner);
        translatedLang = findViewById(R.id.translatedLanguagesSpinner);

    }

    // Method to get the list of languages
    public void getLanguages(){
        AndroidNetworking.get("https://api-free.deepl.com/v2/languages")
                .addHeaders("Authorization","DeepL-Auth-Key 6c3cec34-e521-c80e-f6c2-ff924debf1d9:fx")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                            String jsonString = response.toString();
                            setupSpinners(jsonString);
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.e("DeepL error", anError.getErrorDetail());
                        Log.e("DeepL error", "Response code: " + anError.getErrorCode());
                        Log.e("DeepL error", "Response body: " + anError.getErrorBody());
                        Toast.makeText(getApplicationContext(),"Failed to retrieve available languages from DeepL API",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //Method to setup the language spinners fram the DeepL API
    public void setupSpinners(String jsonString){

        languages.add(new Language("Detect Language","dl"));
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String language = jsonObject.getString("language");
                String name = jsonObject.getString("name");
                final Language languageObj = new Language(name,language);
                languages.add(languageObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Add the names of the languages to the spinners
        ArrayAdapter spinnerAdapter= new ArrayAdapter<Language>(getApplicationContext(),android.R.layout.simple_spinner_item, languages);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        originalLang.setAdapter(spinnerAdapter);
        translatedLang.setAdapter(spinnerAdapter);

        originalLang.setSelection(0);
        translatedLang.setSelection(10);

        originalLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                originalLangSelected = parent.getItemAtPosition(position).toString();
                originalText.setHint(originalLangSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        translatedLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetLangSelected = parent.getItemAtPosition(position).toString();
                translatedText.setHint(targetLangSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Method to get the code for each language to be used in the API call when translating
    public String getSelectedLangCode(String selectedLang){
        String code = "";
        for(Language lang:languages){
            if(lang.getName().equalsIgnoreCase(selectedLang)){
                code = lang.getLanguage();
            }
        }
        return code;
    }

    //Method called when the user clicks on the history button to launch the history activity
    public void onClickHistory(View view){
        Intent historyActivity = new Intent(this,HistoryActivity.class);
        ArrayList<Translation> translations = translationManager.getTranslations();
        historyActivity.putExtra("Translations",translations);
        startActivity(historyActivity);
    }

    //Method called when the user clicks on the swap icon to switch the selected languages in the spinners
    public void onClickSwap(View view){
        if(!originalLang.getSelectedItem().toString().equals("Detect Language")){
            originalLang.setSelection(findSelectedPos(targetLangSelected),true);
            translatedLang.setSelection(findSelectedPos(originalLangSelected),true);
        }else{
            Toast.makeText(this,"Please choose a language before swapping",Toast.LENGTH_SHORT).show();
        }
    }

    //Method to get the index of an element by name
    public int findSelectedPos(String name){
        for(int i = 0; i<languages.size(); i++){
            if(languages.get(i).getName().equalsIgnoreCase(name)){
                return i;
            }
        }
        return -1;
    }

    //Method when the user clicks on the character count button
    public void onClickCharCount(View view){
        Intent nextAct = new Intent(this,CountCharacters.class);
        startActivity(nextAct);
    }


    //Method to set the spinner to the source original langauge
    public void setSourceLang(String langName){
        for(int i = 0; i<languages.size(); i++){
            if(languages.get(i).getLanguage().equalsIgnoreCase(langName)){
                originalLang.setSelection(i);
                return;
            }
        }
        Toast.makeText(this,"Could not identify the source language for this text",Toast.LENGTH_SHORT).show();
    }

    //Method to get the translated text from JSON response
    public void translateText(String jsonString, String textToTranslate, String targetLangCode){
       try{
           JSONObject jsonObject = new JSONObject(jsonString);
           JSONArray translationsArray = jsonObject.getJSONArray("translations");
           JSONObject firstTranslation = translationsArray.getJSONObject(0);
           String sourceLanguage = firstTranslation.getString("detected_source_language");
           String translatedTextStr = firstTranslation.getString("text");
           setSourceLang(sourceLanguage);
           translatedText.setText(translatedTextStr);
           translationManager.addTranslation(targetLangCode,textToTranslate,translatedText.getText().toString());
       }catch(JSONException e){
           e.printStackTrace();
       }

    }

    //Method to translate without specifying source language
    public void translateWithoutSourceLang(String textToTranslate, String targetLangCode){
        AndroidNetworking.post("https://api-free.deepl.com/v2/translate")
                .addHeaders("Authorization",AUTH_KEY)
                .addQueryParameter("text",textToTranslate)
                .addQueryParameter("target_lang",targetLangCode)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String jsonString = response.toString();
                        translateText(jsonString,textToTranslate,targetLangCode);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(),"Failed to translate object",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Method to translate with source language
    public void translateWithSource(String textToTranslate,String sourceLangCode, String targetLangCode){
        AndroidNetworking.post("https://api-free.deepl.com/v2/translate")
                .addHeaders("Authorization",AUTH_KEY)
                .addQueryParameter("text",textToTranslate)
                .addQueryParameter("source_lang",sourceLangCode)
                .addQueryParameter("target_lang",targetLangCode)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String jsonString = response.toString();
                        translateText(jsonString,textToTranslate,targetLangCode);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(),"Failed to translate object",Toast.LENGTH_SHORT).show();
                    }
                });
    }


}