package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 * Use the {@link my_account_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class my_account_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public my_account_fragment() {
        // Required empty public constructor
    }

    private ImageButton view_all_address_Button;
    public static final int MANAGE_ADDRESS = 1;
    private Dialog loading_dialog;

    private CircleImageView profile_view, current_order_image;
    private TextView name, email, tv_current_order_status;
    private LinearLayout layout_container, recent_orders_container;
    private ImageView ordered_indicator, packed_indicator, shipped_indicator, delivered_indicator;
    private ProgressBar o_p_progress, p_s_progress, s_d_progress;
    private TextView YOUR_RECENT_ORDERS_TITLE;
    private TextView address_name, address, pincode;
    private Button sign_out_btn;
    private FloatingActionButton settings_button;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment my_account_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static my_account_fragment newInstance(String param1, String param2) {
        my_account_fragment fragment = new my_account_fragment();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account_fragment, container, false);


        /////loading dialog
        loading_dialog = new Dialog(getContext());
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       // loading_dialog.show();
        /////loading dialog


        profile_view = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.username);
        email = view.findViewById(R.id.user_email);
        layout_container = view.findViewById(R.id.layout_container);
        current_order_image = view.findViewById(R.id.current_order_image);
        tv_current_order_status = view.findViewById(R.id.tv_current_order_status);
        ordered_indicator = view.findViewById(R.id.Ordered_indicator);
        packed_indicator = view.findViewById(R.id.Packed_indicator);
        shipped_indicator = view.findViewById(R.id.Shipped_indicator);
        delivered_indicator = view.findViewById(R.id.Delivered_indicator);
        o_p_progress = view.findViewById(R.id.Ordered_Packed_Progress);
        p_s_progress = view.findViewById(R.id.Packed_Shipped_progress);
        s_d_progress = view.findViewById(R.id.Shipped_Delivered_progress);
        YOUR_RECENT_ORDERS_TITLE = view.findViewById(R.id.your_recent_orders_title);
        recent_orders_container = view.findViewById(R.id.recent_orders_container);
        address_name = view.findViewById(R.id.address_fullname);
        address = view.findViewById(R.id.address);
        pincode = view.findViewById(R.id.address_pincode);
        sign_out_btn = view.findViewById(R.id.sign_out_button);
        settings_button=view.findViewById(R.id.settings_button);



        layout_container.getChildAt(1).setVisibility(View.GONE);
        loading_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                for (my_orders_items_model orders_items_model : db_querries.my_orders_items_modelList) {
                    if (!orders_items_model.isCancellation_requested()) {
                        if (!orders_items_model.getOrder_status().equals("Delivered") && !orders_items_model.getOrder_status().equals("Cancelled")) {
                            layout_container.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(orders_items_model.getProduct_image()).apply(new RequestOptions().placeholder(R.drawable.no_image)).into(current_order_image);
                            tv_current_order_status.setText(orders_items_model.getOrder_status());
                            switch (orders_items_model.getOrder_status()) {
                                case "Ordered":
                                    ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    break;
                                case "Packed":
                                    ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    o_p_progress.setProgress(100);
                                    break;
                                case "Shipped":
                                    ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    shipped_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    o_p_progress.setProgress(100);
                                    p_s_progress.setProgress(100);
                                    break;
                                case "Out For Delivery":
                                    ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    shipped_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    delivered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    o_p_progress.setProgress(100);
                                    p_s_progress.setProgress(100);
                                    s_d_progress.setProgress(100);
                                    break;

                            }

                        }
                    }
                }
                int i = 0;
                for (my_orders_items_model myOrdersItemsModel : db_querries.my_orders_items_modelList) {
                    if (i < 4) {
                        if (myOrdersItemsModel.getOrder_status().equals("Delivered")) {
                            Glide.with(getContext()).load(myOrdersItemsModel.getProduct_image()).apply(new RequestOptions().placeholder(R.drawable.no_image)).into((CircleImageView) recent_orders_container.getChildAt(i));
                            i++;
                        }
                    } else {
                        break;
                    }

                }
                if (i == 0) {
                    YOUR_RECENT_ORDERS_TITLE.setText("No Recent Orders");
                }
                if (i < 3) {
                    for (int x = i; x < 4; x++) {
                        recent_orders_container.getChildAt(x).setVisibility(View.GONE);
                    }
                }
                loading_dialog.show();
                loading_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loading_dialog.setOnDismissListener(null);
                        if (db_querries.addresses_modelList.size() == 0) {
                            address_name.setText("No Address");
                            address.setText("-");
                            pincode.setText("-");
                        } else {


                            set_address();

                        }
                    }
                });
                db_querries.loadAddresses(getContext(), loading_dialog, false);

            }
        });

        db_querries.loadOrders(getContext(), null, loading_dialog);


        view_all_address_Button = view.findViewById(R.id.view_all_address_button);
        view_all_address_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myaddresses_intent = new Intent(getContext(), my_adresses_activity.class);
                myaddresses_intent.putExtra("MODE", MANAGE_ADDRESS);
                startActivity(myaddresses_intent);
            }
        });

        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                db_querries.clearData();
                Intent register_intent = new Intent(getContext(), Registry_activity.class);
                startActivity(register_intent);
                getActivity().finish();
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update_user_info=new Intent(getContext(),update_user_info_activity.class);
                update_user_info.putExtra("Name",name.getText());
                update_user_info.putExtra("Email",email.getText());
                update_user_info.putExtra("Photo",db_querries.profile);
                startActivity(update_user_info);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(db_querries.fullname);
        email.setText(db_querries.email);


        if (!db_querries.profile.equals("")) {
            Glide.with(getContext()).load(db_querries.profile).apply(new RequestOptions().placeholder(R.drawable.account_main_hdpi)).into(profile_view);
        }else{
            profile_view.setImageResource(R.drawable.account_main_hdpi);
        }

        if (!loading_dialog.isShowing()){
            if (db_querries.addresses_modelList.size() == 0) {
                address_name.setText("No Address");
                address.setText("-");
                pincode.setText("-");
            } else {
                set_address();
            }
        }
    }

    private void set_address() {
        String name_text,  mobileno;
        name_text = db_querries.addresses_modelList.get(db_querries.selected_address).getName();
        mobileno = db_querries.addresses_modelList.get(db_querries.selected_address).getMobile_no();
        if (db_querries.addresses_modelList.get(db_querries.selected_address).getAlternate_mobile_no().equals("")) {
            address_name.setText(name_text + " - " + mobileno);
        } else {
            address_name.setText(name_text + " - " + mobileno + " or " + db_querries.addresses_modelList.get(db_querries.selected_address).getAlternate_mobile_no());
        }
        String flat_no = db_querries.addresses_modelList.get(db_querries.selected_address).getFlat_no();
        String locality = db_querries.addresses_modelList.get(db_querries.selected_address).getLocality();
        String landmark = db_querries.addresses_modelList.get(db_querries.selected_address).getLandmark();
        String city = db_querries.addresses_modelList.get(db_querries.selected_address).getCity();
        String state = db_querries.addresses_modelList.get(db_querries.selected_address).getState();

        if (landmark.equals("")){
            address.setText(flat_no +" "+ locality +" "+ city +" "+ state);
        }else{
            address.setText(flat_no +" "+ locality +" "+ landmark +" "+ city +" "+ state);
        }


        pincode.setText(db_querries.addresses_modelList.get(db_querries.selected_address).getPincode());
    }

}
