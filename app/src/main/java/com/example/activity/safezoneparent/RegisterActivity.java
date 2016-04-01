package com.example.activity.safezoneparent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nguyen on 1/1/2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnRegister;
    EditText edtName, edtUsername, edtPassword, edtPhone, edtEmail, edtPassConfirm;
    String fullname_parent, usernameParentRegister,
            passwordParentRegister, passConfirm, phoneParent, emailParent;
    public static final String FULL_NAME = "full_name";
    public static final String USERNAME = "user_name";
    public static final String PASS = "password";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";

    private final String urlInsert = "http://192.168.238.1:80/demo/insert.php";

    RequestQueue requestQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtName = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUserParent);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPassConfirm = (EditText) findViewById(R.id.edtPasswordConfirm);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btnRegister.setOnClickListener(this);
    }


    HashMap<String, String> hashMap;
    List<ParentAccount> parentAccountList = new ArrayList<>();
    boolean isUserExist = false;
    @Override
    public void onClick(View view) {
        fullname_parent = edtName.getText().toString();
        usernameParentRegister = edtUsername.getText().toString();
        passwordParentRegister = edtPassword.getText().toString();
        passConfirm = edtPassConfirm.getText().toString();
        phoneParent = edtPhone.getText().toString();
        emailParent = edtEmail.getText().toString();
        if (fullname_parent.equals("") || usernameParentRegister.equals("") || passwordParentRegister.equals("")
                || phoneParent.equals("") || emailParent.equals("")) {
            Toast.makeText(getBaseContext(), "Enter all data, please!", Toast.LENGTH_LONG).show();
        } else {
            // test if pass and pass confirm is not same
            if (!passwordParentRegister.equals(passConfirm)) {
                Toast.makeText(getBaseContext(), "the passwords are different!", Toast.LENGTH_SHORT).show();
            } else {
                //
                // insert
                hashMap = new HashMap<>();
                hashMap.put("GET", "2");
                hashMap.put("INSERT", "1");
                hashMap.put(FULL_NAME, fullname_parent);
                hashMap.put(USERNAME, usernameParentRegister);
                hashMap.put(PASS, passwordParentRegister);
                hashMap.put(PHONE, phoneParent);
                hashMap.put(EMAIL, emailParent);

                SocketAsynctask socketAsynctask = new SocketAsynctask(this);
                socketAsynctask.execute(hashMap);
                socketAsynctask.socketResponse = new SocketAsynctask.SocketResponse() {
                    @Override
                    public void response(String result) {
                        Log.d("R", ""+result);
                        Toast.makeText(getBaseContext(), ""+result, Toast.LENGTH_SHORT).show();
                    }
                };

                Log.d("", "execute");
            }
        }
    }
}
