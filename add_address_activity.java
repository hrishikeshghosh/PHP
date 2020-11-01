package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class add_address_activity extends AppCompatActivity {

    private EditText city;
    private EditText locality;
    private EditText flat_no;
    private EditText pincode;
    private EditText landmark;
    private EditText name;
    private EditText mobile_no;
    private EditText alternate_mobile_no;
    private Spinner state_spinner;
    private Button save_button;

    private String[] state_list;
    private String selected_state;
    private Dialog loading_dialog;
    private boolean update_address = false;
    private addresses_model addressesModel;
    private int position;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);

        state_list = getResources().getStringArray(R.array.india_states);
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flat_no = findViewById(R.id.flat_no);
        pincode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobile_no = findViewById(R.id.phone_number);
        alternate_mobile_no = findViewById(R.id.alternate_mobile_number);
        state_spinner = findViewById(R.id.state_spinner);
        save_button = findViewById(R.id.save_button);


        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, state_list);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        state_spinner.setAdapter(spinner_adapter);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_state = state_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /////loading dialog
        loading_dialog = new Dialog(this);
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog


        if (getIntent().getStringExtra("INTENT").equals("update_address")) {
            update_address = true;
            position = getIntent().getIntExtra("INDEX", -1);
            addressesModel = db_querries.addresses_modelList.get(position);

            city.setText(addressesModel.getCity());
            locality.setText(addressesModel.getLocality());
            flat_no.setText(addressesModel.getFlat_no());
            landmark.setText(addressesModel.getLandmark());
            name.setText(addressesModel.getName());
            mobile_no.setText(addressesModel.getMobile_no());
            alternate_mobile_no.setText(addressesModel.getAlternate_mobile_no());
            pincode.setText(addressesModel.getPincode());
            for (int i = 0; i < state_list.length; i++) {
                if (state_list[i].equals(addressesModel.getState())) {
                    state_spinner.setSelection(i);
                }
            }
            save_button.setText("update");
        } else {
            position = db_querries.addresses_modelList.size();
        }


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(city.getText())) {
                    if (!TextUtils.isEmpty(locality.getText())) {
                        if (!TextUtils.isEmpty(flat_no.getText())) {
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6) {
                                if (!TextUtils.isEmpty(name.getText())) {
                                    if (!TextUtils.isEmpty(mobile_no.getText()) && mobile_no.getText().length() == 10) {
                                        loading_dialog.show();


                                        Map<String, Object> add_address = new HashMap();
                                        add_address.put("city" + String.valueOf(position + 1), city.getText().toString());
                                        add_address.put("locality" + String.valueOf(position + 1), locality.getText().toString());
                                        add_address.put("flat_no" + String.valueOf(position + 1), flat_no.getText().toString());
                                        add_address.put("pincode" + String.valueOf(position + 1), pincode.getText().toString());
                                        add_address.put("landmark" + String.valueOf(position + 1), landmark.getText().toString());
                                        add_address.put("name" + String.valueOf(position + 1), name.getText().toString());
                                        add_address.put("mobile_no" + String.valueOf(position + 1), mobile_no.getText().toString());
                                        add_address.put("alternate_mobile_no" + String.valueOf(position + 1), alternate_mobile_no.getText().toString());
                                        add_address.put("state" + String.valueOf(position + 1), selected_state);

                                        if (!update_address) {
                                            add_address.put("list_size", (long) db_querries.addresses_modelList.size() + 1);
                                            if (getIntent().getStringExtra("INTENT").equals("manage")){
                                                if (db_querries.addresses_modelList.size()==0){
                                                    add_address.put("selected" + String.valueOf(position + 1), true);
                                                }else{
                                                    add_address.put("selected" + String.valueOf(position + 1), false);
                                                }
                                            }else{
                                                add_address.put("selected" + String.valueOf(position + 1), true);
                                            }

                                            if (db_querries.addresses_modelList.size() > 0) {
                                                add_address.put("selected" + (db_querries.selected_address + 1), false);
                                            }
                                        }


                                        FirebaseFirestore.getInstance().collection("Users")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .collection("USER_DATA")
                                                .document("MY_ADDRESS")
                                                .update(add_address).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (!update_address) {
                                                        if (db_querries.addresses_modelList.size() > 0) {
                                                            db_querries.addresses_modelList.get(db_querries.selected_address).setSelected(false);
                                                        }
                                                        db_querries.addresses_modelList.add(new addresses_model(true, city.getText().toString(), locality.getText().toString().toString(), flat_no.getText().toString(), pincode.getText().toString(), landmark.getText().toString(), name.getText().toString(), mobile_no.getText().toString(), alternate_mobile_no.getText().toString(), selected_state));
                                                        if (getIntent().getStringExtra("INTENT").equals("manage")){
                                                            if (db_querries.addresses_modelList.size()==0){
                                                                db_querries.selected_address = db_querries.addresses_modelList.size() - 1;
                                                            }
                                                        }else{
                                                            db_querries.selected_address = db_querries.addresses_modelList.size() - 1;
                                                        }

                                                    } else {
                                                        db_querries.addresses_modelList.set(position, new addresses_model(true, city.getText().toString(), locality.getText().toString().toString(), flat_no.getText().toString(), pincode.getText().toString(), landmark.getText().toString(), name.getText().toString(), mobile_no.getText().toString(), alternate_mobile_no.getText().toString(), selected_state));
                                                    }

                                                    if (getIntent().getStringExtra("INTENT").equals("delivery_intent")) {
                                                        Intent delivery_intent = new Intent(add_address_activity.this, delivery_activity.class);
                                                        startActivity(delivery_intent);
                                                    } else {
                                                        my_adresses_activity.refresh_item(db_querries.selected_address, db_querries.addresses_modelList.size() - 1);
                                                    }
                                                    finish();
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(add_address_activity.this, error, Toast.LENGTH_LONG).show();
                                                }
                                                loading_dialog.dismiss();
                                            }
                                        });

                                    } else {
                                        mobile_no.requestFocus();
                                        Toast.makeText(add_address_activity.this, "Please Provide Valid Phone Number", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    name.requestFocus();
                                }
                            } else {
                                pincode.requestFocus();
                                Toast.makeText(add_address_activity.this, "Please Provide Valid Pincode", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            flat_no.requestFocus();
                        }
                    } else {
                        locality.requestFocus();
                    }
                } else {
                    city.requestFocus();
                }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
