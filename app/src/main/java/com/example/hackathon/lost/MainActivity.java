package com.example.hackathon.lost;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;


public class MainActivity extends ActionBarActivity {
    public static final String API_KEY="AIzaSyA8F60HygLJWrkeezbTvj392tDuf7s1aW0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDirections(View button){
        EditText originText = (EditText) findViewById(R.id.fromLocation);
        String origin = originText.getText().toString();
        EditText destinationText = (EditText) findViewById(R.id.toLocation);
        String destination = destinationText.getText().toString();
        String URL = String.format("https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",origin, destination, API_KEY);
        System.out.println(URL);
        new GetDirectionsTask().execute(URL);
    }
}
