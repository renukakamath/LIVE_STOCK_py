package com.example.livestock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class Registration extends AppCompatActivity implements JsonResponse {
    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    Button b1;
    String firstname,lastname,phone,email,longitude,latitude,username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        e1=(EditText) findViewById(R.id.firstname);
        e2=(EditText) findViewById(R.id.lastname);
        e3=(EditText) findViewById(R.id.phone);
        e4=(EditText) findViewById(R.id.email);
        e5=(EditText) findViewById(R.id.longitude);
        e6=(EditText) findViewById(R.id.latitude);
        e7=(EditText) findViewById(R.id.username);
        e8=(EditText) findViewById(R.id.password);
        b1=(Button) findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname=e1.getText().toString();
                lastname=e2.getText().toString();
                phone=e3.getText().toString();
                email=e4.getText().toString();
                longitude=e5.getText().toString();
                latitude=e6.getText().toString();
                username=e7.getText().toString();
                password=e8.getText().toString();
                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) Registration.this;
                String q ="/Registration?firstname="+firstname+"&lastname="+lastname+"&phone="+phone+"&email="+email+"&longitude="+longitude+"&latitude="+latitude+"&username="+username+"&password="+password;
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
                Toast.makeText(getApplicationContext(), "REGISTRATION SUCCESS", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Login.class));

            } else if (status.equalsIgnoreCase("duplicate")) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
                Toast.makeText(getApplicationContext(), "Username and Password already Exist...", Toast.LENGTH_LONG).show();

            } else {
                startActivity(new Intent(getApplicationContext(), Registration.class));

                Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}