package com.example.activity.safezoneparent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ShowSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLocation = null;
    private Button btnWebHistory = null;
    private Button btnCallHistory = null;
    private Button btnTimetable = null;
    public static String childNickname = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLocation = (Button) findViewById(R.id.btnLocation);
        btnWebHistory = (Button) findViewById(R.id.btnWeb);
        btnCallHistory = (Button) findViewById(R.id.btnCall);
        btnTimetable = (Button) findViewById(R.id.btnTimetable);

        btnLocation.setOnClickListener(this);
        btnWebHistory.setOnClickListener(this);
        btnCallHistory.setOnClickListener(this);
        btnTimetable.setOnClickListener(this);

        // get nickname of child
        Bundle b = getIntent().getExtras();
        childNickname = b.getString(Child.CHILD_NICKNAME);

    }

    @Override
    public void onClick(View view) {
        int idBtn = view.getId();
        switch (idBtn) {
            case R.id.btnLocation:
                Intent intentLocation = new Intent(ShowSelectionActivity.this, RouteActivity.class);
                startActivity(intentLocation);
                break;
            case R.id.btnWeb:

                break;
            case R.id.btnCall:

                break;
            case R.id.btnTimetable:

                break;
        }
    }
}
