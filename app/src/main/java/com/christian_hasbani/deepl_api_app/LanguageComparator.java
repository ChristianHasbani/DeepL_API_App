package com.christian_hasbani.deepl_api_app;


import java.util.Comparator;

//This class is used to compare 2 languages in order to sort the languages in ascending order
public class LanguageComparator implements Comparator<Language> {

    @Override
    public int compare(Language o1, Language o2) {
        return o1.getName().compareTo(o2.getName());
    }
}