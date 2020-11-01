package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class cart_adapter extends RecyclerView.Adapter {
    private List<cart_item_model> cartItemModelList;
    private int last_position = -1;
    private TextView cartTotal_amount;
    private boolean show_delete_button;


    public cart_adapter(List<cart_item_model> cartItemModelList, TextView cartTotal_amount, boolean show_delete_button) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotal_amount = cartTotal_amount;
        this.show_delete_button = show_delete_button;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:

                return cart_item_model.CART_ITEM;

            case 1:
                return cart_item_model.TOTAL_AMOUNT;
            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case cart_item_model.CART_ITEM:

                View cart_item_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                return new cartItemViewHolder(cart_item_view);

            case cart_item_model.TOTAL_AMOUNT:
                View cart_total_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amaount_layout, viewGroup, false);
                return new carttotalAmountViewHolder(cart_total_view);

            default:
                return null;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()) {

            case cart_item_model.CART_ITEM:
                String product_id = cartItemModelList.get(position).getProduct_id();
                String resource = cartItemModelList.get(position).getProduct_image();
                String title = cartItemModelList.get(position).getProduct_title();
                long freecoupons = cartItemModelList.get(position).getFree_coupons();
                String productprice = cartItemModelList.get(position).getProduct_price();
                String cuttedprice = cartItemModelList.get(position).getCutted_price();
                long offersapplied = cartItemModelList.get(position).getOffers_applied();
                boolean in_stock = cartItemModelList.get(position).isIn_stock();
                long product_quantity = cartItemModelList.get(position).getProduct_quantity();
                long max_quantity = cartItemModelList.get(position).getProduct_max_quantity();
                boolean qty_eror = cartItemModelList.get(position).isQty_error();
                List<String> qty_ids = cartItemModelList.get(position).getQuantity_IDs();
                long stock_quantity = cartItemModelList.get(position).getProduct_stock_quantity();
                boolean cod=cartItemModelList.get(position).isCOD();

                ((cartItemViewHolder) viewHolder).set_itemdetails(product_id, resource, title, freecoupons, productprice, cuttedprice, offersapplied, position, in_stock, String.valueOf(product_quantity), max_quantity, qty_eror, qty_ids, stock_quantity,cod);

                if (last_position < position) {
                    Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.face_in);
                    viewHolder.itemView.setAnimation(animation);
                    last_position = position;
                }
                break;

            case cart_item_model.TOTAL_AMOUNT:

                int total_items = 0;
                int total_items_price = 0;
                String deliveryprice;
                int totalAmount;
                int savedAmount = 0;

                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == cart_item_model.CART_ITEM && cartItemModelList.get(x).isIn_stock()) {
                        int quantity = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProduct_quantity()));
                        total_items = total_items + quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelected_coupon_id())) {
                            total_items_price = total_items_price + Integer.parseInt(cartItemModelList.get(x).getProduct_price()) * quantity;
                        } else {
                            total_items_price = total_items_price + Integer.parseInt(cartItemModelList.get(x).getDiscounted_price()) * quantity;

                        }

                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCutted_price())) {
                            savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getCutted_price()) - Integer.parseInt(cartItemModelList.get(x).getProduct_price())) * quantity;

                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelected_coupon_id())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProduct_price()) - Integer.parseInt(cartItemModelList.get(x).getDiscounted_price())) * quantity;
                            }
                        } else {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelected_coupon_id())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProduct_price()) - Integer.parseInt(cartItemModelList.get(x).getDiscounted_price())) * quantity;
                            }
                        }

                    }
                }
                if (total_items_price > 15000) {
                    deliveryprice = "FREE";
                    totalAmount = total_items_price;
                } else {
                    deliveryprice = "100";
                    totalAmount = total_items_price + 100;
                }

                cartItemModelList.get(position).setTotal_items(total_items);
                cartItemModelList.get(position).setTotalItems_price(total_items_price);
                cartItemModelList.get(position).setDelivery_price(deliveryprice);
                cartItemModelList.get(position).setTotal_amount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);

                ((carttotalAmountViewHolder) viewHolder).set_totalamount(total_items, total_items_price, deliveryprice, totalAmount, savedAmount);


                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class cartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_image;
        private ImageView free_coupon_icon;
        private TextView product_title;
        private TextView free_coupons;
        private TextView product_price;
        private TextView cutted_price;
        private TextView offers_applied;
        private TextView coupons_applied;
        private TextView product_quantity;
        private LinearLayout coupon_redemption_layout;
        private TextView coupon_redeemtion_body;
        private TextView tv_remove_item;
        private LinearLayout delete_button;
        private Button redeem_btn;

        ////coupon dialog
        private TextView coupon_title;
        private TextView coupon_body;
        private TextView coupon_Expiry_date;
        private TextView discounted_price;
        private TextView original_price;
        private RecyclerView coupons_recycler_view;
        private LinearLayout selected_coupon;
        private Button remove_coupon_btn;
        private Button apply_coupon_btn;
        private LinearLayout apply_or_remove_btns_container;
        private TextView footer_text;
        private String product_original_price;
        private ImageView cod_indicator;
        private TextView cod_indicator_text;

        ////coupon dialog


        public cartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_title = itemView.findViewById(R.id.product_title);
            free_coupon_icon = itemView.findViewById(R.id.free_coupon_icon);
            free_coupons = itemView.findViewById(R.id.tv_free_coupon);
            product_price = itemView.findViewById(R.id.product_price);
            cutted_price = itemView.findViewById(R.id.cutted_price);
            offers_applied = itemView.findViewById(R.id.offers_applied);
            coupons_applied = itemView.findViewById(R.id.coupons_applied);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            redeem_btn = itemView.findViewById(R.id.coupon_redemption_button);
            delete_button = itemView.findViewById(R.id.remove_item_button);
            coupon_redemption_layout = itemView.findViewById(R.id.coupon_redemption_layout);
            tv_remove_item = itemView.findViewById(R.id.tv_remove_item);
            coupon_redeemtion_body = itemView.findViewById(R.id.tv_coupon_redemption);
            cod_indicator=itemView.findViewById(R.id.cod_indicator);
            cod_indicator_text=itemView.findViewById(R.id.cod_indicator_text);



        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void set_itemdetails(final String product_id, String resource, String title, long freeCouponsNo, final String productpricetext, String cuttedpricetext, long offers_applied_no, final int position, boolean in_stock, final String quantity, final long max_quantity, boolean qty_error, final List<String> qty_ids, final long stock_quantity,boolean cod) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.no_image)).into(product_image);
            product_title.setText(title);

            final Dialog check_coupon_price_dialog = new Dialog(itemView.getContext());
            check_coupon_price_dialog.setContentView(R.layout.coupon_redeem_dialoog);
            check_coupon_price_dialog.setCancelable(false);
            check_coupon_price_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if(cod){
                cod_indicator.setVisibility(View.VISIBLE);
                cod_indicator_text.setVisibility(View.VISIBLE);
            }else{
                cod_indicator.setVisibility(View.INVISIBLE);
                cod_indicator_text.setVisibility(View.INVISIBLE);
            }

            if (in_stock) {
                if (freeCouponsNo >= 0) {
                    free_coupon_icon.setVisibility(View.VISIBLE);
                    free_coupons.setVisibility(View.VISIBLE);
                    if (freeCouponsNo == 1) {
                        free_coupons.setText("free " + freeCouponsNo + " coupon");
                    } else {
                        free_coupons.setText("free " + freeCouponsNo + " coupons");
                    }
                } else {
                    free_coupon_icon.setVisibility(View.INVISIBLE);
                    free_coupons.setVisibility(View.INVISIBLE);
                }

                product_price.setText("Rs " + productpricetext + "/-");
                product_price.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cutted_price.setText("Rs " + cuttedpricetext + "/-");
                coupon_redemption_layout.setVisibility(View.VISIBLE);

                ///////////////////coupon_dialog

                ImageView toggle_recycler_view = check_coupon_price_dialog.findViewById(R.id.toggle_recycler_view);
                coupons_recycler_view = check_coupon_price_dialog.findViewById(R.id.coupons_recycler_view);
                selected_coupon = check_coupon_price_dialog.findViewById(R.id.selected_coupon);

                coupon_title = check_coupon_price_dialog.findViewById(R.id.coupon_title);
                coupon_Expiry_date = check_coupon_price_dialog.findViewById(R.id.coupon_validity);
                coupon_body = check_coupon_price_dialog.findViewById(R.id.coupon_body);
                apply_or_remove_btns_container = check_coupon_price_dialog.findViewById(R.id.apply_or_remove_butons_container);
                remove_coupon_btn = check_coupon_price_dialog.findViewById(R.id.remove_btn);
                apply_coupon_btn = check_coupon_price_dialog.findViewById(R.id.apply_btn);
                footer_text = check_coupon_price_dialog.findViewById(R.id.footer_text);

                footer_text.setVisibility(View.GONE);
                apply_or_remove_btns_container.setVisibility(View.VISIBLE);
                original_price = check_coupon_price_dialog.findViewById(R.id.original_price);
                discounted_price = check_coupon_price_dialog.findViewById(R.id.discounted_price);

                LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                coupons_recycler_view.setLayoutManager(layoutManager);

                apply_coupon_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelected_coupon_id())) {
                            for (my_rewards_model rewardsModel : db_querries.rewards_modelList) {

                                if (rewardsModel.getCoupon_id().equals(cartItemModelList.get(position).getSelected_coupon_id())) {
                                    rewardsModel.setAlready_used(true);
                                    coupon_redemption_layout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.gradient_background_version_b));
                                    coupon_redeemtion_body.setText(rewardsModel.getCoupon_body());
                                    redeem_btn.setText("Coupons");
                                }
                            }
                            coupons_applied.setVisibility(View.VISIBLE);
                            cartItemModelList.get(position).setDiscounted_price(discounted_price.getText().toString().substring(3, discounted_price.getText().length() - 2));
                            product_price.setText(discounted_price.getText());
                            String couponDiscountedAMT = String.valueOf(Long.valueOf(productpricetext) - Long.valueOf(discounted_price.getText().toString().substring(3, discounted_price.getText().length() - 2)));
                            coupons_applied.setText("Coupon Applied: -Rs " + couponDiscountedAMT + "/-");
                            notifyItemChanged(cartItemModelList.size() - 1);
                            check_coupon_price_dialog.dismiss();
                        }
                    }
                });

                remove_coupon_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (my_rewards_model rewardsModel : db_querries.rewards_modelList) {

                            if (rewardsModel.getCoupon_id().equals(cartItemModelList.get(position).getSelected_coupon_id())) {
                                rewardsModel.setAlready_used(false);

                            }
                        }

                        coupons_applied.setVisibility(View.INVISIBLE);
                        coupon_redemption_layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupon_yellow));
                        coupon_redeemtion_body.setText("Apply Your Coupons Here");
                        redeem_btn.setText("Redeem");
                        cartItemModelList.get(position).setSelected_coupon_id(null);
                        product_price.setText("Rs " + productpricetext + "/-");
                        notifyItemChanged(cartItemModelList.size() - 1);
                        check_coupon_price_dialog.dismiss();
                    }
                });

                original_price.setText(product_price.getText());
                product_original_price = productpricetext;
                my_rewards_adapter my_rewards_adapter = new my_rewards_adapter(position, db_querries.rewards_modelList, true, coupons_recycler_view, selected_coupon, product_original_price, coupon_title, coupon_Expiry_date, coupon_body, discounted_price,cartItemModelList);
                coupons_recycler_view.setAdapter(my_rewards_adapter);
                my_rewards_adapter.notifyDataSetChanged();

                toggle_recycler_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show_dialog_recycler_view();

                    }
                });

                if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelected_coupon_id())) {
                    for (my_rewards_model rewardsModel : db_querries.rewards_modelList) {

                        if (rewardsModel.getCoupon_id().equals(cartItemModelList.get(position).getSelected_coupon_id())) {
                            coupon_redemption_layout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.gradient_background_version_b));
                            coupon_redeemtion_body.setText(rewardsModel.getCoupon_body());
                            redeem_btn.setText("Coupons");


                            coupon_body.setText(rewardsModel.getCoupon_body());
                            if (rewardsModel.getType().equals("Discount")) {
                                coupon_title.setText(rewardsModel.getType());
                            } else {
                                coupon_title.setText("Cashback");
                            }
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
                            coupon_Expiry_date.setText("Till " + simpleDateFormat.format(rewardsModel.getTimestamp()));
                        }
                    }
                    discounted_price.setText("Rs " + cartItemModelList.get(position).getDiscounted_price() + "/-");
                    coupons_applied.setVisibility(View.VISIBLE);
                    product_price.setText("Rs " + cartItemModelList.get(position).getDiscounted_price() + "/-");
                    String couponDiscountedAMT = String.valueOf(Long.valueOf(productpricetext) - Long.valueOf(cartItemModelList.get(position).getDiscounted_price()));
                    coupons_applied.setText("Coupon Applied: -Rs " + couponDiscountedAMT + "/-");
                } else {
                    coupons_applied.setVisibility(View.INVISIBLE);
                    coupon_redemption_layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupon_yellow));
                    coupon_redeemtion_body.setText("Apply Your Coupons Here");
                    redeem_btn.setText("Redeem");
                }

                ///////////////////coupon_dialog

                product_quantity.setText("Qty: " + quantity);
                if (!show_delete_button) {
                    if (qty_error) {
                        product_quantity.setTextColor(itemView.getContext().getResources().getColor(R.color.error));
                        product_quantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.error)));
                    } else {
                        product_quantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                        product_quantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.black)));
                    }
                }

                product_quantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantity_dialog = new Dialog(itemView.getContext());
                        quantity_dialog.setContentView(R.layout.quantity_dialog);
                        quantity_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        quantity_dialog.setCancelable(false);
                        final EditText quantity_number = quantity_dialog.findViewById(R.id.quantity_number);
                        Button cancel_button = quantity_dialog.findViewById(R.id.cancl_button);
                        Button okay_button = quantity_dialog.findViewById(R.id.okay_button);
                        quantity_number.setHint("Max " + String.valueOf(max_quantity));

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantity_dialog.dismiss();
                            }
                        });
                        okay_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(quantity_number.getText())) {
                                    if (Long.valueOf(quantity_number.getText().toString()) <= max_quantity && Long.valueOf(quantity_number.getText().toString()) != 0) {

                                        if (itemView.getContext() instanceof MainActivity2) {
                                            cartItemModelList.get(position).setProduct_quantity(Long.valueOf(quantity_number.getText().toString()));
                                        } else {
                                            if (delivery_activity.FROM_CART) {
                                                cartItemModelList.get(position).setProduct_quantity(Long.valueOf(quantity_number.getText().toString()));
                                            } else {
                                                delivery_activity.cart_item_modelList.get(position).setProduct_quantity(Long.valueOf(quantity_number.getText().toString()));
                                            }
                                        }
                                        product_quantity.setText("Qty: " + quantity_number.getText());
                                        notifyItemChanged(cartItemModelList.size() - 1);
                                        if (!show_delete_button) {
                                            delivery_activity.loading_dialog.show();
                                            delivery_activity.cart_item_modelList.get(position).setQty_error(false);
                                            final int initial_quantity = Integer.parseInt(quantity);
                                            final int final_quantity = Integer.parseInt(quantity_number.getText().toString());
                                            int quantity_difference;
                                            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                            if (final_quantity > initial_quantity) {

                                                for (int y = 0; y < final_quantity - initial_quantity; y++) {
                                                    final String qty_document_name = UUID.randomUUID().toString().substring(0, 20);
                                                    final int finalY = y;
                                                    Map<String, Object> time_stamp = new HashMap<>();
                                                    time_stamp.put("time", FieldValue.serverTimestamp());
                                                    firebaseFirestore.collection("PRODUCTS").document(product_id)
                                                            .collection("QUANTITY")
                                                            .document(qty_document_name)
                                                            .set(time_stamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            qty_ids.add(qty_document_name);
                                                            if (finalY + 1 == final_quantity - initial_quantity) {

                                                                firebaseFirestore.collection("PRODUCTS").document(product_id)
                                                                        .collection("QUANTITY")
                                                                        .orderBy("time", Query.Direction.ASCENDING)
                                                                        .limit(stock_quantity)
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {

                                                                                    List<String> server_quantity = new ArrayList<>();
                                                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                        server_quantity.add(queryDocumentSnapshot.getId());
                                                                                    }
                                                                                    long available_quantity = 0;
                                                                                    for (String qtyID : qty_ids) {

                                                                                        if (!server_quantity.contains(qtyID)) {
                                                                                            delivery_activity.cart_item_modelList.get(position).setQty_error(true);
                                                                                            delivery_activity.cart_item_modelList.get(position).setProduct_max_quantity(available_quantity);
                                                                                            Toast.makeText(itemView.getContext(), "Sorry! All products may not be available in required quantity", Toast.LENGTH_LONG).show();


                                                                                        } else {
                                                                                            available_quantity++;
                                                                                        }
                                                                                    }
                                                                                    delivery_activity.cartAdapter.notifyDataSetChanged();
                                                                                } else {
                                                                                    String error = task.getException().getMessage();
                                                                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_LONG).show();
                                                                                }
                                                                                delivery_activity.loading_dialog.dismiss();
                                                                            }
                                                                        });

                                                            }
                                                        }
                                                    });
                                                }
                                            } else if (initial_quantity > final_quantity) {

                                                for (int x = 0; x < initial_quantity - final_quantity; x++) {
                                                    final String qtyID = qty_ids.get(qty_ids.size() - 1 - x);
                                                    final int finalX = x;
                                                    firebaseFirestore.collection("PRODUCTS").document(product_id)
                                                            .collection("QUANTITY")
                                                            .document(qtyID)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qty_ids.remove(qtyID);
                                                                    delivery_activity.cartAdapter.notifyDataSetChanged();
                                                                    if (finalX +1== initial_quantity - final_quantity){
                                                                        delivery_activity.loading_dialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                }


                                            }
                                        }

                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max quantity: " + max_quantity, Toast.LENGTH_LONG).show();

                                    }

                                }

                                quantity_dialog.dismiss();
                            }
                        });
                        quantity_dialog.show();
                    }


                });
                if (offers_applied_no > 0) {
                    offers_applied.setVisibility(View.VISIBLE);
                    String offerDiscountedAMT = String.valueOf(Long.valueOf(cuttedpricetext) - Long.valueOf(productpricetext));
                    offers_applied.setText("Offer Applied: -Rs " + offerDiscountedAMT + "/-");
                } else {
                    offers_applied.setVisibility(View.INVISIBLE);
                }

            } else {
                product_price.setText("Out Of Stock");
                product_price.setTextColor(itemView.getContext().getResources().getColor(R.color.error));
                cutted_price.setText(" ");
                free_coupons.setVisibility(View.INVISIBLE);
                coupon_redemption_layout.setVisibility(View.GONE);
                product_quantity.setVisibility(View.INVISIBLE);
                coupons_applied.setVisibility(View.GONE);
                free_coupon_icon.setVisibility(View.GONE);
                delete_button.setBackgroundColor(Color.TRANSPARENT);
                tv_remove_item.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                offers_applied.setVisibility(View.GONE);
            }

            if (show_delete_button) {
                delete_button.setVisibility(View.VISIBLE);
            } else {
                delete_button.setVisibility(View.GONE);
            }


            redeem_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (my_rewards_model rewardsModel : db_querries.rewards_modelList) {

                        if (rewardsModel.getCoupon_id().equals(cartItemModelList.get(position).getSelected_coupon_id())) {
                            rewardsModel.setAlready_used(false);

                        }
                    }
                    check_coupon_price_dialog.show();
                }
            });

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelected_coupon_id())) {
                        for (my_rewards_model rewardsModel : db_querries.rewards_modelList) {

                            if (rewardsModel.getCoupon_id().equals(cartItemModelList.get(position).getSelected_coupon_id())) {
                                rewardsModel.setAlready_used(false);

                            }
                        }
                    }

                    if (!product_details_Activity.running_cart_querry) {
                        product_details_Activity.running_cart_querry = true;
                        db_querries.RemoveFromCart(position, itemView.getContext(), cartTotal_amount);
                    }
                }
            });

        }

        private void show_dialog_recycler_view() {
            if (coupons_recycler_view.getVisibility() == View.GONE) {
                coupons_recycler_view.setVisibility(View.VISIBLE);
                selected_coupon.setVisibility(View.GONE);
            } else {
                coupons_recycler_view.setVisibility(View.GONE);
                selected_coupon.setVisibility(View.VISIBLE);
            }
        }
    }

    class carttotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView total_items;
        private TextView total_item_price;
        private TextView delivery_price;
        private TextView total_amount;
        private TextView saved_amount;
        private ConstraintLayout total_item_layout;
        LinearLayout parent;

        public carttotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            total_items = itemView.findViewById(R.id.total_items);
            total_item_price = itemView.findViewById(R.id.total_items_price);
            delivery_price = itemView.findViewById(R.id.delivery_price);
            total_amount = itemView.findViewById(R.id.total_price);
            saved_amount = itemView.findViewById(R.id.save_amount);
            total_item_layout = itemView.findViewById(R.id.total_item_layout);

        }

        private void set_totalamount(int totalItemtext, int totalItemPricetext, String deliveryPricetext, int totalAmounttext, int savedAmounttext) {
            total_items.setText("price(" + totalItemtext + ")");
            total_item_price.setText("Rs " + totalItemPricetext + "/-");
            if (deliveryPricetext.equals("FREE")) {
                delivery_price.setText(deliveryPricetext);
            } else {
                delivery_price.setText("Rs" + deliveryPricetext + "/-");
            }

            total_amount.setText("Rs" + totalAmounttext + "/-");
            cartTotal_amount.setText("Rs" + totalAmounttext + "/-");
            saved_amount.setText("You Saved Rs " + savedAmounttext + "/- On This Order");

            LinearLayout parent = (LinearLayout) cartTotal_amount.getParent().getParent();
            if (totalItemPricetext == 0) {
                //layout logic code(and not direct java code attack), if any problems acquires futher regarding total item layout, come back here.
                //total_item_layout.setVisibility(View.GONE);
                //if any problem acquires un comment the code below as it is a direct java code attack.
                if (delivery_activity.FROM_CART) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                    delivery_activity.cart_item_modelList.remove(cartItemModelList.size() - 1);
                }
                if (show_delete_button) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);

            } else {
                //total_item_layout.setVisibility(View.VISIBLE);
                parent.setVisibility(View.VISIBLE);

            }
        }
    }
}

