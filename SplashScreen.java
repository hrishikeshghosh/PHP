package com.example.ecommerce_03;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth=FirebaseAuth.getInstance();



    }
    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser currentuser=firebaseAuth.getCurrentUser();
        if(currentuser==null){
            Intent loginintent=new Intent(this,Registry_activity.class);
            startActivity(loginintent);
            finish();
        }else{

            FirebaseFirestore.getInstance().collection("Users").document(currentuser.getUid()).update("last_seen", FieldValue.serverTimestamp())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Intent mainintent=new Intent(SplashScreen.this,MainActivity2.class);
                                startActivity(mainintent);
                                finish();
                                
                            }else{
                                String error=task.getException().getMessage();
                                Toast.makeText(SplashScreen.this, error, Toast.LENGTH_LONG).show();
                                
                            }
                        }
                    });


        }
    }
}
