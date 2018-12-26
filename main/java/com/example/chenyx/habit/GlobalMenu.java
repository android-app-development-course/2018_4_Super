package com.example.chenyx.habit;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GlobalMenu extends FrameLayout implements View.OnClickListener {
        private FrameLayout rl_parent;
        private Button btn_start, btn_one, btn_two, btn_three;
        private Context mContext;
        private MenuChangeListener mChangeListener;
        private MenuItemClickListener menuItemClickListener;
        private List<Button> btns = new ArrayList<Button>();
        private boolean isShowing = false;
        private FrameLayout outLineView;

    public void setChangeListener(MenuChangeListener mChangeListener) {
        this.mChangeListener = mChangeListener;
    }

    public void setMenuItemClickListener(MenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
    }


    public boolean isShowing() {
        return isShowing;
    }


    public GlobalMenu(Context context) {
        super(context);
        initView(context);
    }


    public GlobalMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public GlobalMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.globalmenu, this);
        btn_start = (Button) findViewById(R.id.btn_main);
        rl_parent = (FrameLayout) findViewById(R.id.menu_parent);


        btn_one = (Button) findViewById(R.id.btn_one);
        btn_two = (Button) findViewById(R.id.btn_two);
        btn_three = (Button) findViewById(R.id.btn_three);
        outLineView = (FrameLayout) findViewById(R.id.menu_parent);


        btns.add(btn_one);
        btns.add(btn_two);
        btns.add(btn_three);

        for (int i = 0; i < btns.size(); i++) {
            btns.get(i).setOnClickListener(this);
            // btns.get(i).setVisibility(INVISIBLE);
        }

        //空白处点击
        outLineView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing){
                    if (mChangeListener != null) {
                        mChangeListener.dismiss();//添加菜单隐藏监听
                    }
                    dismiss();
                    rl_parent.setFocusable(false);
                    rl_parent.setClickable(false);

                    rl_parent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.menuDismiss));
                }
            }
        });

        btn_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowing) {
                    if (mChangeListener != null) {
                        mChangeListener.show();//添加菜单显示监听
                    }
                    show();

                } else {
                    if (mChangeListener != null) {
                        mChangeListener.dismiss();//添加菜单隐藏监听
                    }
                    dismiss();
                }
                Log.e("GlobalMenu", isShowing + "");
            }
        });


    }

    public void show() {
        btn_one.setVisibility(View.VISIBLE);
        btn_two.setVisibility(View.VISIBLE);
        btn_three.setVisibility(View.VISIBLE);
        //for循环来开始小图标的出现动画
        for (int i = 0; i < btns.size(); i++) {
            AnimatorSet set = new AnimatorSet();
            //标题1与x轴负方向角度为20°，标题2为100°，转换为弧度
            double a = -Math.cos(20 * Math.PI / 180 * (i * 2 + 1));
            double b = -Math.sin(20 * Math.PI / 180 * (i * 2 + 1));
            double x = a * dip2px(120);
            double y = b * dip2px(120);

            set.playTogether(
                    ObjectAnimator.ofFloat(btns.get(i), "translationX", (float) (x * 0.25), (float) x),
                    ObjectAnimator.ofFloat(btns.get(i), "translationY", (float) (y * 0.25), (float) y)
                    , ObjectAnimator.ofFloat(btns.get(i), "alpha", 0, 1).setDuration(300)
            );
            //set.setInterpolator(new BounceInterpolator());
            set.setDuration(300).setStartDelay(100);
            set.start();

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    //菜单状态置打开
                    isShowing = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        //转动加号大图标本身45°
        ObjectAnimator rotate = ObjectAnimator.ofFloat(btn_start, "rotation", 0, 90).setDuration(300);
        //rotate.setInterpolator(new BounceInterpolator());
        rotate.start();
        rl_parent.setFocusable(true);
        rl_parent.setClickable(true);
        rl_parent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.menuShow));
    }

    public void dismiss() {
        //for循环来开始小图标的出现动画
        for (int i = 0; i < btns.size(); i++) {
            AnimatorSet set = new AnimatorSet();
            double a = -Math.cos(20 * Math.PI / 180 * (i * 2 + 1));
            double b = -Math.sin(20 * Math.PI / 180 * (i * 2 + 1));
            double x = a * dip2px(120);
            double y = b * dip2px(120);

            set.playTogether(
                    ObjectAnimator.ofFloat(btns.get(i), "translationX", (float) x, (float) (x * 0.25)),
                    ObjectAnimator.ofFloat(btns.get(i), "translationY", (float) y, (float) (y * 0.25)),
                    ObjectAnimator.ofFloat(btns.get(i), "alpha", 1, 0).setDuration(300)
            );
            //      set.setInterpolator(new AccelerateInterpolator());
            set.setDuration(300);
            set.start();

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    btn_one.setVisibility(View.GONE);
                    btn_two.setVisibility(View.GONE);
                    btn_three.setVisibility(View.GONE);


                    //菜单状态置关闭
                    isShowing= false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }


        //转动加号大图标本身45°
        ObjectAnimator rotate = ObjectAnimator.ofFloat(btn_start, "rotation", 0, 90).setDuration(300);
        //rotate.setInterpolator(new BounceInterpolator());
        rotate.start();
        rl_parent.setFocusable(false);
        rl_parent.setClickable(false);

        rl_parent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.menuDismiss));
    }

    private void showSelctor(int id) {//如果需要实现item点击选中的效果，请在drawable中配置相应的selector文件，然后在下方的点击事件中调用该方法
        for (int i = 0; i < btns.size(); i++) {
            if (id == btns.get(i).getId()) {
                btns.get(i).setSelected(true);
            } else {
                btns.get(i).setSelected(false);
            }
        }
    }

    private int dip2px(int value) {
        float density = getResources()
                .getDisplayMetrics().density;
        return (int) (density * value + 0.5f);
    }

    @Override
    public void onClick(View v) {

//        switch (v.getId()) {
//            case R.id.btn_one:
//                menuItemClickListener.menuOneItemClick();
//                break;
//            case R.id.btn_two:
//                menuItemClickListener.menuTwoItemClick();
//                break;
//            case R.id.btn_three:
//                menuItemClickListener.menuThreeItemClick();
//                break;
//        }

    }


    public interface MenuChangeListener {
        public void show();

        public void dismiss();
    }


    public interface MenuItemClickListener {
        void menuOneItemClick();

        void menuTwoItemClick();

        void menuThreeItemClick();

    }

}
