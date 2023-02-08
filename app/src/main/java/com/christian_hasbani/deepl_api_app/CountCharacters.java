package com.christian_hasbani.deepl_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CountCharacters extends AppCompatActivity {

    //UI Components
    private EditText authKeyText;
    private ProgressBar pb;

    private String charCount,charLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_characters);
        initVariables();
    }

    //Method to activaite when the user clicks on the get character count button
    public void onClickGetCharCount(View view){
        load();
        //check if the user entered text in the authentication key edit text field
        if (!authKeyText.getText().toString().isEmpty()) {
            AndroidNetworking.get("https://api-free.deepl.com/v2/usage")
                    .addHeaders("Authorization","DeepL-Auth-Key " + authKeyText.getText().toString())
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String jsonStr = response.toString();
                            try {
                                JSONObject jsonObj = new JSONObject(jsonStr);
                                charCount = jsonObj.getString("character_count");
                                charLimit = jsonObj.getString("character_limit");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(getApplicationContext(),"Failed to get character count, invalid key!",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this,"Please enter your key before pressing on the button",Toast.LENGTH_SHORT).show();
        }
    }

    //Method to initialize the variables in the UI
    public void initVariables(){
        authKeyText = findViewById(R.id.authKeyText);
        pb = findViewById(R.id.progressBar);
//        pb.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    //Method to create a loading effect
    public void load(){
        pb.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int remaining = Integer.valueOf(charLimit) - Integer.valueOf(charCount);
                pb.setVisibility(View.INVISIBLE);
                new AlertDialog.Builder(CountCharacters.this)
                        .setTitle("Number of characters used")
                        .setMessage("You have used: "
                                + charCount + "/"
                                +charLimit +"\nYou still have " + String.valueOf(remaining)
                                + " characters")
                        .show();
            }
        },2000);
    }
}