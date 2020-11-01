package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.ecommerce_03.delivery_activity.SELECT_ADDRESS;

public class my_adresses_activity extends AppCompatActivity {
    private RecyclerView my_addresses_recycler_view;
    private static addresses_adapter addressesAdapter;
    private Button deliver_button;
    private LinearLayout addNewAddressButton;
    private TextView addresses_saved;
    private int previous_address;
    private Dialog loading_dialog;
    private int mode;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adresses_activity);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /////loading dialog
        loading_dialog=new Dialog(this);
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loading_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                addresses_saved.setText(String.valueOf(db_querries.addresses_modelList.size())+" Saved Addresses");
            }
        });
        /////loading dialog

        my_addresses_recycler_view=findViewById(R.id.addresses_recycler_view);
        deliver_button=findViewById(R.id.deliver_here_button);
        addNewAddressButton=findViewById(R.id.add_new_address_button);
        addresses_saved=findViewById(R.id.address_saved);

        previous_address=db_querries.selected_address;

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        my_addresses_recycler_view.setLayoutManager(layoutManager);

          mode=getIntent().getIntExtra("MODE",-1);
         if(mode==SELECT_ADDRESS){
             deliver_button.setVisibility(View.VISIBLE);
         }else{
             deliver_button.setVisibility(View.GONE);
         }

         deliver_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
         if (db_querries.selected_address!=previous_address){
             final int previous_address_index=previous_address;
             loading_dialog.show();
             Map<String,Object>update_selection=new HashMap<>();
             update_selection.put("selected"+String.valueOf(previous_address+1),false);
             update_selection.put("selected"+String.valueOf(db_querries.selected_address+1),true);
             previous_address=db_querries.selected_address;

             FirebaseFirestore.getInstance().collection("Users")
                     .document(FirebaseAuth.getInstance().getUid())
                     .collection("USER_DATA")
                     .document("MY_ADDRESS")
                     .update(update_selection)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()){
                             finish();
                         }   else{
                             previous_address=previous_address_index;
                             String error=task.getException().getMessage();
                             Toast.makeText(my_adresses_activity.this, error, Toast.LENGTH_LONG).show();
                         }
                         loading_dialog.dismiss();
                         }
                     });
         }else{
             finish();
         }
             }
         });

         addressesAdapter=new addresses_adapter(db_querries.addresses_modelList,mode,loading_dialog);
        my_addresses_recycler_view.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)my_addresses_recycler_view.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();

   addNewAddressButton.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent add_address_intent=new Intent(my_adresses_activity.this,add_address_activity.class);
           if (mode!=SELECT_ADDRESS){
               add_address_intent.putExtra("INTENT","manage");
           }else{
               add_address_intent.putExtra("INTENT","null");
           }
           startActivity(add_address_intent);

       }
   });

    }

    @Override
    protected void onStart() {
        super.onStart();
        addresses_saved.setText(String.valueOf(db_querries.addresses_modelList.size())+" Saved Addresses");

    }

    public static void refresh_item(int deselect, int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(mode==SELECT_ADDRESS){
                if (db_querries.selected_address!=previous_address){
                    db_querries.addresses_modelList.get(db_querries.selected_address).setSelected(false);
                    db_querries.addresses_modelList.get(previous_address).setSelected(true);
                    db_querries.selected_address=previous_address;
                }
            }

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mode== SELECT_ADDRESS){
            if (db_querries.selected_address!=previous_address){
                db_querries.addresses_modelList.get(db_querries.selected_address).setSelected(false);
                db_querries.addresses_modelList.get(previous_address).setSelected(true);
                db_querries.selected_address=previous_address;
            }
        }

        super.onBackPressed();
    }
}
