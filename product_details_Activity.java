package com.example.ecommerce_03;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ecommerce_03.MainActivity2.showcart;
import static com.example.ecommerce_03.Registry_activity.set_signup_fragment;

public class product_details_Activity extends AppCompatActivity {

    private ViewPager product_images_viewpager;
    private TabLayout viewpager_indicator;
    private ViewPager product_details_viewpager;
    private TabLayout product_details_tablayout;
    private LinearLayout coupon_redeem_layout;
    private Button coupon_redeem_button;
    private TextView product_title;
    private TextView average_rating_miniview;
    private TextView total_rating_miniview;
    private TextView product_price;
    private TextView cutted_price;
    private ImageView cod_indicator;
    private TextView tv_cod_indicator;
    private TextView reward_title;
    private TextView reward_body;
    private TextView product_only_description_body;
    private LinearLayout ratingsprogress_bar_container;
    private DocumentSnapshot documentSnapshot;
    private TextView total_ratings;
    private LinearLayout rating_number_container;
    private TextView total_ratings_figure;
    private Dialog signin_dialog;
    private Dialog loading_dialog;
    private ConstraintLayout product_details_only_container;
    private ConstraintLayout product_details_tabs_container;
    private static String product_description;
    private String product_other_details;
    private FirebaseUser current_user;
    private RecyclerView coupons_recycler_view;
    private LinearLayout selected_coupon;
    public static String product_id;
    private TextView average_rating;
    private Button buy_now_button;
    private LinearLayout add_to_cart_button;
    private TextView badgecount;
    private boolean inStock=false;
    private String product_original_price;
    private TextView coupon_title;
    private TextView coupon_body;
    private TextView coupon_Expiry_date;
    private TextView discounted_price;
    private TextView original_price;

    private List<product_specification_model> product_specification_modelList = new ArrayList<>();

    public static LinearLayout ratenowcontainer;
    public static boolean running_wishlist_querry = false;
    public static boolean running_rating_querry = false;
    public static boolean running_cart_querry = false;
    public static Boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static FloatingActionButton addto_wishlist_button;
    public static int initial_rating;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static MenuItem cart_item;
    public static Activity productDetails_activity;
    public static boolean fromSEARCH=false;


    private FirebaseFirestore firebaseFirestore;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        product_images_viewpager = findViewById(R.id.product_images_viewPager);
        viewpager_indicator = findViewById(R.id.viewPager_indicator);
        addto_wishlist_button = findViewById(R.id.addTo_wishList_button);
        product_details_viewpager = findViewById(R.id.product_details_viewpager);
        product_details_tablayout = findViewById(R.id.product_details_tablayout);
        buy_now_button = findViewById(R.id.Buy_now_button);
        coupon_redeem_button = findViewById(R.id.coupon_redemption_button);
        product_title = findViewById(R.id.product_title);
        average_rating_miniview = findViewById(R.id.tv_product_rating_mini_view);
        total_rating_miniview = findViewById(R.id.total_ratings_miniview);
        product_price = findViewById(R.id.product_price);
        cutted_price = findViewById(R.id.cutted_price);
        cod_indicator = findViewById(R.id.cod_indicator_imageview);
        tv_cod_indicator = findViewById(R.id.tv_cod_indicator);
        reward_title = findViewById(R.id.reward_title);
        reward_body = findViewById(R.id.reward_body);
        product_details_only_container = findViewById(R.id.product_details_containers);
        product_details_tabs_container = findViewById(R.id.product_details_tabs_container);
        product_only_description_body = findViewById(R.id.tv_product_details_body);
        total_ratings = findViewById(R.id.total_ratings_figure);
        rating_number_container = findViewById(R.id.ratings_numbers_container);
        total_ratings_figure = findViewById(R.id.tv_total_ratings_figure);
        ratingsprogress_bar_container = findViewById(R.id.ratings_progressbar_container);
        average_rating = findViewById(R.id.average_rating);
        add_to_cart_button = findViewById(R.id.add_to_cart_button);
        coupon_redeem_layout = findViewById(R.id.coupon_redemption_layout);
        initial_rating = -1;


        /////loading dialog
        loading_dialog = new Dialog(product_details_Activity.this);
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading_dialog.show();
        /////loading dialog

        ///////////////////coupon_dialog
        final Dialog check_coupon_price_dialog = new Dialog(product_details_Activity.this);
        check_coupon_price_dialog.setContentView(R.layout.coupon_redeem_dialoog);
        check_coupon_price_dialog.setCancelable(true);
        check_coupon_price_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggle_recycler_view = check_coupon_price_dialog.findViewById(R.id.toggle_recycler_view);
        coupons_recycler_view = check_coupon_price_dialog.findViewById(R.id.coupons_recycler_view);
        selected_coupon = check_coupon_price_dialog.findViewById(R.id.selected_coupon);

        coupon_title = check_coupon_price_dialog.findViewById(R.id.coupon_title);
        coupon_Expiry_date = check_coupon_price_dialog.findViewById(R.id.coupon_validity);
        coupon_body = check_coupon_price_dialog.findViewById(R.id.coupon_body);

         original_price = check_coupon_price_dialog.findViewById(R.id.original_price);
         discounted_price = check_coupon_price_dialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(product_details_Activity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupons_recycler_view.setLayoutManager(layoutManager);




        toggle_recycler_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog_recycler_view();

            }
        });
        /////// coupon dialog


        firebaseFirestore = FirebaseFirestore.getInstance();
        final List<String> productimages = new ArrayList<>();
        product_id = getIntent().getStringExtra("product_id");
        firebaseFirestore.collection("PRODUCTS").document(product_id).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    firebaseFirestore.collection("PRODUCTS").document(product_id)
                            .collection("QUANTITY")
                            .orderBy("time", Query.Direction.ASCENDING)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                            productimages.add(documentSnapshot.get("product_image" + x).toString());
                                        }
                                        product_images_adapter productImagesAdapter = new product_images_adapter(productimages);
                                        product_images_viewpager.setAdapter(productImagesAdapter);
                                        product_title.setText(documentSnapshot.get("product_title").toString());
                                        average_rating_miniview.setText(documentSnapshot.get("average_rating").toString());
                                        total_rating_miniview.setText("(" + (long) documentSnapshot.get("total_rating") + ")ratings");
                                        product_price.setText("Rs " + documentSnapshot.get("product_price").toString() + "/-");


                                        ////for coupon dialog
                                        original_price.setText(product_price.getText());
                                        product_original_price= documentSnapshot.get("product_price").toString();
                                        my_rewards_adapter my_rewards_adapter = new my_rewards_adapter(db_querries.rewards_modelList, true,coupons_recycler_view,selected_coupon,product_original_price,coupon_title,coupon_Expiry_date,coupon_body,discounted_price);
                                        coupons_recycler_view.setAdapter(my_rewards_adapter);
                                        my_rewards_adapter.notifyDataSetChanged();
                                        ////for coupon dialog

                                        cutted_price.setText("Rs " + documentSnapshot.get("cutted_price").toString() + "/-");
                                        if ((boolean) documentSnapshot.get("cod") == true) {
                                            cod_indicator.setVisibility(View.VISIBLE);
                                            tv_cod_indicator.setVisibility(View.VISIBLE);
                                        } else {
                                            cod_indicator.setVisibility(View.INVISIBLE);
                                            tv_cod_indicator.setVisibility(View.INVISIBLE);
                                        }
                                        reward_title.setText((long) documentSnapshot.get("free_coupon") + documentSnapshot.get("free_coupon_title").toString());
                                        reward_body.setText(documentSnapshot.get("free_coupon_body").toString());


                                        if ((boolean) documentSnapshot.get("use_tab_layout") == true) {
                                            product_details_tabs_container.setVisibility(View.VISIBLE);
                                            product_details_only_container.setVisibility(View.GONE);

                                            product_description = documentSnapshot.get("product_description").toString();

                                            product_other_details = documentSnapshot.get("product_other_details").toString();

                                            for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                                product_specification_modelList.
                                                        add(new product_specification_model(0, documentSnapshot.get("spec_title" + x).toString()));
                                                for (long y = 1; y < (long) documentSnapshot.get("spec_title" + x + "_total_fields") + 1; y++) {
                                                    product_specification_modelList.
                                                            add(new product_specification_model(1, documentSnapshot.get("spec_title" + x + "_field" + y + "_name").toString(),
                                                                    documentSnapshot.get("spec_title" + x + "_field" + y + "_value").toString()));

                                                }
                                            }
                                        } else {
                                            product_details_tabs_container.setVisibility(View.GONE);
                                            product_details_only_container.setVisibility(View.VISIBLE);
                                            product_only_description_body.setText(documentSnapshot.get("product_description").toString());
                                        }

                                        total_ratings.setText((long) documentSnapshot.get("total_rating") + " ratings");


                                        for (int x = 0; x < 5; x++) {
                                            TextView rating = (TextView) rating_number_container.getChildAt(x);
                                            rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));
                                            ProgressBar progressBar = (ProgressBar) ratingsprogress_bar_container.getChildAt(x);
                                            int maxprogress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_rating")));
                                            progressBar.setMax(maxprogress);
                                            progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));

                                        }
                                        total_ratings_figure.setText(String.valueOf((long) documentSnapshot.get("total_rating")));
                                        average_rating.setText(documentSnapshot.get("average_rating").toString());
                                        product_details_viewpager.setAdapter(new product_details_adapter(getSupportFragmentManager(), product_details_tablayout.getTabCount(), product_description, product_other_details, product_specification_modelList));

                                        if (current_user != null) {
                                            if (db_querries.my_ratings.size() == 0) {
                                                db_querries.load_ratinglist(product_details_Activity.this);
                                            }
                                            if (db_querries.cart_list.size() == 0) {
                                                db_querries.loadCartList(product_details_Activity.this, loading_dialog, false, badgecount, new TextView(product_details_Activity.this));
                                            }
                                            if (db_querries.wish_list.size() == 0) {
                                                db_querries.loadWishlist(product_details_Activity.this, loading_dialog, false);
                                            }
                                            if (db_querries.rewards_modelList.size()==0){
                                                db_querries.loadRewards(product_details_Activity.this,loading_dialog,false);
                                            }
                                            if (db_querries.cart_list.size() != 0 && db_querries.wish_list.size() != 0  &&  db_querries.rewards_modelList.size()!=0  ){
                                                loading_dialog.dismiss();
                                            }
                                        } else {
                                            loading_dialog.dismiss();
                                        }

                                        if (db_querries.my_rated_ids.contains(product_id)) {
                                            int index = db_querries.my_rated_ids.indexOf(product_id);
                                            initial_rating = Integer.parseInt(String.valueOf(db_querries.my_ratings.get(index))) - 1;
                                            setrating(initial_rating);
                                        }
                                        if (db_querries.cart_list.contains(product_id)) {
                                            ALREADY_ADDED_TO_CART = true;
                                        } else {
                                            ALREADY_ADDED_TO_CART = false;
                                        }
                                        if (db_querries.wish_list.contains(product_id)) {
                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addto_wishlist_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FA4848")));
                                        } else {
                                            addto_wishlist_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                                            ALREADY_ADDED_TO_WISHLIST = false;
                                        }

                                        if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                            inStock = true;
                                            buy_now_button.setVisibility(View.VISIBLE);
                                            add_to_cart_button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (current_user == null) {
                                                        signin_dialog.show();
                                                    } else {
                                                        if (!running_cart_querry) {
                                                            running_cart_querry = true;
                                                            if (ALREADY_ADDED_TO_CART) {
                                                                running_cart_querry = false;
                                                                Toast.makeText(product_details_Activity.this, "Already Added to Cart", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                Map<String, Object> addproduct = new HashMap<>();
                                                                addproduct.put("product_id" + String.valueOf(db_querries.cart_list.size()), product_id);
                                                                addproduct.put("list_size", (long) (db_querries.cart_list.size() + 1));


                                                                firebaseFirestore.collection("Users").document(current_user.getUid())
                                                                        .collection("USER_DATA").document("MY_CART")
                                                                        .update(addproduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {


                                                                            if (db_querries.cart_item_modelList.size() != 0) {

                                                                                db_querries.cart_item_modelList.add(0, new cart_item_model(documentSnapshot.getBoolean("cod"),
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
                                                                                        inStock,
                                                                                        (long) documentSnapshot.get("max_quantity"),
                                                                                        (long) documentSnapshot.get("stock_quantity")));

                                                                            }

                                                                            ALREADY_ADDED_TO_CART = true;
                                                                            db_querries.cart_list.add(product_id);
                                                                            Toast.makeText(product_details_Activity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                                                                            invalidateOptionsMenu();
                                                                            running_cart_querry = false;
                                                                        } else {
                                                                            //addto_wishlist_button.setEnabled(true);
                                                                            running_cart_querry = false;
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(product_details_Activity.this, error, Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });


                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        } else {
                                            inStock=false;
                                            buy_now_button.setVisibility(View.GONE);
                                            TextView out_of_stock = (TextView) add_to_cart_button.getChildAt(0);
                                            out_of_stock.setText("OUT OF STOCK");
                                            out_of_stock.setTextColor(getResources().getColor(R.color.error));
                                            out_of_stock.setCompoundDrawables(null, null, null, null);

                                        }

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(product_details_Activity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    loading_dialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(product_details_Activity.this, error, Toast.LENGTH_LONG).show();
                }
            }
        });


        viewpager_indicator.setupWithViewPager(product_images_viewpager, true);

        addto_wishlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_user == null) {
                    signin_dialog.show();
                } else {

                    //addto_wishlist_button.setEnabled(false);
                    if (!running_wishlist_querry) {
                        running_wishlist_querry = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = db_querries.wish_list.indexOf(product_id);
                            db_querries.RemoveFrom_wishlist(index, product_details_Activity.this);
                            addto_wishlist_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        } else {
                            addto_wishlist_button.setSupportImageTintList(getResources().getColorStateList(R.color.wishlist_red));
                            Map<String, Object> addproduct = new HashMap<>();
                            addproduct.put("product_id" + String.valueOf(db_querries.wish_list.size()), product_id);
                            addproduct.put("list_size", (long) (db_querries.wish_list.size() + 1));

                            firebaseFirestore.collection("Users").document(current_user.getUid())
                                    .collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addproduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        if (db_querries.wish_list_model_list.size() != 0) {

                                            db_querries.wish_list_model_list.add(new wishlist_model(product_id, documentSnapshot.get("product_image1").toString(),
                                                    documentSnapshot.get("product_title").toString(),
                                                    (long) documentSnapshot.get("free_coupons"),
                                                    documentSnapshot.get("average_rating").toString(),
                                                    (long) documentSnapshot.get("total_rating"),
                                                    documentSnapshot.get("product_price").toString(),
                                                    documentSnapshot.get("cutted_price").toString(),
                                                    (boolean) documentSnapshot.get("cod"),
                                                    inStock));

                                        }

                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        addto_wishlist_button.setSupportImageTintList(getResources().getColorStateList(R.color.wishlist_red));
                                        db_querries.wish_list.add(product_id);
                                        Toast.makeText(product_details_Activity.this, "Added To Wishlist", Toast.LENGTH_SHORT).show();


                                    } else {
                                        //addto_wishlist_button.setEnabled(true);
                                        addto_wishlist_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));

                                        String error = task.getException().getMessage();
                                        Toast.makeText(product_details_Activity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                    running_wishlist_querry = false;
                                }
                            });


                        }
                    }

                }
            }
        });

        product_details_viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(product_details_tablayout));

        product_details_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                product_details_viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//////////////rating layout code
        ratenowcontainer = findViewById(R.id.rate_now_container);
        for (int x = 0; x < ratenowcontainer.getChildCount(); x++) {
            final int star_position = x;
            ratenowcontainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if (current_user == null) {
                        signin_dialog.show();
                    } else {
                        if (star_position != initial_rating) {
                            if (!running_rating_querry) {
                                running_rating_querry = true;
                                setrating(star_position);
                                Map<String, Object> update_rating = new HashMap<>();
                                if (db_querries.my_rated_ids.contains(product_id)) {

                                    TextView oldrating = (TextView) rating_number_container.getChildAt(5 - initial_rating - 1);
                                    TextView finalrating = (TextView) rating_number_container.getChildAt(5 - star_position - 1);

                                    update_rating.put(initial_rating + 1 + "_star", Long.parseLong(oldrating.getText().toString()) - 1);
                                    update_rating.put(star_position + 1 + "_star", Long.parseLong(finalrating.getText().toString()) + 1);
                                    update_rating.put("average_rating", (calculate_aveage_rating((long) star_position - initial_rating, true)));

                                } else {

                                    update_rating.put(star_position + 1 + "_star", (long) documentSnapshot.get(star_position + 1 + "_star") + 1);
                                    update_rating.put("average_rating", (calculate_aveage_rating((long) star_position + 1, false)));
                                    update_rating.put("total_rating", (long) documentSnapshot.get("total_rating") + 1);
                                }

                                firebaseFirestore.collection("PRODUCTS").document(product_id)
                                        .update(update_rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myrating = new HashMap<>();

                                            if (db_querries.my_rated_ids.contains(product_id)) {
                                                myrating.put("rating" + db_querries.my_rated_ids.indexOf(product_id), (long) star_position + 1);
                                            } else {

                                                myrating.put("list_size", (long) db_querries.my_rated_ids.size() + 1);
                                                myrating.put("product_id" + db_querries.my_rated_ids.size(), product_id);
                                                myrating.put("rating" + db_querries.my_rated_ids.size(), (long) star_position + 1);

                                            }
                                            firebaseFirestore.collection("Users").document(current_user.getUid())
                                                    .collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (db_querries.my_rated_ids.contains(product_id)) {
                                                            db_querries.my_ratings.set(db_querries.my_rated_ids.indexOf(product_id), (long) star_position + 1);

                                                            TextView oldrating = (TextView) rating_number_container.getChildAt(5 - initial_rating - 1);
                                                            TextView finalrating = (TextView) rating_number_container.getChildAt(5 - star_position - 1);
                                                            oldrating.setText(String.valueOf(Integer.parseInt(oldrating.getText().toString()) - 1));
                                                            finalrating.setText(String.valueOf(Integer.parseInt(finalrating.getText().toString()) + 1));


                                                        } else {
                                                            db_querries.my_rated_ids.add(product_id);
                                                            db_querries.my_ratings.add((long) star_position + 1);

                                                            TextView rating = (TextView) rating_number_container.getChildAt(5 - star_position - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            total_rating_miniview.setText("(" + ((long) documentSnapshot.get("total_rating") + 1) + ")ratings");
                                                            total_ratings.setText((long) documentSnapshot.get("total_rating") + 1 + " ratings");
                                                            total_ratings_figure.setText(String.valueOf((long) documentSnapshot.get("total_rating") + 1));


                                                            Toast.makeText(product_details_Activity.this, "Thank You For Rating this Product!", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) rating_number_container.getChildAt(x);

                                                            ProgressBar progressBar = (ProgressBar) ratingsprogress_bar_container.getChildAt(x);
                                                            int maxprogress = Integer.parseInt(total_ratings_figure.getText().toString());
                                                            progressBar.setMax(maxprogress);
                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));

                                                        }
                                                        initial_rating = star_position;
                                                        average_rating.setText((calculate_aveage_rating(0, true)));
                                                        average_rating_miniview.setText((calculate_aveage_rating(0, true)));

                                                        if (db_querries.wish_list.contains(product_id) && db_querries.wish_list_model_list.size() != 0) {
                                                            int index = db_querries.wish_list.indexOf(product_id);
                                                            db_querries.wish_list_model_list.get(index).setRating(average_rating.getText().toString());
                                                            db_querries.wish_list_model_list.get(index).setTotal_ratings(Long.parseLong(total_ratings_figure.getText().toString()));

                                                        }


                                                    } else {
                                                        setrating(initial_rating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(product_details_Activity.this, error, Toast.LENGTH_LONG).show();
                                                    }
                                                    running_rating_querry = false;
                                                }
                                            });
                                        } else {
                                            running_rating_querry = false;
                                            setrating(initial_rating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(product_details_Activity.this, error, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }


            });
        }


//////////////rating layout code
        buy_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current_user == null) {
                    signin_dialog.show();
                } else {
                    delivery_activity.FROM_CART = false;
                    loading_dialog.show();
                    productDetails_activity = product_details_Activity.this;
                    delivery_activity.cart_item_modelList = new ArrayList<>();
                    delivery_activity.cart_item_modelList.add(new cart_item_model(documentSnapshot.getBoolean("cod"),
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
                            inStock,
                            (long) documentSnapshot.get("max_quantity"),
                            (long) documentSnapshot.get("stock_quantity")));
                    delivery_activity.cart_item_modelList.add(new cart_item_model(cart_item_model.TOTAL_AMOUNT));

                    if (db_querries.addresses_modelList.size() == 0) {
                        db_querries.loadAddresses(product_details_Activity.this, loading_dialog,true);
                    } else {
                        loading_dialog.dismiss();
                        Intent delivery_intent = new Intent(product_details_Activity.this, delivery_activity.class);
                        startActivity(delivery_intent);
                    }
                }
            }
        });



        coupon_redeem_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_coupon_price_dialog.show();


            }
        });

        //////signin dialog
        signin_dialog = new Dialog(product_details_Activity.this);
        signin_dialog.setContentView(R.layout.sign_in_dialog);
        signin_dialog.setCancelable(true);

        signin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dialog_sign_in_button = signin_dialog.findViewById(R.id.sign_in_button);
        Button dialog_sign_up_button = signin_dialog.findViewById(R.id.sign_up_button);
        final Intent register_intent = new Intent(product_details_Activity.this, Registry_activity.class);

        dialog_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signin.disable_close_button = true;
                Signup.disable_close_button = true;
                signin_dialog.dismiss();
                set_signup_fragment = false;
                startActivity(register_intent);
            }
        });
        dialog_sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup.disable_close_button = true;
                Signin.disable_close_button = true;
                signin_dialog.dismiss();
                set_signup_fragment = true;
                startActivity(register_intent);

            }
        });
        //////signin dialog


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        if (current_user == null) {
            coupon_redeem_layout.setVisibility(View.GONE);
        } else {
            coupon_redeem_layout.setVisibility(View.VISIBLE);
        }
        if (current_user != null) {
            if (db_querries.my_ratings.size() == 0) {
                db_querries.load_ratinglist(product_details_Activity.this);
            }
            if (db_querries.wish_list.size() == 0) {
                db_querries.loadWishlist(product_details_Activity.this, loading_dialog, false);
            }
            if (db_querries.rewards_modelList.size()==0){
                db_querries.loadRewards(product_details_Activity.this,loading_dialog,false);
            }
            if (db_querries.cart_list.size() != 0 && db_querries.wish_list.size() != 0  &&  db_querries.rewards_modelList.size()!=0  ){
                loading_dialog.dismiss();
            }
        } else {
            loading_dialog.dismiss();
        }

        if (db_querries.my_rated_ids.contains(product_id)) {
            int index = db_querries.my_rated_ids.indexOf(product_id);
            initial_rating = Integer.parseInt(String.valueOf(db_querries.my_ratings.get(index))) - 1;
            setrating(initial_rating);
        }
        if (db_querries.cart_list.contains(product_id)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }

        if (db_querries.wish_list.contains(product_id)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addto_wishlist_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FA4848")));
        } else {
            product_details_Activity.addto_wishlist_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setrating(int star_position) {

        for (int x = 0; x < ratenowcontainer.getChildCount(); x++) {
            ImageView star_button = (ImageView) ratenowcontainer.getChildAt(x);
            star_button.setImageTintList(ColorStateList.valueOf(Color.parseColor("#AFA5A5")));
            if (x <= star_position) {
                star_button.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
            }
        }
    }

    private String calculate_aveage_rating(long current_user_rating, boolean update) {
        Double total_stars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView raing_number = (TextView) rating_number_container.getChildAt(5 - x);
            total_stars = total_stars + (Long.parseLong(raing_number.getText().toString()) * x);
        }
        total_stars = total_stars + current_user_rating;
        if (update) {
            return String.valueOf(total_stars / (Long.parseLong(total_ratings_figure.getText().toString()))).substring(0, 3);
        } else {
            return String.valueOf(total_stars / (Long.parseLong(total_ratings_figure.getText().toString()) + 1)).substring(0, 3);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cart_item = menu.findItem(R.id.main_cart_icon);
        cart_item.setActionView(R.layout.badge_layout);
        ImageView badgeIcon = cart_item.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.cart_final2);
        badgecount = cart_item.getActionView().findViewById(R.id.badge_count);
        if (current_user != null) {
            if (db_querries.cart_list.size() == 0) {
                db_querries.loadCartList(product_details_Activity.this, loading_dialog, false, badgecount, new TextView(product_details_Activity.this));
            } else {
                badgecount.setVisibility(View.VISIBLE);
                if (db_querries.cart_list.size() < 99) {
                    badgecount.setText(String.valueOf(db_querries.cart_list.size()));
                } else {
                    badgecount.setText("99");
                }
            }
        }
        cart_item.getActionView().setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (current_user == null) {
                    signin_dialog.show();
                } else {
                    Intent cart_intent = new Intent(product_details_Activity.this, MainActivity2.class);
                    showcart = true;
                    startActivity(cart_intent);

                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            productDetails_activity = null;
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            if (fromSEARCH){
                finish();
            }else{
                Intent search_intent=new Intent(this,search_activity.class);
                startActivity(search_intent);
            }

            return true;
        } else if (id == R.id.main_cart_icon) {
            if (current_user == null) {
                signin_dialog.show();
            } else {
                Intent cart_intent = new Intent(product_details_Activity.this, MainActivity2.class);
                showcart = true;
                startActivity(cart_intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSEARCH=false;
    }

    @Override
    public void onBackPressed() {
        productDetails_activity = null;
        super.onBackPressed();
    }
}
