package com.example.ecommerce_03;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class update_user_info_activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private Fragment update_info_fragment;
    private Fragment update_password_fragment;
    private String name;
    private String email;
    private String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info_activity);
        tabLayout = findViewById(R.id.tab_layout);
        frameLayout = findViewById(R.id.frame_layout);

        update_info_fragment = new update_info_fragment();
        update_password_fragment=new update_passowrd_fragment();

        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        photo = getIntent().getStringExtra("Photo");


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //selecting tabs by postion reference
                if (tab.getPosition() == 0) {
                    setFragment(update_info_fragment,true);
                }
                if (tab.getPosition() == 1) {
                    setFragment(update_password_fragment,false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(0).select();//selected a default tab at position 0
        setFragment(update_info_fragment,true);

    }

    private void setFragment(Fragment fragment, boolean setBundle) {
        // follow the below written code for setting a fragment on any layout and passing additional data to it(for future usage preferences)

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//setting fragment transaction class to replace the frame layout with the fragment created
        if (setBundle) {
            Bundle bundle = new Bundle();//creating a bundle class
            bundle.putString("Name", name);//putting the intent extras in the format of name and key values
            bundle.putString("Email", email);//putting the intent extras in the format of name and key values
            bundle.putString("Photo", photo);//putting the intent extras in the format of name and key values
            fragment.setArguments(bundle);//passing the intent extra values to the set fragment through bundle(this is the way to send additional/intent data to the transacted fragment)
        }else{
            Bundle bundle = new Bundle();
            bundle.putString("Email", email);
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(frameLayout.getId(), fragment);//replacing the frame layout by the fragemnt
        fragmentTransaction.commit();// calling the function to cmmit the task
    }
}