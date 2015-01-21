package com.example.x;

import java.util.ArrayList;

import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.x.Agree.Comment;
import com.example.x.Agree.CommentsAdapter;
import com.example.x.Agree.GetComments;
import com.example.x.Question.Agreed;
import com.example.x.fragment_agree.CommentsAdapter.MyCommentHolder;
import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_agree extends Fragment implements
		android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

	JSONArray array;
	GetComments getpost;
	String url = "http://192.168.0.108/X/getallcomments.php";
	int disc_id;
	List<NameValuePair> params;
	public static CommentsAdapter adapter;
	public static ArrayList<Comment> commentList;
	public static Comment comment;
	ListView list;
	int flag = 0;
	android.support.v4.widget.SwipeRefreshLayout refresh;
	MyCommentHolder holder;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_agree, container, false);
		refresh = (android.support.v4.widget.SwipeRefreshLayout) v
				.findViewById(R.id.refresh);
		refresh.setOnRefreshListener(this);
		refresh.setColorSchemeColors(Color.BLACK, Color.DKGRAY, Color.GRAY,
				Color.WHITE);
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("discussion_id", "1"));
		params.add(new BasicNameValuePair("type", "1"));
		getpost = new GetComments(params, url);
		getpost.execute();
		list = (ListView) v.findViewById(R.id.listA);
		array = new JSONArray();
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		View v = (View) menu.findItem(R.id.action_comment).getActionView();
		super.onCreateOptionsMenu(menu, inflater);
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
			refresh.setRefreshing(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONfunction func = new JSONfunction();
			JSONObject jObject = func.getJSONfromURL(url, paramets);
			commentList = new ArrayList<Comment>();
			try {
				JSONArray arra = (JSONArray) jObject.get("comments");
				for (int i = 0; i < arra.length(); i++) {
					jObject = arra.getJSONObject(i);
					Log.d("jo", jObject.toString());
					commentList.add(new Comment(jObject.getString("name"),
							jObject.getString("comment"), jObject
									.getInt("comment_id"), jObject
									.getInt("nmbr_of_logical"), jObject
									.getInt("nmbr_of_ilogical"), 12, jObject
									.getString("client_id")));
				}
			} catch (Exception e) {
				Log.e("arrayList", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			refresh.setRefreshing(false);
			adapter = new CommentsAdapter(getActivity(), commentList);
			list.setAdapter(adapter);

			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	public static class Comment {
		String name, comment, client_id;
		int comment_id, nmbr_of_logical, nmbr_of_ilogical, nmbr_of_comments;

		public Comment(String name, String comment, int com_id, int logicals,
				int ilogicals, int coms, String client_id) {
			// TODO Auto-generated constructor stub
			this.name = name;
			this.comment = comment;
			this.comment_id = com_id;
			this.nmbr_of_logical = logicals;
			this.nmbr_of_ilogical = ilogicals;
			this.nmbr_of_comments = coms;
			this.client_id = client_id;
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

		public String getClient_id() {
			return client_id;
		}

		public void setClient_id(String client_id) {
			this.client_id = client_id;
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
			View v = inflater.inflate(R.layout.single_reply_comment, parent,
					false);
			Log.d("getView", "coming");
			comment = commList.get(position);
			holder = new MyCommentHolder(v, comment.getComment_id(),
					comment.getNmbr_of_logical(),
					comment.getNmbr_of_ilogical(),
					comment.getNmbr_of_comments());
			holder.name.setText(comment.getName());
			holder.comment.setText(comment.getComment());
			holder.logical.setTag(holder);
			holder.illogical.setTag(holder);
			holder.rep.setTag(holder);
			holder.propic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getActivity(), profileView.class);
					i.putExtra("client_id", comment.getClient_id());
					startActivity(i);
				}
			});
			holder.illogical.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyCommentHolder hld = (MyCommentHolder) v.getTag();
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("comment_id", hld.com_id
							+ ""));
					params.add(new BasicNameValuePair("client_id", "1"));
					params.add(new BasicNameValuePair("do", "2"));
					Agreed async = new Agreed(params,
							"http://192.168.0.108/X/logical.php", 0);
					if (hld.logical.getAlpha() == 0.7f) {
						if (hld.illogical.getAlpha() == 0.7f) {
							hld.illogical.setAlpha(1);
							hld.illogicals += 1;
							hld.illogical.setText(hld.illogicals
									+ " Illogicals");
							Log.d("illogical alpha", "l");
						} else {
							hld.illogical.setAlpha(0.7f);
							hld.illogicals -= 1;
							hld.illogical.setText(hld.illogicals
									+ " Illogicals");
							Log.d("illogical alpha", "0.7");
						}
					}
					async.execute();
				}
			});
			holder.logical.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyCommentHolder hld = (MyCommentHolder) v.getTag();
					Log.d("clicked", "logical");
					if (hld.illogical.getAlpha() == 0.7f) {
						if (hld.logical.getAlpha() == 0.7f) {
							hld.logical.setAlpha(1);
							hld.logicals += 1;
							hld.logical.setText(hld.logicals + " Logicals");
						} else {
							hld.logical.setAlpha(0.7f);
							hld.logicals -= 1;
							hld.logical.setText(hld.logicals + " Logicals");
						}
					}

					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("comment_id", hld.com_id
							+ ""));
					params.add(new BasicNameValuePair("client_id", "1"));
					params.add(new BasicNameValuePair("do", "1"));
					Agreed async = new Agreed(params,
							"http://192.168.0.108/X/logical.php", 0);
					async.execute();
				}
			});
			holder.logical.setText(comment.getNmbr_of_logical() + " Logicals");
			holder.illogical.setText(comment.nmbr_of_ilogical + " Illogicals");
			holder.letter.setText(comment.getName().substring(0, 1));
			holder.rep.setText("2" + " Replies");
			holder.propic.setProfileId(comment.getClient_id());
			v.setTag(holder);
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyCommentHolder mch = (MyCommentHolder) v.getTag();
					int i = mch.com_id;
					Intent intent = new Intent(getActivity(),
							ReplyComment.class);
					intent.putExtra("comm_id", i);
					startActivity(intent);
				}
			});
			return v;
		}

		class MyCommentHolder {
			TextView name, comment, logical, illogical, letter, rep;
			ProfilePictureView propic;
			int com_id, logicals, illogicals, subcomms;
			int flag = 0;

			MyCommentHolder(View view, int com_id, int uV, int dV, int replies) {
				name = (TextView) view.findViewById(R.id.commentorName);
				comment = (TextView) view.findViewById(R.id.comm);
				logical = (TextView) view.findViewById(R.id.ivUpvote);
				illogical = (TextView) view.findViewById(R.id.ivDownvote);
				propic = (ProfilePictureView) view.findViewById(R.id.propic);
				rep = (TextView) view.findViewById(R.id.ivReply);
				this.com_id = com_id;
				letter = (TextView) view.findViewById(R.id.imageView1);
				logicals = uV;
				illogicals = dV;

				subcomms = replies;
			}
		}
	}

	public class Agreed extends AsyncTask<Void, Void, Void> {

		List<NameValuePair> paramets;
		String url;
		JSONObject jObject;
		String temp;
		int result, i;

		public Agreed(List<NameValuePair> params, String url, int i) {
			this.paramets = params;
			this.url = url;
			this.i = i;
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
				temp = jObject.getString("agree");
				result = jObject.getInt("success");
				Log.d("amitSir", result + "");
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (this.result == 1) {

			} else {
				Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT)
						.show();
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		GetComments getcmnts = new GetComments(params, url);
		getcmnts.execute();
	}

}
