package com.example.x;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

public class welcome extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		TextView mytext = (TextView)findViewById(R.id.textView1);
		TextView mytext2 = (TextView)findViewById(R.id.textView2);
		getActionBar().hide();
		final FrameLayout black =(FrameLayout)findViewById(R.id.black);
		final Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.slidebottom);
		hyperspaceJumpAnimation.setFillEnabled(true);
		hyperspaceJumpAnimation.setDuration(3000);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				black.startAnimation(hyperspaceJumpAnimation);
			}
		},1000);	
		
		hyperspaceJumpAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(welcome.this,HelloGridView.class));
				overridePendingTransition(R.anim.slidetop,R.anim.slidebottom);
				finish();
			}
		});
		
	}

}
