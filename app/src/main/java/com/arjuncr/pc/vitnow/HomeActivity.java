package com.arjuncr.pc.vitnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Pc on 25-Apr-16.
 */
public class HomeActivity  extends Activity {

    private Button btnLoginPage;
    private Button btnHome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        btnLoginPage = (Button) findViewById(R.id.btnLoginPage);
        btnHome = (Button) findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1) {
                Intent i1 = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i1);
                finish();
            }
        });

        btnLoginPage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view2) {
                Intent i2 = new Intent(HomeActivity.this,
                        LoginActivity.class);
                startActivity(i2);
                finish();
            }
        });

    }
}


