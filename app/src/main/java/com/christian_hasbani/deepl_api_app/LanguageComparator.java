package com.christian_hasbani.deepl_api_app;


import java.util.Comparator;

public class LanguageComparator implements Comparator<Language> {

    @Override
    public int compare(Language o1, Language o2) {
        return o1.getName().compareTo(o2.getName());
    }
}