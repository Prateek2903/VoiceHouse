package com.example.x;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


public class new_fragment extends FragmentActivity implements ActionBar.TabListener {
    private ViewPager mPager;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agree_disagree_with_animation);
        PagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new DisagreeFragment();
                    case 1:
                        return new DisagreeFragment();
                }
                return null;
            }
            @Override
            public int getCount() {
                return 3;
            }
        };
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
        });
        mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int position = 0; position < adapter.getCount(); position++) {
            getActionBar().addTab(getActionBar().newTab()
                    .setText(adapter.getPageTitle(position))
                    .setTabListener(this));
        }
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}