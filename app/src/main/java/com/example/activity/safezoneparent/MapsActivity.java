package com.example.activity.safezoneparent;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements View.OnClickListener{
    double dLatitude, dLongitude;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    EditText edtSearch;
    Button btnSearch, btnDone;
    public static final String ADDRESS = "address";
    public static final String DLATITUTE = "dLatitute";
    public static final String DLONGTITUTE = "dLongtitute";
    double parentLat;
    double parentLong;
    double childrenLat;
    double childrenLong;
    List<String> polylineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        edtSearch = (EditText) findViewById(R.id.edtSearchMap);
        btnDone = (Button) findViewById(R.id.btnDone);
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtSearch, 0);
        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        String childUser = b.getString("ChildName");
        parentLat = b.getDouble(MotherActvity.PARENT_LAT);
        parentLong = b.getDouble(MotherActvity.PARENT_LONG);
        childrenLat = b.getDouble(MotherActvity.CHILDREN_LAT);
        childrenLong = b.getDouble(MotherActvity.CHILDREN_LONG);
        dLatitude = childrenLat;
        dLongitude = childrenLong;

        //load on map
        setUpMapIfNeeded();

        // draw polyline
        PolylineAsynctask polylineAsynctask = new PolylineAsynctask(null);
        polylineAsynctask.mCallback = new Callback() {
            @Override
            public void callback(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.d("MSG", status);
                    if (status.equals("OK")) {
                        polylineList = getPolyline(jsonObject);
                        decodeListPolyline(polylineList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

         String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                 String.valueOf(parentLat) + "," + String.valueOf(parentLong) + "+&destination="
                 + String.valueOf(childrenLat) + "," + String.valueOf(childrenLong) +
                 "&key=AIzaSyDattAic5XN9hH94xGgMTpY-BVnTxubs3A";
        Log.d("URL", url);

//        polylineAsynctask.execute("https://maps.googleapis.com/maps/api/directions/json?origin=" +
//                "10.7656618,106.6839945+&destination=10.7594044,106.6753839&key=AIzaSyDattAic5XN9hH94xGgMTpY-BVnTxubs3A");
        polylineAsynctask.execute(url);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSearch) {
            onSearch();
        }
        else
            if (id == R.id.btnDone) {
                Bundle bundle = getIntent().getExtras();
//                int positionClick = bundle.getInt(RouteAdapter.ITEM_POSITION);
                Intent intentAddress = new Intent();
//                intentAddress.putExtra(RouteAdapter.ITEM_POSITION, positionClick); // send item clicked
                intentAddress.putExtra(ADDRESS, addressStr); // send address update to listView
                intentAddress.putExtra(DLATITUTE,dLatitude); // dens Latlng to update to DB
                intentAddress.putExtra(DLONGTITUTE, dLongitude);
                setResult(Activity.RESULT_OK, intentAddress);
                finish();
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

  /*  public void ParseQuery(String userChild){
        QueryByUser queryByUser = new QueryByUser();
        queryByUser.queryByuser("Children", "child_name", userChild, new QueryByUser.QueryByUserCallBack() {
            @Override
            public void queryByUserSuccess(ParseObject parseObject) {
                if (parseObject != null) {
                    dLatitude = parseObject.getDouble("child_latitude");
                    dLongitude = parseObject.getDouble("child_longitude");

                    setUpMapIfNeeded();
                    mMap.setMyLocationEnabled(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else
                    Toast.makeText(MapsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

        });
        }*/




    private void setUpMapIfNeeded() {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                getCurrentLocation();
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }


    private String addressStr = ""; // ten dia chi
    void getCurrentLocation() {
        this.dLatitude = dLatitude;
        this.dLongitude = dLongitude;
        // add marker and set for drag
        mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude)).title("It's Me!")).setDraggable(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 16));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng newPosition = marker.getPosition();
//                dLatitude = newPosition.latitude;
//                dLongitude   = newPosition.longitude;
                childrenLat = newPosition.latitude;
                childrenLong = newPosition.longitude;
                // lay dia chi tu Latlng
                Geocoder geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
//                    List<Address> addresses = geo.getFromLocation(dLatitude, dLongitude, 1);
                    List<Address> addresses = geo.getFromLocation(dLatitude, dLongitude, 1);
                    Address address = addresses.get(0);

                    int i = 0;
                    while (address.getAddressLine(i) != null) {
                        addressStr += address.getAddressLine(i) + ", ";
                        i++;
                    }
                    Toast.makeText(MapsActivity.this, "acdress: " + addressStr + "\n" + "lat: " + dLatitude
                            + "\n" + "long: " + dLongitude, Toast.LENGTH_LONG).show();
                    // set lai ten dia chi nay len EditText Search
                    edtSearch.setText(addressStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    /*private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }*/

    private void onSearch(){
        String locationName = edtSearch.getText().toString();
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(locationName,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
        dLatitude = latLng.latitude;
        dLongitude = latLng.longitude;
        setUpMapIfNeeded();
    }

    // parse the json to the polyline
    public List<String> getPolyline(JSONObject jsonObject) {
        List<String> listPolyline = new ArrayList<>();
        try {
            JSONArray routes = jsonObject.getJSONArray("routes");
            for (int i = 0; i < routes.length(); i++) {
                JSONObject routes_i = routes.getJSONObject(i);
                JSONArray legsArray = routes_i.getJSONArray("legs");
                for (int j = 0; j < legsArray.length(); j++) {
                    JSONObject leg_i = legsArray.getJSONObject(j);
                    JSONArray stepsArray = leg_i.getJSONArray("steps");
                    for (int k = 0; k < stepsArray.length(); k++) {
                        JSONObject step_i = stepsArray.getJSONObject(k);
                        JSONObject polylineJson = step_i.getJSONObject("polyline");
                        String polylineStr = polylineJson.getString("points");
                        listPolyline.add(polylineStr);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listPolyline;
    }

// the method to decode polyline
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public void decodeListPolyline(List<String> polylines){
        for(int i = 0;i<polylines.size();i++){
            String polyline = polylines.get(i); // lay ra duoc polylineStr
            // decoode polylineStr
            List<LatLng> pointsPoly= decodePoly(polyline);
            PolylineOptions polylineOptions =  new PolylineOptions();
            for(int j = 0;j<pointsPoly.size();j++){
                polylineOptions.add(pointsPoly.get(j));
            }
            Polyline line = mMap.addPolyline(polylineOptions);
        }
    }

}
