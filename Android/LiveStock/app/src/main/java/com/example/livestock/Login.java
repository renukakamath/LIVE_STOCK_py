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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements JsonResponse{
    EditText e1,e2;
    Button b1;
    TextView t1;
    public static String username,password,login_id,usertype,phone;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText) findViewById(R.id.username);
        e2=(EditText) findViewById(R.id.password);
        b1=(Button) findViewById(R.id.login);
        t1=(TextView) findViewById(R.id.signup);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=e1.getText().toString();
                password=e2.getText().toString();

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)Login.this;
                String q="/Login?username=" + username + "&password=" + password;
                q = q.replace(" ", "%20");
                JR.execute(q);

            }
        });
        t1.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
                Intent i=new Intent(getApplicationContext(),Registration.class);
                startActivity(i);
            }
        }));

    }


    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {
                JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                login_id = ja1.getJSONObject(0).getString("login_id");
                usertype = ja1.getJSONObject(0).getString("usertype");
                phone = ja1.getJSONObject(0).getString("phone");

                SharedPreferences.Editor e = sh.edit();
                e.putString("login_id", login_id);
                e.commit();

                if(usertype.equals("customer"))
                {
                    Toast.makeText(getApplicationContext(),"Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Userhome.class));

                }



            }
            else {
                Toast.makeText(getApplicationContext(), "Login failed invalid username and password", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}