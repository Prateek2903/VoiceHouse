package com.example.x;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.x.ReplyComment.CommentsAdapter.MyCommentHolder;

public class ReplyComment extends Activity{
	
	JSONArray array;
	GetComments getpost;
	Reply replytocomment;
	String url = "http://voicehouse.in/php/getreply.php",reply_url="http://voicehouse.in/php/reply.php";
	int disc_id;
	JSONObject jObject;
	List<NameValuePair> params;
	List<NameValuePair> replyparams;
	public static CommentsAdapter adapter;
	public static ArrayList<Comment> commentList;
	public static Comment comment;
	ListView list;
	ImageView button;
	EditText reply;
	int flag=0;
	int ilgc,lgcs;
	MyCommentHolder holder;
	TextView logical;
	TextView illogical;
	int com_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thirdview);
		TextView name = (TextView)findViewById(R.id.thirdviewname);
		TextView comment = (TextView)findViewById(R.id.thirdviewcomm);
	    logical = (TextView)findViewById(R.id.thirdviewthirdviewivUpvote);
		illogical = (TextView)findViewById(R.id.thirdviewivDownvote);
		name.setText(getIntent().getStringExtra("name"));
		ilgc = getIntent().getIntExtra("illogical", 0);
		lgcs = getIntent().getIntExtra("logical", 0);
		logical.setText(lgcs+" Logicals");
		illogical.setText(ilgc+" Illogicals");
		com_id=getIntent().getIntExtra("comm_id", 0);
		logical.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("comment_id",com_id+ ""));
				params.add(new BasicNameValuePair("client_id", "1"));
				params.add(new BasicNameValuePair("do", "1"));
				Agreed async = new Agreed(params, "http://voicehouse.in/php/logical.php");
				if(illogical.getAlpha()==0.7f)
				if(logical.getAlpha()==0.7f)
				{	
					lgcs+=1;
					logical.setAlpha(1);
					logical.setText(lgcs+" Logicals");
				}
				else
				{	
					lgcs-=1;
					logical.setAlpha(0.7f);
					logical.setText(lgcs+" Logicals");
				}
				async.execute();
			}
		});
		illogical.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("comment_id",com_id+ ""));
				params.add(new BasicNameValuePair("client_id", "1"));
				params.add(new BasicNameValuePair("do", "2"));
				Agreed async = new Agreed(params, "http://voicehouse.in/php/logical.php");
				if(logical.getAlpha()==0.7f)
				if(illogical.getAlpha()==0.7f)
				{
					ilgc+=1;
					illogical.setAlpha(1);
					illogical.setText(ilgc+" Illogical");
				}
				else
				{
					ilgc-=1;
					illogical.setAlpha(0.7f);
					illogical.setText(ilgc+" Illogical");
				}
				async.execute();
			}
			
		});
		
		
		
		comment.setText(getIntent().getStringExtra("comment"));
		reply = (EditText)findViewById(R.id.thirdviewcomment);
		replyparams = new ArrayList<NameValuePair>();
		replyparams.add(new BasicNameValuePair("comment_id","1"));
		button = (ImageView)findViewById(R.id.post);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				replyparams.add(new BasicNameValuePair("reply",reply.getText().toString()));
				replytocomment = new Reply(replyparams,reply_url);
				replytocomment.execute();
			}
		});
		getActionBar().hide();
		list = (ListView)findViewById(R.id.replylist);
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("comment_id",com_id+""));
		getpost = new GetComments(params, url);
		getpost.execute();
		array = new JSONArray();
	}
	public class Agreed extends AsyncTask<Void, Void, Void> {

		List<NameValuePair> paramets;
		String url;
		JSONObject jObject;
		String temp;
		int result,i;
		public Agreed(List<NameValuePair> params, String url) {
			this.paramets = params;
			this.url = url;
			
		}
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONfunction func = new JSONfunction();
			jObject = func.getJSONfromURL(url, paramets);
			try {
				temp=jObject.getString("agree");
				result=jObject.getInt("success");
				Log.d("amitSir", result+"");
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
				if (this.result==1) {
					
				} else {
					
				}
			super.onPostExecute(result);
		}
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
			reply.setText("");
		}
	}

	class GetComments extends AsyncTask<Void, Void, Void> {
		List<NameValuePair> paramets;
		String url;
		public GetComments(List<NameValuePair> params, String url) {
			this.paramets = params;
			this.url = url;
		}
		@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
			super.onPreExecute();
			}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONfunction func = new JSONfunction();
			JSONObject jObject = func.getJSONfromURL(url, paramets);
			commentList=new ArrayList<Comment>();
			Log.d("message","came here");
			try{
				JSONArray arra = (JSONArray) jObject.get("reply");
				for (int i = 0; i < arra.length(); i++) {
					jObject = arra.getJSONObject(i);
					Log.d("jo", jObject.toString());
					commentList.add(new Comment(jObject.getString("name"),
							jObject.getString("reply")));
				}
			} catch (Exception e) {
				Log.e("arrayList", e.toString());
				Log.d("message","jumped in error");
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			adapter = new CommentsAdapter(ReplyComment.this, commentList);
			list.setAdapter(adapter);
			super.onPostExecute(result);
		}	
	}
	public static class Comment {
		String name, reply;

		public Comment(String name, String reply, int com_id, int logicals,
				int ilogicals, int coms) {
			// TODO Auto-generated constructor stub
			this.name = name;
			this.reply = reply;
			
		}
		
		public Comment(String name, String reply) {
			// TODO Auto-generated constructor stub
			this.name = name;
			this.reply = reply;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getReply() {
			return reply;
		}

		public void setReply(String reply) {
			this.reply = reply;
		}
	}

	class CommentsAdapter extends BaseAdapter {

		Context context;
		Comment comment;
		ArrayList<Comment> commList;
		JSONObject jsonObject = new JSONObject();
		int like, dislike;
		

		public CommentsAdapter(Context c, ArrayList<Comment> listComment) {
			// TODO Auto-generated constructor stub
			context = c;
			commList = new ArrayList<Comment>();
			commList = listComment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return commList.get(position);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return commList.get(position).hashCode();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.reply_third_single_comment, parent, false);
			Log.d("getView", "coming");
			comment = commList.get(position);
			holder = new MyCommentHolder(v);
			holder.name.setText(comment.getName());
			Log.d("name",comment.getName());
			holder.comment.setText(comment.getReply());
			v.setTag(holder);
			return v;
		}
		class MyCommentHolder {
			TextView name, comment;
			int flag=0;
			MyCommentHolder(View view) {
				name = (TextView) view.findViewById(R.id.commentorName);
				comment = (TextView) view.findViewById(R.id.comm);
			}
		}
	}


}
