package com.example.activity.safezoneparent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvRoute;
    public static List<Route> routes;
    EditText edtLocation, edtRadius;
    String timeFrom, timeTo, location, radius;
    Button btnPosition, btnEdit, btnTimeFrom, btnTimeTo;
    ImageButton btnDelete;
    public static RouteAdapter adapter;
    String dLat;
    String dLong;
    public static final int ROUTE_REQUEST_CODE = 0;
    public static int mode_update_insert = 0; //default :insert
    public static final int MODE_UPDATE = 1;
    public static final int MODE_INSERT = 0;
    public static boolean mode_delete = false;  // khong delete
    String childrenName;
    int positionItem;

    final RouteHelper routeHelper = new RouteHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
//        Bundle b =  getIntent().getExtras();
//        childrenName = b.getString("childrenName");
        childrenName = ShowSelectionActivity.childNickname;

        lvRoute = (ListView) findViewById(R.id.listRoute);
        routes = new ArrayList<>();
//        routes = routeHelper.get(childrenName);
        getListRoute();
        addNullRoute();

        adapter = new RouteAdapter(RouteActivity.this,R.layout.row_route_layout,routes,this);
        lvRoute.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // insert or update
                Route r = getData();
                // dend to server
                if (mode_update_insert == MODE_INSERT) {
                    /*positionItem = lvRoute.getCount() - lvRoute.getFirstVisiblePosition() - 1;
                    Route r = getData(); // Get data client input
                    if (r != null) {
                        routes.add(routes.size() - 1, r);
                        // luu vao database
                        routeHelper.insert(r);
                    }*/

                    // add on server
                    HashMap<String, String> hashMapPutRoute = new HashMap<String, String>();
                    hashMapPutRoute.put("PUT", "8");
                    hashMapPutRoute.put("parent_user", LoginMotherActivity.parent_user);
                    hashMapPutRoute.put("children_user", ShowSelectionActivity.childNickname);
                    hashMapPutRoute.put(Route.LATITUTE, dLat);
                    hashMapPutRoute.put(Route.LONGITUTE, dLong);
                    hashMapPutRoute.put(Route.TIME_FROM, timeFrom);
                    hashMapPutRoute.put(Route.TIME_TO, timeTo);
                    hashMapPutRoute.put(Route.ADDRESS, location);
                    hashMapPutRoute.put(Route.RADIUS, radius);

                    SocketAsynctask socketAsynctask = new SocketAsynctask(RouteActivity.this);
                    socketAsynctask.execute(hashMapPutRoute);
                    socketAsynctask.socketResponse = new SocketAsynctask.SocketResponse() {
                        @Override
                        public void response(String resultJson) {
                            try {
                                JSONObject json = new JSONObject(resultJson);
                                String message = json.getString("RESULT");
                                if (message.equals("OK")) {
                                    Toast.makeText(RouteActivity.this, "insert route complete!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };




                } else {
                    positionItem = RouteAdapter.positionClickedEdit - lvRoute.getFirstVisiblePosition();
//                    Route r = getData(); // Get data client input to update
                    routes.remove(positionItem);
                    routes.add(positionItem, r);
                    // update trong db
                    routeHelper.delete(RouteAdapter.positionClickedEdit + 1);
                    routeHelper.insert(r);
                }
                adapter.notifyDataSetChanged();

            }
        });

    }
    private String address = "";
    public static int mode_setMap = 0;

    private Route getData() {
        View v = lvRoute.getChildAt(positionItem);
        Route r = null;

        btnTimeFrom = (Button) v.findViewById(R.id.btnTime_From);
        btnTimeTo = (Button) v.findViewById(R.id.btnTime_To);
        edtLocation = (EditText) v.findViewById(R.id.edtPosition_Route);
        edtRadius = (EditText) v.findViewById(R.id.edtRadius);
        btnPosition = (Button) v.findViewById(R.id.btnSet_Position);
        btnEdit = (Button) v.findViewById(R.id.btnEdit);
        btnDelete = (ImageButton) v.findViewById(R.id.imgBtnDelete);

        timeFrom = btnTimeFrom.getText().toString();
        timeTo = btnTimeTo.getText().toString();
        location = edtLocation.getText().toString();
        radius = edtRadius.getText().toString();
        // kiem tra xem gia tri nhap vao co null k
        if (timeFrom.equals("") || timeTo.equals("") || location.equals("") || radius.equals("")) {
            Toast.makeText(RouteActivity.this, "Enter all data, please!", Toast.LENGTH_SHORT).show();
        } else { // dua du lieu len Parse.com roi add vao list routes, luu vao databse
            
            //add to listRoute
            r = new Route();
            r.setTimeFrom(timeFrom);
            r.setTimeTo(timeTo);
            r.setAddress(location);
            r.setRadius(radius);
            r.setChildrenName(childrenName);
            if(mode_update_insert == MODE_INSERT) {
                r.setdLatitute(dLat);
                r.setdLongtitute(dLong);
            } else { // mode update
                if (mode_setMap == 0) {
                    dLat = routes.get(positionItem).getdLatitute();
                    dLong = routes.get(positionItem).getdLongtitute();
                    r.setdLatitute(dLat);
                    r.setdLongtitute(dLong);
                } else {
                    r.setdLatitute(dLat);
                    r.setdLongtitute(dLong);
                }
            }
            // sau khi xong, vo hieu hoa cac editText
            enableView(v, false);
        }
        return r;
    }


    private void getListRoute() {
        GetListRoute getListRoute = GetListRoute.getInstance();
        routes.clear();
        routes = getListRoute.GetListRoute(this);
        getListRoute.mGetListCallback = new GetListRoute.GetlistCallback() {
            @Override
            public void getListCallback(List<Route> routeList) {
                routes = routeList;
                adapter.notifyDataSetChanged();
            }
        };


    }

//    private int itemClick = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ROUTE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mode_setMap = 1;
                address = data.getExtras().getString(MapsActivity.ADDRESS);
                dLat = String.valueOf(data.getExtras().getDouble(MapsActivity.DLATITUTE));
                dLong = String.valueOf(data.getExtras().getDouble(MapsActivity.DLONGTITUTE));
                // update on ListRouts
                if (mode_update_insert == MODE_UPDATE) {
                    positionItem = RouteAdapter.positionClickedEdit - lvRoute.getFirstVisiblePosition();
                } else {
                    positionItem = lvRoute.getCount() - lvRoute.getFirstVisiblePosition() - 1;
                }
                routes.get(positionItem).setAddress(address);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        routes = routeHelper.get(childrenName);
//        addNullRoute();
        adapter.notifyDataSetChanged();
    }

    // lay du lieu tu Parse.com ve de load len ListVie

    @Override
    public void onClick(View view) {
    }

    void addNullRoute(){
        Route routeNull = new Route();
        routeNull.setTimeFrom("");
        routeNull.setTimeTo("");
        routeNull.setAddress("");
        routeNull.setRadius("");

        routes.add(routeNull);
    }

    private void enableView(View view, boolean isEnable){
        btnTimeFrom = (Button) view.findViewById(R.id.btnTime_From);
        btnTimeTo = (Button) view.findViewById(R.id.btnTime_To);
        edtLocation = (EditText) view.findViewById(R.id.edtPosition_Route);
        edtRadius = (EditText) view.findViewById(R.id.edtRadius);
        btnPosition = (Button) view.findViewById(R.id.btnSet_Position);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);

        btnTimeTo.setEnabled(isEnable);
        btnTimeFrom.setEnabled(isEnable);
//      edtLocation.setEnabled(isEnable);
        edtRadius.setEnabled(isEnable);
        btnPosition.setEnabled(isEnable);
    }

}
