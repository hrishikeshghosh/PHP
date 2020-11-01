package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class my_cart_fragment extends Fragment {

    public my_cart_fragment() {
        // Required empty public constructor
    }

    private RecyclerView cart_items_recycler_view;
    private Button continue_button;
    private LinearLayout linearLayout4;
    private TextView total_amount;


    private Dialog loading_dialog;

    public static cart_adapter cart_adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart_fragment, container, false);

        /////loading dialog
        loading_dialog = new Dialog(getContext());
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading_dialog.show();
        /////loading dialog

        cart_items_recycler_view = view.findViewById(R.id.cart_items_recycler_view);
        continue_button = view.findViewById(R.id.cart_continue_button);
        linearLayout4 = view.findViewById(R.id.continue_totalAmount_layout);
        total_amount = view.findViewById(R.id.total_cart_amount);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cart_items_recycler_view.setLayoutManager(layoutManager);


        cart_adapter = new cart_adapter(db_querries.cart_item_modelList, total_amount, true);
        cart_items_recycler_view.setAdapter(cart_adapter);
        cart_adapter.notifyDataSetChanged();


        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delivery_activity.cart_item_modelList = new ArrayList<>();
                delivery_activity.FROM_CART=true;

                for (int x = 0; x < db_querries.cart_item_modelList.size(); x++) {
                    cart_item_model cart_item_model=db_querries.cart_item_modelList.get(x);
                    if (cart_item_model.isIn_stock()){
                        delivery_activity.cart_item_modelList.add(cart_item_model);
                    }
                }
                delivery_activity.cart_item_modelList.add(new cart_item_model(cart_item_model.TOTAL_AMOUNT));

                loading_dialog.show();
                if (db_querries.addresses_modelList.size()==0) {
                    db_querries.loadAddresses(getContext(), loading_dialog,true);
                }else {

                    loading_dialog.dismiss();
                    Intent delivery_intent = new Intent(getContext(), delivery_activity.class);
                    startActivity(delivery_intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cart_adapter.notifyDataSetChanged();
        if (db_querries.rewards_modelList.size()==0){
            loading_dialog.show();
            db_querries.loadRewards(getContext(),loading_dialog,false);
        }
        if (db_querries.cart_item_modelList.size() == 0) {
            db_querries.cart_list.clear();
            db_querries.loadCartList(getContext(), loading_dialog, true, new TextView(getContext()),total_amount);
        } else {
            if (db_querries.cart_item_modelList.get(db_querries.cart_item_modelList.size() - 1).getType() == cart_item_model.TOTAL_AMOUNT) {
                LinearLayout parent = (LinearLayout) total_amount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loading_dialog.dismiss();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (cart_item_model cart_item_model:db_querries.cart_item_modelList){
            if (!TextUtils.isEmpty(cart_item_model.getSelected_coupon_id())){
                for (my_rewards_model rewardsModel : db_querries.rewards_modelList) {

                    if (rewardsModel.getCoupon_id().equals(cart_item_model.getSelected_coupon_id())) {
                        rewardsModel.setAlready_used(false);

                    }
                }
                cart_item_model.setSelected_coupon_id(null);
                if (my_rewards_fragment.my_rewards_adapter!=null){
                    my_rewards_fragment.my_rewards_adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
