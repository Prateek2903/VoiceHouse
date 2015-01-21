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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ReplyFinalTry extends Activity {

	int comm_id;
	CommentsAdapter adapter;
	List<NameValuePair> params;
	GetReply goc;
	ListView list;
	LinearLayout ll;
	String url = "http://voicehouse.in/php/getreply.php";
	ArrayList<Comment> commentList;
	MyCommentHolder holder;
	EditText et;
	JSONObject jObject;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.reply_comment);
		i = getIntent();
		comm_id = i.getIntExtra("comm_id", 0);

		list = (ListView) findViewById(R.id.replyCommentList);
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("comment_id", comm_id + ""));
		goc = new GetReply(params, url);
		goc.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_text, menu);
		View v = (View) menu.findItem(R.id.action_comment).getActionView();
		//et = (EditText) v.findViewById(R.id.edit_text);
		ImageView b = (ImageView) v.findViewById(R.id.post);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et.getText().toString() != null) {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("reply", et.getText()
							.toString()));
					params.add(new BasicNameValuePair("comment_id", comm_id
							+ ""));
					params.add(new BasicNameValuePair("client_id", "1"));
					Reply async = new Reply(params,
							"http://voicehouse.in/php/reply.php");
					async.execute();
				}
			}
		});
		return super.onCreateOptionsMenu(menu);
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
			jObject = func.getJSONfromURL(url, paramets);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			try {
				if (jObject.getInt("success") == 1) {
					commentList.add(new Comment("Sandeep", et.getText()
							.toString(), 0));
					et.setText("");
					adapter.notifyDataSetChanged();
				}
			} catch (Exception e) {

			}
			super.onPostExecute(result);
		}
	}

	public class GetReply extends AsyncTask<Void, Void, Void> {

		List<NameValuePair> paramets;
		String url;

		public GetReply(List<NameValuePair> params, String url) {
			this.paramets = params;
			this.url = url;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONfunction func = new JSONfunction();
			JSONObject jObject = func.getJSONfromURL(url, paramets);
			try {
				JSONArray arra = (JSONArray) jObject.get("reply");
				Log.d("saw", arra.toString());
				commentList = new ArrayList<Comment>();
				for (int i = 0; i < arra.length(); i++) {
					jObject = arra.getJSONObject(i);
					commentList.add(new Comment(jObject.getString("name"),
							jObject.getString("comment"), jObject
									.getInt("comment_id")));
				}

			} catch (Exception e) {
				Log.e("arrayList", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			adapter = new CommentsAdapter(ReplyFinalTry.this, commentList);
			list.setAdapter(adapter);
			super.onPostExecute(result);
		}
	}

	class Comment {
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

		public Comment(String name, String comment, int com_i) {
			// TODO Auto-generated constructor stub
			this.name = name;
			this.comment = comment;
			this.comment_id = com_i;
			// adapter = new CommentsAdapter("HARISH", "DO");
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

		// MyCommentHolder holder;

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
			if (position != 0) {
				comment = commList.get(position-1);
				holder = new MyCommentHolder(v, comm_id, 0, 0, 0);
				holder.comment.setText(comment.getComment());
				holder.logical.setVisibility(View.GONE);
				holder.illogical.setVisibility(View.GONE);
				holder.reply.setVisibility(View.GONE);
			}else{
				holder = new MyCommentHolder(v, comm_id, 0, 0, 0);
				holder.v.setBackground(new ColorDrawable(Color.BLACK));
				holder.comment.setText(i.getStringExtra("comment"));
				holder.name.setText(i.getStringExtra("name"));
				holder.name.setTextColor(Color.WHITE);
				holder.comment.setTextColor(Color.WHITE);
				holder.illogical.setText(i.getStringExtra("ilogical"));
				holder.illogical.setTextColor(Color.WHITE);
				holder.logical.setText(i.getStringExtra("logical"));
				holder.logical.setTextColor(Color.WHITE);
				holder.reply.setText(i.getStringExtra("reply"));
				holder.reply.setTextColor(Color.WHITE);
				holder.rating.setTextColor(Color.WHITE);
			}
			return v;
		}
	}

	//
	class MyCommentHolder {

		TextView name, comment, logical, illogical, subcomm, reply,rating;
		int com_id, logicals, illogicals;
		View v;

		MyCommentHolder(View view, int com_id, int uV, int dV, int replies) {
			v=view.findViewById(R.id.colorIt);
			name = (TextView) view.findViewById(R.id.commentorName);
			comment = (TextView) view.findViewById(R.id.comm);
			logical = (TextView) view.findViewById(R.id.ivUpvote);
			illogical = (TextView) view.findViewById(R.id.ivDownvote);
			this.com_id = com_id;
			logicals = uV;
			illogicals = dV;
			rating=(TextView) view.findViewById(R.id.textView1);
			reply = (TextView) view.findViewById(R.id.ivReply);
		}
		
		
		
		
	}
}