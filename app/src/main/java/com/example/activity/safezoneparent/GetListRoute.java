package com.example.activity.safezoneparent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tien on 09-Apr-16.
 */
public class GetListRoute {
    public static GetListRoute instance = new GetListRoute();

    public static GetListRoute getInstance(){
        return instance;
    }

    List<Route> routes = new ArrayList<>();
    GetlistCallback mGetListCallback;
    public List<Route> GetListRoute(Context context){

        HashMap<String, String> hashMapListRoute = new HashMap<>();
        hashMapListRoute.put("PUT", "7");
        hashMapListRoute.put("parent_user", LoginMotherActivity.parent_user);
        hashMapListRoute.put("children_user", ShowSelectionActivity.childNickname);
        SocketAsynctask socketAsynctask = new SocketAsynctask(context);
        socketAsynctask.execute(hashMapListRoute);
        socketAsynctask.socketResponse = new SocketAsynctask.SocketResponse() {
            @Override
            public void response(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Route");
                    routes.clear();
                    for (int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonRoute = jsonArray.getJSONObject(i);
                        String pr_user = LoginMotherActivity.parent_user;
                        String children_user = ShowSelectionActivity.childNickname;
                        int id = jsonRoute.getInt(Route.ID);
                        String latitude = jsonRoute.getString(Route.LATITUTE);
                        String longitude = jsonRoute.getString(Route.LONGITUTE);
                        String timeFrom = jsonRoute.getString(Route.TIME_FROM);
                        String timeTo = jsonRoute.getString(Route.TIME_TO);
                        String address = jsonRoute.getString(Route.ADDRESS);
                        String radius = jsonRoute.getString(Route.RADIUS);
                        Route r = new Route(timeFrom, timeTo, address, radius, latitude, longitude, children_user,id);
                        routes.add(r);
                    }


                    mGetListCallback.getListCallback(routes);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        return routes;
    }

    interface GetlistCallback {
        void getListCallback(List<Route> routes);
    }
}

