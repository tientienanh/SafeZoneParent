package com.example.activity.safezoneparent;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Tien on 01-Dec-15.
 */
public class RouteAdapter extends ArrayAdapter<Route> implements View.OnClickListener{

    List<Route> routes;
    int layout;
    Context context;
    Activity activity;
    int positon;
    static final int TIME_DIALOG_ID = 999;
    public static int positionClickedEdit;
    public static final String ITEM_POSITION = "positionItem";
    public RouteAdapter(Context context, int layout, List<Route> routes, Activity activity) {
        super(context, layout, routes);
        this.routes = routes;
        this.layout = layout;
        this.context = context;
        this.activity = activity;
    }
    Button btnTimeFrom;
    Button btnTimeTo;
    EditText edtLocation;
    EditText edtRadius;
    Button btnSet_position;
    Button btn_Edit;
    ImageButton btnDelete;
    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.row_route_layout, parent, false);

        String timeFrom = routes.get(position).getTimeFrom();
        String timeTo = routes.get(position).getTimeTo();
        String location = routes.get(position).getAddress();
        String radius = routes.get(position).getRadius();

//        btnTimeFrom = (EditText) theView.findViewById(R.id.edtTime_From);
        btnTimeFrom = (Button) theView.findViewById(R.id.btnTime_From);

        btnTimeTo = (Button) theView.findViewById(R.id.btnTime_To);
        edtLocation = (EditText) theView.findViewById(R.id.edtPosition_Route);
        edtRadius = (EditText) theView.findViewById(R.id.edtRadius);
        btnSet_position = (Button) theView.findViewById(R.id.btnSet_Position);
        btn_Edit = (Button) theView.findViewById(R.id.btnEdit);
        btnDelete = (ImageButton) theView.findViewById(R.id.imgBtnDelete);

//        btnTime = (Button)theView.findViewById(R.id.btnTime);

        btnTimeFrom.setText(timeFrom);
        btnTimeTo.setText(timeTo);
        edtLocation.setText(location);
        edtRadius.setText(radius);


        if (position == routes.size() - 1) {
            edtRadius.setEnabled(true);
            btnTimeFrom.setEnabled(true);
            btnTimeFrom.setText("00:00");
            btnTimeTo.setEnabled(true);
            btnTimeTo.setText("00:00");
            edtRadius.setEnabled(true);
            btnSet_position.setEnabled(true);
            btnDelete.setEnabled(false);
    }

        btnSet_position.setOnClickListener(this);
        btn_Edit.setOnClickListener(this);
        btnTimeFrom.setOnClickListener(this);
        btnTimeTo.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

//        btnTime.setOnClickListener(this);
        return theView;
    }


    String hourStr = "";
    String minuteStr = "";
    RouteHelper routeHelper = new RouteHelper(getContext());
    List<Route> rs;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        View view1 = (View) view.getParent();
        View parentView = (View) view1.getParent();
        ListView lv  = (ListView) parentView.getParent();
        final Button btnFrom = (Button) parentView.findViewById(R.id.btnTime_From);
        final Button btnTo = (Button) parentView.findViewById(R.id.btnTime_To);
        Button btnPosition = (Button) parentView.findViewById(R.id.btnSet_Position);
        EditText edtRa = (EditText) parentView.findViewById(R.id.edtRadius);

        switch (id) {
            case R.id.btnSet_Position:
                Toast.makeText(getContext(), "on click", Toast.LENGTH_LONG).show();
                Intent intentMapRoute = new Intent(getContext(), MapsActivity.class);
                intentMapRoute.putExtra("ChildName", ShowChildrenActivity.childrenNameClick);
                activity.startActivityForResult(intentMapRoute, RouteActivity.ROUTE_REQUEST_CODE);
                break;
            case R.id.btnEdit:

                positionClickedEdit =  lv.getPositionForView(parentView);
                // mode_update_insert = 1 when update

                edtRa.setEnabled(true);

                btnFrom.setEnabled(true);
                btnTo.setEnabled(true);
                btnPosition.setEnabled(true);
                RouteActivity.mode_update_insert = RouteActivity.MODE_UPDATE;
                break;
            case R.id.btnTime_From:
                TimePickerDialog mTimePicker = new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                        hourStr = convertTimeToString(selectHour);
                        minuteStr = convertTimeToString(selectMinute);
//                        Toast.makeText(getContext(),selectHour + ": " + selectMinute,Toast.LENGTH_LONG).show();
                        btnFrom.setText(hourStr + ":" + minuteStr);
                    }
                },0,0,true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.btnTime_To:
                TimePickerDialog nTimePicker = new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                        hourStr = convertTimeToString(selectHour);
                        minuteStr = convertTimeToString(selectMinute);
//                        Toast.makeText(getContext(),selectHour + ": " + selectMinute,Toast.LENGTH_LONG).show();
                        btnTo.setText(hourStr + ":" + minuteStr);
                    }
                },0,0,true);
                nTimePicker.setTitle("Select Time");
                nTimePicker.show();
                break;
            case R.id.imgBtnDelete:
                int positionDelete = lv.getPositionForView(parentView) - lv.getFirstVisiblePosition();
                Route r = RouteActivity.routes.get(positionDelete);
                rs = routeHelper.get();
                routeHelper.delete(r);
                RouteActivity.routes.remove(positionDelete);
                RouteActivity.adapter.notifyDataSetChanged();
                rs = routeHelper.get();
                break;
        }

    }

    private String convertTimeToString(int time) {
        String timeStr = "";
        if(time >= 0 && time <= 9) {
            timeStr = "0" + String.valueOf(time);
        } else {
            timeStr = String.valueOf(time);
        }

        return  timeStr;
    }
}
