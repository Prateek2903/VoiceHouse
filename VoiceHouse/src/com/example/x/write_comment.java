package com.example.x;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class write_comment extends TabActivity {
	
	TabHost tabs;
	EditText et;
	ImageView post;
	ActionBar actionbar;
	float lastX,currentX;
	List<NameValuePair> params;
	String url = "http://voicehouse.in/php/comment.php";
	WriteComment writeComment;
	int comm_id,d;
	private GestureDetectorCompat gDetector;
	int success;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments_viw);
        tabs = getTabHost();
        actionbar = getActionBar();
        TabSpec tab1 = tabs.newTabSpec("tab1");
        TabSpec tab2 = tabs.newTabSpec("tab2");
        tab1.setIndicator("Agree");
        tab2.setIndicator("Disagree");
        Intent i = getIntent();
        TextView tv = (TextView) findViewById(R.id.tvQuestionComments);
        tv.setText(i.getStringExtra("discussion"));

        d=i.getIntExtra("discussion_id", 0);
        
        Intent i1= new Intent(this,Agree.class);
        i1.putExtra("discussion_id", d);
        tab1.setContent(i1);
        
        Intent i2=new Intent(this,Disagree.class);
        i2.putExtra("discussion_id", d);
        tab2.setContent(i2);
        
        tabs.addTab(tab1);
        tabs.addTab(tab2);
        tabs.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String arg0) {
				// TODO Auto-generated method stub
			}
		});
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayUseLogoEnabled(false);
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayUseLogoEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowHomeEnabled(false);
        
//        gDetector = new GestureDetectorCompat(this, new OnGestureListener() {
//
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return true;
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                float velocityY) {
//                Log.i("motion", "onFling has been called!");
//                final int SWIPE_MIN_DISTANCE = 120;
//                final int SWIPE_MAX_OFF_PATH = 250;
//                final int SWIPE_THRESHOLD_VELOCITY = 200;
//                try {
//                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                        return false;
//                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                        Log.i("motion", "Right to Left");
//                        switchTabs(false);
//                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//
//                        Log.i("motion", "Left to Right");
//                        switchTabs(true);
//
//                    }
//                } catch (Exception e) {
//                    // nothing
//                }
//                return true;
//            }

//           @Override
//           public void onLongPress(MotionEvent e) {
//
//           }
//
//           @Override
//           public boolean onScroll(MotionEvent e1, MotionEvent e2,
//                   float distanceX, float distanceY) {
//               return false;
//           }
//
//           @Override
//           public void onShowPress(MotionEvent e) {
//
//           }
//
//           @Override
//           public boolean onSingleTapUp(MotionEvent e) {
//                           return false;
//           }
//       });
        
        tabs.setOnTouchListener(new View.OnTouchListener() {
            @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gDetector.onTouchEvent(event);
                }
            });
	}
	
	
	public void switchTabs(boolean direction) {

		   Log.w("switch Tabs", "idemo direction");
		   if (direction) // true = move left
		   {
		        if (tabs.getCurrentTab() != 0)
		        tabs.setCurrentTab(tabs.getCurrentTab() - 1);
		   } else
		   // move right
		   {
		       if (tabs.getCurrentTab() != (tabs.getTabWidget()
		            .getTabCount() - 1))
		        tabs.setCurrentTab(tabs.getCurrentTab() + 1);
		   }
		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_text,menu);
		View v = (View) menu.findItem(R.id.action_comment).getActionView();
		//et = (EditText) v.findViewById(R.id.edit_text);
		ImageView b = (ImageView) v.findViewById(R.id.post);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et.getText().toString() != null) {
					params = new ArrayList<NameValuePair>();
					String s = null;
					params.add(new BasicNameValuePair("comment", et.getText()
							.toString()));
					params.add(new BasicNameValuePair("discussion_id", d + ""));
					params.add(new BasicNameValuePair("client_id", "1"));
					if (tabs.getCurrentTab() == 0) {
						s = "3";
					} else {
						s = "4";
					}
					params.add(new BasicNameValuePair("type", s));
					writeComment = new WriteComment(params, url);
					writeComment.execute();
				}
			}
		});
		return super.onCreateOptionsMenu(menu);
	}
	
	class WriteComment extends AsyncTask<Void, Void, Void> {

		List<NameValuePair> paramets;
		String url;

		public WriteComment(List<NameValuePair> params, String url) {
			this.paramets = params;
			this.url = url;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONfunction func = new JSONfunction();
			JSONObject jObject = func.getJSONfromURL(url, paramets);
			try {
				success = Integer.parseInt(jObject.get("success").toString());
				comm_id = Integer
						.parseInt(jObject.get("comment_id").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if (success == 1) {
				if (tabs.getCurrentTab() == 0) {
					Agree.commentList.add(new Agree.Comment("Name", "comment", comm_id, 0, 0, 0));
					Agree.adapter.notifyDataSetChanged();
					et.setText("");
				}else{
					Disagree.commentList.add(new Disagree.Comment("Sandeep", et
							.getText().toString(), comm_id, 0, 0, 0));
					Disagree.adapter.notifyDataSetChanged();
					et.setText("");
				}
			} else {
				Toast.makeText(write_comment.this, "Connection Error",
						Toast.LENGTH_SHORT).show();
			}

			super.onPostExecute(result);
		}
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.action_comment){
				
		}
		return true;
	}
	@Override
	public void onBackPressed() {
		overridePendingTransition(R.anim.closetop, R.anim.closebottom);
		finish();
	}
}