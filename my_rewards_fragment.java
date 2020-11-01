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
public class my_rewards_fragment extends Fragment {

    public my_rewards_fragment() {
        // Required empty public constructor
    }

    private RecyclerView my_rewards_recycler_view;
    private Dialog loading_dialog;
    public  static my_rewards_adapter my_rewards_adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards_fragment, container, false);
        /////loading dialog
        loading_dialog = new Dialog(getContext());
        loading_dialog.setContentView(R.layout.loading_progress_dialog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loading_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading_dialog.show();
        /////loading dialog
        my_rewards_recycler_view = view.findViewById(R.id.my_rewards_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        my_rewards_recycler_view.setLayoutManager(layoutManager);

         my_rewards_adapter = new my_rewards_adapter(db_querries.rewards_modelList, false);
        my_rewards_recycler_view.setAdapter(my_rewards_adapter);

        if (db_querries.rewards_modelList.size() == 0) {
            db_querries.loadRewards(getContext(),loading_dialog,true);
        }else{
            loading_dialog.dismiss();
        }


        my_rewards_adapter.notifyDataSetChanged();
        return view;
    }
}
