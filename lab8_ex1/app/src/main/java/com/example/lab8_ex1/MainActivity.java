package com.example.lab8_ex1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.textclassifier.TextLinks;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Myadapter adapter;
    List<Student> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView= findViewById(R.id.recyclerview);
        //genmockdata();
        data = new ArrayList<>();
        /*
        for(int i=1; i<=11;i++){
            students.add(new Student(i,"Student "+i,"Student"+i+"@gmail.com"));

        }

         */
        fetchAllEvents();


        adapter= new Myadapter(MainActivity.this,data);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);


    }

    private void fetchAllEvents() {
        data.clear();
        RequestQueue queue = Volley.newRequestQueue(this);

        String url="http://192.168.1.6/api/get-students.php";

        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onRespone",response);
                try {
                    JSONObject jsonObject= new JSONObject(response);

                    Boolean status= jsonObject.getBoolean("status");
                    String message= jsonObject.getString("message");
                    if(status==true){
                        JSONArray students= jsonObject.getJSONArray("data");
                        for(int i=0;i< students.length();i++){
                            JSONObject jsonStudent= students.getJSONObject(i);
                            int id= jsonStudent.getInt("id");
                            String name= jsonStudent.getString("name");
                            String email= jsonStudent.getString("email");
                            String phone= jsonStudent.getString("phone");
                            Student student=new Student(id,name,email,phone);

                            data.add(student);
                        }
                        adapter.notifyDataSetChanged();

                    }
                    else {
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);




    }

    /* private void genmockdata() {
         students = new ArrayList<Student>();
         students.add(new Student(1,"Nguyeng Van A","B@gmail.com"));
         students.add(new Student(2,"Tran Thi B","A@gamil.com"));


     }

     */
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == 1234 && resultCode == RESULT_OK){
           fetchAllEvents();
       }
   }

   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_menu,menu);
       return true;
   }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                Intent intent= new Intent(MainActivity.this,AddEventActivity.class);
                startActivityForResult(intent,1234);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

}
