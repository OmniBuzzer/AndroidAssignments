package com.example.androidapplications;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class WeatherForecast extends AppCompatActivity {

    ProgressBar progressBar;
    TextView currentTempText;
    TextView minTempText;
    TextView maxTempText;
    ImageView weatherPic;

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

        progressBar.setVisibility(View.VISIBLE);

        new ForecastQuery().execute();
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
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=c281a4bb058894c187d911bbf348853b&mode=xml&units=metric";
            String iconName = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
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

                Log.i("WeatherForecast", "Current: " + current_temp + ", Min: " + min_temp + ", Max: " + max_temp + ", Icon: " + iconName);

                // Handle the weather icon (load from disk or download)
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
                        String imageURL = "http://openweathermap.org/img/w/" + iconName + ".png";
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
            currentTempText.setText("Current Temperature: " + current_temp + "°C");
            minTempText.setText("Min Temperature: " + min_temp + "°C");
            maxTempText.setText("Max Temperature: " + max_temp + "°C");
            weatherPic.setImageBitmap(weather_icon);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}