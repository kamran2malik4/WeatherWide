package com.example.weatherwide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    private EditText m_city;
    private Button m_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SharedPreferences preferences = getSharedPreferences("CityName", MODE_PRIVATE);
        String currentCity = preferences.getString("name", "");
        m_city = findViewById(R.id.search_city_edit_text);
        m_city.setText(currentCity);
        m_search = findViewById(R.id.search_city_button);
        m_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertCityIntoSharedPreference();
            }
        });
    }

    private void insertCityIntoSharedPreference(){
        String city = m_city.getText().toString();
        if(city.isEmpty()){
            Toast.makeText(this, "City cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            SharedPreferences preferences = getSharedPreferences("CityName", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", city);
            editor.apply();
        }
        finish();
    }


}
