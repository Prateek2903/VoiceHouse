package com.example.x;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.x.Disagree_Fragment.CommentsAdapter.MyCommentHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Disagree_Fragment extends ListFragment{
	
	private QuickReturnListView mListView;
	private View mHeader;
	private TextView mQuickReturnView;
	private TextView mPlaceHolder;
	
	private int mCachedVerticalScrollRange;
	private int mQuickReturnHeight;
	
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;
	ArrayList<Comment> commentList;
	MyCommentHolder mch;
	GetComments get;
	
	private TranslateAnimation anim;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.agree_fragment, null);
		mHeader = inflater.inflate(R.layout.header, null);
		mQuickReturnView = (TextView) view.findViewById(R.id.sticky);
		mPlaceHolder = (TextView)mHeader.findViewById(R.id.placeholder);
		mPlaceHolder.setText("hello");
		get=new GetComments();
		get.execute();
		mQuickReturnView.setText(getActivity().getIntent().getStringExtra("discussion"));
		mPlaceHolder.setText(getActivity().getIntent().getStringExtra("discussion"));
		return view;
	}
	public void on()
	{
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		
//		mListView = (QuickReturnListView) getListView();
//		
//		mQuickReturnView.setText("Default");
//		mListView.addHeaderView(mHeader);
//		
		String[] array = new String[] { "Apurb", "Aprateek", "Android",
				"Android", "Android", "Android", "Android", "Android",
				"Android", "Android", "Android", "Android", "Android",
				"Android", "Android", "Android" };
//		setListAdapter(new ArrayAdapter<String>(getActivity(),
//				R.layout.single_comment,
//				R.id.text1, array));
//		
	
		
		mListView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mQuickReturnHeight = mQuickReturnView.getHeight();
						mListView.computeScrollY();
						mCachedVerticalScrollRange = mListView.getListHeight();
					}
				});

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				mScrollY = 0;
				int translationY = 0;
				
				if (mListView.scrollYIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}

				int rawY = mPlaceHolder.getTop()
						- Math.min(
								mCachedVerticalScrollRange
										- mListView.getHeight(), mScrollY);
				
				Log.d("raw Y"," "+rawY);
				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY <= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;
				case STATE_RETURNING:
					translationY =30+ rawY - mMinRawY - mQuickReturnHeight;
					Log.d("Translation y", "ray "+rawY+" Min RawY "+mMinRawY+" mQuick "+mQuickReturnHeight);
					if (translationY > 0) {
						translationY = 0;
						mMinRawY = rawY - mQuickReturnHeight;
					}
					if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}
					if (translationY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}
				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}
	class GetComments extends AsyncTask<Void, Void, Void> {
		List<NameValuePair> paramets;
		String url = "http://192.168.0.108/X/getallcomments.php";

		public GetComments() {
			// this.paramets = params;
			paramets = new ArrayList<NameValuePair>();
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
			paramets.add(new BasicNameValuePair("discussion_id", "1"));
			paramets.add(new BasicNameValuePair("type", "" + 1));
			JSONObject jObject = func.getJSONfromURL(url, paramets);
			commentList = new ArrayList<Comment>();
			Log.d("JSON oBJECT", jObject.toString());
			try {
				JSONArray arra = (JSONArray) jObject.get("comments");
				Log.d("arra", arra.toString());
				for (int i = 0; i < arra.length(); i++) {
					jObject = arra.getJSONObject(i);
					Log.d("jo", jObject.toString());
					commentList.add(new Comment(jObject.getString("name"),
							jObject.getString("comment"), jObject
									.getInt("comment_id"), jObject
									.getInt("nmbr_of_logical"), jObject
									.getInt("nmbr_of_ilogical"), jObject
									.getInt("nmbr_of_reply")));
				}

			} catch (Exception e) {
				Log.e("arrayList", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			mListView = (QuickReturnListView) getListView();
			mListView.addHeaderView(mHeader);
			setListAdapter( new CommentsAdapter(getActivity(), commentList));
			on();
			
			
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
		int like, dislike, reply;

		public int getReply() {
			return reply;
		}

		public void setReply(int reply) {
			this.reply = reply;
		}

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
//			holder.logical.setText(comment.getNmbr_of_logical() + " Logical");
//			holder.illogical.setText(comment.nmbr_of_ilogical + " Illogical");
//			holder.reply.setText(comment.getNmbr_of_comments() + " Reply");
//			holder.logical.setTag(holder);
//			holder.illogical.setTag(holder);
//			holder.reply.setTag(holder);
			// v.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// MyCommentHolder mch = (MyCommentHolder) v.getTag();
			// int i=mch.com_id;
			// Intent intent = new Intent(getActivity(), ReplyComment.class);
			// intent.putExtra("comm_id", i);
			// startActivity(intent);
			// }
			// });
//			holder.logical.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stu
//					mch = (MyCommentHolder) v.getTag();
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					params.add(new BasicNameValuePair("client_id", "1"));
//					params.add(new BasicNameValuePair("comment_id", mch.com_id
//							+ ""));
//					params.add(new BasicNameValuePair("do", "1"));
////					udv = new UpDownVotesAgree(params,
//							"http://192.168.0.108/X/logical.php", 0);
//					udv.execute();

//				}
//			});
//			holder.illogical.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					mch = (MyCommentHolder) v.getTag();
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					params.add(new BasicNameValuePair("client_id", "1"));
//					params.add(new BasicNameValuePair("comment_id", mch.com_id
//							+ ""));
//					params.add(new BasicNameValuePair("do", "0"));
//					udv = new UpDownVotesAgree(params,
//							"http://192.168.0.108/X/logical.php", 1);
//					udv.execute();
//				}
//			});
			// v.setTag(holder);
			// v.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// MyCommentHolder mch = (MyCommentHolder) v.getTag();
			// int i=mch.com_id;
			// Intent intent = new Intent(getActivity(), ReplyComment.class);
			// intent.putExtra("comm_id", i);
			// startActivity(intent);
			// }
			// });
//			holder.reply.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					MyCommentHolder mch = (MyCommentHolder) v.getTag();
//					int i = mch.com_id;
//					Intent intent = new Intent(getActivity(),
//							ReplyFinalTry.class);
//					intent.putExtra("comm_id", i);
//					intent.putExtra("comment", mch.comment.getText().toString());
//					intent.putExtra("name", mch.name.getText().toString());
//					intent.putExtra("logical", mch.logical.getText().toString());
//					intent.putExtra("ilogical", mch.illogical.getText()
//							.toString());
//					intent.putExtra("reply", mch.reply.getText().toString());
//					startActivity(intent);
//				}
//			});
			return v;
		}

		class MyCommentHolder {

			TextView name, comment, logical, illogical, reply;
			int com_id, logicals, illogicals, subcomms;

			MyCommentHolder(View view, int com_id, int uV, int dV, int replies) {
				name = (TextView) view.findViewById(R.id.commentorName);
				comment = (TextView) view.findViewById(R.id.comm);
//				logical = (TextView) view.findViewById(R.id.ivUpvote);
//				illogical = (TextView) view.findViewById(R.id.ivDownvote);
				this.com_id = com_id;
//				reply = (TextView) view.findViewById(R.id.ivReply);
				logicals = uV;
				illogicals = dV;
				subcomms = replies;
			}
		}
	}
}