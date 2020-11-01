package com.example.ecommerce_03;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link update_info_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class update_info_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public update_info_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment update_info_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static update_info_fragment newInstance(String param1, String param2) {
        update_info_fragment fragment = new update_info_fragment();
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

    private CircleImageView circleImageView;
    private Button remove_btn, update_btn, done_btn;
    private TextView change_photo_btn;
    private EditText namefield, emailfield, password;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private Dialog edit_dialog, loading_dialog, password_dialog;
    private String name, email, photo;
    private Uri image_uri;
    private boolean update_photo = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info_fragment, container, false);


        /////loading dialog
        loading_dialog = new Dialog(getContext());
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog


        /////password dialog
        password_dialog = new Dialog(getContext());
        password_dialog.setContentView(R.layout.password_confirmation_dialog);
        password_dialog.setCancelable(true);
        password_dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        password_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////password dialog

        /////edit dialog
        edit_dialog = new Dialog(getContext());
        edit_dialog.setContentView(R.layout.edit_photo_dialog);
        edit_dialog.setCancelable(true);
        edit_dialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        //cancel_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////edit dialog

        password = password_dialog.findViewById(R.id.password_);
        done_btn = password_dialog.findViewById(R.id.done_btn);

        circleImageView = view.findViewById(R.id.profile_image);
        change_photo_btn = view.findViewById(R.id.change_photo_btn);
        //remove_btn = view.findViewById(R.id.remove_photo_btn);
        update_btn = view.findViewById(R.id.update_profile_btn);
        namefield = view.findViewById(R.id.name);
        emailfield = view.findViewById(R.id.email);

        name = getArguments().getString("Name");
        email = getArguments().getString("Email");
        photo = getArguments().getString("Photo");

        Glide.with(getContext()).load(photo).apply(new RequestOptions().placeholder(R.drawable.account_main_hdpi)).into(circleImageView);
        namefield.setText(name);
        emailfield.setText(email);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_dialog.show();
                edit_dialog.findViewById(R.id.edit_profile_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit_dialog.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                                Intent gallery_intent = new Intent(Intent.ACTION_PICK);
                                gallery_intent.setType("image/*");
                                startActivityForResult(gallery_intent, 1);

                            } else {
                                getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

                            }
                        } else {
                            Intent gallery_intent = new Intent(Intent.ACTION_PICK);
                            gallery_intent.setType("image/*");
                            startActivityForResult(gallery_intent, 1);

                        }
                    }
                });

                edit_dialog.findViewById(R.id.remove_profile_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit_dialog.dismiss();
                        image_uri = null;
                        update_photo = true;
                        Glide.with(getContext()).load(R.drawable.account_main_hdpi).into(circleImageView);
                    }
                });


            }
        });

       /* remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        namefield.addTextChangedListener(new TextWatcher() {
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
        emailfield.addTextChangedListener(new TextWatcher() {
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
        if (!TextUtils.isEmpty(namefield.getText())) {
            if (!TextUtils.isEmpty(emailfield.getText())) {
                update_btn.setEnabled(true);
            } else {
                update_btn.setEnabled(false);
                update_btn.setAlpha(0.2f);
            }

        } else {
            update_btn.setEnabled(false);
            update_btn.setAlpha(0.2f);
        }
    }

    private void checkemailandpassword() {

        Drawable CustomErrorIcon = getResources().getDrawable(R.drawable.errormdpi);
        CustomErrorIcon.setBounds(0, 0, CustomErrorIcon.getIntrinsicWidth(), CustomErrorIcon.getIntrinsicHeight());
        if (emailfield.getText().toString().matches(emailpattern)) {
            /////update email and name
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (emailfield.getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim())) {///it is the same email
                loading_dialog.show();
                update_profile_image(user);


            } else {////not same email, update required

                password_dialog.show();
                done_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loading_dialog.show();
                        String userPassword = password.getText().toString();

                        password_dialog.dismiss();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, userPassword);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            user.updateEmail(emailfield.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        update_profile_image(user);

                                                    } else {
                                                        loading_dialog.dismiss();
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                                    }


                                                }
                                            });

                                        } else {
                                            loading_dialog.dismiss();
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });


            }

        } else {
            emailfield.setError("Email is Invalid", CustomErrorIcon);

        }
    }

    private void update_profile_image(final FirebaseUser user) {
        ////////updating profile photo

        if (update_photo) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("PROFILES/" + user.getUid() + ".jpg");
            if (image_uri != null) {

                Glide.with(getContext()).asBitmap().load(image_uri).circleCrop().into(new ImageViewTarget<Bitmap>(circleImageView) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                image_uri = task.getResult();
                                                db_querries.profile = task.getResult().toString();
                                                Glide.with(getContext()).load(db_querries.profile).into(circleImageView);

                                                Map<String, Object> update_data = new HashMap<>();
                                                update_data.put("email", emailfield.getText().toString());
                                                update_data.put("fullname", namefield.getText().toString());
                                                update_data.put("profile", db_querries.profile);


                                                update_fields(user, update_data);

                                            } else {
                                                loading_dialog.dismiss();
                                                db_querries.profile = "";
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                                } else {
                                    loading_dialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        circleImageView.setImageResource(R.drawable.account_main_hdpi);

                    }
                });

            } else {//////remove photo

                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            db_querries.profile = "";

                            Map<String, Object> update_data = new HashMap<>();
                            update_data.put("email", emailfield.getText().toString());
                            update_data.put("fullname", namefield.getText().toString());
                            update_data.put("profile", "");


                            update_fields(user, update_data);

                        } else {
                            loading_dialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            Map<String, Object> update_data = new HashMap<>();
            update_data.put("fullname", namefield.getText().toString());
            update_fields(user, update_data);
        }

        ////////updating profile photo
    }

    private void update_fields(FirebaseUser user, final Map<String, Object> update_data) {
        FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).update(update_data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (update_data.size() > 1) {
                        db_querries.email = emailfield.getText().toString().trim();
                        db_querries.fullname = namefield.getText().toString().trim();


                    } else {
                        db_querries.fullname = namefield.getText().toString().trim();
                    }
                    getActivity().finish();
                    Toast.makeText(getContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                }
                loading_dialog.dismiss();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    image_uri = data.getData();
                    update_photo = true;
                    Glide.with(getContext()).load(image_uri).into(circleImageView);
                } else {
                    Toast.makeText(getContext(), "Image Not Found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent gallery_intent = new Intent(Intent.ACTION_PICK);
                gallery_intent.setType("image/*");
                startActivityForResult(gallery_intent, 1);

            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }


}