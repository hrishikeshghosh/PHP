package com.example.ecommerce_03;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class db_querries {

    public static String email, fullname, profile;


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<category_model> categorymodellist = new ArrayList<>();

    public static List<List<home_page_model>> lists = new ArrayList<>();
    public static List<String> loadedcategories_names = new ArrayList<>();

    public static List<String> wish_list = new ArrayList<>();
    public static List<wishlist_model> wish_list_model_list = new ArrayList<>();

    public static List<String> my_rated_ids = new ArrayList<>();
    public static List<Long> my_ratings = new ArrayList<>();

    public static List<String> cart_list = new ArrayList<>();
    public static List<cart_item_model> cart_item_modelList = new ArrayList<>();

    public static int selected_address = -1;
    public static List<addresses_model> addresses_modelList = new ArrayList<>();

    public static List<my_rewards_model> rewards_modelList = new ArrayList<>();

    public static List<my_orders_items_model> my_orders_items_modelList = new ArrayList<>();

    public static List<notification_model> notificationModelList = new ArrayList<>();
    private static ListenerRegistration registration;


    public static void load_categories(final RecyclerView category_recycler_view, final Context context) {
        categorymodellist.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        categorymodellist.add(new category_model(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                    }
                    category_adapter categoryAdapter = new category_adapter(categorymodellist);
                    category_recycler_view.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void loadFragmentData(final RecyclerView home_page_recycler_view, final Context context, final int index, String category_name) {
        firebaseFirestore.collection("CATEGORIES").document(category_name.toUpperCase()).collection("TOP DEALS").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if ((long) documentSnapshot.get("view_type") == 0) {
                            List<slider_model> sliderModelList = new ArrayList<>();
                            long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                            for (long x = 1; x < no_of_banners + 1; x++) {
                                sliderModelList.add(new slider_model(documentSnapshot.get("banner" + x).toString(), documentSnapshot.get("banner" + x + "_background").toString()));
                            }
                            lists.get(index).add(new home_page_model(0, sliderModelList));

                        } else if ((long) documentSnapshot.get("view_type") == 1) {
                            lists.get(index).add(new home_page_model(1, documentSnapshot.get("strip_ad_banner").toString(), documentSnapshot.get("background").toString()));

                        } else if ((long) documentSnapshot.get("view_type") == 2) {
                            List<wishlist_model> view_all_product_list = new ArrayList<>();
                            List<horizontal_scroll_product_model> horizontal_scroll_product_modelList = new ArrayList<>();
                            // long no_of_products = (long) documentSnapshot.get("no_of_products");
                            ArrayList<String> productIDS = (ArrayList<String>) documentSnapshot.get("products");
                            for (String productID : productIDS) {
                                horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(productID,
                                      "",
                                      "",
                                      "",
                                      ""));


                                view_all_product_list.add(new wishlist_model(productID,
                                       "",
                                       "",
                                       0,
                                       "",
                                        0,
                                       "",
                                       "",
                                       false,
                                       false));
                            }
                            lists.get(index).add(new home_page_model(2, documentSnapshot.get("layout_title").toString(),
                                    documentSnapshot.get("layout_background").toString(), horizontal_scroll_product_modelList, view_all_product_list));

                        } else if ((long) documentSnapshot.get("view_type") == 3) {
                            List<horizontal_scroll_product_model> grid_layout_list = new ArrayList<>();
                            ArrayList<String> productIDS = (ArrayList<String>) documentSnapshot.get("products");
                            for (String productID : productIDS) {

                                grid_layout_list.add(new horizontal_scroll_product_model(productID,
                                        "",
                                        "",
                                        "",
                                        ""));
                            }
                            lists.get(index).add(new home_page_model(3, documentSnapshot.get("layout_title").toString(),
                                    documentSnapshot.get("layout_background").toString(), grid_layout_list));

                        }
                    }
                    home_page_adapter homePageAdapter = new home_page_adapter(lists.get(index));
                    home_page_recycler_view.setAdapter(homePageAdapter);
                    homePageAdapter.notifyDataSetChanged();
                    Home.swipeRefreshLayout.setRefreshing(false);
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public static void loadWishlist(final Context context, final Dialog dialog, final boolean load_product_data) {
        wish_list.clear();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        wish_list.add(task.getResult().get("product_id" + x).toString());


                        if (db_querries.wish_list.contains(product_details_Activity.product_id)) {
                            product_details_Activity.ALREADY_ADDED_TO_WISHLIST = true;
                            if (product_details_Activity.addto_wishlist_button != null) {
                                product_details_Activity.addto_wishlist_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FA4848")));
                            }
                        } else {
                            product_details_Activity.ALREADY_ADDED_TO_WISHLIST = false;
                            if (product_details_Activity.addto_wishlist_button != null) {
                                product_details_Activity.addto_wishlist_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                            }
                        }
                        if (load_product_data) {
                            wish_list_model_list.clear();

                            final String product_id = task.getResult().get("product_id" + x).toString();
                            firebaseFirestore.collection("PRODUCTS")
                                    .document(product_id).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                            if (task.isSuccessful()) {
                                                final DocumentSnapshot documentSnapshot = task.getResult();
                                                FirebaseFirestore.getInstance().collection("PRODUCTS").document(product_id)
                                                        .collection("QUANTITY")
                                                        .orderBy("time", Query.Direction.ASCENDING)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {

                                                                        wish_list_model_list.add(new wishlist_model(product_id, documentSnapshot.get("product_image1").toString(),
                                                                                documentSnapshot.get("product_title").toString(),
                                                                                (long) documentSnapshot.get("free_coupon"),
                                                                                documentSnapshot.get("average_rating").toString(),
                                                                                (long) documentSnapshot.get("total_rating"),
                                                                                documentSnapshot.get("product_price").toString(),
                                                                                documentSnapshot.get("cutted_price").toString(),
                                                                                (boolean) documentSnapshot.get("cod"),
                                                                                true));

                                                                    } else {
                                                                        wish_list_model_list.add(new wishlist_model(product_id, documentSnapshot.get("product_image1").toString(),
                                                                                documentSnapshot.get("product_title").toString(),
                                                                                (long) documentSnapshot.get("free_coupon"),
                                                                                documentSnapshot.get("average_rating").toString(),
                                                                                (long) documentSnapshot.get("total_rating"),
                                                                                documentSnapshot.get("product_price").toString(),
                                                                                documentSnapshot.get("cutted_price").toString(),
                                                                                (boolean) documentSnapshot.get("cod"),
                                                                                false));
                                                                    }
                                                                    my_wishlist_fragment.wishlist_adapter.notifyDataSetChanged();
                                                                } else {
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

    }

    public static void RemoveFrom_wishlist(final int index, final Context context) {
        final String removed_product_id = wish_list.get(index);
        wish_list.remove(index);
        Map<String, Object> updateWishList = new HashMap<>();

        for (int x = 0; x < wish_list.size(); x++) {
            updateWishList.put("product_id" + x, wish_list.get(x));
        }
        updateWishList.put("list_size", (long) wish_list.size());

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (wish_list_model_list.size() != 0) {
                        wish_list_model_list.remove(index);
                        my_wishlist_fragment.wishlist_adapter.notifyDataSetChanged();
                    }
                    product_details_Activity.ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_LONG).show();
                } else {
                    if (product_details_Activity.addto_wishlist_button != null) {
                        product_details_Activity.addto_wishlist_button.setSupportImageTintList(context.getResources().getColorStateList(R.color.wishlist_red));
                    }
                    wish_list.add(index, removed_product_id);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
                /*if(product_details_Activity.addto_wishlist_button!=null) {
                    product_details_Activity.addto_wishlist_button.setEnabled(true);
                    }*/
                product_details_Activity.running_wishlist_querry = false;
            }
        });

    }

    public static void load_ratinglist(final Context context) {
        if (!product_details_Activity.running_rating_querry) {
            product_details_Activity.running_rating_querry = true;
            my_rated_ids.clear();
            my_ratings.clear();

            firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).
                    collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        List<String> order_product_ids = new ArrayList<>();
                        for (int x = 0; x < my_orders_items_modelList.size(); x++) {
                            order_product_ids.add(my_orders_items_modelList.get(x).getProduct_id());
                        }

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                            my_rated_ids.add(task.getResult().get("product_id" + x).toString());
                            my_ratings.add((long) task.getResult().get("rating" + x));

                            if (task.getResult().get("product_id" + x).toString().equals(product_details_Activity.product_id)) {
                                product_details_Activity.initial_rating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating" + x))) - 1;
                                if (product_details_Activity.ratenowcontainer != null) {
                                    product_details_Activity.setrating(product_details_Activity.initial_rating);
                                }
                            }
                            if (order_product_ids.contains(task.getResult().get("product_id" + x).toString())) {

                                my_orders_items_modelList.get(order_product_ids.indexOf(task.getResult().get("product_id" + x).toString())).setRating(Integer.parseInt(String.valueOf((long) task.getResult().get("rating" + x))) - 1);

                            }

                        }
                        if (my_orders.myOrdersAdapter != null) {
                            my_orders.myOrdersAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                    }
                    product_details_Activity.running_rating_querry = false;
                }
            });
        }
    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean load_product_data, final TextView badgecount, final TextView cart_total_amt) {
        cart_list.clear();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        cart_list.add(task.getResult().get("product_id" + x).toString());


                        if (db_querries.cart_list.contains(product_details_Activity.product_id)) {
                            product_details_Activity.ALREADY_ADDED_TO_CART = true;
                        } else {
                            product_details_Activity.ALREADY_ADDED_TO_CART = false;
                        }
                        if (load_product_data) {
                            cart_item_modelList.clear();
                            final String product_id = task.getResult().get("product_id" + x).toString();
                            firebaseFirestore.collection("PRODUCTS")
                                    .document(product_id).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                            if (task.isSuccessful()) {


                                                final DocumentSnapshot documentSnapshot = task.getResult();
                                                FirebaseFirestore.getInstance().collection("PRODUCTS").document(product_id)
                                                        .collection("QUANTITY")
                                                        .orderBy("time", Query.Direction.ASCENDING)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    int index = 0;
                                                                    if (cart_list.size() >= 2) {
                                                                        index = cart_list.size() - 2;
                                                                    }
                                                                    if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {

                                                                        cart_item_modelList.add(index, new cart_item_model(documentSnapshot.getBoolean("cod"),
                                                                                cart_item_model.CART_ITEM,
                                                                                product_id,
                                                                                documentSnapshot.get("product_image1").toString(),
                                                                                documentSnapshot.get("product_title").toString(),
                                                                                (long) documentSnapshot.get("free_coupon"),
                                                                                documentSnapshot.get("product_price").toString(),
                                                                                documentSnapshot.get("cutted_price").toString(),
                                                                                (long) 1,
                                                                                (long) documentSnapshot.get("offers_applied"),
                                                                                (long) 0,
                                                                                true,
                                                                                (long) documentSnapshot.get("max_quantity"),
                                                                                (long) documentSnapshot.get("stock_quantity")));

                                                                    } else {
                                                                        cart_item_modelList.add(index, new cart_item_model(documentSnapshot.getBoolean("cod"),
                                                                                cart_item_model.CART_ITEM,
                                                                                product_id,
                                                                                documentSnapshot.get("product_image1").toString(),
                                                                                documentSnapshot.get("product_title").toString(),
                                                                                (long) documentSnapshot.get("free_coupon"),
                                                                                documentSnapshot.get("product_price").toString(),
                                                                                documentSnapshot.get("cutted_price").toString(),
                                                                                (long) 1,
                                                                                (long) documentSnapshot.get("offers_applied"),
                                                                                (long) 0,
                                                                                false,
                                                                                (long) documentSnapshot.get("max_quantity"),
                                                                                (long) documentSnapshot.get("stock_quantity")));
                                                                    }
                                                                    if (cart_list.size() == 1) {
                                                                        cart_item_modelList.add(new cart_item_model(cart_item_model.TOTAL_AMOUNT));
                                                                        LinearLayout parent = (LinearLayout) cart_total_amt.getParent().getParent();
                                                                        parent.setVisibility(View.VISIBLE);
                                                                    }
                                                                    if (cart_list.size() == 0) {
                                                                        cart_item_modelList.clear();
                                                                    }

                                                                    my_cart_fragment.cart_adapter.notifyDataSetChanged();
                                                                } else {
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });


                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                    if (cart_list.size() != 0) {
                        badgecount.setVisibility(View.VISIBLE);
                    } else {
                        badgecount.setVisibility(View.INVISIBLE);
                    }
                    if (db_querries.cart_list.size() < 99) {
                        badgecount.setText(String.valueOf(db_querries.cart_list.size()));
                    } else {
                        badgecount.setText("99");
                    }

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

    }

    public static void RemoveFromCart(final int index, final Context context, final TextView cart_total_amt) {
        final String removed_product_id = cart_list.get(index);
        cart_list.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();

        for (int x = 0; x < cart_list.size(); x++) {
            updateCartList.put("product_id" + x, cart_list.get(x));
        }
        updateCartList.put("list_size", (long) cart_list.size());

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (cart_item_modelList.size() != 0) {
                        cart_item_modelList.remove(index);
                        my_cart_fragment.cart_adapter.notifyDataSetChanged();
                    }
                    if (cart_list.size() == 0) {
                        LinearLayout parent = (LinearLayout) cart_total_amt.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        cart_item_modelList.clear();
                    }
                    Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_LONG).show();
                } else {
                    cart_list.add(index, removed_product_id);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
                /*if(product_details_Activity.addto_wishlist_button!=null) {
                    product_details_Activity.addto_wishlist_button.setEnabled(true);
                    }*/
                product_details_Activity.running_cart_querry = false;
            }
        });

    }

    public static void loadAddresses(final Context context, final Dialog loading_dialog, final boolean goto_deliverey_activity) {
        addresses_modelList.clear();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_ADDRESS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Intent Delivery_intent = null;

                    if ((long) task.getResult().get("list_size") == 0) {
                        Delivery_intent = new Intent(context, add_address_activity.class);
                        Delivery_intent.putExtra("INTENT", "delivery_intent");
                    } else {
                        for (long x = 1; x < (long) task.getResult().get("list_size") + 1; x++) {
                            addresses_modelList.add(new addresses_model(

                                    task.getResult().getBoolean("selected" + x)
                                    , task.getResult().getString("city" + x)
                                    , task.getResult().getString("locality" + x)
                                    , task.getResult().getString("flat_no" + x)
                                    , task.getResult().getString("pincode" + x)
                                    , task.getResult().getString("landmark" + x)
                                    , task.getResult().getString("name" + x)
                                    , task.getResult().getString("mobile_no" + x)
                                    , task.getResult().getString("alternate_mobile_no" + x)
                                    , task.getResult().getString("state" + x)


                            ));


                            if ((boolean) task.getResult().get("selected" + x)) {
                                selected_address = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        if (goto_deliverey_activity) {
                            Delivery_intent = new Intent(context, delivery_activity.class);
                        }

                    }
                    if (goto_deliverey_activity) {
                        context.startActivity(Delivery_intent);
                    }

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
                loading_dialog.dismiss();
            }
        });
    }

    public static void loadRewards(final Context context, final Dialog loading_dialog, final boolean onRewardFragmnet) {
        rewards_modelList.clear();

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final Date lastseen_date = task.getResult().getDate("last_seen");

                            firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                    if (documentSnapshot.get("type").toString().equals("Discount") && lastseen_date.before(documentSnapshot.getDate("validity"))) {

                                                        rewards_modelList.add(new my_rewards_model(documentSnapshot.getId()
                                                                , documentSnapshot.get("type").toString()
                                                                , documentSnapshot.get("lower_limit").toString()
                                                                , documentSnapshot.get("upper_limit").toString()
                                                                , documentSnapshot.get("percentage").toString()
                                                                , documentSnapshot.get("body").toString()
                                                                , documentSnapshot.getDate("validity")
                                                                , (boolean) documentSnapshot.get("already_used")


                                                        ));
                                                    } else if (documentSnapshot.get("type").toString().equals("Cashback") && lastseen_date.before(documentSnapshot.getDate("validity"))) {

                                                        rewards_modelList.add(new my_rewards_model(documentSnapshot.getId()
                                                                , documentSnapshot.get("type").toString()
                                                                , documentSnapshot.get("lower_limit").toString()
                                                                , documentSnapshot.get("upper_limit").toString()
                                                                , documentSnapshot.get("amount").toString()
                                                                , documentSnapshot.get("body").toString()
                                                                , documentSnapshot.getDate("validity")
                                                                , (boolean) documentSnapshot.get("already_used")
                                                        ));
                                                    }

                                                }
                                                if (onRewardFragmnet) {
                                                    my_rewards_fragment.my_rewards_adapter.notifyDataSetChanged();
                                                }

                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                            }
                                            loading_dialog.dismiss();

                                        }
                                    });


                        } else {
                            loading_dialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    public static void loadOrders(final Context context, @Nullable final my_orders_adapter my_orders_adapter, final Dialog loading_dialog) {
        my_orders_items_modelList.clear();

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").orderBy("time", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {


                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                                firebaseFirestore.collection("ORDERS").document(documentSnapshot.getString("order_id")).collection("order_items").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    for (DocumentSnapshot order_ITEMS : task.getResult().getDocuments()) {

                                                        final my_orders_items_model myOrdersItemsModel = new my_orders_items_model(order_ITEMS.getString("product_id")
                                                                , order_ITEMS.getString("order_status")
                                                                , order_ITEMS.getString("address")
                                                                , order_ITEMS.getString("coupon_id")
                                                                , order_ITEMS.getString("cutted_price")
                                                                , order_ITEMS.getDate("order date")
                                                                , order_ITEMS.getDate("packed date")
                                                                , order_ITEMS.getDate("shipped date")
                                                                , order_ITEMS.getDate("delivered date")
                                                                , order_ITEMS.getDate("cancelled date")
                                                                , order_ITEMS.getString("discounted_price")
                                                                , order_ITEMS.getLong("free_coupons")
                                                                , order_ITEMS.getString("full_name")
                                                                , order_ITEMS.getString("ORDER_ID")
                                                                , order_ITEMS.getString("payment_method")
                                                                , order_ITEMS.getString("pincode")
                                                                , order_ITEMS.getString("product_price")
                                                                , order_ITEMS.getLong("product_quantity")
                                                                , order_ITEMS.getString("user_id")
                                                                , order_ITEMS.getString("product_image")
                                                                , order_ITEMS.getString("product_title")
                                                                , order_ITEMS.getString("delivery_price")
                                                                , order_ITEMS.getBoolean("cancellation_requested"));

                                                        my_orders_items_modelList.add(myOrdersItemsModel);


                                                    }
                                                    load_ratinglist(context);
                                                    if (my_orders_adapter != null) {
                                                        my_orders_adapter.notifyDataSetChanged();
                                                    }


                                                } else {

                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                                                }
                                                loading_dialog.dismiss();
                                            }
                                        });

                            }


                        } else {
                            loading_dialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();


                        }

                    }
                });


    }

    public static void checkNotifications(boolean remove, @Nullable final TextView notify_count) {
        if (remove) {
            registration.remove();
        } else {
            registration = firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("MY_NOTIFICATIONS").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                notificationModelList.clear();
                                int unread = 0;
                                for (long x = 0; x < (long) documentSnapshot.get("list_size"); x++) {
                                    notificationModelList.add(0, new notification_model(documentSnapshot.get("image" + x).toString(), documentSnapshot.get("body" + x).toString(), documentSnapshot.getBoolean("readed" + x)));
                                    if (!documentSnapshot.getBoolean("readed" + x)) {
                                        unread++;
                                        if (notify_count != null) {
                                            if (unread > 0) {
                                                notify_count.setVisibility(View.VISIBLE);
                                                if (unread < 99) {
                                                    notify_count.setText(String.valueOf(unread));
                                                } else {
                                                    notify_count.setText("99");
                                                }
                                            } else {
                                                notify_count.setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    }

                                }
                                if (notification_activity.adapter != null) {
                                    notification_activity.adapter.notifyDataSetChanged();
                                }

                            }

                        }
                    });
        }

    }

    public static void clearData() {
        categorymodellist.clear();
        lists.clear();
        loadedcategories_names.clear();
        wish_list.clear();
        wish_list_model_list.clear();
        cart_list.clear();
        cart_item_modelList.clear();
        my_rated_ids.clear();
        my_ratings.clear();
        addresses_modelList.clear();
        rewards_modelList.clear();
        my_orders_items_modelList.clear();


    }


}
