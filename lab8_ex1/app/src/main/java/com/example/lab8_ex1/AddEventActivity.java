package com.example.lab8_ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {
    TextInputLayout textphoneLayout;
    TextInputLayout textNameLayout;
    TextInputLayout textEmailLayout;
    //TextInputLayout textTimeLayout;

    TextInputEditText txtPhone;
    TextInputEditText txtName;
    TextInputEditText txtEmail;
    //TextInputEditText txtTime;
    Button btnSave;
    TextInputEditText txtID;
    String Inserturl="http://192.168.1.6/api/add-students.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        textphoneLayout= findViewById(R.id.text_phone_layout);
        textNameLayout= findViewById(R.id.text_name_layout);
        textEmailLayout= findViewById(R.id.text_email_layout);
        txtPhone= findViewById(R.id.txtphone);
        txtEmail= findViewById(R.id.txtemail);
        txtName= findViewById(R.id.txtname);
        btnSave= findViewById(R.id.btnssave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= txtName.getText().toString().trim();
                String email= txtEmail.getText().toString().trim();
                String phone= txtPhone.getText().toString().trim();
                if(name.isEmpty() || email.isEmpty() || phone.isEmpty()){
                    Toast.makeText(AddEventActivity.this, "Please! Enter full information", Toast.LENGTH_SHORT).show();


                } else {
                    addStudent(Inserturl);
                }           //addStudent(student);
                setResult(RESULT_OK);
                finish();
            }
        });



    }

    private void addStudent(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(AddEventActivity.this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    Boolean status= jsonObject.getBoolean("status");
                    String message= jsonObject.getString("message");
                    if(status==true){
                        Toast.makeText(AddEventActivity.this,"sucessfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(AddEventActivity.this,message,Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<>();
                params.put("name", txtName.getText().toString().trim());
                params.put("email", txtEmail.getText().toString().trim());
                params.put("phone", txtPhone.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}
