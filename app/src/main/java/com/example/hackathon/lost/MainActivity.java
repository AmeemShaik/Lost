package com.example.hackathon.lost;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;


public class MainActivity extends ActionBarActivity {
    public static final String API_KEY="AIzaSyA8F60HygLJWrkeezbTvj392tDuf7s1aW0";
    private static final int CONTACT_PICKER_RESULT = 1001;

    private TextView text2;

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
        LinearLayout directionView = (LinearLayout)findViewById(R.id.directions_layout);
        directionView.removeAllViews();
        new GetDirectionsTask().execute(URL);


    }
    public void launchContactPicker(View button){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent,CONTACT_PICKER_RESULT);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String phone = "";
                    try{
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                        cursor.moveToFirst();
                        String cNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();
                        TextView contactText = (TextView)findViewById(R.id.contact);
                        contactText.setText(cNumber);
                    }catch(Exception e){

                    }
                    break;
            }

        }
        else{

        }
    }
    public class GetDirectionsTask extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            String URL = params[0];
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpClient.execute(new HttpGet(URL));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }




        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);


            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray jsonSteps = jsonData.getJSONArray("routes")
                        .getJSONObject(0).getJSONArray("legs")
                        .getJSONObject(0).getJSONArray("steps");

                JSONObject jsonCurrent = null;
                String current = null;
                LinearLayout directionList = (LinearLayout) findViewById(R.id.directions_layout);

                for(int i = 0; i < jsonSteps.length(); i++){
                    jsonCurrent = jsonSteps.getJSONObject(i);
                    current = jsonCurrent.getString("html_instructions").replaceAll("\\<.*?\\>", "");
                    System.out.println(current);
                    TextView currentView = new TextView(getApplicationContext());
                    currentView.setText(current);
                    currentView.setTextColor(Color.BLACK);
                    directionList.addView(currentView);
                }
//                TextView textView = (TextView) findViewById(R.id.textView);
//                textView.setText(current);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //text2 = (TextView) findViewById(R.id.text2);

        }
    }
}
