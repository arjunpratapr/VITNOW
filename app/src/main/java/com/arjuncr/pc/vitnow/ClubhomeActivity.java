package com.arjuncr.pc.vitnow;

/**
 * Created by Pc on 26-Apr-16.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arjuncr.pc.vitnow.app.AppConfig;
import com.arjuncr.pc.vitnow.app.AppController;
import com.arjuncr.pc.vitnow.sessionandsql.SQLiteHandler;
import com.arjuncr.pc.vitnow.sessionandsql.Sessions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClubhomeActivity extends Activity {
    private static final String TAG1 = ClubhomeActivity.class.getSimpleName();

    private EditText txtClubName;
    private EditText txtEvent;
    private EditText txtDate;
    private EditText txtVenue;
    private EditText txtPhno;
    private EditText txtdesc;
    private Button btnsclub;
    private Button logout;
    private SQLiteHandler db;
    private Sessions session;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubhome);

        txtClubName = (EditText) findViewById(R.id.clubname);
        txtDate = (EditText) findViewById(R.id.ddate);
        txtEvent = (EditText) findViewById(R.id.devent);
        txtVenue = (EditText) findViewById(R.id.dvenue);
        txtPhno = (EditText) findViewById(R.id.dphno);
        txtdesc = (EditText) findViewById(R.id.eventdesc);
        btnsclub = (Button) findViewById(R.id.btnClub);
        logout = (Button) findViewById(R.id.logout);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        db = new SQLiteHandler(getApplicationContext());


        session = new Sessions(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        btnsclub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String clubname = txtClubName.getText().toString().trim();
                String event = txtEvent.getText().toString().trim();
                String date = txtDate.getText().toString().trim();
                String venue = txtVenue.getText().toString().trim();
                String phno = txtPhno.getText().toString().trim();
                String desc = txtdesc.getText().toString().trim();

                if (!clubname.isEmpty() && !event.isEmpty() && !date.isEmpty() && !venue.isEmpty() && !phno.isEmpty() && !desc.isEmpty()) {
                    registerEvent(clubname, event, date, venue, phno, desc);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter all the details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });



    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerEvent(final String clubname, final String event, final String date, final String venue, final String phno, final String descp) {

        String tag_string_req = "req_register";

        pDialog.setMessage("Adding Event ...");
        showDialog();

        StringRequest strReq1 = new StringRequest(Request.Method.POST, AppConfig.URL_EVENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG1, "Event Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        String eid = jObj.getString("eid");

                        JSONObject events = jObj.getJSONObject("event");
                        String clubname = events.getString("clubname");
                        String event = events.getString("event");
                        String date = events.getString("date");
                        String venue = events.getString("venue");
                        String phno = events.getString("phno");
                        String descp = events.getString("descp");



                        db.addEvent(clubname, event, eid, date, venue, phno, descp);

                        Toast.makeText(getApplicationContext(), "Event Successfully Created. Click to Logout!", Toast.LENGTH_LONG).show();


                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    } else {


                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG1, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("clubname", clubname);
                params.put("event", event);
                params.put("date", date);
                params.put("venue", venue);
                params.put("phno", phno);
                params.put("descp", descp);

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq1, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();


        Intent intent = new Intent(ClubhomeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}