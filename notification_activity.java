package com.example.ecommerce_03;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class notification_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static  notification_adapter adapter;
    private boolean runQuery=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

         adapter=new notification_adapter(db_querries.notificationModelList);
        recyclerView.setAdapter(adapter);

        Map<String,Object>read_map=new HashMap<>();
        for (int x=0;x<db_querries.notificationModelList.size();x++){
            if (!db_querries.notificationModelList.get(x).isReaded()){
                runQuery=true;
            }
            read_map.put("readed"+x,true);
        }
        if (runQuery){
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("MY_NOTIFICATIONS").update(read_map);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (int x=0;x<db_querries.notificationModelList.size();x++) {
           db_querries.notificationModelList.get(x).setReaded(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }




}