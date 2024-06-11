package com.example.livestock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class Conformfeed extends AppCompatActivity {
    ListView l1;
    String []f_name,total,quantity,date_time,value,pmaster_id;
    public static String pid, amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conformfeed);
        l1=(ListView) findViewById(R.id.list);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Conformfeed.this;
        String q = "/Conformfeed";
        q = q.replace(" ", "%20");
        JR.execute(q);
    }
}