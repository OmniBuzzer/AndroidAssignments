package com.example.androidapplications;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherForecast extends AppCompatActivity {

    ProgressBar progressBar;
    TextView currentTempText;
    TextView minTempText;
    TextView maxTempText;
    ImageView weatherPic;
    Spinner citySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_forecast);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar);
        currentTempText = findViewById(R.id.current_temp_text);
        minTempText = findViewById(R.id.min_temp_text);
        maxTempText = findViewById(R.id.max_temp_text);
        weatherPic = findViewById(R.id.weather_pic);
        citySpinner = findViewById(R.id.city_dropdown);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.canadian_cities,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        citySpinner.setAdapter(adapter);
        
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                new ForecastQuery().execute(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        String min_temp;
        String max_temp;
        String current_temp;
        Bitmap weather_icon;

        @Override
        protected String doInBackground(String... strings) {
            String iconName = null;

            try {
                String city = strings[0];
                String userPickedCity = URLEncoder.encode(city, "UTF-8");
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + userPickedCity + ",ca&APPID=c281a4bb058894c187d911bbf348853b&mode=xml&units=metric";

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream inputStream = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStream, "UTF-8");

                int eventType = xpp.next();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName();

                        if (tagName.equals("temperature")) {
                            current_temp = xpp.getAttributeValue(null, "value");
                            publishProgress(25);

                            min_temp = xpp.getAttributeValue(null, "min");
                            publishProgress(50);

                            max_temp = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if (tagName.equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                        }
                    }
                    eventType = xpp.next();
                }

                if (iconName != null) {
                    String fileName = iconName + ".png";
                    Log.i("WeatherForecast", "Looking for icon file: " + fileName);

                    if (fileExistance(fileName)) {
                        Log.i("WeatherForecast", "Found image locally, loading from disk");
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(fileName);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        weather_icon = BitmapFactory.decodeStream(fis);
                    } else {
                        Log.i("WeatherForecast", "Image not found locally, downloading");
                        String imageURL = "https://openweathermap.org/img/w/" + iconName + ".png";
                        weather_icon = HTTPUtils.getImage(imageURL);
                        FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                        weather_icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                    publishProgress(100);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            currentTempText.setText(getString(R.string.current_temp_text, current_temp));
            minTempText.setText(getString(R.string.min_temp_text, min_temp));
            maxTempText.setText(getString(R.string.max_temp_text, max_temp));
            weatherPic.setImageBitmap(weather_icon);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}