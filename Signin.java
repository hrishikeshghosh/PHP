package com.example.ecommerce_03;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class Signin extends Fragment {

    public Signin() {
        // Required empty public constructor
    }
private TextView donthaveanaccount;
    private FrameLayout parentframeLayout;

    public static EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private TextView forgotpassword;

    private ImageButton closeButton;
    private Button signinbutton;

    private FirebaseAuth firebaseAuth;
    private String emailpattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disable_close_button=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_signin, container, false);
        donthaveanaccount=(TextView)view.findViewById(R.id.donthaveanaccount);
        parentframeLayout=(FrameLayout)getActivity().findViewById(R.id.register_framelayout);


        email=(EditText)view.findViewById(R.id.signin_email);
        password=(EditText)view.findViewById(R.id.signin_password);
        forgotpassword=(TextView)view.findViewById(R.id.forgotpassword);

        closeButton=(ImageButton)view.findViewById(R.id.signin_closebutton);
        signinbutton=(Button)view.findViewById(R.id.signinbutton);
        progressBar=(ProgressBar)view.findViewById(R.id.signin_progressbar);

        firebaseAuth=FirebaseAuth.getInstance();



        if(disable_close_button){
            closeButton.setVisibility(View.GONE);
        }else{
            closeButton.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donthaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setfragment(new Signup());
                
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean resetpassworffragment = true;
                setfragment(new resetpassword());
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mainIntent();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailandpassword();
            }
        });
    }



    private void setfragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentframeLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkinputs() {

        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                signinbutton.setEnabled(true);

            }else{
                signinbutton.setEnabled(false);

            }
        }else{
            signinbutton.setEnabled(false);

        }
    }

    private void checkemailandpassword() {
        if(email.getText().toString().matches(emailpattern)){
            if(password.length()>=8){
                progressBar.setVisibility(View.VISIBLE);
                signinbutton.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                   mainIntent();
                                }else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signinbutton.setEnabled(true);
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(getActivity(),"Incorrect Email or Password",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(),"Incorrect Email or Password",Toast.LENGTH_LONG).show();
        }

    }
    private void mainIntent() {
        if (disable_close_button) {
            disable_close_button = false;
        } else {
            Intent mainintent = new Intent(getActivity(), MainActivity2.class);
            startActivity(mainintent);
        }
        getActivity().finish();
    }



}
