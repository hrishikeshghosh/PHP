package com.example.ecommerce_03;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.ecommerce_03.db_querries.lists;
import static com.example.ecommerce_03.db_querries.loadFragmentData;
import static com.example.ecommerce_03.db_querries.loadedcategories_names;

public class category_Activity extends AppCompatActivity {
private RecyclerView category_recyclerView;
    private List<home_page_model> home_page_model_fake_List=new ArrayList<>();
private  home_page_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title= getIntent().getStringExtra("Category Name");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ////////home page fake list(will execute when the system is retrieving teh data from the servers)
        List<slider_model>slider_model_fake_list=new ArrayList<>();
        slider_model_fake_list.add(new slider_model("null","#FFFFFF"));
        slider_model_fake_list.add(new slider_model("null","#FFFFFF"));
        slider_model_fake_list.add(new slider_model("null","#FFFFFF"));
        slider_model_fake_list.add(new slider_model("null","#FFFFFF"));
        slider_model_fake_list.add(new slider_model("null","#FFFFFF"));

        List<horizontal_scroll_product_model>horizontal_scroll_product_fake_modelList=new ArrayList<>();
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));
        horizontal_scroll_product_fake_modelList.add(new horizontal_scroll_product_model("","","","",""));

        home_page_model_fake_List.add(new home_page_model(0,slider_model_fake_list));
        home_page_model_fake_List.add(new home_page_model(1,"","#FFFFFF"));
        home_page_model_fake_List.add(new home_page_model(2,"","#FFFFFF",horizontal_scroll_product_fake_modelList,new ArrayList<wishlist_model>()));
        home_page_model_fake_List.add(new home_page_model(3,"","#FFFFFF",horizontal_scroll_product_fake_modelList));
        ////////home page fake list(will execute when the system is retrieving teh data from the servers)

        category_recyclerView=findViewById(R.id.category_recycler_view);

        LinearLayoutManager testinglayoutmanager=new LinearLayoutManager(this);
        testinglayoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        category_recyclerView.setLayoutManager(testinglayoutmanager);
        adapter=new home_page_adapter(home_page_model_fake_List);


        int list_position=0;
       for(int x=0;x<loadedcategories_names.size();x++){
           if(loadedcategories_names.get(x).equals(title.toUpperCase())){
               list_position=x;
           }
       }

       if(list_position==0){
           loadedcategories_names.add(title.toUpperCase());
           lists.add(new ArrayList<home_page_model>());
           loadFragmentData(category_recyclerView,this,loadedcategories_names.size()-1,title);
       }else{
           adapter=new home_page_adapter(lists.get(list_position));
       }
        category_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            Intent search_intent=new Intent(this,search_activity.class);
            startActivity(search_intent);
            return true;
        }else if(id==android.R.id.home){
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
      /*home_page_modelList.add(new home_page_model(0,slider_modelList));
        home_page_modelList.add(new home_page_model(1,R.drawable.mybathroom,"#000000"));
        home_page_modelList.add(new home_page_model(2,"Deals of the day",horizontal_scroll_product_modelList));
        home_page_modelList.add(new home_page_model(3,"Deals of the day",horizontal_scroll_product_modelList));

        home_page_modelList.add(new home_page_model(2,"Deals of the day",horizontal_scroll_product_modelList));
        home_page_modelList.add(new home_page_model(1,R.drawable.account_main_hdpi,"#000000"));

        home_page_modelList.add(new home_page_model(1,R.drawable.addimage_ldpi,"#000000"));
        home_page_modelList.add(new home_page_model(3,"Deals of the day",horizontal_scroll_product_modelList));*/
               /*List<slider_model>slider_modelList=new ArrayList<slider_model>();

       slider_modelList.add(new slider_model(R.drawable.cart_main_ldpi,"#000"));
        slider_modelList.add(new slider_model(R.drawable.ic_menu_send,"#000"));
        slider_modelList.add(new slider_model(R.drawable.account_main_hdpi,"#000"));

        slider_modelList.add(new slider_model(R.drawable.addimage_ldpi,"#000"));
        slider_modelList.add(new slider_model(R.drawable.asddsa,"#000"));
        slider_modelList.add(new slider_model(R.drawable.common_google_signin_btn_icon_disabled,"#000"));

        slider_modelList.add(new slider_model(R.drawable.cart_main_ldpi,"#000"));
        slider_modelList.add(new slider_model(R.drawable.account_main_hdpi,"#000"));
        slider_modelList.add(new slider_model(R.drawable.addimage_ldpi,"#000"));*/
                       /*  List<horizontal_scroll_product_model> horizontal_scroll_product_modelList=new ArrayList<>();

       horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_1,"Bemlmonte Wash Basins","Wall hung","5,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_2,"Cemlmonte Wash Basins","Wall2 hung","6,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_3,"Bemlmonte Wash Basins","Wall3 hung","7,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_4,"Bemlmonte Wash Basins","Wall4 hung","8,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.cart_main_ldpi,"Bemlmonte Wash Basins","Wall5 hung","9,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.account,"Bemlmonte Wash Basins","Wall6 hung","10,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.unlock,"Bemlmonte Wash Basins","Wall7 hung","11,474.00 "));
        horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.myorders,"Bemlmonte Wash Basins","Wall8 hung","12,474.00 "));
        // horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_1,"Bemlmonte Wash Basins","Wall hung","5,474.00 "));*/


