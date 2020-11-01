package com.example.ecommerce_03;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Signup extends Fragment {

    public Signup() {
        // Required empty public constructor
    }

    private TextView alreadyhaveanaccount;
    private FrameLayout parentframeLayout;
    public static EditText name;
    public static EditText email;
    public static EditText password;
    private EditText confirmpassword;
    private ImageButton back_button;
    private Button signup_button;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disable_close_button = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        alreadyhaveanaccount = (TextView) view.findViewById(R.id.alreadyhaveanaccount);
        parentframeLayout = (FrameLayout) getActivity().findViewById(R.id.register_framelayout);

        name = (EditText) view.findViewById(R.id.signup_fullname);
        email = (EditText) view.findViewById(R.id.signup_email);
        password = (EditText) view.findViewById(R.id.signup_password);
        confirmpassword = (EditText) view.findViewById(R.id.signup_confirmpassword);

        back_button = (ImageButton) view.findViewById(R.id.signup_closebutton);
        signup_button = (Button) view.findViewById(R.id.signup_button);

        progressBar = (ProgressBar) view.findViewById(R.id.signin_progressbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        if (disable_close_button) {
            back_button.setVisibility(View.GONE);
        } else {
            back_button.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyhaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setfragment(new Signin());
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkinputs();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkinputs();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkinputs();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkemailandpassword();
            }
        });
    }

    private void setfragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframeLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkinputs() {
        if (!TextUtils.isEmpty(name.getText())) {
            if (!TextUtils.isEmpty(email.getText())) {
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
                    if (!TextUtils.isEmpty(confirmpassword.getText())) {
                        signup_button.setEnabled(true);
                    } else {

                        signup_button.setEnabled(false);
                    }

                } else {
                    signup_button.setEnabled(false);
                }
            } else {
                signup_button.setEnabled(false);
            }

        } else {
            signup_button.setEnabled(false);
        }
    }

    private void checkemailandpassword() {

        Drawable CustomErrorIcon = getResources().getDrawable(R.drawable.errormdpi);
        CustomErrorIcon.setBounds(0, 0, CustomErrorIcon.getIntrinsicWidth(), CustomErrorIcon.getIntrinsicHeight());
        if (email.getText().toString().matches(emailpattern)) {
            if (password.getText().toString().equals(confirmpassword.getText().toString())) {
                progressBar.setVisibility(View.VISIBLE);
                signup_button.setEnabled(false);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> userdata = new HashMap<>();
                                    userdata.put("fullname", name.getText().toString());
                                    userdata.put("email",email.getText().toString());
                                    userdata.put("profile","");
                                    firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        CollectionReference user_data_reference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid())
                                                                .collection("USER_DATA");
                                                        ///MAPS
                                                        Map<String, Object> wishlist_map = new HashMap<>();
                                                        wishlist_map.put("list_size", (long) 0);

                                                        Map<String, Object> ratings_map = new HashMap<>();
                                                        ratings_map.put("list_size", (long) 0);

                                                        Map<String, Object> cart_map = new HashMap<>();
                                                        cart_map.put("list_size", (long) 0);

                                                        Map<String, Object> myaddressmap = new HashMap<>();
                                                        myaddressmap.put("list_size", (long) 0);

                                                        Map<String, Object> notifications_map = new HashMap<>();
                                                        notifications_map.put("list_size", (long) 0);
                                                        ///MAPS

                                                        final List<String>document_names=new ArrayList<>();
                                                        document_names.add("MY_WISHLIST");
                                                        document_names.add("MY_RATINGS");
                                                        document_names.add("MY_CART");
                                                        document_names.add("MY_ADDRESS");
                                                        document_names.add("MY_NOTIFICATIONS");

                                                        List<Map<String, Object>>document_fields=new ArrayList<>();
                                                        document_fields.add(wishlist_map);
                                                        document_fields.add(ratings_map);
                                                        document_fields.add(cart_map);
                                                        document_fields.add(myaddressmap);
                                                        document_fields.add(notifications_map);


                                                        for(int x=0;x<document_names.size();x++){
                                                            final int finalX = x;
                                                            user_data_reference.document(document_names.get(x)).set(document_fields.get(x)).
                                                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                if(finalX ==document_names.size()-1) {
                                                                                    mainIntent();
                                                                                }
                                                                            } else {
                                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                                signup_button.setEnabled(true);
                                                                                String error = task.getException().getMessage();
                                                                                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

                                                                            }
                                                                        }
                                                                    });
                                                        }

                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signup_button.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                }
                            }

                        });
            } else {
                confirmpassword.setError("Password Doesn't Match", CustomErrorIcon);
            }
        } else {
            email.setError("Email is Invalid", CustomErrorIcon);

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

