package com.christian_hasbani.deepl_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    private TextView charCountText, percentageText;
    private EditText authKeyText;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_characters);
        initVariables();
    }

    //Method to activaite when the user clicks on the get character count button
    public void onClickGetCharCount(View view){
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
                                String charCount = jsonObj.getString("character_count");
                                String charLimit = jsonObj.getString("character_limit");
                                updateView(charCount,charLimit);
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
        charCountText = findViewById(R.id.charCountText);
        authKeyText = findViewById(R.id.authKeyText);
        pb = findViewById(R.id.progressBar);
        percentageText = findViewById(R.id.percentageText);
        pb.setVisibility(View.INVISIBLE);
        percentageText.setVisibility(View.INVISIBLE);
    }

    //After getting the authentication key display
    public void updateView(String charCount, String charLimit){
        charCountText.setText("Characters used: " +charCount + "/" + charLimit);
        double progress = (Double.valueOf(charCount)/Double.valueOf(charLimit) )* 100;
        pb.setProgress((int)progress,true);
        percentageText.setText(percentageText.getText().toString() + progress + "%");
        percentageText.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
    }
}