package com.christian_hasbani.deepl_api_app;

public class Language {

    private String language;
    private String name;

    public Language(String name, String language){
        this.language = language;
        this.name = name;
    }



    public String getLanguage(){
        return language;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return name;
    }

}
