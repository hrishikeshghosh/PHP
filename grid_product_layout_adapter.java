package com.example.ecommerce_03;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class grid_product_layout_adapter extends BaseAdapter {

    List<horizontal_scroll_product_model>horizontalScrollProductModelList;

    public grid_product_layout_adapter(List<horizontal_scroll_product_model> horizontalScrollProductModelList) {
        this.horizontalScrollProductModelList = horizontalScrollProductModelList;
    }

    public int getCount() {
        return horizontalScrollProductModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
       View view;
       if(convertView==null){
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizintal_scroll_item_layout,null);

               view.setElevation(0);

           view.setBackgroundColor(Color.parseColor("#ffffff"));
           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent product_details_intent=new Intent(parent.getContext(),product_details_Activity.class);
                   product_details_intent.putExtra("product_id",horizontalScrollProductModelList.get(position).getProduct_id());
                   parent.getContext().startActivity(product_details_intent);
               }
           });



           ImageView productimage=view.findViewById(R.id.horizontal_scroll_product_image);
           TextView producttitle=view.findViewById(R.id.horizintal_scroll_product_title);
           TextView productdescription=view.findViewById(R.id.horizontal_scroll_product_description);
           TextView productprice=view.findViewById(R.id.horizontal_scroll_product_price);

           Glide.with(parent.getContext()).load(horizontalScrollProductModelList.get(position)
                   .getProduct_image()).apply(new RequestOptions().placeholder(R.drawable.sync_small)).into(productimage);
           producttitle.setText(horizontalScrollProductModelList.get(position).getProduct_title());
           productdescription.setText(horizontalScrollProductModelList.get(position).getProduct_description());
           productprice.setText("Rs "+horizontalScrollProductModelList.get(position).getProduct_price()+"/-");
       }else{
           view=convertView;

       }
       return  view;
    }
}
