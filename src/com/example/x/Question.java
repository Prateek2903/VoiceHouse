package com.example.x;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Question extends Fragment implements
		android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

	ListView list;
	Discussion disc;
	DiscussionAdapter adapter;
	ArrayList<Discussion> discussionList;
	GetPos getpost;
	ProgressDialog pDialog;
	MyViewHolder mvh;
	List<NameValuePair> params;
	String url = "http://192.168.0.108/X/getquestion.php";
	MyViewHolder holder;
	android.support.v4.widget.SwipeRefreshLayout swipe;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle SavedInstances) {
		SharedPreferences page = getActivity().getSharedPreferences("page",
				Context.MODE_PRIVATE);
		String choice_id = page.getString("Page", null);
		View view = inflater.inflate(R.layout.first_topic, container, false);
		list = (ListView) view.findViewById(R.id.listFirstTopic);
		swipe = (android.support.v4.widget.SwipeRefreshLayout) view
				.findViewById(R.id.agreeswipelist);
		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeColors(Color.BLACK, Color.GRAY, Color.DKGRAY,
				Color.WHITE);
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("choices_id", choice_id));
		params.add(new BasicNameValuePair("client_id","1"));
		getpost = new GetPos(params, url);
		getpost.execute();
		return view;
	}

	public class GetPos extends AsyncTask<Void, Void, Void> {
		List<NameValuePair> paramets;
		String url;
		public GetPos(List<NameValuePair> params, String url) {
			this.paramets = params;
			this.url = url;
		}
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			swipe.setRefreshing(true);
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONfunction func = new JSONfunction();
			JSONObject jObject = func.getJSONfromURL(url, paramets);
			try {
				JSONArray array = (JSONArray) jObject.get("discussion_table");
				discussionList = new ArrayList<Discussion>();
				for (int i = 0; i < array.length(); i++) {
					jObject = array.getJSONObject(i);
					discussionList.add(new Discussion(jObject
							.getString("discussion"), jObject
							.getInt("discussion_id"), 1, jObject
							.getInt("agree"), jObject.getInt("disagree"),
							jObject.getJSONArray("comments")));
				}
			} catch (Exception e) {
				Log.e("arrayList", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			adapter = new DiscussionAdapter(getActivity(), discussionList);
			list.setAdapter(adapter);
			swipe.setRefreshing(false);
			super.onPostExecute(result);
		}
	}

	public class DiscussionAdapter extends BaseAdapter {
		Context c;
		ArrayList<Discussion> discussion;

		public DiscussionAdapter(Context c, ArrayList<Discussion> disc) {
			// TODO Auto-generated constructor stub
			this.c = c;
			this.discussion = disc;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return discussion.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return discussion.get(position);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return discussion.get(position).hashCode();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(c.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.single_posts, parent, false);
			disc = new Discussion();
			disc = discussion.get(position);
			holder = new MyViewHolder(view, disc.getDis_id(),
					disc.getDiscussion(),disc.getAgree(),disc.getDisagree());
			view.setTag(holder);
			holder.relative_layout.setTag(holder);
			holder.agree.setTag(holder);
			holder.disagree.setTag(holder);
			holder.invite.setTag(holder);
			holder.ll.setTag(holder);

			holder.agree.setText(disc.getAgree() + " AGREE");
			holder.disagree.setText(disc.getDisagree() + " DISAGREE");
			holder.d_id = disc.getDis_id();
			Log.e("D_id", "" + holder.d_id);
			holder.question.setText(disc.getDiscussion());
			for (int i = 0; i < disc.getArray().length(); i++) {
				JSONObject obj;
				try {
					obj = (JSONObject) disc.getArray().get(i);
					SingleCommentView scv = new SingleCommentView(c, obj,
							disc.getDiscussion());
					holder.ll.addView(scv);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			holder.agree.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
								
					
					mvh = (MyViewHolder) arg0.getTag();
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("discussion_id", mvh.d_id
							+ ""));
					params.add(new BasicNameValuePair("client_id", "1"));
					params.add(new BasicNameValuePair("do", "1"));
					Agreed async = new Agreed(params, "http://192.168.0.108/X/Agree.php",0);
					async.execute();
					if(mvh.disagree.getAlpha()==0.7f)
					if(mvh.agree.getAlpha()==0.7f )
					{
						mvh.agree.setAlpha(1);
						mvh.agrees+=1;
						mvh.agree.setText(mvh.agrees+" Agrees");
					}
					else
					{
						mvh.agree.setAlpha(0.7f);
						mvh.agrees-=1;
						mvh.agree.setText(mvh.agrees+" Agrees");
					}
				}
			});
			holder.disagree.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mvh = (MyViewHolder) arg0.getTag();
					params.add(new BasicNameValuePair("discussion_id", mvh.d_id
							+ ""));
					params.add(new BasicNameValuePair("client_id", "1"));
					params.add(new BasicNameValuePair("do", "2"));
					Agreed async = new Agreed(params, "http://192.168.0.108/X/Agree.php",1);
					async.execute();
					if(mvh.agree.getAlpha()==0.7f)
					if(mvh.disagree.getAlpha()==0.7f )
					{
						mvh.disagree.setAlpha(1);
						mvh.disagrees+=1;
						mvh.disagree.setText(mvh.disagrees+" Disagrees");
					}
					else
					{
						mvh.disagree.setAlpha(0.7f);
						mvh.disagrees-=1;
						mvh.disagree.setText(mvh.disagrees+" Disagrees");

					}
					
				}
			});
			holder.invite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyViewHolder mvh = (MyViewHolder) v.getTag();
				}
			});
			holder.ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyViewHolder mvh = (MyViewHolder) v.getTag();
					Intent i = new Intent(getActivity(), comments_tab.class);
					i.putExtra("discussion", mvh.discussion);
					i.putExtra("discussion_id", mvh.d_id);
					i.putExtra("name", "name goes here");
					Activity activity = (Activity) c;
					activity.overridePendingTransition(R.anim.slidetop,
							R.anim.slidebottom);
					c.startActivity(i);
				}
			});
			return view;
		}
	}
	public class Agreed extends AsyncTask<Void, Void, Void> {
		List<NameValuePair> paramets;
		String url;
		JSONObject jObject;
		String temp;
		int result,i;
		public Agreed(List<NameValuePair> params, String url, int i) {
			this.paramets = params;
			this.url = url;
			this.i=i;
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
			// T
				
			super.onPostExecute(result);
		}
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		GetPos getpost = new GetPos(params, url);
		getpost.execute();
	}
}
class Discussion {
	String discussion;
	JSONArray array;
	int dis_id, cat_id, agree, disagree;
	public Discussion() {
		// TODO Auto-generated constructor stub
	}

	public Discussion(String discussion, int dis_id, int cat_id, int agree,
			int disagree, JSONArray array) {
		// TODO Auto-generated constructor stub
		this.agree = agree;
		this.cat_id = cat_id;
		this.dis_id = dis_id;
		this.disagree = disagree;
		this.discussion = discussion;
		this.array = array;
	}

	public JSONArray getArray() {
		return array;
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	public String getDiscussion() {
		return discussion;
	}

	public void setDiscussion(String discussion) {
		this.discussion = discussion;
	}

	public int getDis_id() {
		return dis_id;
	}

	public void setDis_id(int dis_id) {
		this.dis_id = dis_id;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public int getAgree() {
		return agree;
	}

	public void setAgree(int agree) {
		this.agree = agree;
	}

	public int getDisagree() {
		return disagree;
	}

	public void setDisagree(int disagree) {
		this.disagree = disagree;
	}
}

class MyViewHolder {
	TextView question, agree, disagree, invite;
	// ListView listComments;
	LinearLayout ll;
	RelativeLayout relative_layout;
	String discussion;
	int d_id,agrees,disagrees;
	MyViewHolder(View view, int d_id, String discussion,int agrees,int disagrees) {
		relative_layout = (RelativeLayout) view.findViewById(R.id.relative_layout);
		question = (TextView) view.findViewById(R.id.tvQuestion);
		agree = (TextView) view.findViewById(R.id.tvAgree);
		ll = (LinearLayout) view.findViewById(R.id.linearComments);
		disagree = (TextView) view.findViewById(R.id.tvDisagree);
		invite = (TextView) view.findViewById(R.id.inviteHome);
		this.d_id = d_id;
		this.agrees = agrees;
		this.disagrees = disagrees;
		this.discussion = discussion;
	}
}