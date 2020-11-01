package com.example.ecommerce_03;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class search_activity extends AppCompatActivity {

    private SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);
        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.text_view);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        final List<wishlist_model> list = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        final Adapter adapter = new Adapter(list, false);
        adapter.setFrom_SEARCH(true);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                list.clear();
                ids.clear();

                final String[] tags = query.toLowerCase().split(" ");
                for (final String tag : tags) {
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("PRODUCTS").whereArrayContains("tags", tag).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                    wishlist_model model = new wishlist_model(

                                            documentSnapshot.getId()
                                            , documentSnapshot.get("product_image1").toString()
                                            , documentSnapshot.get("product_title").toString()
                                            , (long) documentSnapshot.get("free_coupon")
                                            , documentSnapshot.get("average_rating").toString()
                                            , (long) documentSnapshot.get("total_rating")
                                            , documentSnapshot.get("product_price").toString()
                                            , documentSnapshot.get("cutted_price").toString()
                                            , (boolean) documentSnapshot.get("cod"),
                                            true

                                    );
                                    model.setTags((ArrayList<String>) documentSnapshot.get("tags"));
                                    if (!ids.contains(model.getProduct_id())) {
                                        list.add(model);
                                        ids.add(model.getProduct_id());
                                    }


                                }
                                if (tag.equals(tags[tags.length - 1])) {
                                    if (list.size() == 0) {
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    } else {
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter.getFilter().filter(query);
                                    }

                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(search_activity.this, error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    class Adapter extends wishlist_adapter implements Filterable {

        private List<wishlist_model> original_list;

        public Adapter(List<wishlist_model> wishlist_modelList, Boolean wishlist) {
            super(wishlist_modelList, wishlist);
            original_list = wishlist_modelList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    /////////filter logic

                    FilterResults results = new FilterResults();
                     List<wishlist_model> filtered_list=new ArrayList<>();
                    final String[] tags = constraint.toString().toLowerCase().split(" ");

                    for (wishlist_model model : original_list) {
                        ArrayList<String> present_tags = new ArrayList<>();
                        for (String tag : tags) {
                            if (model.getTags().contains(tag)) {
                                present_tags.add(tag);
                            }
                        }
                        model.setTags(present_tags);
                    }
                    for (int i = tags.length; i > 0; i--) {
                        for (wishlist_model model:original_list){
                            if (model.getTags().size()==i){
                                filtered_list.add(model);
                            }
                        }
                    }
                    results.values=filtered_list;
                    results.count=filtered_list.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results.count>0){
                        setWishlist_modelList((List<wishlist_model>) results.values);
                    }
                    notifyDataSetChanged();
                }
            };
        }
    }
}