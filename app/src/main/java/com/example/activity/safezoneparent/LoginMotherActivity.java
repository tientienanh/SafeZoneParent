package com.example.activity.safezoneparent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginMotherActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtUserNameParent, edtPassParent;
    Button btnLoginParent = null;
    TextView tvRegisterParent, tvLostPassParent;
    public static String parentUserNameLogin;
    public static final String MODE_LOGIN = "3";
    public static final String USER_PARENT = "user_parent";
    public static final String PASS_PARENT = "pass_parent";
    public static final String KEY_LOGIN = "PUT";

    public static String parent_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login_mother);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        edtUserNameParent = (EditText) findViewById(R.id.edtUsernameParent);
        edtPassParent = (EditText) findViewById(R.id.edtPasswordParent);
        btnLoginParent = (Button) findViewById(R.id.btnLoginParent);
        tvRegisterParent = (TextView) findViewById(R.id.tvRegisterParent);
        tvLostPassParent = (TextView) findViewById(R.id.tvLostPasswordParent);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/segoescb.ttf");
        tvRegisterParent.setTypeface(tf);
        tvLostPassParent.setTypeface(tf);


        btnLoginParent.setOnClickListener(this);
        tvRegisterParent.setOnClickListener(this);
        tvLostPassParent.setOnClickListener(this);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    String userNameParent, passParent;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvRegisterParent:
                if (isNetworkAvailable(LoginMotherActivity.this) == false) {
                    diaLogInternet();
                } else {
                    Intent intentRegisterParent = new Intent(LoginMotherActivity.this, RegisterActivity.class);
                    startActivity(intentRegisterParent);
                }
                break;
            case R.id.btnLoginParent:
                if (isNetworkAvailable(LoginMotherActivity.this) == false) {
                    diaLogInternet();
                } else {
                    loginParent();
                    isAccountTrue = false;
                }
                break;
        }
    }

    public void diaLogInternet(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Check the Internet!");
        dialog.setCancelable(false).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    boolean isAccountTrue = false;
    SocketAsynctask socketAsynctask;
    HashMap<String, String> hashMapLogin = new HashMap<>();
    public void loginParent() {
        userNameParent = edtUserNameParent.getText().toString();
        passParent = edtPassParent.getText().toString();
        if (userNameParent.equals("") || passParent.equals("")) {
            Toast.makeText(getBaseContext(), "Enter all data, please!", Toast.LENGTH_SHORT).show();
        } else {
            // send mode login
            hashMapLogin.put(KEY_LOGIN, MODE_LOGIN);
            hashMapLogin.put(USER_PARENT, userNameParent);
            hashMapLogin.put(PASS_PARENT, passParent);
            socketAsynctask = new SocketAsynctask(this);
            socketAsynctask.execute(hashMapLogin);
            socketAsynctask.socketResponse = new SocketAsynctask.SocketResponse() {
                @Override
                public void response(String strJson) {
                    String result = "";
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        result = jsonObject.getString("RESULT");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                        if (result.equals("LOGIN_OK")) {

                            // dang nhap thanh cong -> chạy ngam + so sanh
                            Toast.makeText(getBaseContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                            parent_user = userNameParent;
                            Intent intentMotherLogin = new Intent(getBaseContext(), ShowChildrenActivity.class);
                            intentMotherLogin.putExtra("ParrentUser", userNameParent);
                            startActivity(intentMotherLogin);
                        } else {
                            Toast.makeText(getBaseContext(), "Username or Password incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    }

            };
            // reset editText
            edtUserNameParent.setText("");
            edtPassParent.setText("");
        }
    }



    public void startMotherSrevice(View v) {
        Intent intentServiceMother = new Intent(getBaseContext(), MotherService.class);
        startService(intentServiceMother);
    }
}
