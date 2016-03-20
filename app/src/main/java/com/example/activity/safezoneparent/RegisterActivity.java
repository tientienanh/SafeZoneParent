package com.example.activity.safezoneparent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen on 1/1/2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnRegister;
    EditText edtName, edtUsername, edtPassword, edtPhone, edtEmail;
    String nameParent, usernameParentRegister,passwordParentRegister, phoneParent, emailParent;
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String PASS = "pass";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtName = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);


        btnRegister.setOnClickListener(this);
    }

    List<ParentAccount> parentAccountList = new ArrayList<>();
    boolean isUserExist = false;
    @Override
    public void onClick(View view) {
        nameParent = edtName.getText().toString();
        usernameParentRegister = edtUsername.getText().toString();
        passwordParentRegister = edtPassword.getText().toString();
        phoneParent = edtPhone.getText().toString();
        emailParent = edtEmail.getText().toString();
        if (nameParent.equals("") || usernameParentRegister.equals("") || passwordParentRegister.equals("")
                || phoneParent.equals("") || emailParent.equals("")) {
            Toast.makeText(getBaseContext(), "Enter all data, please!", Toast.LENGTH_LONG).show();
        } else {
            // kiem tra username co trung k?
            QueryAccount queryAccount = new QueryAccount();
            queryAccount.queryAccount("ParentAccount", new QueryAccount.QueryRouteCallBack() {
                @Override
                public void queryRouteSuccess(List<ParseObject> parseObjects) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParentAccount parentAccount = new ParentAccount(parseObjects.get(i).getString(NAME), parseObjects.get(i).getString(USERNAME),
                                parseObjects.get(i).getString(PASS), parseObjects.get(i).getString(PHONE), parseObjects.get(i).getString(EMAIL));
                        if (parseObjects.get(i).getString(USERNAME).equals(usernameParentRegister)) {
                            isUserExist = true;
                            Log.d("", "Exist");
                            return;
                        }
//                    parentAccountList.add(parentAccount);
                    }
                    if (isUserExist == false) {
                        ParseObject parent = new ParseObject("ParentAccount");
                        parent.put(NAME, nameParent);
                        parent.put(USERNAME, usernameParentRegister);
                        parent.put(PASS, passwordParentRegister);
                        parent.put(PHONE, phoneParent);
                        parent.put(EMAIL, emailParent);
                        parent.saveInBackground();
                        Toast.makeText(getBaseContext(), "Register successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Username is exist!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            // neu chua ton tai -> dua thong tin len Parse
            Log.d("","Put Parse");

        }
    }
}
