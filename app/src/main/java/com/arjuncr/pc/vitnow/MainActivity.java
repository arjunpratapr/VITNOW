package com.arjuncr.pc.vitnow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class MainActivity extends Activity {
    ArrayAdapter<String> adapter;
    ArrayList<String> items;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.listv);
        items=new ArrayList<>();
        adapter=new ArrayAdapter(this, R.layout.item_layout,R.id.txt,items);
        listView.setAdapter(adapter);
    }


    public void onStart(){
        super.onStart();

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST,"http://vitnow.2fh.co/vitnow/displayevent.php",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){

                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        items.add(jsonObject.getString("event"));
                       } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array");
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

}