package com.bfurns.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bfurns.R;
import com.bfurns.ziffylink.LoginActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;


/**
 * Created by LENOVO on 4/20/2016.
 */
public class CommonClass {
    Activity activity;
    public SharedPreferences settings;
    ProgressDialog dialog;
    public CommonClass(Activity activity){
        this.activity = activity;
        settings = activity.getSharedPreferences(MyPreferences.PREF_NAME, 0);
    }
    public void logOut(){
        settings.edit().clear().commit();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }
    public Typeface getCustomFont(){
        return Typeface.createFromAsset(activity.getAssets(), "LobsterTwo-BoldItalic.ttf");
    }

    public boolean containKeyInSession(String key){
        return  settings.contains(key);
    }
    public void setSession(String key, String value){
        settings.edit().putString(key,value).apply();
    }
    public String getSession(String key)

    {
        return settings.getString(key,"");
    }

    public void setSessionInt(String key, int value){

        settings.edit().putInt(key,value).apply();
    }
    public int getSessionInt(String key){

        return settings.getInt(key,0);
    }

    public String getSessionSerialize(String key, String defaultValue){
        return settings.getString(key,defaultValue);
    }
    public void setSessionBool(String key, boolean value){
        settings.edit().putBoolean(key,value).apply();
    }
    public void distroySession(String key) { settings.edit().remove(key).apply(); }
    public boolean getSessionBool(String key){
        return settings.getBoolean(key,false);
    }


    /**
     * END Session management
     */

    /** common user management classes
     *
     * @return
     */
    public String get_user_id(){
        return getSession(MyPreferences.USER_ID);
    }
    public boolean is_user_login(){
        String key = getSession(MyPreferences.USER_ID);
        if (key==null || key.equalsIgnoreCase("")){
            return  false;
        }else {
            return  true;
        }

    }
    /*public String get_user_data(String key){
        if (is_user_login()){
            try {
                JSONObject jsonObject = new JSONObject(getSession(ApiParams.USER_DATA));
                if (jsonObject.has(key)){
                    return jsonObject.getString(key);
                }else{
                    return "";
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }
    */
    /** common user management classes
     * END
     * @return
     */




    public String totalTime(String dateString, String addString){
        String[] time = dateString.split(":");
        int Hour = Integer.parseInt(time[0]);
        int Minut = Integer.parseInt(time[1]);
        int Second = Integer.parseInt(time[2]);
        String[] timeadd = addString.split(":");
        int HourAdd = Integer.parseInt(timeadd[0]);
        int MinutAdd = Integer.parseInt(timeadd[1]);
        int SecondAdd = Integer.parseInt(timeadd[2]);

        Second = Second + SecondAdd;
        if (Second > 60){
            Minut = Minut + (Second / 60);
            Second = Second % 60;
        }
        Minut = Minut + MinutAdd;
        if(Minut > 60){
            Hour = Hour + (Minut / 60);
            Minut = Minut % 60 ;
        }

        Hour = Hour + HourAdd;
        return Hour+":"+Minut+":"+Second;
    }


    public boolean is_internet_connected(){
        ConnectivityManager cm =
                (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    public  void open_screen(int position){
        Intent intent = null;
        switch (position)
        {


        }
        if (intent!=null){
            activity.startActivity(intent);
        }
    }

    public void closeDialog(){
        dialog.dismiss();
    }

    public void setToastMessage(String message){
        Toast.makeText(activity,message, Toast.LENGTH_LONG).show();
    }

    public void menuNavigation(Activity act, MenuItem item){
        Intent intent = null;
        switch (item.getItemId()){
            case android.R.id.home:
                act.finish();
                break;

        }
        if (intent!=null){
            act.startActivity(intent);
        }
    }
    public  void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    /**
     * JSON to RESPONCE AND RESPONCE TO JSON
     * @return
     */
    public ArrayList<HashMap<String,String>> getArrayListFromJsonArray(JSONArray jsonArray){
        ArrayList<HashMap<String,String>> postItems = new ArrayList<>();
        for(int i = 0 ; i < jsonArray.length(); i++){
            try {
                postItems.add(getMapJsonObject(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return postItems;
    }
    public HashMap<String,String> getMapJsonObject(JSONObject jsonObject){
        HashMap<String,String> map = new HashMap<String, String>();
        Iterator<String> iter = jsonObject.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String value = jsonObject.getString(key);
                map.put(key,value);
            } catch (JSONException e) {
                // Something went wrong!
            }
        }
        return map;
    }
    public JSONObject getJsonMapObject(HashMap<String,String> hashMap){

        JSONObject jobj = new JSONObject();
        for (Object key : hashMap.keySet()) {
            try {
                jobj.put(key.toString(), hashMap.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jobj;
    }

    /**
     * END
     * @return
     */

    public String printDifference2(String parsedate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date startDate = new Date();
        Date endDate = null;
        try {
            endDate = dateFormat.parse(parsedate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //milliseconds
        long different =   startDate.getTime() - endDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;
        long monthInMilli = daysInMilli * 30;
        long yearInMilli = monthInMilli * 12;

        long elapsedYear = different / yearInMilli;
        different = different % yearInMilli;

        long elapsedMonths = different / monthInMilli;
        different = different % monthInMilli;

        long elapsedWeeks = different / weeksInMilli;
        different = different % weeksInMilli;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if(elapsedYear != 0){
            return String.format(Locale.ENGLISH, "%d years %d days",elapsedYear, elapsedDays);
        }else if(elapsedMonths != 0){
            return String.format(Locale.ENGLISH, "%d months %d days",elapsedMonths, elapsedDays);
        }else if(elapsedWeeks != 0){
            return String.format(Locale.ENGLISH, "%d weeks %d days",elapsedWeeks, elapsedDays);
        }else if (elapsedDays == 0 && elapsedHours ==0){
            return String.format(Locale.ENGLISH, "%d min %d sec",elapsedMinutes, elapsedSeconds);
        }else if (elapsedDays == 0){
            return String.format(Locale.ENGLISH, "%d hours %d min",elapsedHours, elapsedMinutes);
        }else if(elapsedDays != 0){
            return String.format(Locale.ENGLISH, "%d days %d hours",elapsedDays, elapsedHours);
        }
        return String.format(Locale.ENGLISH, "%d days, %d hours, %d min, %d sec%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }
}
