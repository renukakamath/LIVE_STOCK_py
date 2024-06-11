package com.example.livestock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class purchase extends AppCompatActivity implements JsonResponse{
    EditText e1,e2,e3;
    Button b1;
    String amount,quantity,login_id,total;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        e1=(EditText) findViewById(R.id.amount);
        e2=(EditText) findViewById(R.id.quantity);
        e3=(EditText) findViewById(R.id.total);
        b1=(Button) findViewById(R.id.button3);
        e1.setText(viewfeed.amt);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount=e1.getText().toString();
                quantity=e2.getText().toString();
                total=e3.getText().toString();
                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) purchase.this;
                String q ="/purchase?amount="+amount+"&quantity="+quantity+"&lid="+Login.login_id+"&fid="+viewfeed.fid+"&total="+total;
                q = q.replace(" ","%20");
                JR.execute(q);


                final CharSequence[] items = {"product name :"+viewfeed.pname,"Amount:"+viewfeed.amt};

                AlertDialog.Builder builder = new AlertDialog.Builder(purchase.this);
                // builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("purchase")) {

                            startActivity(new Intent(getApplicationContext(),purchase.class));

                        }





                    }

                });
                builder.show();
            }

        });

        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(e2.getText().toString().equalsIgnoreCase(""))
                {

                }
                else if(e2.getText().toString().equalsIgnoreCase("."))
                {

                }
                else
                {
                    Integer s=Integer.parseInt(e1.getText().toString())*Integer.parseInt(e2.getText().toString());
                    e3.setText(s+"");
                }
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
                startActivity(new Intent(getApplicationContext(), purchase.class));

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}