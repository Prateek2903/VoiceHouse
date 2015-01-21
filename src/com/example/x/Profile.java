package com.example.x;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Profile extends Activity{

	ImageView iv1,iv2,iv3,iv4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		iv1=(ImageView) findViewById(R.id.one);
		iv2=(ImageView) findViewById(R.id.two);
		iv3=(ImageView) findViewById(R.id.three);
		iv4=(ImageView) findViewById(R.id.four);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/2, width/2);
		iv1.setLayoutParams(params);
		iv2.setLayoutParams(params);
		iv3.setLayoutParams(params);
		iv4.setLayoutParams(params);
	}
}
