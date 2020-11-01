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
 * A simple {@link Fragment} subclass.
 */
public class my_orders extends Fragment {

    public my_orders() {
        // Required empty public constructor
    }
    private RecyclerView my_orders_recycler_view;
    public static  my_orders_adapter myOrdersAdapter;
    private Dialog loading_dialog;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_orders, container, false);

        /////loading dialog
        loading_dialog = new Dialog(getContext());
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading_dialog.show();
        /////loading dialog


        my_orders_recycler_view=view.findViewById(R.id.my_orders_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        my_orders_recycler_view.setLayoutManager(layoutManager);

        myOrdersAdapter=new my_orders_adapter(db_querries.my_orders_items_modelList,loading_dialog);
        my_orders_recycler_view.setAdapter(myOrdersAdapter);
        myOrdersAdapter.notifyDataSetChanged();


            db_querries.loadOrders(getContext(),myOrdersAdapter,loading_dialog);


        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myOrdersAdapter.notifyDataSetChanged();
    }
}
