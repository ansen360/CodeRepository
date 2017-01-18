package com.tomorrow_p.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tomorrow_p.R;
import com.tomorrow_p.fragment.BackgroundApps;
import com.tomorrow_p.fragment.BootAutostartApps;

/**
 * Created by Ansen on 2017/1/18 15:40.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/1031307403/
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.activity
 * @Description: TODO
 */
public class TabLayoutActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private String[] mTabTitles = new String[]
            {"Muli Selected", "Limit 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);
        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return new BackgroundApps();
                    case 1:
                        return new BootAutostartApps();
                }
                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return mTabTitles[position];
            }

            @Override
            public int getCount() {
                return mTabTitles.length;
            }
        });


        mTabLayout.setupWithViewPager(mViewPager);
    }
}
