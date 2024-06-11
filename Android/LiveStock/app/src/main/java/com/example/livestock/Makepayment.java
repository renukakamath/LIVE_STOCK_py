package com.example.livestock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class Makepayment extends AppCompatActivity implements JsonResponse{
    EditText e1;
    Button b1;
    String amount;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setContentView(R.layout.activity_makepayment);

        e1=(EditText) findViewById(R.id.amount);
        b1=(Button) findViewById(R.id.button5);
        e1.setText(Viewpurchase.amt);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount=e1.getText().toString();
                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) Makepayment.this;
                String q ="/Makepayment?amount="+amount+"&pid="+Viewpurchase.pid+"&date="+Viewpurchase.exdate+"&pdetails_id="+Viewpurchase.pdid;
                q = q.replace(" ","%20");
                JR.execute(q);

            }
        });

    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("success")) {
                Toast.makeText(getApplicationContext(), " SUCCESS", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Makepayment.class));

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}