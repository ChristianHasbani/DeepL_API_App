package com.christian_hasbani.deepl_api_app;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class TranslationManager {
    private static final String PREFS_NAME = "Translations";
    private static final String KEY_PREFIX = "Translation_";//prefix key for saving the translations
    private static final int MAX_TRANSLATIONS = 10;//Max number of translations saved

    private SharedPreferences sharedPrefs;//Shared Preferences file to be saved in


    public TranslationManager(Context context) {
        sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addTranslation(String language, String text, String translatedText) {
        int currentSize = sharedPrefs.getAll().size();
        SharedPreferences.Editor editor = sharedPrefs.edit();

        // Remove the oldest translation if we have reached the max size
        if (currentSize >= MAX_TRANSLATIONS) {
            Map<String, ?> allEntries = sharedPrefs.getAll();
            ArrayList<String> keys = new ArrayList<>(allEntries.keySet());
            Collections.sort(keys);
            editor.remove(keys.get(0));
        }

        // Add the new translation
        editor.putString(KEY_PREFIX + currentSize, language + "|" + text + " -> " + translatedText);
        editor.commit();
    }

    public ArrayList<Translation> getTranslations() {
        ArrayList<Translation> translations = new ArrayList<>();
        Map<String, ?> allEntries = sharedPrefs.getAll();
        ArrayList<String> keys = new ArrayList<>(allEntries.keySet());
        Collections.sort(keys, Collections.reverseOrder());

        for (String key : keys) {
            String[] langText = ((String) allEntries.get(key)).split("\\|");
            String [] text = langText[1].split("\\->");
            translations.add(new Translation(langText[0],text[0],text[1]));
        }

        return translations;
    }



}
