package com.christian_hasbani.deepl_api_app;


import java.io.Serializable;

public class Translation implements Serializable {
    private String lang;
    private String text;
    private String translatedText;

    public Translation(String detectedSourceLanguage, String text,String translatedText) {
        this.lang = detectedSourceLanguage;
        this.text = text;
        this.translatedText = translatedText;
    }

    public String getLang(){
        return lang;
    }

    public String getText(){
        return text;
    }

    public String getTranslatedText(){
        return translatedText;
    }
}


