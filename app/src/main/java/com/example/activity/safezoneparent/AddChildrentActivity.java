package com.example.activity.safezoneparent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddChildrentActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imgChidren;
    ChildHelper childHelper;
    Button btnGet, btnAdd;
    EditText edtFullname, edtNickname, edtAge, edtGrade;
    public static final int MALE = 1;
    public static final int FEMALE = 0;
    RadioGroup radioGroupGender;
    String genderSelected;
    public static final String PARENT_USER = "parent_user";
    public static final String CHILD_FULLNAME = "full_name";
    public static final String CHILD_NICK = "nick_name";
    public static final String CHILD_GENDER = "gender";
    public static final String CHILD_AGE = "age";
    public static final String CHILD_GRADE = "grade";
    public static final String CHILD_IMAGE = "image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_childrent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnAdd = (Button) findViewById(R.id.btnSaveAdd);
        btnAdd.setOnClickListener(this);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnGet.setOnClickListener(this);

        edtFullname = (EditText) findViewById(R.id.edtFullnameChildren);
        edtNickname = (EditText) findViewById(R.id.edtNicknameChildren);
        edtAge = (EditText) findViewById(R.id.edtAgeChildren);
        edtGrade = (EditText) findViewById(R.id.edtGradeChildren);
        radioGroupGender = (RadioGroup) findViewById(R.id.groupGender);

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int selectedId) {
                RadioButton rdoChecked = (RadioButton) findViewById(selectedId);
                genderSelected = rdoChecked.getText().toString();
                Toast.makeText(AddChildrentActivity.this, "select: " + genderSelected, Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvTitle = (TextView) findViewById(R.id.tv_title_add);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/segoescb.ttf");
        tvTitle.setTypeface(tf);

        imgChidren = (ImageView) findViewById(R.id.imgChildren);

        imgChidren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddChildrentActivity.this, "Image clicked", Toast.LENGTH_SHORT).show();
                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "");
                Uri uri = Uri.fromFile(path);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, uri);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);

            }
        });
        childHelper = new ChildHelper(this);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    List<Child> childList = new ArrayList<Child>();
    HashMap<String, String> hashMapInsertChild = new HashMap<>();
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnGet) {
            childList = childHelper.getAll();

            byte[] bTest = childList.get(0).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bTest, 0, bTest.length);
            imgChidren.setImageBitmap(bitmap);
        } else if (id == R.id.btnSaveAdd) { //insert
            saveData();
            Intent i = new Intent(AddChildrentActivity.this, ShowChildrenActivity.class);
            setResult(RESULT_OK, i);
            i.putExtra("NICK_NAME", child.getChild_nickname());
            i.putExtra("IMAGE", child.getImage());
            startActivity(i);
//            finish();
            SocketAsynctask socketChild = new SocketAsynctask(this);
            socketChild.execute(hashMapInsertChild);
            socketChild.socketResponse = new SocketAsynctask.SocketResponse() {
                @Override
                public void response(String result) {
                    if (!result.equals("OK")) {
                        Toast.makeText(AddChildrentActivity.this, "The nickname of child is exist!", Toast.LENGTH_SHORT).show();
                    }
                }
            };
        }
    }

    public int getGender() {
        int gender = 0;
        if (genderSelected.equals("Male")) {
            gender = MALE;
        } else {
            gender = FEMALE;
        }
        return gender;
    }
    Child child = null;
    public long saveData() {
        long rs = 0;
        String fullname = edtFullname.getText().toString();
        String nickname = edtNickname.getText().toString();
        String ageStr = edtAge.getText().toString();
        String gradeStr = edtGrade.getText().toString();
//        byte[] byteImage = putImage();
        if (fullname.equals("") || nickname.equals("") || ageStr.equals("") ||
                gradeStr.equals("")) {
            Toast.makeText(AddChildrentActivity.this, "Something missing!", Toast.LENGTH_SHORT).show();
        } else {
            Child c =  childHelper.getChild(nickname);
            if (c != null) {
                Toast.makeText(AddChildrentActivity.this, "Nickname exist, try other!", Toast.LENGTH_SHORT).show();
            } else {
                int age = Integer.valueOf(ageStr);
                int grade = Integer.valueOf(gradeStr);
                int genderChild = getGender();
                child = new Child(fullname, nickname, age, grade, genderChild, imageByte);
                rs = childHelper.insert(child);

                // insert on DB online
                hashMapInsertChild.put("INSERT", "2");
                hashMapInsertChild.put(PARENT_USER, LoginMotherActivity.parent_user);
                hashMapInsertChild.put(CHILD_FULLNAME, fullname);
                hashMapInsertChild.put(CHILD_NICK, nickname);
                hashMapInsertChild.put(CHILD_AGE, ageStr);
                hashMapInsertChild.put(CHILD_GRADE, gradeStr);
                hashMapInsertChild.put(CHILD_GENDER, genderSelected);
                hashMapInsertChild.put(CHILD_IMAGE, String.valueOf(imageByte));
                if (rs != -1) {
                    Toast.makeText(AddChildrentActivity.this, "Insert Compaleted!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return rs;
    }


    // get the path of image and load on imageview
    String selectImagePath;
    Uri imageUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                imageUri = data.getData();
                // get path
                selectImagePath = imageUri.getPath();
                imgChidren.setImageURI(imageUri);
//                putImage();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // save image on DB
    byte[] imageByte;
    public byte[] putImage() {
        try {
            if (imageUri == null) {
                return null;
            } else {
                InputStream iStream = getContentResolver().openInputStream(imageUri);
                Bitmap bm = BitmapFactory.decodeStream(iStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageByte = baos.toByteArray();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imageByte;
    }
}
