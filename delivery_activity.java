package com.example.ecommerce_03;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/*import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;*/

public class delivery_activity extends AppCompatActivity implements PaymentResultListener {
    public static final int SELECT_ADDRESS = 0;
    private RecyclerView delivery_recycler_view;
    private Button change_add_new_address_button;
    private TextView total_amount;
    private TextView fullname;
    private String name, mobileno;
    private TextView full_address;
    private TextView pincode;
    public static List<cart_item_model> cart_item_modelList;
    private Button continue_button;
    public static Dialog loading_dialog;
    private Dialog payment_method_dialog;
    private ImageButton online_payment;
    private ImageButton cod;
    private TextView cod_btn_title;
    private View payment_divider;
    private static final String TAG = delivery_activity.class.getSimpleName();
    final String order_id = UUID.randomUUID().toString().substring(0, 28);
    private ConstraintLayout order_confirmation_layout;
    private ImageButton continue_shopping_button;
    private TextView Order_id;
    public static boolean cod_order_confirmed = false;
    private String payment_method = "Razor Pay";


    Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    public static cart_adapter cartAdapter;

    private String trimmed_amount;
    private String payment;
    private double total;

    private boolean success_response = false;
    public static boolean FROM_CART;
    public static boolean get_qty_ids = true;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        Checkout.preload(getApplicationContext());


        delivery_recycler_view = findViewById(R.id.delivery_recycler_view);
        change_add_new_address_button = findViewById(R.id.change_or_add_address);
        total_amount = findViewById(R.id.total_cart_amount);

        continue_button = findViewById(R.id.cart_continue_button);

        order_confirmation_layout = findViewById(R.id.order_confirmation_layout);
        continue_shopping_button = findViewById(R.id.continue_shopping_btn);
        Order_id = findViewById(R.id.order_id);

        /////loading dialog
        loading_dialog = new Dialog(delivery_activity.this);
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog

        /////payment dialog
        payment_method_dialog = new Dialog(delivery_activity.this);
        payment_method_dialog.setContentView(R.layout.payment_method);
        payment_method_dialog.setCancelable(true);
        payment_method_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        payment_method_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        online_payment = payment_method_dialog.findViewById(R.id.online_payment_button);
        cod = payment_method_dialog.findViewById(R.id.cod_button);
        cod_btn_title = payment_method_dialog.findViewById(R.id.cod_btn_title);
        payment_divider = payment_method_dialog.findViewById(R.id.payment_divider);
        /////payment dialog

        firebaseFirestore = FirebaseFirestore.getInstance();
        get_qty_ids = true;

        fullname = findViewById(R.id.fullname);
        full_address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        delivery_recycler_view.setLayoutManager(layoutManager);

        cartAdapter = new cart_adapter(cart_item_modelList, total_amount, false);
        delivery_recycler_view.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        change_add_new_address_button.setVisibility(View.VISIBLE);
        change_add_new_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_qty_ids = false;
                Intent myaddresses_intent = new Intent(delivery_activity.this, my_adresses_activity.class);
                myaddresses_intent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myaddresses_intent);
            }
        });

        /*fullname.setText(db_querries.addresses_modelList.get(db_querries.selected_address).getFullname());
        full_address.setText(db_querries.addresses_modelList.get(db_querries.selected_address).getAddress());
        pincode.setText(db_querries.addresses_modelList.get(db_querries.selected_address).getPincode());*/

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean all_products_available = true;
                for (cart_item_model cart_item_model : cart_item_modelList) {
                    if (cart_item_model.isQty_error()) {
                        all_products_available = false;
                        break;
                    }
                    if (cart_item_model.getType() == com.example.ecommerce_03.cart_item_model.CART_ITEM) {
                        if (!cart_item_model.isCOD()) {
                            cod.setEnabled(false);
                            cod.setAlpha(0.2f);
                            cod_btn_title.setAlpha(0.2f);


                            break;
                        } else {
                            cod.setEnabled(true);
                            cod.setAlpha(1f);
                            cod_btn_title.setAlpha(1f);

                        }
                    }

                }
                if (all_products_available) {
                    payment_method_dialog.show();
                }

            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payment_method = "Cod";

                place_order_details();

            }
        });


        online_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payment_method = "Razor Pay";

                place_order_details();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

///////accessing quantity

        if (get_qty_ids) {
            loading_dialog.show();
            for (int x = 0; x < cart_item_modelList.size() - 1; x++) {

                for (int y = 0; y < cart_item_modelList.get(x).getProduct_quantity(); y++) {
                    final String qty_document_name = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> time_stamp = new HashMap<>();
                    time_stamp.put("time", FieldValue.serverTimestamp());
                    final int finalX = x;
                    final int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cart_item_modelList.get(x).getProduct_id())
                            .collection("QUANTITY")
                            .document(qty_document_name)
                            .set(time_stamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        cart_item_modelList.get(finalX).getQuantity_IDs().add(qty_document_name);
                                        if (finalY + 1 == cart_item_modelList.get(finalX).getProduct_quantity()) {

                                            firebaseFirestore.collection("PRODUCTS").document(cart_item_modelList.get(finalX).getProduct_id())
                                                    .collection("QUANTITY")
                                                    .orderBy("time", Query.Direction.ASCENDING)
                                                    .limit(cart_item_modelList.get(finalX).getProduct_stock_quantity())
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
                                                                boolean no_longer_available = true;
                                                                for (String qtyID : cart_item_modelList.get(finalX).getQuantity_IDs()) {
                                                                    cart_item_modelList.get(finalX).setQty_error(false);
                                                                    if (!server_quantity.contains(qtyID)) {

                                                                        if (no_longer_available) {
                                                                            cart_item_modelList.get(finalX).setIn_stock(false);
                                                                        } else {
                                                                            cart_item_modelList.get(finalX).setQty_error(true);
                                                                            cart_item_modelList.get(finalX).setProduct_max_quantity(available_quantity);
                                                                            Toast.makeText(delivery_activity.this, "Sorry! All products may not be available in required quantity", Toast.LENGTH_LONG).show();
                                                                        }

                                                                    } else {
                                                                        available_quantity++;
                                                                        no_longer_available = false;
                                                                    }
                                                                }
                                                                cartAdapter.notifyDataSetChanged();
                                                            } else {
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(delivery_activity.this, error, Toast.LENGTH_LONG).show();
                                                            }
                                                            loading_dialog.dismiss();
                                                        }
                                                    });

                                        }
                                    } else {
                                        loading_dialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(delivery_activity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        } else {
            get_qty_ids = true;
        }
///////accessing quantity

        name = db_querries.addresses_modelList.get(db_querries.selected_address).getName();
        mobileno = db_querries.addresses_modelList.get(db_querries.selected_address).getMobile_no();
        if (db_querries.addresses_modelList.get(db_querries.selected_address).getAlternate_mobile_no().equals("")) {
            fullname.setText(name + " - " + mobileno);
        } else {
            fullname.setText(name + " - " + mobileno + " or " + db_querries.addresses_modelList.get(db_querries.selected_address).getAlternate_mobile_no());
        }
        String flat_no = db_querries.addresses_modelList.get(db_querries.selected_address).getFlat_no();
        String locality = db_querries.addresses_modelList.get(db_querries.selected_address).getLocality();
        String landmark = db_querries.addresses_modelList.get(db_querries.selected_address).getLandmark();
        String city = db_querries.addresses_modelList.get(db_querries.selected_address).getCity();
        String state = db_querries.addresses_modelList.get(db_querries.selected_address).getState();

        if (landmark.equals("")){
            full_address.setText(flat_no +" "+ locality +" "+ city +" "+ state);
        }else{
            full_address.setText(flat_no +" "+ locality +" "+ landmark +" "+ city +" "+ state);
        }


        pincode.setText(db_querries.addresses_modelList.get(db_querries.selected_address).getPincode());

        if (cod_order_confirmed) {
            show_confirmationlayout();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loading_dialog.dismiss();
        if (get_qty_ids) {
            for (int x = 0; x < cart_item_modelList.size() - 1; x++) {
                if (!success_response) {
                    for (final String qtyID : cart_item_modelList.get(x).getQuantity_IDs()) {
                        final int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cart_item_modelList.get(x).getProduct_id())
                                .collection("QUANTITY")
                                .document(qtyID)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (qtyID.equals(cart_item_modelList.get(finalX).getQuantity_IDs().get(cart_item_modelList.get(finalX).getQuantity_IDs().size() - 1))) {
                                    cart_item_modelList.get(finalX).getQuantity_IDs().clear();
                                }
                            }
                        });

                    }
                } else {
                    cart_item_modelList.get(x).getQuantity_IDs().clear();
                }


            }
        }
    }

    @Override
    public void onBackPressed() {
        if (success_response) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void show_confirmationlayout() {
        success_response = true;
        cod_order_confirmed = false;
        get_qty_ids = false;
        for (int x = 0; x < cart_item_modelList.size() - 1; x++) {

            for (String qtyID : cart_item_modelList.get(x).getQuantity_IDs()) {

                firebaseFirestore.collection("PRODUCTS").document(cart_item_modelList.get(x).getProduct_id())
                        .collection("QUANTITY")
                        .document(qtyID)
                        .update("user_id", FirebaseAuth.getInstance().getUid());

            }


        }

        if (MainActivity2.mainActivity != null) {
            MainActivity2.mainActivity.finish();
            MainActivity2.mainActivity = null;
            MainActivity2.showcart = false;
        } else {
            MainActivity2.reset_main_activity = true;
        }
        if (product_details_Activity.productDetails_activity != null) {
            product_details_Activity.productDetails_activity.finish();
            product_details_Activity.productDetails_activity = null;
        }
        //////sent confirmation code
        String SMS_API = "https://www.fast2sms.com/dev/bulk";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //////noting
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //////noting
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "sWOc6pJPNVqSL4Y7QmlBkdHbnGXax9CrZDFy10hfiIwg5tvMeRo2HkElGUIBLNpzZq98DWmCePOr7fvJ");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "FSTSMS");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", mobileno);
                body.put("message", "31122");
                body.put("variables", "{#FF#}");
                body.put("variables_values", order_id);
                return body;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue requestQueue = Volley.newRequestQueue(delivery_activity.this);
        requestQueue.add(stringRequest);
        //////sent confirmation code


        if (FROM_CART) {
            loading_dialog.show();
            Map<String, Object> updateCartList = new HashMap<>();
            long cartlist_size = 0;
            final List<Integer> indexList = new ArrayList<>();
            for (int x = 0; x < db_querries.cart_list.size(); x++) {
                if (!cart_item_modelList.get(x).isIn_stock()) {
                    updateCartList.put("product_id" + cartlist_size, cart_item_modelList.get(x).getProduct_id());
                    cartlist_size++;
                } else {
                    indexList.add(x);
                }
            }
            updateCartList.put("list_size", cartlist_size);

            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        for (int x = 0; x < indexList.size(); x++) {
                            db_querries.cart_list.remove(indexList.get(x).intValue());
                            db_querries.cart_item_modelList.remove(indexList.get(x).intValue());
                            db_querries.cart_item_modelList.remove(db_querries.cart_item_modelList.size() - 1);
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(delivery_activity.this, error, Toast.LENGTH_LONG).show();
                    }
                    loading_dialog.dismiss();
                }
            });
        }

        continue_button.setEnabled(false);
        change_add_new_address_button.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Order_id.setText("Order ID: " + order_id);
        order_confirmation_layout.setVisibility(View.VISIBLE);
        continue_shopping_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void place_order_details() {

        loading_dialog.show();
        String userID = FirebaseAuth.getInstance().getUid();
        for (cart_item_model cart_item_model : cart_item_modelList) {

            if (cart_item_model.getType() == cart_item_model.CART_ITEM) {

                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER_ID", order_id);
                orderDetails.put("product_id", cart_item_model.getProduct_id());
                orderDetails.put("product_image", cart_item_model.getProduct_image());
                orderDetails.put("product_title", cart_item_model.getProduct_title());
                orderDetails.put("user_id", userID);
                orderDetails.put("product_quantity", cart_item_model.getProduct_quantity());
                if (cart_item_model.getCutted_price() != null) {
                    orderDetails.put("cutted_price", cart_item_model.getCutted_price());
                } else {
                    orderDetails.put("cutted_price", "");
                }
                orderDetails.put("product_price", cart_item_model.getProduct_price());
                if (cart_item_model.getSelected_coupon_id() != null) {
                    orderDetails.put("coupon_id", cart_item_model.getSelected_coupon_id());
                } else {
                    orderDetails.put("coupon_id", "");
                }
                if (cart_item_model.getDiscounted_price() != null) {
                    orderDetails.put("discounted_price", cart_item_model.getDiscounted_price());
                } else {
                    orderDetails.put("discounted_price", "");
                }
                orderDetails.put("order date", FieldValue.serverTimestamp());
                orderDetails.put("packed date", FieldValue.serverTimestamp());
                orderDetails.put("shipped date", FieldValue.serverTimestamp());
                orderDetails.put("delivered date", FieldValue.serverTimestamp());
                orderDetails.put("cancelled date", FieldValue.serverTimestamp());
                orderDetails.put("order_status", "Ordered");
                orderDetails.put("payment_method", payment_method);
                orderDetails.put("address", full_address.getText());
                orderDetails.put("full_name", fullname.getText());
                orderDetails.put("pincode", pincode.getText());
                orderDetails.put("free_coupons", cart_item_model.getFree_coupons());
                orderDetails.put("delivery_price", cart_item_modelList.get(cart_item_modelList.size() - 1).getDelivery_price());
                orderDetails.put("cancellation_requested", false);

                firebaseFirestore.collection("ORDERS").document(order_id).collection("order_items").document(cart_item_model.getProduct_id())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {

                            String error = task.getException().getMessage();
                            Toast.makeText(delivery_activity.this, error, Toast.LENGTH_LONG).show();

                        }
                    }
                });
            } else {

                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("total_items", cart_item_model.getTotal_items());
                orderDetails.put("total_item_price", cart_item_model.getTotalItems_price());
                orderDetails.put("delivery_price", cart_item_model.getDelivery_price());
                orderDetails.put("total_amount", cart_item_model.getTotal_amount());
                orderDetails.put("saved_amount", cart_item_model.getSavedAmount());
                orderDetails.put("payment_status", "Not Paid");
                orderDetails.put("order_status", "Cancelled");

                firebaseFirestore.collection("ORDERS").document(order_id)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            if (payment_method.equals("Razor Pay")) {

                                Razorpay();

                            } else {

                                cash_on_delivery();

                            }


                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(delivery_activity.this, error, Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }


        }

    }

    //////////////////////  P A Y M E N T   M E T H O D S //////////////////////


    ///razor pay payment methods

    private void Razorpay() {
        get_qty_ids = false;
        payment_method_dialog.dismiss();
        loading_dialog.show();
                /*if (ContextCompat.checkSelfPermission(delivery_activity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(delivery_activity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }

                final String M_ID = "BANtLO31605626663231";
                final String customer_id = FirebaseAuth.getInstance().getUid();
                final String order_id = UUID.randomUUID().toString().substring(0, 28);
                final String url = "https://fitarms.000webhostapp.com/paytm/generateChecksum.php";
                final String callbackURL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";


                RequestQueue requestQueue = Volley.newRequestQueue(delivery_activity.this);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.has("CHECKSUMHASH"));
                            String payurl="https://securegw.paytm.in/";
                            String CHECKSUMHASH=jsonObject.getString("CHECKSUMHASH");

                            PaytmPGService paytmPGService=PaytmPGService.getProductionService();

                            HashMap<String, String> paramMap = new HashMap<String, String>();
                            paramMap.put("MID", M_ID);
                            paramMap.put("ORDER_ID", order_id);
                            paramMap.put("CUST_ID", customer_id);
                            paramMap.put("CHANNEL_ID", "WAP");
                            paramMap.put("TXN_AMOUNT", total_amount.getText().toString().substring(3, total_amount.getText().length() - 2));
                            paramMap.put("WEBSITE", "WEBSTAGING");
                            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                            paramMap.put("CALLBACK_URL", callbackURL);
                            paramMap.put("CHECKSUMHASH",CHECKSUMHASH);

                            PaytmOrder Order=new PaytmOrder(paramMap);

                            paytmPGService.initialize(Order,null);
                            paytmPGService.startPaymentTransaction(delivery_activity.this, true, true, new PaytmPaymentTransactionCallback() {
                                @Override
                                public void onTransactionResponse(Bundle inResponse) {
                                    Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void networkNotAvailable() {
                                    Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void clientAuthenticationFailed(String inErrorMessage) {
                                    Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void someUIErrorOccurred(String inErrorMessage) {
                                    Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                    Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onBackPressedCancelTransaction() {
                                    Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                    Toast.makeText(getApplicationContext(), "Transaction cancelled"+inResponse.toString() , Toast.LENGTH_LONG).show();

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading_dialog.dismiss();
                        Toast.makeText(delivery_activity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", M_ID);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", total_amount.getText().toString().substring(3, total_amount.getText().length() - 2));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callbackURL);

                        return paramMap;
                    }
                };
                requestQueue.add(stringRequest);*/

        trimmed_amount = total_amount.getText().toString().substring(2, total_amount.getText().length() - 2);
        payment = trimmed_amount;
        total = Double.parseDouble(payment);
        if (trimmed_amount.equals("")) {
            Toast.makeText(delivery_activity.this, "Amount is Empty", Toast.LENGTH_SHORT).show();

        } else {
            Checkout.clearUserData(delivery_activity.this);
            startPayment();

        }

    }

    public void startPayment() {

        final Activity checkout_activity = delivery_activity.this;
        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_Y5E9AEtKlDnpDT");
        total = total * 100;

        try {

            JSONObject options = new JSONObject();
            options.put("name", "FiTARMS");
            options.put("description", "Easy, Fast And Secured Payments");
            //options.put("order_id", order);
            options.put("currency", "INR");
            //options.put("order_id",id);
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            preFill.put("email", user.getEmail());
            preFill.put("contact", mobileno.substring(0, 10));
            options.put("prefill", preFill);

            co.open(checkout_activity, options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        loading_dialog.dismiss();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e(TAG, " payment successfull " + s.toString());
        // Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
        Checkout.clearUserData(delivery_activity.this);

        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("payment_status", "paid");
        updateStatus.put("order_status", "Ordered");
        firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> userOrder = new HashMap<>();
                            userOrder.put("order_id", order_id);
                            userOrder.put("time", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id)
                                    .set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        show_confirmationlayout();
                                    } else {
                                        Toast.makeText(delivery_activity.this, "Failed to update User's order list", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(delivery_activity.this, "Order Cancelled", Toast.LENGTH_LONG).show();
                        }

                    }
                });


    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG, "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        try {
            Toast.makeText(this, "Payment error please try again" + s.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    ///razor pay payment methods

    private void cash_on_delivery() {
        get_qty_ids = false;
        payment_method_dialog.dismiss();
        Intent otp_intent = new Intent(delivery_activity.this, otp_verificiation_activity.class);
        otp_intent.putExtra("mobile_no", mobileno.substring(0, 10));
        otp_intent.putExtra("Order ID", order_id);
        startActivity(otp_intent);
    }


    //////////////////////  P A Y M E N T   M E T H O D S //////////////////////

}


