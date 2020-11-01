package com.example.ecommerce_03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class product_specification_fragment extends Fragment {


    public product_specification_fragment() {
        // Required empty public constructor
    }
    private RecyclerView product_specification_recyclerView;
    public List<product_specification_model>product_specification_modelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_specification_fragment, container, false);

        product_specification_recyclerView=view.findViewById(R.id.product_specification__recyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        product_specification_recyclerView.setLayoutManager(linearLayoutManager);







     /* product_specification_modelList.add(new product_specification_model(0,"General Details"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));
        product_specification_modelList.add(new product_specification_model(0,"Contents"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));
        product_specification_modelList.add(new product_specification_model(1,"Dimension","18 Inch X 14 Inch Or 46cm X 36cm"));*/


        product_specification_adapter productSpecificationAdapter=new product_specification_adapter(product_specification_modelList);
        product_specification_recyclerView.setAdapter(productSpecificationAdapter);
        productSpecificationAdapter.notifyDataSetChanged();
        return view;
    }
}
