package com.example.ecommerce_03;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class category_adapter extends RecyclerView.Adapter<category_adapter.ViewHolder> {
    private List<category_model> category_modelList;
    private int last_position = -1;

    public category_adapter(List<category_model> category_modelList) {
        this.category_modelList = category_modelList;
    }

    @NonNull
    @Override
    public category_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull category_adapter.ViewHolder viewHolder, int position) {
        String icon = category_modelList.get(position).getCategory_icon_link();
        String name = category_modelList.get(position).getCategory_name();
        viewHolder.setCategory(name, position);
        viewHolder.setcategoryicons(icon);

        if (last_position < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.face_in);
            viewHolder.itemView.setAnimation(animation);
            last_position = position;
        }
    }


        @Override
        public int getItemCount () {
            return category_modelList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView category_icon;
            private TextView category_name;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                category_icon = itemView.findViewById(R.id.category_icon);
                category_name = itemView.findViewById(R.id.category_name);

            }

            private void setcategoryicons(String icon_url) {
                if (!icon_url.equals("null")) {
                    //Glide.with(itemView.getContext()).load(icon_url).apply(new RequestOptions().placeholder(R.drawable.store_2)).into(category_icon);
                    Glide.with(itemView.getContext()).load(icon_url).apply(new RequestOptions()).placeholder(R.drawable.no_image2).into(category_icon);
                } else {
                    category_icon.setImageResource(R.drawable.app_store);
                }
            }

            private void setCategory(final String name, final int position) {
                category_name.setText(name);
                if (!name.equals("")) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (position != 0) {
                                Intent categoryintent = new Intent(itemView.getContext(), category_Activity.class);
                                categoryintent.putExtra("Category Name", name);
                                itemView.getContext().startActivity(categoryintent);
                            }
                        }
                    });

                }
            }

        }
    }

