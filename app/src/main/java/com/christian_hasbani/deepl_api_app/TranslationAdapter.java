package com.christian_hasbani.deepl_api_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TranslationAdapter  extends ArrayAdapter<Translation> {
    //the list values in the List of type hero
    ArrayList<Translation> translations;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public TranslationAdapter(Context context, int resource, ArrayList<Translation> translations) {
        super(context, resource, translations);
        this.context = context;
        this.resource = resource;
        this.translations = translations;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, parent, false);

        //getting the view elements of the list from the view
        TextView langText = view.findViewById(R.id.langText);
        TextView originalText = view.findViewById(R.id.originalText);
        TextView translatedText = view.findViewById(R.id.translatedText);


        //getting the hero of the specified position
        Translation translation = translations.get(position);



        //adding values to the list item
        langText.setText(translation.getLang());
        originalText.setText(translation.getText());
        translatedText.setText(translation.getTranslatedText());

        //finally returning the view
        return view;
    }
}
