package com.example.ecommerce_03;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ecommerce_03.Registry_activity.set_signup_fragment;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showcart = false;
    public static Activity mainActivity;
    public static boolean reset_main_activity=false;
    //private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;
    private int current_fragment = -1;
    private ImageView actionbar_logo;
    private Toolbar toolbar;
    private Dialog signin_dialog;
    private TextView badgecount;

    public static DrawerLayout drawer;

    private Window window;
    private FirebaseUser current_user;
    private AppBarLayout.LayoutParams params;

    private int scroll_flags;

    private CircleImageView profile_view;
    private TextView fullname,email;
    private ImageView add_profile_icon;

    NavigationView navigationView;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = findViewById(R.id.toolbar);
        actionbar_logo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
         params= (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
         scroll_flags=params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        frameLayout = findViewById(R.id.main_framelayout);

        profile_view= navigationView.getHeaderView(0).findViewById(R.id.mail_profile_image);
        fullname=navigationView.getHeaderView(0).findViewById(R.id.main_fullname);
        email=navigationView.getHeaderView(0).findViewById(R.id.main_email);
        add_profile_icon=navigationView.getHeaderView(0).findViewById(R.id.add_profile_image);

        if (showcart) {
            mainActivity=this;
            drawer.setDrawerLockMode(1);//drawer.LOCK_MODE_LOCKED_CLOSED
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            gotofragment("My Cart", new my_cart_fragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            setFragment(new Home(), HOME_FRAGMENT);
        }
        signin_dialog = new Dialog(MainActivity2.this);
        signin_dialog.setContentView(R.layout.sign_in_dialog);
        signin_dialog.setCancelable(true);

        signin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dialog_sign_in_button = signin_dialog.findViewById(R.id.sign_in_button);
        Button dialog_sign_up_button = signin_dialog.findViewById(R.id.sign_up_button);
        final Intent register_intent = new Intent(MainActivity2.this, Registry_activity.class);

        dialog_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signin.disable_close_button = true;
                signin_dialog.dismiss();
                set_signup_fragment = false;
                startActivity(register_intent);
            }
        });
        dialog_sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup.disable_close_button = true;
                signin_dialog.dismiss();
                set_signup_fragment = true;
                startActivity(register_intent);

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        if (current_user == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {



            if (db_querries.email == null) {

            FirebaseFirestore.getInstance().collection("Users").document(current_user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        db_querries.fullname = task.getResult().getString("fullname");
                        db_querries.email = task.getResult().getString("email");
                        db_querries.profile = task.getResult().getString("profile");

                        fullname.setText(db_querries.fullname);
                        email.setText(db_querries.email);
                        if (db_querries.profile.equals("")) {
                            add_profile_icon.setVisibility(View.VISIBLE);
                        } else {
                            add_profile_icon.setVisibility(View.INVISIBLE);
                            Glide.with(MainActivity2.this).load(db_querries.profile).apply(new RequestOptions().placeholder(R.drawable.account_main_hdpi)).into(profile_view);
                        }


                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity2.this, error, Toast.LENGTH_LONG).show();

                    }

                }
            });
        }else{
                fullname.setText(db_querries.fullname);
                email.setText(db_querries.email);
                if (db_querries.profile.equals("")) {
                    profile_view.setImageResource(R.drawable.account_main_hdpi);
                    add_profile_icon.setVisibility(View.VISIBLE);
                } else {
                    add_profile_icon.setVisibility(View.INVISIBLE);
                    Glide.with(MainActivity2.this).load(db_querries.profile).apply(new RequestOptions().placeholder(R.drawable.account_main_hdpi)).into(profile_view);
                }


            }

            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
        if (reset_main_activity){
            actionbar_logo.setVisibility(View.VISIBLE);
            reset_main_activity=false;
            setFragment(new Home(), HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);

        }
        invalidateOptionsMenu();


    }

    @Override
    protected void onPause() {
        super.onPause();
        db_querries.checkNotifications(true,null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (current_fragment == HOME_FRAGMENT) {
                current_fragment = -1;
                super.onBackPressed();
            } else {
                if (showcart) {
                    mainActivity=null;
                    showcart = false;
                    finish();

                } else {
                    actionbar_logo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new Home(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (current_fragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main_activity2, menu);

            MenuItem cart_item = menu.findItem(R.id.main_cart_icon);
                cart_item.setActionView(R.layout.badge_layout);
                ImageView badgeIcon = cart_item.getActionView().findViewById(R.id.badge_icon);
                badgeIcon.setImageResource(R.drawable.cart_try2);
                 badgecount = cart_item.getActionView().findViewById(R.id.badge_count);
                 if(current_user!=null){
                     if (db_querries.cart_list.size() == 0) {
                         db_querries.loadCartList(MainActivity2.this, new Dialog(MainActivity2.this), false,badgecount,new TextView(MainActivity2.this));
                     }else{
                         badgecount.setVisibility(View.VISIBLE);
                         if (db_querries.cart_list.size() < 99){
                             badgecount.setText(String.valueOf(db_querries.cart_list.size()));
                         }else {
                             badgecount.setText("99");
                         }
                     }
                 }


                cart_item.getActionView().setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {

                        if (current_user == null) {
                            signin_dialog.show();
                        } else {
                            gotofragment("My Cart", new my_cart_fragment(), CART_FRAGMENT);
                        }
                    }
                });

            MenuItem notify_item = menu.findItem(R.id.main_notification_icon);
            notify_item.setActionView(R.layout.badge_layout2);
            ImageView notifyIcon = notify_item.getActionView().findViewById(R.id.notify_icon);
            notifyIcon.setImageResource(R.drawable.notification_try2);
             TextView notify_count = notify_item.getActionView().findViewById(R.id.notify_count);
            if(current_user!=null){
                db_querries.checkNotifications(false,notify_count);
            }
            notify_item.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current_user == null) {
                        signin_dialog.show();
                    } else {
                        Intent notification_intent = new Intent(MainActivity2.this, notification_activity.class);
                        startActivity(notification_intent);
                    }
                }
            });
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            Intent search_intent=new Intent(this,search_activity.class);
            startActivity(search_intent);
            return true;
        } else if (id == R.id.main_notification_icon) {
            Intent notification_intent=new Intent(this,notification_activity.class);
            startActivity(notification_intent);
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (current_user == null) {
                signin_dialog.show();
            } else {
                gotofragment("My Cart", new my_cart_fragment(), CART_FRAGMENT);
            }
            return true;
        } else if (id == android.R.id.home) {
            if (showcart) {
                mainActivity=null;
                showcart = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gotofragment(String title, Fragment fragment, int fragmentno) {
        actionbar_logo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment, fragmentno);
        if (fragmentno == CART_FRAGMENT || showcart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else{
            params.setScrollFlags(scroll_flags);
        }
    }
 MenuItem menuItem;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem=item;
        if (current_user != null) {
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_mystore) {
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        actionbar_logo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new Home(), HOME_FRAGMENT);

                    } else if (id == R.id.nav_myorders) {
                        gotofragment("My Orders", new my_orders(), ORDERS_FRAGMENT);

                    } else if (id == R.id.nav_myrewards) {

                        gotofragment("My Rewards", new my_rewards_fragment(), REWARDS_FRAGMENT);

                    } else if (id == R.id.nav_mycart) {

                        gotofragment("My Cart", new my_cart_fragment(), CART_FRAGMENT);

                    } else if (id == R.id.nav_mywishlist) {
                        gotofragment("My Wishlist", new my_wishlist_fragment(), WISHLIST_FRAGMENT);

                    } else if (id == R.id.nav_myaccount) {
                        gotofragment("My Account", new my_account_fragment(), ACCOUNT_FRAGMENT);

                    } else if (id == R.id.nav_signout) {
                        FirebaseAuth.getInstance().signOut();
                        db_querries.clearData();
                        Intent register_intent = new Intent(MainActivity2.this, Registry_activity.class);
                        startActivity(register_intent);
                        finish();
                    }
                    drawer.removeDrawerListener(this);
                }
            });

            return true;
        } else {

            signin_dialog.show();
            return false;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setFragment(Fragment fragment, int fragmentno) {
        if (fragmentno != current_fragment) {
            if (fragmentno == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#3BC7C7"));
                toolbar.setBackgroundColor(Color.parseColor("#3BC7C7"));

            } else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            current_fragment = fragmentno;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.face_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }


}
    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/


       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
// Passing each menu ID as a set of Ids because each
// menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();*/
      /*  NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
   /* @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/