package com.example.activity.safezoneparent;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

public class MotherActvity extends AppCompatActivity implements View.OnClickListener{
Button btnLotrinh, btnMap;
    String childrenUser;
    LatLng parentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_actvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLotrinh = (Button) findViewById(R.id.btnLotrinh);
        btnMap = (Button) findViewById(R.id.btnBando);

        btnMap.setOnClickListener(this);
        btnLotrinh.setOnClickListener(this);

//        btnLotrinh.setBackgroundColor();
//        btnMap.setBackgroundColor(Color.TRANSPARENT);

        Bundle b = getIntent().getExtras();
        childrenUser = b.getString("UserChildren");  // lay tu ShowChildrenActivity

        parentLocation = getCurrentLocation();
        ParseQuery(childrenUser);

    }

    LocationManager locationManager;
    boolean isGPSEnabled;
    Location location;
    double lat;
    double llong;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 30;

    // get parent location
    LatLng getCurrentLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        // check if GPS is on
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE,
                        new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {

                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {

                            }

                            @Override
                            public void onProviderEnabled(String s) {

                            }

                            @Override
                            public void onProviderDisabled(String s) {

                            }
                        });
                if (locationManager != null)
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    lat = location.getLatitude();
                    llong = location.getLongitude();
                }
            }
        }
        LatLng latLng = new LatLng(lat, llong);
        return  latLng;
    }

    double childLatitude;
    double childLongitude;
    public void ParseQuery(String userChild) {
        QueryByUser queryByUser = new QueryByUser();
        queryByUser.queryByuser("Children", "child_name", userChild, new QueryByUser.QueryByUserCallBack() {
            @Override
            public void queryByUserSuccess(ParseObject parseObject) {
                if (parseObject != null) {
                    childLatitude = parseObject.getDouble("child_latitude");
                    childLongitude = parseObject.getDouble("child_longitude");

                } else
                    Toast.makeText(MotherActvity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static final String PARENT_LAT = "parent latitude";
    public static final String PARENT_LONG = "parent longitude";
    public static final String CHILDREN_LAT = "children latitude";
    public static final String CHILDREN_LONG = "children longitude";
                                            @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnLotrinh:
                // to do something
                Intent intentRoute =  new Intent(MotherActvity.this,RouteActivity.class);
                intentRoute.putExtra("childrenName", childrenUser);
                startActivity(intentRoute);
                break;
            case R.id.btnBando:
                Intent intentMap = new Intent(MotherActvity.this,MapsActivity.class);
                // send parentLocation and Children Location
                intentMap.putExtra(PARENT_LAT, parentLocation.latitude);
                intentMap.putExtra(PARENT_LONG, parentLocation.longitude);
                intentMap.putExtra(CHILDREN_LAT, childLatitude);
                intentMap.putExtra(CHILDREN_LONG, childLongitude);
                intentMap.putExtra("ChildName", childrenUser);
                startActivity(intentMap);
                break;
        }

    }
}
