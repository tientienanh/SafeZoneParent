package com.example.activity.safezoneparent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.List;

public class LoginMotherActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtUserNameParent, edtPassParent;
    Button btnLoginParent, btnRegisterParent, btnLostPassParent;
    public static String parentUserNameLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mother2);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        edtUserNameParent = (EditText) findViewById(R.id.edtUsernameParent);
        edtPassParent = (EditText) findViewById(R.id.edtPasswordParent);
        btnLoginParent = (Button) findViewById(R.id.btnLoginParent);
        btnRegisterParent = (Button) findViewById(R.id.btnRegisterParent);
        btnLostPassParent = (Button) findViewById(R.id.btnLostPasswordParent);

        btnLoginParent.setOnClickListener(this);
        btnRegisterParent.setOnClickListener(this);
        btnLostPassParent.setOnClickListener(this);

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
            case R.id.btnRegisterParent:
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

    public void loginParent() {
        userNameParent = edtUserNameParent.getText().toString();
        passParent = edtPassParent.getText().toString();
        QueryAccount queryAccount = new QueryAccount();
        if (userNameParent.equals("") || passParent.equals("")) {
            Toast.makeText(getBaseContext(), "Enter all data, please!", Toast.LENGTH_SHORT).show();
        } else {
            queryAccount.queryAccount("ParentAccount", new QueryAccount.QueryRouteCallBack() {

                @Override
                public void queryRouteSuccess(List<ParseObject> parseObjects) {
                    for (int i =0;i < parseObjects.size();i++) {
                        String usernameParse = parseObjects.get(i).getString(RegisterActivity.USERNAME);
                        String passParse = parseObjects.get(i).getString(RegisterActivity.PASS);
                        if (usernameParse.equals(userNameParent) && passParse.equals(passParent)) {
                            isAccountTrue = true;
                            break;
                        }
                    }

                    if (isAccountTrue == false) {
                        Toast.makeText(getBaseContext(), "Username or passwword is not correct!", Toast.LENGTH_SHORT).show();
                    } else {
                        // dang nhap thanh cong -> chạy ngam + so sanh
                        Toast.makeText(getBaseContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                        parentUserNameLogin = userNameParent;
                        startMotherSrevice(getCurrentFocus());
                        Intent intentMotherLogin = new Intent(getBaseContext(), ShowChildrenActivity.class);
                        intentMotherLogin.putExtra("ParrentUser", userNameParent);
                        startActivity(intentMotherLogin);
                        // login thanh cong thi chuyen mode login thanh true
//                        MainActivity.isLoginMother = true;
                        // set lai cac editText la null
                        edtUserNameParent.setText("");
                        edtPassParent.setText("");
                    }
                }
            });
        }

    }

    public void startMotherSrevice(View v) {
        Intent intentServiceMother = new Intent(getBaseContext(), MotherService.class);
        startService(intentServiceMother);
    }
}
