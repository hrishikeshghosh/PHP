package com.example.ecommerce_03;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Transaction;


/**
 * A simple {@link Fragment} subclass.
 */
public class resetpassword extends Fragment {

    public resetpassword() {

        // Required empty public constructor
    }
    private EditText email;
    private TextView goback;
    private Button resetpasswordbutton;
    private FrameLayout parentframeLayout;
    private FirebaseAuth firebaseAuth;
    private ViewGroup emailiconcontainer;
    private ImageView Emailicon;
    private TextView Emailicontext;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_resetpassword, container, false);
        email=(EditText)view.findViewById(R.id.et_reset_page_email);
        goback=(TextView)view.findViewById(R.id.tvgoback);
        emailiconcontainer=(ViewGroup)view.findViewById(R.id.check_mail_box_container);
        Emailicon=(ImageView)view.findViewById(R.id.check_mail_box_image);
        Emailicontext=(TextView)view.findViewById(R.id.Check_mail_box);
        progressBar=(ProgressBar)view.findViewById(R.id.checj_mail_box_progress_bar);
        firebaseAuth=FirebaseAuth.getInstance();
        resetpasswordbutton=(Button)view.findViewById(R.id.restbutton);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentframeLayout=getActivity().findViewById(R.id.register_framelayout);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetpasswordbutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(emailiconcontainer);
                Emailicon.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(emailiconcontainer);
                Emailicon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                resetpasswordbutton.setEnabled(false);




                firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Emailicontext.setVisibility(View.VISIBLE);
                                    resetpasswordbutton.setEnabled(false);

                                }else{
                                    String error=task.getException().getMessage();


                                   Emailicontext.setText(error);
                                   Emailicontext.setTextColor(getResources().getColor(R.color.error));
                                    TransitionManager.beginDelayedTransition(emailiconcontainer);
                                    resetpasswordbutton.setEnabled(true);
                                    Emailicontext.setVisibility(View.VISIBLE);
                                }

                                progressBar.setVisibility(View.GONE);

                            }
                        });
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfragment(new Signin());
            }
        });
    }

    private void checkInputs() {
        if(TextUtils.isEmpty(email.getText())){
            resetpasswordbutton.setEnabled(false);

        }else{
            resetpasswordbutton.setEnabled(true);
        }
    }
    private void setfragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentframeLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
