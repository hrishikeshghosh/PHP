package com.example.ecommerce_03;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Registry_activity extends AppCompatActivity {
    private FrameLayout frameLayout;
    public static boolean resetpassworffragment=false;
    public static Boolean set_signup_fragment=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry_activity);
        frameLayout=(FrameLayout)findViewById(R.id.register_framelayout);
        if(set_signup_fragment) {
            set_signup_fragment=false;
            setdefaultfragment(new Signup());
        }else{
            setdefaultfragment(new Signin());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Signin.disable_close_button=false;
            Signup.disable_close_button=false;
            if(resetpassworffragment==true){
                setfragment(new Signin());
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setdefaultfragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void setfragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
