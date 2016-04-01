package com.example.activity.safezoneparent;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Tien on 30-Mar-16.
 */
public class SocketAsynctask extends AsyncTask<HashMap, Void, String> {
    private Socket clientSocket;
    private static final String IP = "192.168.238.1";
    private static final int PORT = 4445;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    ProgressDialog progressDialog;

    SocketAsynctask(Context context) {
        progressDialog = new ProgressDialog(context);
    }
    SocketResponse socketResponse;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressBar.

        progressDialog.show();
    }

    HashMap<String, String> hashMapResult = new HashMap<>();
    @Override
    protected String doInBackground(HashMap... hashMaps) {
        HashMap<String, String> hashMap = hashMaps[0];
        String rsl = "";
        try {
            clientSocket = new Socket(IP, PORT);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(hashMap);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            hashMapResult = (HashMap) ois.readObject();
            rsl = hashMapResult.get("RESULT");
            Log.d("", "write");
            Log.d("", "RSL: " + rsl);
            oos.close();
            ois.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.cancel();
        socketResponse.response(result);
    }

    public interface SocketResponse {
        void response(String result);
    }
}
