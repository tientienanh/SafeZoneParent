package com.example.activity.safezoneparent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class DetailChildren extends AppCompatActivity {

    EditText edtFullname, edtNickname, edtAge, edtGrade;
    RadioGroup groupGender;
    ImageView imageView;
    Button btnDone;

    private String fullname = "";
    private String nickname = "";
    private int age = 0;
    private int grade = 0;
    private int gender;
    private static final int MALE = 1;
    private static final int FEMALE = 0;
    private byte[] image = null;
    private int id = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_children);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtFullname = (EditText) findViewById(R.id.edtFullnameDetail);
        edtNickname = (EditText) findViewById(R.id.edtNicknameDetail);
        edtAge = (EditText) findViewById(R.id.edtAgeDetail);
        edtGrade = (EditText) findViewById(R.id.edtGradeDetail);
        groupGender = (RadioGroup) findViewById(R.id.groupGenderDetail);
        imageView = (ImageView) findViewById(R.id.imgChildrenDetail);
        btnDone = (Button) findViewById(R.id.btnDone);

        Bundle bundle = getIntent().getExtras();
        fullname = bundle.getString(Child.CHILD_FULLNAME);
        nickname = bundle.getString(Child.CHILD_NICKNAME);
        age = bundle.getInt(Child.CHILD_AGE);
        grade = bundle.getInt(Child.CHILD_GRADE);
        gender = bundle.getInt(Child.GENDER);
        image = bundle.getByteArray(Child.IMAGE);
        id = bundle.getInt(Child.ID);


        final ChildHelper childHelper = new ChildHelper(this);
        edtFullname.setText(fullname);
        edtNickname.setText(nickname);
        edtGrade.setText(String.valueOf(grade));
        edtAge.setText(String.valueOf(age));
        if (gender == 1) {
            groupGender.check(R.id.rdoMaleDetail);
        } else if (gender == 0) {
            groupGender.check(R.id.rdoFemaleDetail);
        }
        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setImageBitmap(bitmap);

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Child c = new Child(fullname, nickname, age, grade, gender, image);
                    childHelper.updateChild(c);
                }
            });
        }



       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
