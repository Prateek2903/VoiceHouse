package com.example.x;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnDragListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressWarnings({ "deprecation", "deprecation" })
public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	ActionBar actionbar;
	ViewPager pager;
	FragmentManager manager;
	fragments frgmnt;
	int flag = 0;
	int count = 0;
	DrawerLayout drawer;
	static String[] choices;
	static HashMap<String, String> map;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pager = (ViewPager) findViewById(R.id.pager);
		manager = getSupportFragmentManager();

		actionbar = getActionBar();
		// taking user choices //
		drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
		SharedPreferences user_choices = getSharedPreferences("choices",
				Context.MODE_PRIVATE);

		// dividing it into each string
		choices = user_choices.getString("choices", "Physics;Chemistry;Star")
				.split(";");
Log.d("Choices", choices.toString());
		map = new HashMap<String, String>();
		map.put("Business", "0");
		map.put("Entertainment", "1");
		map.put("Finance", "2");
		map.put("Food", "3");
		map.put("Politics", "4");
		map.put("Sports", "5");
		map.put("Technology", "6");
		map.put("Travel", "7");

		// making tabs
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(false);
		actionbar.setStackedBackgroundDrawable(new ColorDrawable(Color.WHITE));
		actionbar.setSplitBackgroundDrawable(new ColorDrawable(Color.rgb(20,
				25, 45)));
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 0, 0)));
//		actionbar.addTab(actionbar.newTab().setIcon(R.drawable.flame)
//				.setTabListener(this));
		for (int i = 0; i < choices.length; i++) {
			Drawable d;
			try {
				d = Drawable.createFromStream(
						this.getAssets()
								.open(choices[i].toLowerCase() + ".png"), null);
				if (d != null)
					actionbar.addTab(actionbar.newTab().setIcon(d)
							.setTabListener(this));
				count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Context c = MainActivity.this;
		frgmnt = new fragments(manager, count, c);
		pager.setAdapter(frgmnt);

		drawer.setOnDragListener(new OnDragListener() {

			@Override
			public boolean onDrag(View v, DragEvent event) {
				// TODO Auto-generated method stub

				return true;
			}
		});

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionbar.setSelectedNavigationItem(arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		pager.setPageTransformer(false, new ViewPager.PageTransformer() {
			@Override
			public void transformPage(View page, float position) {
				// do transformation here
				page.setPivotX(position < 0 ? page.getWidth() : 0);
				page.setScaleX(position < 0 ? 1f + position : 1f - position);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		pager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
}

class fragments extends FragmentPagerAdapter {
	Context v;
	int count;

	public fragments(FragmentManager fm, int count, Context c) {
		super(fm);
		v = c;
		this.count = count + 1;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {

		SharedPreferences.Editor page = v.getSharedPreferences("page",
				Context.MODE_PRIVATE).edit();
		String choice = MainActivity.choices[arg0];
		String key = MainActivity.map.get(choice);
		page.putString("Page", key);
		page.commit();

		return new Question();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

}