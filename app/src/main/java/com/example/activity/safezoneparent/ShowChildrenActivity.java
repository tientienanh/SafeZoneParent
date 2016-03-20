package com.example.activity.safezoneparent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ShowChildrenActivity extends AppCompatActivity {
    ListView lvShowChildren;
    String currentParentUser;
    public static String childrenNameClick;
    final List<String> childrentUserList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    public static final String CHILD_NAME = "child_name";
    public static final String PARENT_NAME = "parent_name";
    public static final String PARENT_PASS = "parent_pass";
    public static final String CHILD_LATITUDE = "child_latitude";
    public static final String CHILD_LONGITUDE = "child_longitude";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_children);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        currentParentUser = b.getString("ParrentUser"); // gui tu LoginMotherActivity qua
        lvShowChildren = (ListView) findViewById(R.id.lvShowChildren);
        // lay het cac con cua tai khoan nay dua vao List

        QueryAccount queryAccount = new QueryAccount();
        queryAccount.queryAccount("Children", new QueryAccount.QueryRouteCallBack() {
            @Override
            public void queryRouteSuccess(List<ParseObject> parseObjects) {
                for (int i = 0;i <parseObjects.size();i++) {
                    String parentUserParse = parseObjects.get(i).getString(PARENT_NAME);
                    if (parentUserParse.equals(currentParentUser)) {
                        String chidrenUserParse = parseObjects.get(i).getString(CHILD_NAME);
                        childrentUserList.add(chidrenUserParse);
                    }
                }
                // dua childrenUserList vao adapter
//                adapter = new ArrayAdapter<String>(ShowChildrenActivity.this,android.R.layout.simple_list_item_1, childrentUserList);
                adapter = new ChildrenAdapter(ShowChildrenActivity.this, R.layout.row_layout_showchildren,childrentUserList);
                lvShowChildren.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        lvShowChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                childrenNameClick = childrentUserList.get(position);
                Intent intentMother = new Intent(ShowChildrenActivity.this, MotherActvity.class);
                intentMother.putExtra("UserChildren", childrentUserList.get(position));
                startActivity(intentMother);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_children, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
           dialogLogout();
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
}
