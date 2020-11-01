package com.example.ecommerce_03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class product_description_fragment extends Fragment {

    public product_description_fragment() {
        // Required empty public constructor
    }
    private TextView description_body;
    public String body;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_description_fragment, container, false);
        description_body=view.findViewById(R.id.tvproduct__description);
        description_body.setText(body);


        return view;
    }
}
