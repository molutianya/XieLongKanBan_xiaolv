package topteam.com.xielong_xiaolvkanban;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import topteam.com.xielong_xiaolvkanban.adapter.FragmentAdapter;
import topteam.com.xielong_xiaolvkanban.fragment.AllJtXiaoLvFragment;
import topteam.com.xielong_xiaolvkanban.fragment.AllJtstateFragment;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tab;
    FragmentAdapter adapter;
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titlList = new ArrayList<>();
    Handler handler; //定时轮播
    static int cout = 0;  //当前要显示Fragment的id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }

        tab = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);
       // fragmentList.add(new AllJtXiaoLvFragment());
       fragmentList.add(new AllJtstateFragment());


       // titlList.add("机台效率汇总");
       titlList.add("机台状态汇总");
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList, titlList);
        viewPager.setAdapter(adapter);

        //tablayout与fragment的关联
        tab.setupWithViewPager(viewPager);
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab.setVisibility(View.GONE);


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (cout) {
                    case 0:
                        // viewPager.setCurrentItem(0);
                        //  cout = 1;
                        break;
                    case 1:
                     //   viewPager.setCurrentItem(1);
                     //   cout = 0;
                        break;
                    default:
                        break;
                }
                handler.postDelayed(this, 10000);
            }
        }, 0);



    }





}
