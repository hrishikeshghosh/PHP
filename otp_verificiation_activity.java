package com.example.ecommerce_03;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class otp_verificiation_activity extends AppCompatActivity {

private TextView phone_no;
private EditText otp;
private Button verify_btn;
private String user_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verificiation_activity);
        phone_no=findViewById(R.id.phone_no);
        otp=findViewById(R.id.otp);
        verify_btn=findViewById(R.id.verify_btn);
        user_number=getIntent().getStringExtra("mobile_no");
        phone_no.setText("Verification Code Has Been Sent To +91"+user_number);

        Random random=new Random();
        final int otp_no=random.nextInt(999999 - 111111) + 111111;

        String SMS_API="https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest=new StringRequest(Request.Method.POST,SMS_API , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                verify_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (otp.getText().toString().equals(String.valueOf(otp_no))) {

                            Map<String,Object>updateStatus=new HashMap<>();
                            updateStatus.put("order_status", "Ordered");
                            final String order_id=getIntent().getStringExtra("Order ID");
                            FirebaseFirestore.getInstance().collection("ORDERS").document(order_id).update(updateStatus)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Map<String, Object> userOrder = new HashMap<>();
                                                userOrder.put("order_id", order_id);
                                                userOrder.put("time", FieldValue.serverTimestamp());
                                                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id)
                                                        .set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            delivery_activity.cod_order_confirmed = true;
                                                            finish();
                                                        }else{
                                                            Toast.makeText(otp_verificiation_activity.this, "Failed to update User's order list", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });



                                            }else{
                                                Toast.makeText(otp_verificiation_activity.this, "Order Cancelled", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });


                        } else {
                            Toast.makeText(otp_verificiation_activity.this, "Incorrect OTP", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(otp_verificiation_activity.this, "Failed To Send The OTP Verification Code", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
              Map<String,String>headers=new HashMap<>();
              headers.put("authorization","sWOc6pJPNVqSL4Y7QmlBkdHbnGXax9CrZDFy10hfiIwg5tvMeRo2HkElGUIBLNpzZq98DWmCePOr7fvJ");
              return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>body=new HashMap<>();
                body.put("sender_id","FSTSMS");
                body.put("language","english");
                body.put("route","qt");
                body.put("numbers",user_number);
                body.put("message","31076");
                body.put("variables","{#BB#}");
                body.put("variables_values", String.valueOf(otp_no));
                return body;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
               3000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue requestQueue= Volley.newRequestQueue(otp_verificiation_activity.this);
        requestQueue.add(stringRequest);
    }
}