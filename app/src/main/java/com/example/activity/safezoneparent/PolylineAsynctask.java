package com.example.activity.safezoneparent;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tien on 08-Nov-15.
 */
public class PolylineAsynctask extends AsyncTask<String, Void, String> {
    String mUsername;
    String mPass;
    Callback mCallback;
    String mAccess;

    PolylineAsynctask(Callback callback){
//        mUsername = user;
//        mPass = pass;
        mCallback = callback;
    }


    @Override
    protected String doInBackground(String... params) {
        String response = "";
        try {

            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoInput(true);
//            connection.setDoOutput(true);

/*
            JSONObject jsonObject =  new JSONObject();
            jsonObject.put("user_name",mUsername);
            jsonObject.put("pass",mPass);
            jsonObject.put("APIKey","AfkxyA20fjAOiyfu");

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            bufferedWriter.write(jsonObject.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();*/
            connection.connect();
            int responseMessage = connection.getResponseCode();
            Log.d("MSG", responseMessage+"");
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader =  new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = bufferedReader.readLine()) != null){
                    response += line;
                }
                Log.d("MSG", response+"");
//                mCallback.callback();


            }
            else response = "";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mCallback.callback(s);
    }


}

interface Callback{
    void callback(String response);
}
