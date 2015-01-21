	package com.example.x;
	
	import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
	
	public class Agree extends Activity implements
			android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {
	
		JSONArray array;
		GetComments getpost;
		String url = "http://voicehouse.in/php/getallcomments.php";
		int disc_id;
		List<NameValuePair> params;
		public static CommentsAdapter adapter;
		public static ArrayList<Comment> commentList;
		public static Comment comment;
		ListView list;
		android.support.v4.widget.SwipeRefreshLayout swipe;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.agree);
			list = (ListView) findViewById(R.id.agreelist);
			disc_id = getIntent().getIntExtra("discussion_id", 0);
			swipe = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.agreeswipelist);
			swipe.setOnRefreshListener(this);
			swipe.setColorSchemeColors(Color.BLACK, Color.GRAY, Color.DKGRAY,
					Color.WHITE);
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("discussion_id",  disc_id+""));
			params.add(new BasicNameValuePair("type", "3"));
			getpost = new GetComments(params, url);
			getpost.execute();
	
			array = new JSONArray();
		}
	
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
	
		}
	
		class GetComments extends AsyncTask<Void, Void, Void> {
	
			List<NameValuePair> paramets;
			String url;
	
			public GetComments(List<NameValuePair> params, String url) {
				this.paramets = params;
				this.url = url;
			}
	
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				JSONfunction func = new JSONfunction();
				JSONObject jObject = func.getJSONfromURL(url, paramets);

				commentList=new ArrayList<Comment>();
				try {
					JSONArray arra = (JSONArray) jObject.get("comments");
					for (int i = 0; i < arra.length(); i++) {
						jObject = arra.getJSONObject(i);
						Log.d("jo", jObject.toString());
						commentList.add(new Comment(jObject.getString("name"),
								jObject.getString("comment"), jObject
										.getInt("comment_id"), jObject
										.getInt("nmbr_of_logical"), jObject
										.getInt("nmbr_of_ilogical"), jObject
										.getInt("nmbr_of_subcomment")));
					}
	
				} catch (Exception e) {
					Log.e("arrayList", e.toString());
				}
				return null;
			}
	
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				adapter = new CommentsAdapter(getApplicationContext(), commentList);
				list.setAdapter(adapter);
				super.onPostExecute(result);
			}
		}
	
		public static class Comment {
			String name, comment;
			int comment_id, nmbr_of_logical, nmbr_of_ilogical, nmbr_of_comments;
	
			public Comment(String name, String comment, int com_id, int logicals,
					int ilogicals, int coms) {
				// TODO Auto-generated constructor stub
				this.name = name;
				this.comment = comment;
				this.comment_id = com_id;
				this.nmbr_of_logical = logicals;
				this.nmbr_of_ilogical = ilogicals;
				this.nmbr_of_comments = coms;
			}
	
			public int getNmbr_of_logical() {
				return nmbr_of_logical;
			}
	
			public void setNmbr_of_logical(int nmbr_of_logical) {
				this.nmbr_of_logical = nmbr_of_logical;
			}
	
			public int getNmbr_of_ilogical() {
				return nmbr_of_ilogical;
			}
	
			public void setNmbr_of_ilogical(int nmbr_of_ilogical) {
				this.nmbr_of_ilogical = nmbr_of_ilogical;
			}
	
			public int getNmbr_of_comments() {
				return nmbr_of_comments;
			}
	
			public void setNmbr_of_comments(int nmbr_of_comments) {
				this.nmbr_of_comments = nmbr_of_comments;
			}
	
			public int getComment_id() {
				return comment_id;
			}
	
			public void setComment_id(int comment_id) {
				this.comment_id = comment_id;
			}
	
			public Comment(String name, String comment) {
				// TODO Auto-generated constructor stub
				this.name = name;
				this.comment = comment;
			}
	
			public String getName() {
				return name;
			}
	
			public void setName(String name) {
				this.name = name;
			}
	
			public String getComment() {
				return comment;
			}
	
			public void setComment(String comment) {
				this.comment = comment;
			}
		}
	
		class CommentsAdapter extends BaseAdapter {
	
			Context context;
			Comment comment;
			ArrayList<Comment> commList;
			JSONObject jsonObject = new JSONObject();
			int like, dislike;
			MyCommentHolder holder;
	
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
				View v = inflater.inflate(R.layout.single_comment, parent, false);
				Log.d("getView", "coming");
				comment = commList.get(position);
				holder = new MyCommentHolder(v, comment.getComment_id(),
						comment.getNmbr_of_logical(),
						comment.getNmbr_of_ilogical(),
						comment.getNmbr_of_comments());
				holder.name.setText(comment.getName());
				holder.comment.setText(comment.getComment());
				holder.logical.setText(comment.getNmbr_of_logical() + "");
				holder.illogical.setText(comment.nmbr_of_ilogical + "");
				holder.letter.setText(comment.getName().substring(0, 1));
				v.setTag(holder);
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						MyCommentHolder mch = (MyCommentHolder) v.getTag();
						int i=mch.com_id;
						Intent intent = new Intent(Agree.this, ReplyComment.class);
						intent.putExtra("comm_id", i);
						startActivity(intent);
					}
				});
				return v;
			}
	
			class MyCommentHolder {
	
				TextView name, comment, logical, illogical,letter;
				int com_id, logicals, illogicals, subcomms;
	
				MyCommentHolder(View view, int com_id, int uV, int dV, int replies) {
					name = (TextView) view.findViewById(R.id.commentorName);
					comment = (TextView) view.findViewById(R.id.comm);
					logical = (TextView) view.findViewById(R.id.noUpvotes);
					illogical = (TextView) view.findViewById(R.id.noDownvotes);
					this.com_id = com_id;
					letter=(TextView) view.findViewById(R.id.imageView1);
					logicals = uV;
					illogicals = dV;
					subcomms = replies;
				}
			}
		}
		
	}