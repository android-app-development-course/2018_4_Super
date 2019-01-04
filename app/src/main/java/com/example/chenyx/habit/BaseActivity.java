package com.example.chenyx.habit;
import android.app.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenyx on 18-12-17.
 */

public class BaseActivity extends AppCompatActivity {
    public GlobalMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu = new GlobalMenu(this);
//        menu.setMenuItemClickListener(new GlobalMenu.MenuItemClickListener() {
//            @Override
//            public void menuOneItemClick() {
//                //startActivity(new Intent(BaseActivity.this, FullscreenActivity.class));
//            }
//
//            @Override
//            public void menuTwoItemClick() {
////                if (current_act.getClass() == LoginActivity.class) {
////                    Toast.makeText(BaseActivity.this, "我已经在登录页面了", Toast.LENGTH_LONG).show();
////                } else {
////                    startActivity(new Intent(BaseActivity.this, LoginActivity.class));
////                }
//
//            }
//
//            @Override
//            public void menuThreeItemClick() {
//               // startActivity(new Intent(BaseActivity.this, Main2Activity.class));
//            }
//        });
        menu.dismiss();


    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ((ViewGroup) getWindow().getDecorView()).addView(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (menu.isShowing()) {
            menu.dismiss();
        }
    }
}
