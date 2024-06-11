package com.example.livestock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

public class Userhome extends AppCompatActivity implements JsonResponse {

    Button b1 ,b2,b3,b4;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1=(Button) findViewById(R.id.viewfeed);
        b2=(Button) findViewById(R.id.viewpurchase) ;
        b3=(Button)findViewById(R.id.sendcomplaint) ;
        b4=(Button)findViewById(R.id.logout);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Userhome.this;
        String q ="/ViewProduct?lid="+Login.login_id;
        q = q.replace(" ","%20");
        JR.execute(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),viewfeed.class));

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Viewpurchase.class));
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),complaint.class));
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("Expired")) {
                Toast.makeText(getApplicationContext(), "Date Expired", Toast.LENGTH_LONG).show();



                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(Login.phone, null, "Expired Products In Your Cart", null,null);



//                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}