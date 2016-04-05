package com.example.activity.safezoneparent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowChildrenActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvShowChildren;
    String currentParentUser;
    public static String childrenNameClick;
    List<Child> childrenList = new ArrayList<>();
    List<String> childrentUserList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    public static final String CHILD_NAME = "child_name";
    public static final String PARENT_NAME = "parent_name";
    public static final String PARENT_PASS = "parent_pass";
    public static final String CHILD_LATITUDE = "child_latitude";
    public static final String CHILD_LONGITUDE = "child_longitude";
    //    Button btnAdd;
    ChildHelper childHelper = new ChildHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_children);

//        btnAdd = (Button) findViewById(R.id.btnAddChildren);
//        btnAdd.setOnClickListener(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        currentParentUser = b.getString("ParrentUser"); // gui tu LoginMotherActivity qua
        lvShowChildren = (ListView) findViewById(R.id.lvShowChildren);

        getListName();

//        Log.d("A", "FN Create adapter");
        adapter = new ChildrenAdapter(this, R.layout.row_layout_showchildren, childrentUserList);
        lvShowChildren.setAdapter(adapter);
        Log.d("A", "FN Create adapter");

        lvShowChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Child childrenClick = childrenList.get(position);
                Intent intentDetail = new Intent(ShowChildrenActivity.this, DetailChildren.class);
                intentDetail.putExtra(Child.PARENT_USER, LoginMotherActivity.parent_user);
                intentDetail.putExtra(Child.CHILD_FULLNAME, childrenClick.getChild_fullname());
                intentDetail.putExtra(Child.CHILD_NICKNAME, childrenClick.getChild_nickname());
                intentDetail.putExtra(Child.GENDER, childrenClick.getGender());
                intentDetail.putExtra(Child.CHILD_AGE, childrenClick.getAge());
                intentDetail.putExtra(Child.CHILD_GRADE, childrenClick.getGrade());
                byte[] b = childrenClick.getImage();
                intentDetail.putExtra(Child.IMAGE, childrenClick.getImage());
                intentDetail.putExtra(Child.ID, childrenClick.getId());
                startActivity(intentDetail);
            }
        });
    }

    public void getListChild(Context context) {

        HashMap<String, String> hashMapListChild = new HashMap<>();
        hashMapListChild.put("PUT", "5");
        hashMapListChild.put("parent_user", LoginMotherActivity.parent_user);
        SocketAsynctask socketAsynctask = new SocketAsynctask(context);
        socketAsynctask.execute(hashMapListChild);
        socketAsynctask.socketResponse = new SocketAsynctask.SocketResponse() {
            @Override
            public void response(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Children");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonChild = jsonArray.getJSONObject(i);
                        String pr_user = jsonChild.getString(Child.PARENT_USER);
                        String fullname = jsonChild.getString("full_name");
                        String nickname = jsonChild.getString("nick_name");
                        int age = jsonChild.getInt("age");
                        int gender = jsonChild.getInt("gender");
                        int grade = jsonChild.getInt("grade");
                        int id = jsonChild.getInt("id");
                        ///////////NOTE************ add image
//                        String image = jsonChild.getString(Child.IMAGE);
                        Child c = new Child(id, fullname, nickname, age, grade, gender, null);
                        childrenList.add(c);
                        Log.d("A", "finish add");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }



    private void getListName() {
        GetListChild getListChild = GetListChild.getInstance();
        childrenList.clear();
        childrenList = getListChild.getListChild(this);
        getListChild.mGetListCallback = new GetListChild.GetlistCallback() {
            @Override
            public void getListCallback(List<Child> childList) {

                childrenList = childList;
                childrentUserList.clear();
                for (int i = 0; i < childrenList.size();i++) {
                    childrentUserList.add(0, childrenList.get(i).getChild_nickname());
                }

                adapter.notifyDataSetChanged();

                Log.d("A", "callback");
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_children, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                dialogLogout();
                break;
            case R.id.action_add:
                Intent intentAdd = new Intent(ShowChildrenActivity.this, AddChildrentActivity.class);
                startActivity(intentAdd);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        // cai dat lai
//        MainActivity.isLoginMother = false;
        // dung chay ngam
        stopMotherSrevice(getCurrentFocus());
        // quay ve man hinh login
        Intent intentBack = new Intent(ShowChildrenActivity.this, LoginMotherActivity.class);
        startActivity(intentBack);
        finish();
    }

    public void stopMotherSrevice(View v) {
        Intent intentServiceMother = new Intent(getBaseContext(), MotherService.class);
        stopService(intentServiceMother);
    }

    @Override
    public void onBackPressed() {
        dialogLogout();
    }

        public void dialogLogout(){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Logout?");
            dialog.setCancelable(false).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    logout();
                }
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        /*if (id == R.id.btnAddChildren) {
            Intent intentAdd = new Intent(ShowChildrenActivity.this, AddChildrentActivity.class);
            startActivity(intentAdd);*/
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String nick_add = data.getExtras().getString("NICK_NAME");
                byte[] image_add = data.getExtras().getByteArray("IMAGE");
                childrentUserList.add(0, nick_add);
                Log.d("A", "onActivityResult");
                childrenList.clear();
//                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("A","onResume");
//        getListName();
//        adapter = new ChildrenAdapter(ShowChildrenActivity.this, R.layout.row_layout_showchildren, childrentUserList);
//        adapter.notifyDataSetChanged();
    }
}
