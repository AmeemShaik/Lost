package com.example.hackathon.lost;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by amshaik on 2/28/2015.
 */
public class GetDirectionsTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... params) {
        String URL = params[0];
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpClient.execute(new HttpGet(URL));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode()== HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            }
            else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }
        catch(IOException e){
            responseString = "Connection Error";
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        System.out.println(data);
    }
}
