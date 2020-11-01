package com.example.ecommerce_03;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class horizontal_scroll_product_adapter extends RecyclerView.Adapter<horizontal_scroll_product_adapter.ViewHolder> {

    private List<horizontal_scroll_product_model> horizontalmodellist;

    public horizontal_scroll_product_adapter(List<horizontal_scroll_product_model> horizontalmodellist) {
        this.horizontalmodellist = horizontalmodellist;
    }

    @NonNull
    @Override
    public horizontal_scroll_product_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizintal_scroll_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull horizontal_scroll_product_adapter.ViewHolder viewHolder, int position) {
        String resource = horizontalmodellist.get(position).getProduct_image();
        String title = horizontalmodellist.get(position).getProduct_title();
        String description = horizontalmodellist.get(position).getProduct_description();
        String price = horizontalmodellist.get(position).getProduct_price();
        String productid=horizontalmodellist.get(position).getProduct_id();

        viewHolder.setData(productid,resource,title,description,price);

    }

    @Override
    public int getItemCount() {
        if (horizontalmodellist.size() > 8) {
            return 8;
        } else {
            return horizontalmodellist.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView product_title;
        private TextView product_description;
        private TextView product_price;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            product_image = (ImageView) itemView.findViewById(R.id.horizontal_scroll_product_image);
            product_title = (TextView) itemView.findViewById(R.id.horizintal_scroll_product_title);
            product_description = (TextView) itemView.findViewById(R.id.horizontal_scroll_product_description);
            product_price = (TextView) itemView.findViewById(R.id.horizontal_scroll_product_price);


        }

        private void setData(final String productid, String resource, String title, String description, String price) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.no_image2)).into(product_image);
            product_title.setText(title);
            product_description.setText(description);
            product_price.setText("Rs " + price + "/-");

            if (!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productdetailsIntent = new Intent(itemView.getContext(), product_details_Activity.class);
                        productdetailsIntent.putExtra("product_id",productid);
                        itemView.getContext().startActivity(productdetailsIntent);
                    }
                });
            }
        }
    }
}