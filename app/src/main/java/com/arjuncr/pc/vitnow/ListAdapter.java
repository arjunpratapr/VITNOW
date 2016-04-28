package com.arjuncr.pc.vitnow;

/**
 * Created by Pc on 27-Apr-16.
 */
import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.TextView;

import org.json.JSONException;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<JSONObject>{

    int vg;

    ArrayList<JSONObject> list;

    Context context;

    public ListAdapter(Context context, int vg, int id, ArrayList<JSONObject> list){

        super(context,vg, id,list);

        this.context=context;

        this.vg=vg;

        this.list=list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);

        TextView txtdevent=(TextView)itemView.findViewById(R.id.devent);

        TextView txtdvenue=(TextView)itemView.findViewById(R.id.dvenue);

        TextView txtddate=(TextView)itemView.findViewById(R.id.ddate);

        TextView txtdphno=(TextView)itemView.findViewById(R.id.dphno);

        try {

            txtdevent.setText(list.get(position).getString("event"));

            txtdvenue.setText(list.get(position).getString("venue"));

            txtddate.setText(list.get(position).getString("date"));

            txtdphno.setText(list.get(position).getString("phno"));



        } catch (JSONException e) {

            e.printStackTrace();

        }
        return itemView;

    }

}


