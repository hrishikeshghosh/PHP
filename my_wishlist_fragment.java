package com.example.ecommerce_03;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 */
public class my_wishlist_fragment extends Fragment {

    public my_wishlist_fragment() {
        // Required empty public constructor
    }
    private RecyclerView my_wishlist_recyclerview;
    private Dialog loading_dialog;

    public static   wishlist_adapter wishlist_adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_wishlist_fragment, container, false);

        /////loading dialog
        loading_dialog=new Dialog(getContext());
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loading_dialog.show();
        /////loading dialog

        my_wishlist_recyclerview=view.findViewById(R.id.my_wishlist_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        my_wishlist_recyclerview.setLayoutManager(layoutManager);

        if(db_querries.wish_list_model_list.size()==0){
            db_querries.wish_list.clear();
            db_querries.loadWishlist(getContext(),loading_dialog,true);
        }else{
            loading_dialog.dismiss();
        }


        wishlist_adapter=new wishlist_adapter(db_querries.wish_list_model_list,true);
        my_wishlist_recyclerview.setAdapter(wishlist_adapter);
        wishlist_adapter.notifyDataSetChanged();

        return view;

    }
}
