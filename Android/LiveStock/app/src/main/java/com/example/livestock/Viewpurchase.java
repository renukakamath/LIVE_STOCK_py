package com.example.livestock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Viewpurchase extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    ListView l1;
    String []f_name,total,quantity,date_time,value,pmaster_id,date,pdetails_id;
    public static String pid, amt,exdate,pdid;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpurchase);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView) findViewById(R.id.list);
        l1.setOnItemClickListener(this);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Viewpurchase.this;
        String q = "/viewpurchase?&lid="+Login.login_id;
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            if (method.equalsIgnoreCase("viewpurchase")) {
                String status = jo.getString("status");
                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    f_name = new String[ja1.length()];
                    total = new String[ja1.length()];
                    quantity = new String[ja1.length()];
                    date_time = new String[ja1.length()];
                    value = new String[ja1.length()];
                    pmaster_id = new String[ja1.length()];
                    date=new String[ja1.length()];
                    pdetails_id=new String[ja1.length()];


                    String[] value = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        f_name[i] = ja1.getJSONObject(i).getString("f_name");
                        total[i] = ja1.getJSONObject(i).getString("total");
                        quantity[i] = ja1.getJSONObject(i).getString("quantity");
                        date_time[i] = ja1.getJSONObject(i).getString("date_time");
                        pmaster_id[i] = ja1.getJSONObject(i).getString("pmaster_id");
                        date[i]=ja1.getJSONObject(i).getString("date");
                        pdetails_id[i]=ja1.getJSONObject(i).getString("pdetails_id");


                        value[i] = "f_name: " + f_name[i] + "\ntotal: " + total[i] + "\nquantity: " + quantity[i] + "\ndate_time:" + date_time[i];

                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), R.layout.custtext, value);

                    l1.setAdapter(ar);
                }
                }else if(method.equalsIgnoreCase("conformproduct")) {

                String status = jo.getString("status");
                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Conform SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), Viewpurchase.class));

                } else {

                    Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
                }

            }

        }

        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        amt=total[i];
        pid=pmaster_id[i];
        exdate=date[i];
        pdid=pdetails_id[i];

        final CharSequence[] items = {"Make Payment","conform","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Viewpurchase.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Make Payment")) {

                    startActivity(new Intent(getApplicationContext(),Makepayment.class));

                }else if (items[item].equals("conform")){
                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) Viewpurchase.this;
                    String q = "/conformproduct?pid="+pid;
                    q = q.replace(" ", "%20");
                    JR.execute(q);
                }



                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });
        builder.show();
    }
    }
