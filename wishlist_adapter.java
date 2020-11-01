package com.example.ecommerce_03;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class wishlist_adapter extends RecyclerView.Adapter<wishlist_adapter.ViewHolder> {

    private boolean from_SEARCH;
    private List<wishlist_model>wishlist_modelList;
    private Boolean wishlist;
    private int last_position=1;

    public boolean isFrom_SEARCH() {
        return from_SEARCH;
    }

    public void setFrom_SEARCH(boolean from_SEARCH) {
        this.from_SEARCH = from_SEARCH;
    }

    public wishlist_adapter(List<wishlist_model> wishlist_modelList, Boolean wishlist) {
        this.wishlist_modelList = wishlist_modelList;
        this.wishlist=wishlist;
    }

    public List<wishlist_model> getWishlist_modelList() {
        return wishlist_modelList;
    }

    public void setWishlist_modelList(List<wishlist_model> wishlist_modelList) {
        this.wishlist_modelList = wishlist_modelList;
    }

    @NonNull
    @Override
    public wishlist_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item_layout,viewGroup,false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull wishlist_adapter.ViewHolder viewHolder, int position) {
        String product_id=wishlist_modelList.get(position).getProduct_id();
        String resource=wishlist_modelList.get(position).getProduct_image();
        String title=wishlist_modelList.get(position).getProduct_title();
        long freecoupons=wishlist_modelList.get(position).getFree_Coupon();
        String rating=wishlist_modelList.get(position).getRating();
        long totalratings=wishlist_modelList.get(position).getTotal_ratings();
        String product_price=wishlist_modelList.get(position).getProduct_price();
        String cutted_price=wishlist_modelList.get(position).getCutted_price();
        boolean payment_method=wishlist_modelList.get(position).isCod();
        boolean inStock= wishlist_modelList.get(position).isInstock();

        viewHolder.setdata(product_id,resource,title,freecoupons,rating,totalratings,product_price,cutted_price,payment_method,position,inStock);

        if (last_position < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.face_in);
            viewHolder.itemView.setAnimation(animation);
            last_position=position;
        }



    }

    @Override
    public int getItemCount() {
        return wishlist_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_image;
        private TextView product_title;
        private TextView free_coupons;
        private ImageView coupon_icon;
        private TextView ratings;
        private TextView total_ratings;
        private View price_cut;
        private TextView product_price;
        private TextView cutted_price;
        private TextView payment_method;
        private ImageButton delete_Button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image=itemView.findViewById(R.id.product_image);
            product_title=itemView.findViewById(R.id.product_title);
            free_coupons=itemView.findViewById(R.id.free_coupon);
            coupon_icon=itemView.findViewById(R.id.coupon_icon);
            ratings=itemView.findViewById(R.id.tv_product_rating_mini_view);
            total_ratings=itemView.findViewById(R.id.total_ratings);
            price_cut=itemView.findViewById(R.id.price_cut);
            product_price=itemView.findViewById(R.id.product_price);
            cutted_price=itemView.findViewById(R.id.cutted_price);
            payment_method=itemView.findViewById(R.id.payment_method);
            delete_Button=itemView.findViewById(R.id.delete_button);




        }
        private void setdata(final String product_id, String resource, String title, long free_couponno, String average_rate, long total_ratingno, String price, String cuttedpricevalue, boolean cod, final int index, boolean inStock) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.no_image)).into(product_image);
            product_title.setText(title);
            if (free_couponno != 0 && inStock) {
                coupon_icon.setVisibility(View.VISIBLE);
                free_coupons.setText("free "+free_couponno+" coupon"  );
                if(free_couponno==1){
                    free_coupons.setText("free "+free_couponno+" coupon");
                }else {
                    free_coupons.setText("free "+free_couponno+" coupons");
                }
            }else{
                coupon_icon.setVisibility(View.INVISIBLE);
                free_coupons.setVisibility(View.INVISIBLE);
            }
            LinearLayout linearLayout= (LinearLayout) ratings.getParent();
            if (inStock){
                ratings.setVisibility(View.VISIBLE);
                total_ratings.setVisibility(View.VISIBLE);
                product_price.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cutted_price.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

                ratings.setText(average_rate);
                total_ratings.setText("("+total_ratingno+")ratings");
                product_price.setText("Rs "+price+"/-");
                cutted_price.setText("Rs "+cuttedpricevalue+"/-");
                if(cod==true){
                    payment_method.setVisibility(View.VISIBLE);
                }else {
                    payment_method.setVisibility(View.INVISIBLE);
                }
            }else {

                linearLayout.setVisibility(View.INVISIBLE);
                ratings.setVisibility(View.INVISIBLE);
                total_ratings.setVisibility(View.INVISIBLE);
                product_price.setText("OUT OF STOCK");
                product_price.setTextColor(itemView.getContext().getResources().getColor(R.color.error));
                cutted_price.setVisibility(View.INVISIBLE);
                payment_method.setVisibility(View.INVISIBLE);
            }






            if(wishlist){
                delete_Button.setVisibility(View.VISIBLE);
            }else{
                delete_Button.setVisibility(View.GONE);
            }
            delete_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!product_details_Activity.running_wishlist_querry) {
                        product_details_Activity.running_wishlist_querry = true;
                        db_querries.RemoveFrom_wishlist(index, itemView.getContext());
                        Toast.makeText(itemView.getContext(), "Product Deleted", Toast.LENGTH_LONG).show();
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (from_SEARCH){
                        product_details_Activity.fromSEARCH=true;
                    }
                    Intent product_details_intent=new Intent(itemView.getContext(),product_details_Activity.class);
                    product_details_intent.putExtra("product_id",product_id);
                    itemView.getContext().startActivity(product_details_intent);
                }
            });
        }
    }
}
