package com.example.x;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.facebook.widget.WebDialog.RequestsDialogBuilder;

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
	String url = "http://192.168.0.108/X/getpost.php";
	MyViewHolder holder;
	android.support.v4.widget.SwipeRefreshLayout swipe;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle SavedInstances) {
		SharedPreferences page = getActivity().getSharedPreferences("page",
				Context.MODE_PRIVATE);
				String choice_id=page.getString("Page", "0");
		View view = inflater.inflate(R.layout.first_topic, container, false);
		list = (ListView) view.findViewById(R.id.listFirstTopic);
		swipe = (android.support.v4.widget.SwipeRefreshLayout) view
				.findViewById(R.id.agreeswipelist);
		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeColors(Color.BLACK, Color.GRAY, Color.DKGRAY,
				Color.WHITE);
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("choices_id",choice_id));
		params.add(new BasicNameValuePair("client_id", "1"));
		
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
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Saving the Data,Please Wait..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
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
			pDialog.dismiss();
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
					disc.getDiscussion());
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
					params.add(new BasicNameValuePair("do", "0"));
					Agreed async = new Agreed(params, "http://192.168.0.108/X/Agree.php",1);
					async.execute();
				}
			});
			holder.invite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyViewHolder mvh = (MyViewHolder) v.getTag();
					publishStory();
				}
				});
			

		 
			 
	
			holder.ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MyViewHolder mvh = (MyViewHolder) v.getTag();
					Intent i = new Intent(getActivity(), StickyFragment.class);
					i.putExtra("discussion", mvh.discussion);
					Activity activity = (Activity) c;
					activity.overridePendingTransition(R.anim.slidetop,
							R.anim.slidebottom);
					c.startActivity(i);

				}
			});

			return view;
		}

	}
public void publishStory() {
Bundle params = new Bundle();
params.putString("name", "test-name");
params.putString("message", "test-caption");
params.putString("description", "test-description");
params.putString("link", "http://curious-blog.blogspot.com");
Session currentSession=Session.getActiveSession();
Session.setActiveSession(currentSession);

RequestsDialogBuilder feedDialog = new WebDialog.RequestsDialogBuilder(getActivity());
feedDialog.setMessage("Get a life dude..");
feedDialog.setTo(facebook_login.u.getId());
feedDialog.setOnCompleteListener(new OnCompleteListener() {

@Override
public void onComplete(Bundle values,
  FacebookException error) {
 if (error == null) {
  // When the story is posted, echo the success
  // and the post Id.
  final String postId = values.getString("post_id");
  if (postId != null) {
   // do some stuff
  } else {
   // User clicked the Cancel button
   Toast.makeText(getActivity(),
     "Publish cancelled", Toast.LENGTH_SHORT)
     .show();
  }
 } else if (error instanceof FacebookOperationCanceledException) {
  // User clicked the "x" button
  Toast.makeText(getActivity(),
    "Publish cancelled", Toast.LENGTH_SHORT)
    .show();
 } else {
  // Generic, ex: network error
  Toast.makeText(getActivity(),
    "Error posting story", Toast.LENGTH_SHORT)
    .show();
 }
}

}).build().show();

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
				if (this.result==1) {
					if(i==0){
						if (holder.agree.getAlpha()==0.7f) {
							holder.agree.setAlpha(1);
							holder.agree.setText(temp);
							holder.disagree.setClickable(false);
						} else {
							holder.agree.setAlpha(0.7f);
							holder.agree.setText(temp);
							holder.disagree.setClickable(true);
						}
						
					}else{
						if (holder.disagree.getAlpha()==0.7f) {
							holder.disagree.setAlpha(1);
							holder.disagree.setText(temp);
							holder.agree.setClickable(false);
						} else {
							holder.disagree.setAlpha(0.7f);
							holder.disagree.setText(temp);
							holder.agree.setClickable(true);
						}
						
					}
				} else {
					Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
				}
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
	int d_id;

	MyViewHolder(View view, int d_id, String discussion) {
		relative_layout = (RelativeLayout) view
				.findViewById(R.id.relative_layout);
		question = (TextView) view.findViewById(R.id.tvQuestion);
		agree = (TextView) view.findViewById(R.id.tvAgree);
		ll = (LinearLayout) view.findViewById(R.id.linearComments);
		disagree = (TextView) view.findViewById(R.id.tvDisagree);
		invite = (TextView) view.findViewById(R.id.inviteHome);
		this.d_id = d_id;
		this.discussion = discussion;
	}
}