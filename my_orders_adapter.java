package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class my_orders_adapter extends RecyclerView.Adapter<my_orders_adapter.ViewHolder> {
    private List<my_orders_items_model> myOrdersItemsModelList;
    private Dialog loading_dialog;
    private SimpleDateFormat simpleDateFormat;

    public my_orders_adapter(List<my_orders_items_model> myOrdersItemsModelList, Dialog loading_dialog) {
        this.myOrdersItemsModelList = myOrdersItemsModelList;
        this.loading_dialog = loading_dialog;
    }

    @NonNull
    @Override
    public my_orders_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_orders_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull my_orders_adapter.ViewHolder viewHolder, int position) {
        String resource = myOrdersItemsModelList.get(position).getProduct_image();
        String product_id = myOrdersItemsModelList.get(position).getProduct_id();
        int rating = myOrdersItemsModelList.get(position).getRating();
        String title = myOrdersItemsModelList.get(position).getProduct_title();
        String order_status = myOrdersItemsModelList.get(position).getOrder_status();
        Date date;
        switch (order_status) {
            case "Ordered":
                date = myOrdersItemsModelList.get(position).getOrdered_date();
                break;
            case "Packed":
                date = myOrdersItemsModelList.get(position).getPacked_date();
                break;
            case "Shipped":
                date = myOrdersItemsModelList.get(position).getShipped_date();
                break;
            case "Delivered":
                date = myOrdersItemsModelList.get(position).getDelivered_date();
                break;
            case "Cancelled":
                date = myOrdersItemsModelList.get(position).getCancelled_date();
                break;
            default:
                date = myOrdersItemsModelList.get(position).getCancelled_date();
        }

        viewHolder.setdata(resource, title, order_status, date, rating, product_id, position);
    }

    @Override
    public int getItemCount() {
        return myOrdersItemsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_image;
        private ImageView order_indicator;
        private TextView product_title;
        private TextView delivery_status;
        private LinearLayout ratenowcontainer;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_title = itemView.findViewById(R.id.product_title);
            order_indicator = itemView.findViewById(R.id.order_indicator);
            delivery_status = itemView.findViewById(R.id.order_delivered_date);
            ratenowcontainer = itemView.findViewById(R.id.rate_now_container);


        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setdata(String resource, String title, String order_status, final Date date, final int rating, final String product_id, final int position) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.no_image)).into(product_image);
            product_title.setText(title);
            if (order_status.equals("Cancelled")) {
                order_indicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.error)));
            } else {
                order_indicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.green)));
            }
            simpleDateFormat = new SimpleDateFormat("EEE dd MMM YYYY, hh:mm aa");
            delivery_status.setText("Status: " + order_status + " " + "( " + String.valueOf(simpleDateFormat.format(date)) + " )");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderdetails_intent = new Intent(itemView.getContext(), order_details_activity.class);
                    orderdetails_intent.putExtra("position", position);
                    itemView.getContext().startActivity(orderdetails_intent);
                }
            });


            //////////////rating layout code
            setrating(rating);
            for (int x = 0; x < ratenowcontainer.getChildCount(); x++) {
                final int star_position = x;
                ratenowcontainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        loading_dialog.show();
                        setrating(star_position);

                        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(product_id);

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

                                if (db_querries.my_rated_ids.contains(product_id)) {
                                    myrating.put("rating" + db_querries.my_rated_ids.indexOf(product_id), (long) star_position + 1);
                                } else {

                                    myrating.put("list_size", (long) db_querries.my_rated_ids.size() + 1);
                                    myrating.put("product_id" + db_querries.my_rated_ids.size(), product_id);
                                    myrating.put("rating" + db_querries.my_rated_ids.size(), (long) star_position + 1);

                                }
                                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
                                        .collection("USER_DATA").document("MY_RATINGS")
                                        .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            db_querries.my_orders_items_modelList.get(position).setRating(star_position);
                                            if (db_querries.my_rated_ids.contains(product_id)) {
                                                db_querries.my_ratings.set(db_querries.my_rated_ids.indexOf(product_id), Long.parseLong(String.valueOf(star_position + 1)));
                                            } else {
                                                db_querries.my_rated_ids.add(product_id);
                                                db_querries.my_ratings.add(Long.parseLong(String.valueOf(star_position + 1)));
                                            }

                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_LONG).show();
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


        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setrating(int star_position) {
            for (int x = 0; x < ratenowcontainer.getChildCount(); x++) {
                ImageView star_button = (ImageView) ratenowcontainer.getChildAt(x);
                star_button.setImageTintList(ColorStateList.valueOf(Color.parseColor("#AFA5A5")));
                if (x <= star_position) {
                    star_button.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
                }
            }
        }

    }
}
