package com.example.x;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.x.ReplyComment.Comment;
import com.example.x.ReplyComment.Reply;
import com.example.x.fragment_agree.GetComments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
public class comments_tab extends FragmentActivity{
	ViewPager pager;
	agreefragments frgment;
	EditText et;
	ImageView post ;
	List<NameValuePair> params;
	TextView agree,disagree,mytext1,mytext2,agree_disagree;
	int flag;
	int disc_id;
	ArrayList<Comment> commentList;	JSONObject jObject;
	 private final String[] array = {"Hello", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome"};
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.comments_tab_layout);
	        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.view_row, R.id.question, array);
	        TextView mytext = (TextView)findViewById(R.id.full_question);
	        mytext.setText("is this worikng");
	        agree_disagree = (TextView)findViewById(R.id.agree_disagree);
	        agree_disagree.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(flag==1)
					{
						flag=2;
						agree_disagree.setText("Disagree");
						
					}
					else
					{
						flag=1;
						agree_disagree.setText("Agree");
						
					}
				}
			});
	        mytext2 = (TextView)findViewById(R.id.question);
	        mytext2.setText(getIntent().getStringExtra("name"));
	        mytext.setText(getIntent().getStringExtra("discussion"));
	        disc_id=getIntent().getIntExtra("discussion_id", 0);
	        pager = (ViewPager)findViewById(R.id.pager);
	        String text = getIntent().getStringExtra("discussion");
	        char a[] = text.toCharArray();
	        String questions="";
	        mytext2.setText("Discussion Topic");
	        
	        
	        frgment = new agreefragments(getSupportFragmentManager());
	        pager.setAdapter(frgment);
	        et = (EditText)findViewById(R.id.comment);
	        post = (ImageView)findViewById(R.id.post);
	        post.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d("clicked","post");
					String opt=agree_disagree.getText().toString();
					if(opt=="Disagree")
					{
						flag=2;
					}
					else
					{
						flag=1;
					}
						params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("comment",et.getText().toString())); // put et here instead of hello
						params.add(new BasicNameValuePair("discussion_id",disc_id+""));
						params.add(new BasicNameValuePair("client_id", "1"));
						params.add(new BasicNameValuePair("type", flag+""));
						Reply async= new Reply(params,"http://192.168.0.108/X/comments.php");
						Log.d("clicked","post");
						et.setText("");
						async.execute();
				}
			});
	        
	        getActionBar().hide();
	        agree = (TextView)findViewById(R.id.agree);
	        disagree = (TextView)findViewById(R.id.disagree);
	        params = new ArrayList<NameValuePair>();
	        agree.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pager.setCurrentItem(0);
				}
			});
	        disagree.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pager.setCurrentItem(1);
				}
			});
	        pager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					if(arg0==0)
					{
						agree.setBackgroundResource(R.drawable.selected_orange);
						disagree.setBackgroundResource(R.drawable.unselected_orange);
						agree_disagree.setText("Agree");
					}
					else
					{
						disagree.setBackgroundResource(R.drawable.selected_orange);
						agree.setBackgroundResource(R.drawable.unselected_orange);
						agree_disagree.setText("Disagree");
					}
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
	    }
	    

public class Reply extends AsyncTask<Void, Void, Void> {

	List<NameValuePair> paramets;
	String url;

	public Reply(List<NameValuePair> params, String url) {
		this.paramets = params;
		this.url = url;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		JSONfunction func = new JSONfunction();
		jObject= func.getJSONfromURL(url, paramets);
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		
	}
}
}

class agreefragments extends FragmentPagerAdapter
{

	public agreefragments(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		if(arg0==0)
			return new fragment_agree();
		else
			return new fragment_disagree();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}
}
