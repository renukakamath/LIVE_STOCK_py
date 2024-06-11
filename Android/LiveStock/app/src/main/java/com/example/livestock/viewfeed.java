package com.example.livestock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class viewfeed extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener  {
    ListView l1;
    String[] f_name,price,quantity,value,feed_id,date;
    public static String fid,amt,pname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfeed);
        l1=(ListView) findViewById(R.id.list);
        l1.setOnItemClickListener(this);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) viewfeed.this;
        String q = "/viewfeed";
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("success")) {
                JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                f_name = new String[ja1.length()];
                price = new String[ja1.length()];
                quantity = new String[ja1.length()];
                value = new String[ja1.length()];
                feed_id=new String[ja1.length()];
                date=new String[ja1.length()];


                String[] value = new String[ja1.length()];

                for (int i = 0; i < ja1.length(); i++) {
                    f_name[i] = ja1.getJSONObject(i).getString("f_name");
                    price[i] = ja1.getJSONObject(i).getString("price");
                    quantity[i] = ja1.getJSONObject(i).getString("quantity");
                    feed_id[i] = ja1.getJSONObject(i).getString("feed_id");
                    date[i] = ja1.getJSONObject(i).getString("date");




                    value[i] = "f_name: " + f_name[i] + "\nprice: " + price[i] + "\nquantity: " + quantity[i] +"\ndate:" +date[i];


                }
                ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), R.layout.custtext, value);

                l1.setAdapter(ar);
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
        fid=feed_id[i];
        amt=price[i];
        pname=f_name[i];

        final CharSequence[] items = {"purchase","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(viewfeed.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("purchase")) {

                    startActivity(new Intent(getApplicationContext(),purchase.class));

                }



                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });
        builder.show();
    }
}
