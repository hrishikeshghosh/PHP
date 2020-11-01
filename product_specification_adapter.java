package com.example.ecommerce_03;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.TypeVariable;
import java.util.List;

public class product_specification_adapter extends RecyclerView.Adapter<product_specification_adapter.ViewHolder> {
    private List<product_specification_model> productSpecificationModel_List;

    public product_specification_adapter(List<product_specification_model> productSpecificationModel_List) {
        this.productSpecificationModel_List = productSpecificationModel_List;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModel_List.get(position).getType()) {
            case 0:
                return product_specification_model.SPECIFICATION_TITLE;
            case 1:
                return product_specification_model.SPECIFICATION_BODY;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public product_specification_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case product_specification_model.SPECIFICATION_TITLE:
                TextView title = new TextView(viewGroup.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#030000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setdp(16, viewGroup.getContext()),
                                        setdp(16, viewGroup.getContext()),
                                        setdp(16, viewGroup.getContext()),
                                        setdp(8, viewGroup.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);

            case product_specification_model.SPECIFICATION_BODY:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_specification_item_layout, viewGroup, false);
                return new ViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull product_specification_adapter.ViewHolder viewHolder, int position) {
        switch (productSpecificationModel_List.get(position).getType()){
            case product_specification_model.SPECIFICATION_TITLE:
                viewHolder.setTitle(productSpecificationModel_List.get(position).getTitle());


break;
            case  product_specification_model.SPECIFICATION_BODY:

                String FEATURE_TITLE = productSpecificationModel_List.get(position).getFeature_name();
                String FEATURE_DETAILS = productSpecificationModel_List.get(position).getFeature_value();

                viewHolder.setfeatures(FEATURE_TITLE, FEATURE_DETAILS);
                break;

            default:
                return;
        }



    }

    @Override
    public int getItemCount() {
        return productSpecificationModel_List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView feature_name;
        private TextView feature_value;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        private void setTitle(String title_text){
            title= (TextView) itemView;
            title.setText(title_text);
        }

        private void setfeatures(String featuretitle, String feature_detail) {
            feature_name = itemView.findViewById(R.id.feature_name);
            feature_value = itemView.findViewById(R.id.feature_value);

            feature_name.setText(featuretitle);
            feature_value.setText(feature_detail);
        }
    }

    private Integer setdp(int dp, Context context) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
