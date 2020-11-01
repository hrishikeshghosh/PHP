package com.example.ecommerce_03;

import android.app.Dialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link update_passowrd_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class update_passowrd_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public update_passowrd_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment update_passowrd_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static update_passowrd_fragment newInstance(String param1, String param2) {
        update_passowrd_fragment fragment = new update_passowrd_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private EditText old_password, new_password, confirm_new_passowrd;
    private Button update_btn;
    private String email;
    private Dialog loading_dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_passowrd_fragment, container, false);
        old_password = view.findViewById(R.id.old_password);
        new_password = view.findViewById(R.id.new_password);
        confirm_new_passowrd = view.findViewById(R.id.confirm_new_password);
        update_btn = view.findViewById(R.id.update_password_btn);


        /////loading dialog
        loading_dialog = new Dialog(getContext());
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog

        email = getArguments().getString("Email");

        old_password.addTextChangedListener(new TextWatcher() {
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
        new_password.addTextChangedListener(new TextWatcher() {
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
        confirm_new_passowrd.addTextChangedListener(new TextWatcher() {
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

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailandpassword();
            }
        });

        return view;
    }

    private void checkinputs() {
        if (!TextUtils.isEmpty(old_password.getText()) && old_password.length() >= 8) {
            if (!TextUtils.isEmpty(new_password.getText()) && new_password.length() >= 8) {
                if (!TextUtils.isEmpty(confirm_new_passowrd.getText()) && confirm_new_passowrd.length() >= 8) {

                    update_btn.setEnabled(true);
                } else {
                    update_btn.setEnabled(false);
                }
            } else {
                update_btn.setEnabled(false);
            }

        } else {
            update_btn.setEnabled(false);
        }
    }

    private void checkemailandpassword() {

        Drawable CustomErrorIcon = getResources().getDrawable(R.drawable.errormdpi);
        CustomErrorIcon.setBounds(0, 0, CustomErrorIcon.getIntrinsicWidth(), CustomErrorIcon.getIntrinsicHeight());

        if (new_password.getText().toString().equals(confirm_new_passowrd.getText().toString())) {
            loading_dialog.show();
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, old_password.getText().toString());
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
user.updatePassword(new_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()){
            old_password.setText(null);
            new_password.setText(null);
            confirm_new_passowrd.setText(null);
            getActivity().finish();
            Toast.makeText(getContext(), "Password Updated Successfully!", Toast.LENGTH_LONG).show();
        }else{
            String error = task.getException().getMessage();
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        }
        loading_dialog.dismiss();
    }
});

                            } else {
                                loading_dialog.dismiss();
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });


            //////update password
        } else {
            confirm_new_passowrd.setError("Password Doesn't Match", CustomErrorIcon);
        }
    }

}