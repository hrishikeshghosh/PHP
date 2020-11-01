package com.example.ecommerce_03;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class view_all_activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GridView gridView;
    public static List<horizontal_scroll_product_model> horizontal_scroll_product_modelList;
    public static List<wishlist_model>wishlist_modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_activity);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recycler_view);
        gridView=findViewById(R.id.grid_view);

        int Layout_code=getIntent().getIntExtra("Layout_code",-1);


if(Layout_code==0) {
    recyclerView.setVisibility(View.VISIBLE);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(RecyclerView.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    wishlist_adapter adapter = new wishlist_adapter(wishlist_modelList, false);
    recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();

}else if(Layout_code==1) {
    gridView.setVisibility(View.VISIBLE);





    grid_product_layout_adapter grid_product_layout_adapter = new grid_product_layout_adapter(horizontal_scroll_product_modelList);
    gridView.setAdapter(grid_product_layout_adapter);

}
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }
}
/*horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_1, "Bemlmonte Wash Basins", "Wall hung", "5,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_2, "Cemlmonte Wash Basins", "Wall2 hung", "6,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_3, "Bemlmonte Wash Basins", "Wall3 hung", "7,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_4, "Bemlmonte Wash Basins", "Wall4 hung", "8,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.cart_main_ldpi, "Bemlmonte Wash Basins", "Wall5 hung", "9,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.account, "Bemlmonte Wash Basins", "Wall6 hung", "10,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.unlock, "Bemlmonte Wash Basins", "Wall7 hung", "11,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.myorders, "Bemlmonte Wash Basins", "Wall8 hung", "12,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_1, "Bemlmonte Wash Basins", "Wall hung", "5,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_2, "Cemlmonte Wash Basins", "Wall2 hung", "6,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_3, "Bemlmonte Wash Basins", "Wall3 hung", "7,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_4, "Bemlmonte Wash Basins", "Wall4 hung", "8,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.cart_main_ldpi, "Bemlmonte Wash Basins", "Wall5 hung", "9,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.account, "Bemlmonte Wash Basins", "Wall6 hung", "10,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.unlock, "Bemlmonte Wash Basins", "Wall7 hung", "11,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.myorders, "Bemlmonte Wash Basins", "Wall8 hung", "12,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_1, "Bemlmonte Wash Basins", "Wall hung", "5,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_2, "Cemlmonte Wash Basins", "Wall2 hung", "6,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_3, "Bemlmonte Wash Basins", "Wall3 hung", "7,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.washbasin_4, "Bemlmonte Wash Basins", "Wall4 hung", "8,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.cart_main_ldpi, "Bemlmonte Wash Basins", "Wall5 hung", "9,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.account, "Bemlmonte Wash Basins", "Wall6 hung", "10,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.unlock, "Bemlmonte Wash Basins", "Wall7 hung", "11,474.00 "));
    horizontal_scroll_product_modelList.add(new horizontal_scroll_product_model(R.drawable.myorders, "Bemlmonte Wash Basins", "Wall8 hung", "12,474.00 "));*/



   /* List<wishlist_model> wishlist_modelList = new ArrayList<>();
    wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_1, "belmonte washbasin1", 1, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_2, "belmonte washbasin2", 0, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_3, "belmonte washbasin3", 2, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_4, "belmonte washbasin4", 4, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_1, "belmonte washbasin1", 5, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_1, "belmonte washbasin1", 1, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_2, "belmonte washbasin2", 0, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_3, "belmonte washbasin3", 2, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_4, "belmonte washbasin4", 4, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_1, "belmonte washbasin1", 5, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_1, "belmonte washbasin1", 1, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_2, "belmonte washbasin2", 0, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_3, "belmonte washbasin3", 2, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_4, "belmonte washbasin4", 4, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));
            wishlist_modelList.add(new wishlist_model(R.drawable.washbasin_1, "belmonte washbasin1", 5, "3", 145, "Rs 4999/-", "Rs 5999/-", "Cash On Delivery"));*/
