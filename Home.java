package com.example.ecommerce_03;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.example.ecommerce_03.db_querries.categorymodellist;
import static com.example.ecommerce_03.db_querries.lists;
import static com.example.ecommerce_03.db_querries.loadFragmentData;
import static com.example.ecommerce_03.db_querries.load_categories;
import static com.example.ecommerce_03.db_querries.loadedcategories_names;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {



    public Home() {
        // Required empty public constructor
    }
    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryrecyclerview;
    private List<category_model>categoryModel_fake_list=new ArrayList<>();
    private category_adapter categoryadapter;
    private RecyclerView home_page_recycler_view;
    private List<home_page_model>home_page_model_fake_List=new ArrayList<>();
    private ImageView no_internet_connection;
    private ImageButton retry_button;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    private home_page_adapter adapter;
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home2, container, false);
        no_internet_connection=view.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);

        categoryrecyclerview=view.findViewById(R.id.category_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryrecyclerview.setLayoutManager(layoutManager);
        home_page_recycler_view=view.findViewById(R.id.home_page_RecyclerView);
        LinearLayoutManager testinglayoutmanager=new LinearLayoutManager(getContext());
        testinglayoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        home_page_recycler_view.setLayoutManager(testinglayoutmanager);

        retry_button=view.findViewById(R.id.retry_button);
        ////////category fake list(will execute when the system is retrieving teh data from the servers)
        categoryModel_fake_list.add(new category_model("null",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        categoryModel_fake_list.add(new category_model("",""));
        ////////category fake list(will execute when the system is retrieving teh data from the servers)

        ////////home page fake list(will execute when the system is retrieving teh data from the servers)
        List<slider_model>slider_model_fake_list=new ArrayList<>();
        slider_model_fake_list.add(new slider_model("null","#DFDFDF"));
        slider_model_fake_list.add(new slider_model("null","#DFDFDF"));
        slider_model_fake_list.add(new slider_model("null","#DFDFDF"));
        slider_model_fake_list.add(new slider_model("null","#DFDFDF"));
        slider_model_fake_list.add(new slider_model("null","#DFDFDF"));

        List<horizontal_scroll_product_model>horizontal_scroll_product_fake_modelList=new ArrayList<>();
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));

        home_page_model_fake_List.add(new home_page_model(0,slider_model_fake_list));
        home_page_model_fake_List.add(new home_page_model(1,"","#DFDFDF"));
        home_page_model_fake_List.add(new home_page_model(2,"","#DFDFDF",horizontal_scroll_product_fake_modelList,new ArrayList<wishlist_model>()));
        home_page_model_fake_List.add(new home_page_model(3,"","#DFDFDF",horizontal_scroll_product_fake_modelList));
        ////////home page fake list(will execute when the system is retrieving teh data from the servers)


        categoryadapter=new category_adapter(categoryModel_fake_list);


        adapter=new home_page_adapter(home_page_model_fake_List);



        connectivityManager= (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();


        if(networkInfo!=null && networkInfo.isConnected()==true){
            MainActivity2.drawer.setDrawerLockMode(0);
            no_internet_connection.setVisibility(View.GONE);
            retry_button.setVisibility(View.GONE);
            categoryrecyclerview.setVisibility(View.VISIBLE);
            home_page_recycler_view.setVisibility(View.VISIBLE);
            if(categorymodellist.size()==0){
                load_categories(categoryrecyclerview,getContext());
            }else{
                categoryadapter=new category_adapter(categorymodellist);
                categoryadapter.notifyDataSetChanged();
            }
            categoryrecyclerview.setAdapter(categoryadapter);


            if(lists.size()==0){
              loadedcategories_names.add("STORE");
                lists.add(new ArrayList<home_page_model>());
                loadFragmentData(home_page_recycler_view,getContext(),0,"STORE");
            }else{
                adapter=new home_page_adapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            home_page_recycler_view.setAdapter(adapter);

        }else{
            MainActivity2.drawer.setDrawerLockMode(1);
            categoryrecyclerview.setVisibility(View.GONE);
            home_page_recycler_view.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.no_onternet_connection).into(no_internet_connection);
            no_internet_connection.setVisibility(View.VISIBLE);
            retry_button.setVisibility(View.VISIBLE);



        }

        //////// refresh layout code:

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reload_page();
            }
        });

        //////// refresh layout code:
        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload_page();
            }
        });
        return view;
    }
    @SuppressLint("WrongConstant")
    private void reload_page(){
        networkInfo=connectivityManager.getActiveNetworkInfo();
        db_querries.clearData();
       /* categorymodellist.clear();
        lists.clear();
        loadedcategories_names.clear();*/
        if(networkInfo!=null && networkInfo.isConnected()==true){
            MainActivity2.drawer.setDrawerLockMode(0);
            no_internet_connection.setVisibility(View.GONE);
            retry_button.setVisibility(View.GONE);
            categoryrecyclerview.setVisibility(View.VISIBLE);
            home_page_recycler_view.setVisibility(View.VISIBLE);
            categoryadapter=new category_adapter(categoryModel_fake_list);
            adapter=new home_page_adapter(home_page_model_fake_List);
            categoryrecyclerview.setAdapter(categoryadapter);
            home_page_recycler_view.setAdapter(adapter);


            load_categories(categoryrecyclerview,getContext());


            loadedcategories_names.add("STORE");
            lists.add(new ArrayList<home_page_model>());
            loadFragmentData(home_page_recycler_view,getContext(),0,"STORE");

        }else{
            MainActivity2.drawer.setDrawerLockMode(1);
            Toast.makeText(getContext(),"No Internet Connection Found",Toast.LENGTH_SHORT).show();
            categoryrecyclerview.setVisibility(View.GONE);
            home_page_recycler_view.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.no_onternet_connection).into(no_internet_connection);
            retry_button.setVisibility(View.VISIBLE);
            no_internet_connection.setVisibility(View.VISIBLE);

            swipeRefreshLayout.setRefreshing(false);
        }

    }
}
        /*categorymodellist.add(new category_model("trial 1","Home"));
        categorymodellist.add(new category_model("trail 2","Bathroom Fittings"));
        categorymodellist.add(new category_model("trail 3","Plumbing tools"));
        categorymodellist.add(new category_model("trail 4","not mentioned"));
        categorymodellist.add(new category_model("trail 5","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));
        categorymodellist.add(new category_model("trail 6","not mentioned"));*/
//final List<slider_model>slider_modelList=new ArrayList<slider_model>();
 /*slider_modelList.add(new slider_model(R.drawable.account_main_hdpi,"#000"));
        slider_modelList.add(new slider_model(R.drawable.addimage_ldpi,"#000"));
        slider_modelList.add(new slider_model(R.drawable.asddsa,"#000"));
        slider_modelList.add(new slider_model(R.drawable.common_google_signin_btn_icon_disabled,"#000"));
        slider_modelList.add(new slider_model(R.drawable.cart_main_ldpi,"#000"));*/
     /* List<horizontal_scroll_product_model> horizontal_scroll_product_modelList=new ArrayList<>();
      horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_1,"Bemlmonte Wash Basins","Wall hung","5,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_2,"Cemlmonte Wash Basins","Wall2 hung","6,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_3,"Bemlmonte Wash Basins","Wall3 hung","7,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_4,"Bemlmonte Wash Basins","Wall4 hung","8,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.cart_main_ldpi,"Bemlmonte Wash Basins","Wall5 hung","9,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.account,"Bemlmonte Wash Basins","Wall6 hung","10,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.unlock,"Bemlmonte Wash Basins","Wall7 hung","11,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.myorders,"Bemlmonte Wash Basins","Wall8 hung","12,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_1,"Bemlmonte Wash Basins","Wall hung","5,474.00 "));*/
  /*home_page_modelList.add(new home_page_model(0,slider_modelList));
        home_page_modelList.add(new home_page_model(1,R.drawable.mybathroom,"#000000"));
        home_page_modelList.add(new home_page_model(2,"Deals of the day",horizontal_scroll_product_modelList));
        home_page_modelList.add(new home_page_model(3,"Deals of the day",horizontal_scroll_product_modelList));
        home_page_modelList.add(new home_page_model(0,slider_modelList));
        home_page_modelList.add(new home_page_model(2,"Deals of the day",horizontal_scroll_product_modelList));
        home_page_modelList.add(new home_page_model(1,R.drawable.account_main_hdpi,"#000000"));
        home_page_modelList.add(new home_page_model(0,slider_modelList));
        home_page_modelList.add(new home_page_model(1,R.drawable.addimage_ldpi,"#000000"));
        home_page_modelList.add(new home_page_model(3,"Deals of the day",horizontal_scroll_product_modelList));
        home_page_modelList.add(new home_page_model(0,slider_modelList));
        home_page_modelList.add(new home_page_model(0,slider_modelList));*/

//categorymodellist=new ArrayList<category_model>();