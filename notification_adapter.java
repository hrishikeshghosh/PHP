package com.example.ecommerce_03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

class notification_adapter extends RecyclerView.Adapter<notification_adapter.ViewHolder>{

    private List<notification_model> notification_modelList;

    public notification_adapter(List<notification_model> notification_modelList) {
        this.notification_modelList = notification_modelList;
    }

    @NonNull
    @Override
    public notification_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item,viewGroup,false);
        return  new notification_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notification_adapter.ViewHolder viewHolder, int position) {

        String image=notification_modelList.get(position).getImage();
        String body=notification_modelList.get(position).getBody();
        boolean readed=notification_modelList.get(position).isReaded();

        viewHolder.setData(image,body,readed);


    }

    @Override
    public int getItemCount() {
        return notification_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.text_view);

        }
        public void setData(String image, String body,boolean readed){
            Glide.with(itemView.getContext()).load(image).into(imageView);
            if (readed){
                textView.setAlpha(0.5f);
                imageView.setAlpha(0.5f);
            }else{
                textView.setAlpha(1f);
                imageView.setAlpha(1f);
            }
            textView.setText(body);
        }
    }
}