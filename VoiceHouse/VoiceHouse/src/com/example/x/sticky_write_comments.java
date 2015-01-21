package com.example.x;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class sticky_write_comments extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_comment_top_layout);
	}
}

class agree_disagree_fragment extends FragmentPagerAdapter

{
	public agree_disagree_fragment(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return new Agree_comments();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
