package com.example.activity.safezoneparent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
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

        adapter = new ChildrenAdapter(this, R.layout.row_layout_showchildren, childrentUserList);
        lvShowChildren.setAdapter(adapter);

        /*lvShowChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                childrenNameClick = childrentUserList.get(position);
                Intent intentMother = new Intent(ShowChildrenActivity.this, MotherActvity.class);
                intentMother.putExtra("UserChildren", childrentUserList.get(position));
                startActivity(intentMother);
            }
        });*/
    }

    private void getListName() {
        childrenList = childHelper.getAll();
        for (int i = 0; i < childrenList.size();i++) {
            childrentUserList.add(0, childrenList.get(i).getChild_nickname());
        }
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
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getListName();
//        adapter = new ChildrenAdapter(ShowChildrenActivity.this, R.layout.row_layout_showchildren, childrentUserList);
        adapter.notifyDataSetChanged();
    }
}
