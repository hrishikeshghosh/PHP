package com.example.ecommerce_03;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class home_page_adapter extends RecyclerView.Adapter {

    private List<home_page_model> home_page_modelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int last_position = -1;

    public home_page_adapter(List<home_page_model> home_page_modelList) {
        this.home_page_modelList = home_page_modelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (home_page_modelList.get(position).getType()) {
            case 0:
                return home_page_model.BANNER_SLIDER;

            case 1:

                return home_page_model.STRIP_AD_BANNER;
            case 2:
                return home_page_model.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return home_page_model.GRID_PRODUCT_VIEW;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case home_page_model.BANNER_SLIDER:
                View bannerslider_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sliding_add_layout, viewGroup, false);
                return new bannersliderViewHolder(bannerslider_view);
            case home_page_model.STRIP_AD_BANNER:
                View stripadd_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_add_layout, viewGroup, false);
                return new stripaddbannerViewHolder(stripadd_view);
            case home_page_model.HORIZONTAL_PRODUCT_VIEW:
                View horizontalproduct_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout, viewGroup, false);
                return new horizontalproducViewHolder(horizontalproduct_view);
            case home_page_model.GRID_PRODUCT_VIEW:
                View gridproduct_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_layout, viewGroup, false);
                return new GridproductViewHolder(gridproduct_view);

            default:
                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (home_page_modelList.get(position).getType()) {
            case home_page_model.BANNER_SLIDER:
                List<slider_model> slider_modelList = home_page_modelList.get(position).getSlider_modelList();
                ((bannersliderViewHolder) viewHolder).setBannerslider_viewpager(slider_modelList);
                break;

            case home_page_model.STRIP_AD_BANNER:
                String resource = home_page_modelList.get(position).getResource();
                String color = home_page_modelList.get(position).getBackground_color();
                ((stripaddbannerViewHolder) viewHolder).setStripadd(resource, color);
                break;

            case home_page_model.HORIZONTAL_PRODUCT_VIEW:
                String layout_color = home_page_modelList.get(position).getBackground_color();
                String horizontal_layout_title = home_page_modelList.get(position).getTitle();
                List<wishlist_model> viewall_productList = home_page_modelList.get(position).getViewall_productList();
                List<horizontal_scroll_product_model> horizontal_scroll_product_modelList =
                        home_page_modelList.get(position).getHorizontalScrollProductModelList();
                ((horizontalproducViewHolder) viewHolder).sethorizontalproduct_layout(horizontal_scroll_product_modelList, horizontal_layout_title, layout_color, viewall_productList);
                break;
            case home_page_model.GRID_PRODUCT_VIEW:
                String Gridlayoutcolor = home_page_modelList.get(position).getBackground_color();
                String grid_layout_title = home_page_modelList.get(position).getTitle();
                List<horizontal_scroll_product_model> grid_scroll_product_modelList =
                        home_page_modelList.get(position).getHorizontalScrollProductModelList();
                ((GridproductViewHolder) viewHolder).setgridproductlayout(grid_scroll_product_modelList, grid_layout_title, Gridlayoutcolor);
                break;
            default:
                return;
        }
        if (last_position < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.face_in);
            viewHolder.itemView.setAnimation(animation);
            last_position = position;
        }
    }

    @Override
    public int getItemCount() {
        return home_page_modelList.size();
    }


    public class bannersliderViewHolder extends RecyclerView.ViewHolder {
        private ViewPager bannerslider_viewpager;
        private int currentpage;
        private Timer timer;
        final private long delaytime = 3000;
        final private long periodtime = 3000;
        private List<slider_model> arranged_list;

        public bannersliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerslider_viewpager = itemView.findViewById(R.id.BannerSlider_viewpager);


        }

        private void setBannerslider_viewpager(final List<slider_model> slider_modelList) {
            currentpage = 2;
            if (timer != null) {
                timer.cancel();
            }
            arranged_list = new ArrayList<>();

            for (int x = 0; x < slider_modelList.size(); x++) {
                arranged_list.add(x, slider_modelList.get(x));
            }
            arranged_list.add(0, slider_modelList.get(slider_modelList.size() - 2));
            arranged_list.add(1, slider_modelList.get(slider_modelList.size() - 1));

            arranged_list.add(slider_modelList.get(0));
            arranged_list.add(slider_modelList.get(1));

            slider_adapter slider_adapter = new slider_adapter(arranged_list);
            bannerslider_viewpager.setAdapter(slider_adapter);
            bannerslider_viewpager.setClipToPadding(false);
            bannerslider_viewpager.setPageMargin(20);
            bannerslider_viewpager.setCurrentItem(currentpage);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentpage = position;
                }

                @Override
                public void onPageScrollStateChanged(int position) {
                    if (position == ViewPager.SCROLL_STATE_IDLE) {
                        pagelooper(arranged_list);
                    }
                }
            };
            bannerslider_viewpager.addOnPageChangeListener(onPageChangeListener);
            startbannerslideshow(arranged_list);
            bannerslider_viewpager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pagelooper(arranged_list);
                    stopbannerslideshow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startbannerslideshow(arranged_list);
                    }
                    return false;
                }
            });


        }

        private void pagelooper(List<slider_model> slider_modelList) {
            if (currentpage == slider_modelList.size() - 2) {
                currentpage = 2;
                bannerslider_viewpager.setCurrentItem(currentpage, false);
            }
            if (currentpage == 1) {
                currentpage = slider_modelList.size() - 3;
                bannerslider_viewpager.setCurrentItem(currentpage, false);
            }
        }

        private void startbannerslideshow(final List<slider_model> slider_modelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentpage >= slider_modelList.size()) {
                        currentpage = 1;
                    }
                    bannerslider_viewpager.setCurrentItem(currentpage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, delaytime, periodtime);
        }

        private void stopbannerslideshow() {
            timer.cancel();
        }


    }

    public class stripaddbannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView stripaddimage;
        private ConstraintLayout stripadd_container;

        public stripaddbannerViewHolder(@NonNull View itemView) {
            super(itemView);
            stripaddimage = itemView.findViewById(R.id.strip_add_image);
            stripadd_container = itemView.findViewById(R.id.strip_add_container);
        }

        private void setStripadd(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.no_image2)).into(stripaddimage);
            stripaddimage.setBackgroundColor(Color.parseColor(color));

        }
    }

    public class horizontalproducViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView horizontal_layout_title;
        private Button horiontal_view_all_button;
        private RecyclerView horizontal_recycler_view;

        public horizontalproducViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            horizontal_layout_title = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horiontal_view_all_button = itemView.findViewById(R.id.horizintal_viewall_button);
            horizontal_recycler_view = itemView.findViewById(R.id.horizintal_scroll_layout_recycler_view);
            horizontal_recycler_view.setRecycledViewPool(recycledViewPool);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void sethorizontalproduct_layout(final List<horizontal_scroll_product_model> horizontal_scroll_product_modelList, final String title, String color, final List<wishlist_model> viewall_productList) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontal_layout_title.setText(title);

            for (final horizontal_scroll_product_model model : horizontal_scroll_product_modelList) {
                if (!model.getProduct_id().isEmpty() && model.getProduct_title().isEmpty()) {
                    firebaseFirestore.collection("PRODUCTS").document(model.getProduct_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                model.setProduct_title(task.getResult().getString("product_title"));
                                model.setProduct_image(task.getResult().getString("product_image1"));
                                model.setProduct_price(task.getResult().getString("product_price"));

                                wishlist_model wishlist_model = viewall_productList.get(horizontal_scroll_product_modelList.indexOf(model));
                                wishlist_model.setTotal_ratings(task.getResult().getLong("total_rating"));
                                wishlist_model.setRating(task.getResult().getString("average_rating"));
                                wishlist_model.setProduct_title(task.getResult().getString("product_title"));
                                wishlist_model.setProduct_price(task.getResult().getString("product_price"));
                                wishlist_model.setProduct_image(task.getResult().getString("product_image1"));
                                wishlist_model.setFree_Coupon(task.getResult().getLong("free_coupon"));
                                wishlist_model.setCutted_price(task.getResult().getString("cutted_price"));
                                wishlist_model.setCod(task.getResult().getBoolean("cod"));
                                wishlist_model.setInstock(task.getResult().getLong("stock_quantity") > 0);

                                if (horizontal_scroll_product_modelList.indexOf(model) == horizontal_scroll_product_modelList.size() - 1) {
                                    if (horizontal_recycler_view.getAdapter() != null) {
                                        horizontal_recycler_view.getAdapter().notifyDataSetChanged();
                                    }
                                }

                            } else {
                                ////nothing
                            }
                        }
                    });
                }
            }


            if (horizontal_scroll_product_modelList.size() > 8) {
                horiontal_view_all_button.setVisibility(View.VISIBLE);
                horiontal_view_all_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_all_activity.wishlist_modelList = viewall_productList;
                        Intent view_all_intent = new Intent(itemView.getContext(), view_all_activity.class);
                        view_all_intent.putExtra("Layout_code", 0);
                        view_all_intent.putExtra("title", title);
                        itemView.getContext().startActivity(view_all_intent);
                    }
                });
            } else {
                horiontal_view_all_button.setVisibility(View.INVISIBLE);
            }

            horizontal_scroll_product_adapter horizontal_scroll_productAdapter = new horizontal_scroll_product_adapter(horizontal_scroll_product_modelList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            horizontal_recycler_view.setLayoutManager(linearLayoutManager);

            horizontal_recycler_view.setAdapter(horizontal_scroll_productAdapter);
            horizontal_scroll_productAdapter.notifyDataSetChanged();

        }
    }

    public class GridproductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView gridLayouttitle;
        private Button gridLayoutviewallButton;
        private GridLayout gridproductLayout;

        public GridproductViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            gridLayouttitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutviewallButton = itemView.findViewById(R.id.grid_product_layout_viewall_button);
            gridproductLayout = itemView.findViewById(R.id.grid_layout);


        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setgridproductlayout(final List<horizontal_scroll_product_model> horizontal_scroll_product_modelList, final String title, String color) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayouttitle.setText(title);

            for (final horizontal_scroll_product_model model : horizontal_scroll_product_modelList) {
                if (!model.getProduct_id().isEmpty() && model.getProduct_title().isEmpty()) {
                    firebaseFirestore.collection("PRODUCTS").document(model.getProduct_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                model.setProduct_title(task.getResult().getString("product_title"));
                                model.setProduct_image(task.getResult().getString("product_image1"));
                                model.setProduct_price(task.getResult().getString("product_price"));


                                if (horizontal_scroll_product_modelList.indexOf(model) == horizontal_scroll_product_modelList.size() - 1) {

                                    setGRIDDATA(title, horizontal_scroll_product_modelList);

                                    if (!title.equals("")) {
                                        gridLayoutviewallButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                view_all_activity.horizontal_scroll_product_modelList = horizontal_scroll_product_modelList;
                                                Intent view_all_intent = new Intent(itemView.getContext(), view_all_activity.class);
                                                view_all_intent.putExtra("Layout_code", 1);
                                                view_all_intent.putExtra("title", title);
                                                itemView.getContext().startActivity(view_all_intent);

                                            }
                                        });
                                    }

                                }
                            } else {
                                ////nothing

                            }
                        }

                    });
                }
            }
            setGRIDDATA(title, horizontal_scroll_product_modelList);
        }

        private void setGRIDDATA(final String title, final List<horizontal_scroll_product_model> horizontal_scroll_product_modelList) {

            for (int x = 0; x < 4; x++) {
                ImageView productimage = gridproductLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_product_image);
                TextView producttitle = gridproductLayout.getChildAt(x).findViewById(R.id.horizintal_scroll_product_title);
                TextView productdescription = gridproductLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_product_description);
                TextView productprice = gridproductLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_product_price);

                Glide.with(itemView.getContext()).load(horizontal_scroll_product_modelList.get(x).getProduct_image()).apply(new RequestOptions().placeholder(R.drawable.no_image2)).into(productimage);
                producttitle.setText(horizontal_scroll_product_modelList.get(x).getProduct_title());
                productdescription.setText(horizontal_scroll_product_modelList.get(x).getProduct_description());
                productprice.setText("Rs " + horizontal_scroll_product_modelList.get(x).getProduct_price() + "/-");
                gridproductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#FFFFFF"));

                if (!title.equals("")) {
                    final int finalX = x;
                    gridproductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent product_details_intent = new Intent(itemView.getContext(), product_details_Activity.class);
                            product_details_intent.putExtra("product_id", horizontal_scroll_product_modelList.get(finalX).getProduct_id());
                            itemView.getContext().startActivity(product_details_intent);
                        }
                    });
                }
            }


           /* if (!title.equals("")) {
                gridLayoutviewallButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_all_activity.horizontal_scroll_product_modelList = horizontal_scroll_product_modelList;
                        Intent view_all_intent = new Intent(itemView.getContext(), view_all_activity.class);
                        view_all_intent.putExtra("Layout_code", 1);
                        view_all_intent.putExtra("title", title);
                        itemView.getContext().startActivity(view_all_intent);

                    }
                });
            }*/

        }
    }

}
