package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class order_details_activity extends AppCompatActivity {

    private int position;
    private TextView title, price, quantity;
    private ImageView product_image, ordered_indicator, packed_indicator, shipped_indicator, delivered_indicator;
    private ProgressBar order_to_packed_progressBar, packed_to_shipped_progressBar, shipped_to_delivered_progressBar;
    private TextView order_title, packed_title, shipped_title, delivered_title;
    private TextView order_date, packed_date, shipped_date, delivered_date;
    private TextView order_body, packed_body, shipped_body, delivered_body;
    private LinearLayout rate_now_container;
    private int rating;
    private TextView fullname, address, pincode;
    private TextView total_items_price, delivery_price, total_amt, total_items, saved_amt;

    private Dialog loading_dialog, cancel_dialog;
    private SimpleDateFormat simpleDateFormat;
    private Button cancel_order_button;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /////loading dialog
        loading_dialog = new Dialog(order_details_activity.this);
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog

        /////cancel dialog
        cancel_dialog = new Dialog(order_details_activity.this);
        cancel_dialog.setContentView(R.layout.order_cancel_dialog);
        cancel_dialog.setCancelable(true);
        cancel_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        //cancel_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////cancel dialog


        position = getIntent().getIntExtra("position", -1);
        final my_orders_items_model model = db_querries.my_orders_items_modelList.get(position);


        title = findViewById(R.id.product_title);
        price = findViewById(R.id.product_price);
        quantity = findViewById(R.id.product_quantity);
        product_image = findViewById(R.id.product_image);

        ordered_indicator = findViewById(R.id.Ordered_indicator);
        packed_indicator = findViewById(R.id.Packed_indicator);
        shipped_indicator = findViewById(R.id.Shipped_indicator);
        delivered_indicator = findViewById(R.id.Delivered_indicator);

        order_to_packed_progressBar = findViewById(R.id.order_packed_progress);
        packed_to_shipped_progressBar = findViewById(R.id.Packed_Shipped_progress);
        shipped_to_delivered_progressBar = findViewById(R.id.Shipped_Delivered_progress);

        order_title = findViewById(R.id.ordered_title);
        packed_title = findViewById(R.id.packed_title);
        shipped_title = findViewById(R.id.shipped_titlle);
        delivered_title = findViewById(R.id.delivered_title);

        order_date = findViewById(R.id.ordered_date);
        packed_date = findViewById(R.id.packed_date);
        shipped_date = findViewById(R.id.shipping_date);
        delivered_date = findViewById(R.id.delivered_date);

        order_body = findViewById(R.id.ordered_body);
        packed_body = findViewById(R.id.packed_body);
        shipped_body = findViewById(R.id.shipping_body);
        delivered_body = findViewById(R.id.delivered_body);

        rate_now_container = findViewById(R.id.rate_now_container);
        cancel_order_button = findViewById(R.id.cancl_button);

        fullname = findViewById(R.id.fullname);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);

        total_items_price = findViewById(R.id.total_items_price);
        delivery_price = findViewById(R.id.delivery_price);
        total_amt = findViewById(R.id.total_price);
        total_items = findViewById(R.id.total_items);
        saved_amt = findViewById(R.id.save_amount);


        title.setText(model.getProduct_title());
        if (model.getDiscounted_price().equals("")) {
            price.setText("Rs " + model.getProduct_price() + "/-");
        } else {
            price.setText("Rs " + model.getDiscounted_price() + "/-");
        }
        quantity.setText("QTY: " + String.valueOf(model.getProduct_quantity()));
        Glide.with(this).load(model.getProduct_image()).into(product_image);

        simpleDateFormat = new SimpleDateFormat("EEE dd MMM YYYY, hh:mm aa");

        switch (model.getOrder_status()) {

            case "Ordered":
                ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                order_date.setText(String.valueOf(simpleDateFormat.format(model.getOrdered_date())));

                order_to_packed_progressBar.setVisibility(View.GONE);
                packed_to_shipped_progressBar.setVisibility(View.GONE);
                shipped_to_delivered_progressBar.setVisibility(View.GONE);

                packed_indicator.setVisibility(View.GONE);
                packed_body.setVisibility(View.GONE);
                packed_date.setVisibility(View.GONE);
                packed_title.setVisibility(View.GONE);

                shipped_indicator.setVisibility(View.GONE);
                shipped_body.setVisibility(View.GONE);
                shipped_date.setVisibility(View.GONE);
                shipped_title.setVisibility(View.GONE);

                delivered_body.setVisibility(View.GONE);
                delivered_date.setVisibility(View.GONE);
                delivered_title.setVisibility(View.GONE);
                delivered_indicator.setVisibility(View.GONE);

                break;
            case "Packed":
                ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                order_date.setText(String.valueOf(simpleDateFormat.format(model.getOrdered_date())));

                packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packed_date.setText(String.valueOf(simpleDateFormat.format(model.getPacked_date())));

                order_to_packed_progressBar.setProgress(100);
                packed_to_shipped_progressBar.setVisibility(View.GONE);
                shipped_to_delivered_progressBar.setVisibility(View.GONE);

                shipped_indicator.setVisibility(View.GONE);
                shipped_body.setVisibility(View.GONE);
                shipped_date.setVisibility(View.GONE);
                shipped_title.setVisibility(View.GONE);

                delivered_body.setVisibility(View.GONE);
                delivered_date.setVisibility(View.GONE);
                delivered_title.setVisibility(View.GONE);
                delivered_indicator.setVisibility(View.GONE);

                break;
            case "Shipped":
                ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                order_date.setText(String.valueOf(simpleDateFormat.format(model.getOrdered_date())));

                packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packed_date.setText(String.valueOf(simpleDateFormat.format(model.getPacked_date())));

                shipped_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                shipped_date.setText(String.valueOf(simpleDateFormat.format(model.getShipped_date())));

                order_to_packed_progressBar.setProgress(100);
                packed_to_shipped_progressBar.setProgress(100);
                shipped_to_delivered_progressBar.setVisibility(View.GONE);

                delivered_body.setVisibility(View.GONE);
                delivered_date.setVisibility(View.GONE);
                delivered_title.setVisibility(View.GONE);
                delivered_indicator.setVisibility(View.GONE);

                break;

            case "Out For Delivery":
                ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                order_date.setText(String.valueOf(simpleDateFormat.format(model.getOrdered_date())));

                packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packed_date.setText(String.valueOf(simpleDateFormat.format(model.getPacked_date())));

                shipped_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                shipped_date.setText(String.valueOf(simpleDateFormat.format(model.getShipped_date())));

                delivered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                delivered_date.setText(String.valueOf(simpleDateFormat.format(model.getDelivered_date())));


                order_to_packed_progressBar.setProgress(100);
                packed_to_shipped_progressBar.setProgress(100);
                shipped_to_delivered_progressBar.setProgress(100);

                delivered_title.setText("Out For Delivery");
                delivered_body.setText("Your Order is out for delivery.");
                break;

            case "Delivered":
                ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                order_date.setText(String.valueOf(simpleDateFormat.format(model.getOrdered_date())));

                packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packed_date.setText(String.valueOf(simpleDateFormat.format(model.getPacked_date())));

                shipped_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                shipped_date.setText(String.valueOf(simpleDateFormat.format(model.getShipped_date())));

                delivered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                delivered_date.setText(String.valueOf(simpleDateFormat.format(model.getDelivered_date())));


                order_to_packed_progressBar.setProgress(100);
                packed_to_shipped_progressBar.setProgress(100);
                shipped_to_delivered_progressBar.setProgress(100);

                break;
            case "Cancelled":

                if (model.getPacked_date().after(model.getOrdered_date())) {

                    if (model.getShipped_date().after(model.getPacked_date())) {


                        ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        order_date.setText(String.valueOf(simpleDateFormat.format(model.getOrdered_date())));

                        packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        packed_date.setText(String.valueOf(simpleDateFormat.format(model.getPacked_date())));

                        shipped_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        shipped_date.setText(String.valueOf(simpleDateFormat.format(model.getShipped_date())));

                        delivered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.error)));
                        delivered_date.setText(String.valueOf(simpleDateFormat.format(model.getCancelled_date())));
                        delivered_title.setText("Cancelled");
                        delivered_body.setText("Your Order Has Been Cancelled");

                    } else {
                        ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        order_date.setText(String.valueOf(simpleDateFormat.format(model.getOrdered_date())));

                        packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        packed_date.setText(String.valueOf(simpleDateFormat.format(model.getPacked_date())));

                        shipped_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.error)));
                        shipped_date.setText(String.valueOf(simpleDateFormat.format(model.getCancelled_date())));
                        shipped_title.setText("Cancelled");
                        shipped_body.setText("Your Order Has Been Cancelled");

                        order_to_packed_progressBar.setProgress(100);
                        packed_to_shipped_progressBar.setProgress(100);
                        shipped_to_delivered_progressBar.setVisibility(View.GONE);

                        delivered_body.setVisibility(View.GONE);
                        delivered_date.setVisibility(View.GONE);
                        delivered_title.setVisibility(View.GONE);
                        delivered_indicator.setVisibility(View.GONE);


                    }


                } else {
                    ordered_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    order_date.setText(String.valueOf(simpleDateFormat.format(model.getOrdered_date())));

                    packed_indicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.error)));
                    packed_date.setText(String.valueOf(simpleDateFormat.format(model.getCancelled_date())));
                    packed_title.setText("Cancelled");
                    packed_body.setText("Your Order Has Been Cancelled");

                    order_to_packed_progressBar.setProgress(100);
                    packed_to_shipped_progressBar.setVisibility(View.GONE);
                    shipped_to_delivered_progressBar.setVisibility(View.GONE);

                    shipped_indicator.setVisibility(View.GONE);
                    shipped_body.setVisibility(View.GONE);
                    shipped_date.setVisibility(View.GONE);
                    shipped_title.setVisibility(View.GONE);

                    delivered_body.setVisibility(View.GONE);
                    delivered_date.setVisibility(View.GONE);
                    delivered_title.setVisibility(View.GONE);
                    delivered_indicator.setVisibility(View.GONE);
                }


                break;
            default:


        }

        //////////////rating layout code

        rating = model.getRating();
        setrating(rating);
        for (int x = 0; x < rate_now_container.getChildCount(); x++) {
            final int star_position = x;
            rate_now_container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    loading_dialog.show();
                    setrating(star_position);
                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProduct_id());
                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                            if (rating != 0) {

                                long increase = documentSnapshot.getLong(star_position + 1 + "_star") + 1;
                                long decrease = documentSnapshot.getLong(rating + 1 + "_star") - 1;

                                transaction.update(documentReference, star_position + 1 + "_star", increase);
                                transaction.update(documentReference, rating + 1 + "_star", decrease);
                            } else {

                                long increase = documentSnapshot.getLong(star_position + 1 + "_star") + 1;
                                transaction.update(documentReference, star_position + 1 + "_star", increase);
                            }

                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object o) {


                            Map<String, Object> myrating = new HashMap<>();

                            if (db_querries.my_rated_ids.contains(model.getProduct_id())) {
                                myrating.put("rating" + db_querries.my_rated_ids.indexOf(model.getProduct_id()), (long) star_position + 1);
                            } else {

                                myrating.put("list_size", (long) db_querries.my_rated_ids.size() + 1);
                                myrating.put("product_id" + db_querries.my_rated_ids.size(), model.getProduct_id());
                                myrating.put("rating" + db_querries.my_rated_ids.size(), (long) star_position + 1);

                            }
                            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
                                    .collection("USER_DATA").document("MY_RATINGS")
                                    .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        db_querries.my_orders_items_modelList.get(position).setRating(star_position + 1);
                                        if (db_querries.my_rated_ids.contains(model.getProduct_id())) {
                                            db_querries.my_ratings.set(db_querries.my_rated_ids.indexOf(model.getProduct_id()), Long.parseLong(String.valueOf(star_position + 1)));
                                        } else {
                                            db_querries.my_rated_ids.add(model.getProduct_id());
                                            db_querries.my_ratings.add(Long.parseLong(String.valueOf(star_position + 1)));
                                        }

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(order_details_activity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                    loading_dialog.dismiss();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            loading_dialog.dismiss();

                        }
                    });
                }
            });
        }

        //////////////rating layout code

        if (model.isCancellation_requested()) {
            cancel_order_button.setVisibility(View.VISIBLE);
            cancel_order_button.setEnabled(false);
            cancel_order_button.setText("Cancellation in process.");
            cancel_order_button.setTextColor(getResources().getColor(R.color.error));
            cancel_order_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        } else {
            if (model.getOrder_status().equals("Ordered") || model.getOrder_status().equals("Packed")) {
                cancel_order_button.setVisibility(View.VISIBLE);
                cancel_order_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        cancel_dialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancel_dialog.dismiss();
                            }
                        });
                        cancel_dialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancel_dialog.dismiss();
                                loading_dialog.show();

                                Map<String,Object> map=new HashMap<>();
                                map.put("order_id",model.getOrder_id());
                                map.put("product_id",model.getProduct_id());
                                map.put("order_cancelled",false);

                                FirebaseFirestore.getInstance().collection("CANCELLED_ORDERS").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            FirebaseFirestore.getInstance().collection("ORDERS").document(model.getOrder_id()).collection("order_items")
                                                    .document(model.getProduct_id()).update("cancellation_requested",true)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                model.setCancellation_requested(true);
                                                                cancel_order_button.setEnabled(false);
                                                                cancel_order_button.setText("Cancellation in process");
                                                                cancel_order_button.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                                cancel_order_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                                                            }else{
                                                                String error=task.getException().getMessage();
                                                                Toast.makeText(order_details_activity.this, error, Toast.LENGTH_LONG).show();
                                                            }
                                                            loading_dialog.dismiss();
                                                        }
                                                    });

                                        }else{
                                            loading_dialog.dismiss();
                                            String error=task.getException().getMessage();
                                            Toast.makeText(order_details_activity.this, error, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }
                        });
                        cancel_dialog.show();
                    }
                });
            }
        }


        fullname.setText(model.getFull_name());
        address.setText(model.getAddress());
        pincode.setText(model.getPincode());

        total_items.setText("price (" + model.getProduct_quantity() + " items)");

        long total_items_price_value;

        if (model.getDiscounted_price().equals("")) {
            total_items_price_value = model.getProduct_quantity() * Long.valueOf(model.getProduct_price());
            total_items_price.setText("Rs " + total_items_price_value + "/-");
        } else {
            total_items_price_value = model.getProduct_quantity() * Long.valueOf(model.getDiscounted_price());
            total_items_price.setText("Rs " + total_items_price_value + "/-");
        }
        if (model.getDelivery_price().equals("Free")) {
            delivery_price.setText(model.getDelivery_price());
            total_amt.setText("Rs " + total_items_price.getText() + "/-");
        } else {
            delivery_price.setText("Rs " + model.getDelivery_price() + "/-");
            total_amt.setText("Rs " + (total_items_price_value + Long.valueOf(model.getDelivery_price())) + "/-");
        }


        if (!model.getCutted_price().equals("")) {
            if (!model.getDiscounted_price().equals("")) {
                saved_amt.setText("You Have Saved Rs " + model.getProduct_quantity() * (Long.valueOf(model.getCutted_price()) - Long.valueOf(model.getDiscounted_price())) + "/- On This Order");
            } else {
                saved_amt.setText("You Have Saved Rs " + model.getProduct_quantity() * (Long.valueOf(model.getCutted_price()) - Long.valueOf(model.getProduct_price())) + "/- On This Order");
            }
        } else {
            if (!model.getDiscounted_price().equals("")) {
                saved_amt.setText("You Have Saved Rs " + model.getProduct_quantity() * (Long.valueOf(model.getProduct_price()) - Long.valueOf(model.getDiscounted_price())) + "/- On This Order");

            } else {
                saved_amt.setText("You Have Saved  Rs 0/- On This Order");
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setrating(int star_position) {
        for (int x = 0; x < rate_now_container.getChildCount(); x++) {
            ImageView star_button = (ImageView) rate_now_container.getChildAt(x);
            star_button.setImageTintList(ColorStateList.valueOf(Color.parseColor("#AFA5A5")));
            if (x <= star_position) {
                star_button.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
