package com.example.ecommerce_03;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class my_rewards_adapter extends RecyclerView.Adapter<my_rewards_adapter.ViewHolder> {
    private List<my_rewards_model> myRewardsModelList;
    private Boolean use_mini_layount = false;
    private RecyclerView coupons_recycler_view;
    private LinearLayout selected_coupon;
    private String product_original_price;
    private TextView selected_coupon_title;
    private TextView selected_coupon_Expiry_date;
    private TextView selected_coupon_body;
    private TextView discounted_price;
    private int cart_item_position=-1;
    private List<cart_item_model>cart_item_modelList;

    public my_rewards_adapter(List<my_rewards_model> myRewardsModelList, Boolean use_mini_layount) {
        this.myRewardsModelList = myRewardsModelList;
        this.use_mini_layount = use_mini_layount;
    }

    public my_rewards_adapter(List<my_rewards_model> myRewardsModelList, Boolean use_mini_layount, RecyclerView coupons_recycler_view, LinearLayout selected_coupon, String product_original_price, TextView coupon_title, TextView coupon_Expiry_date, TextView coupon_body,TextView discounted_price) {
        this.myRewardsModelList = myRewardsModelList;
        this.use_mini_layount = use_mini_layount;
        this.coupons_recycler_view = coupons_recycler_view;
        this.selected_coupon = selected_coupon;
        this.product_original_price = product_original_price;
        this.selected_coupon_title = coupon_title;
        this.selected_coupon_Expiry_date = coupon_Expiry_date;
        this.selected_coupon_body = coupon_body;
        this.discounted_price=discounted_price;
    }
    public my_rewards_adapter(int cart_item_position,List<my_rewards_model> myRewardsModelList, Boolean use_mini_layount, RecyclerView coupons_recycler_view, LinearLayout selected_coupon, String product_original_price, TextView coupon_title, TextView coupon_Expiry_date, TextView coupon_body,TextView discounted_price,List<cart_item_model>cart_item_modelList) {
        this.myRewardsModelList = myRewardsModelList;
        this.use_mini_layount = use_mini_layount;
        this.coupons_recycler_view = coupons_recycler_view;
        this.selected_coupon = selected_coupon;
        this.product_original_price = product_original_price;
        this.selected_coupon_title = coupon_title;
        this.selected_coupon_Expiry_date = coupon_Expiry_date;
        this.selected_coupon_body = coupon_body;
        this.discounted_price=discounted_price;
        this.cart_item_position=cart_item_position;
        this.cart_item_modelList=cart_item_modelList;
    }


    @NonNull
    @Override
    public my_rewards_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (use_mini_layount) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);
        }

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull my_rewards_adapter.ViewHolder viewHolder, int position) {
        String coupon_id=myRewardsModelList.get(position).getCoupon_id();
        String type = myRewardsModelList.get(position).getType();
        Date validity = myRewardsModelList.get(position).getTimestamp();
        String body = myRewardsModelList.get(position).getCoupon_body();
        String lower_limit = myRewardsModelList.get(position).getLower_limit();
        String upper_limit = myRewardsModelList.get(position).getUpper_limit();
        String discount_OR_Amount = myRewardsModelList.get(position).getDiscount_or_amount();
        boolean already_used=myRewardsModelList.get(position).isAlready_used();


        viewHolder.setdata(coupon_id,type, validity, body, upper_limit, lower_limit, discount_OR_Amount,already_used);


    }

    @Override
    public int getItemCount() {
        return myRewardsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView coupon_title;
        private TextView coupon_expiry_date;
        private TextView coupon_body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coupon_title = itemView.findViewById(R.id.coupon_title);
            coupon_expiry_date = itemView.findViewById(R.id.coupon_validity);
            coupon_body = itemView.findViewById(R.id.coupon_body);

        }

        private void setdata(final String coupon_id, final String type, final Date validity, final String body, final String upper_limit, final String lower_limit, final String discount_or_amount, final boolean already_used) {

            if (type.equals("Discount")) {
                coupon_title.setText(type);
            } else {
                coupon_title.setText("Cashback");
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
            if (already_used){
                coupon_expiry_date.setText("Coupon Expired");
                coupon_expiry_date.setTextColor(itemView.getContext().getResources().getColor(R.color.error));
                coupon_body.setTextColor(Color.parseColor("#50ffffff"));
                coupon_title.setTextColor(Color.parseColor("#50ffffff"));
            }else{
                coupon_expiry_date.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                coupon_expiry_date.setText("Till " + simpleDateFormat.format(validity));
                coupon_body.setTextColor(Color.parseColor("#ffffff"));
                coupon_title.setTextColor(Color.parseColor("#ffffff"));
            }

            coupon_body.setText(body);



            if (use_mini_layount) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!already_used){
                            selected_coupon_title.setText(type);
                            selected_coupon_Expiry_date.setText(simpleDateFormat.format(validity));
                            selected_coupon_body.setText(body);

                            if(Long.valueOf(product_original_price)>Long.valueOf(lower_limit)&&Long.valueOf(product_original_price)<Long.valueOf(upper_limit)){
                                if (type.equals("Discount")){
                                    Long discountamount=Long.valueOf(product_original_price)*Long.valueOf(discount_or_amount)/100;
                                    discounted_price.setText("Rs "+String.valueOf(Long.valueOf(product_original_price)-discountamount+"/-"));
                                }else{
                                    discounted_price.setText("Rs "+String.valueOf(Long.valueOf(product_original_price)-Long.valueOf(discount_or_amount))+"/-");
                                }
                                if (cart_item_position != -1) {
                                    cart_item_modelList.get(cart_item_position).setSelected_coupon_id(coupon_id);
                                }
                            }else{
                                if (cart_item_position != -1) {
                                    cart_item_modelList.get(cart_item_position).setSelected_coupon_id(null);
                                }
                                discounted_price.setText("Invalid");
                                Toast.makeText(itemView.getContext(), "Sorry! Product does not matches the coupon terms. ", Toast.LENGTH_LONG).show();
                            }

                            if (coupons_recycler_view.getVisibility() == View.GONE) {
                                coupons_recycler_view.setVisibility(View.VISIBLE);
                                selected_coupon.setVisibility(View.GONE);
                            } else {
                                coupons_recycler_view.setVisibility(View.GONE);
                                selected_coupon.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });
            }
        }
    }
}
