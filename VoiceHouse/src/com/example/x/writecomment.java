package com.example.x;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class writecomment extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_comment);
		ListView agreelist = (ListView)findViewById(R.id.agreelist);
		
	}
}
