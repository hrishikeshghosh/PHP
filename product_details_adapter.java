package com.example.ecommerce_03;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class product_details_adapter extends FragmentPagerAdapter {
    private int totaltabs;
    private String product_description;
    private String product_other_detail;
    private List<product_specification_model>product_specification_modelList;

    public product_details_adapter(@NonNull FragmentManager fm, int totaltabs, String product_description, String product_other_detail, List<product_specification_model> product_specification_modelList) {
        super(fm);
        this.totaltabs = totaltabs;
        this.product_description = product_description;
        this.product_other_detail = product_other_detail;
        this.product_specification_modelList = product_specification_modelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                product_description_fragment product_description_fragment_1=new product_description_fragment();
                product_description_fragment_1.body=product_description;
                return  product_description_fragment_1;
            case 1:
                product_specification_fragment product_specification_fragment=new product_specification_fragment();
                product_specification_fragment.product_specification_modelList=product_specification_modelList;
                return product_specification_fragment;

            case 2:
                product_description_fragment product_description_fragment_2=new product_description_fragment();
                product_description_fragment_2.body=product_other_detail;
                return  product_description_fragment_2;

            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return totaltabs;
    }
}
