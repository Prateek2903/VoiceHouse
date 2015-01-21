package com.example.x;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReplyCommentQuickReturn extends Activity {

	int comm_id;
	CommentsAdapter adapter;
	List<NameValuePair> params;
	GetReply goc;
	QuickReturnListView list;
	LinearLayout ll;
	private View mPlaceHolder;
	private int mCachedVerticalScrollRange;
	String url="http://voicehouse.in/php/getreply.php";
	ArrayList<Comment> commentList;
	MyCommentHolder holder;
	EditText et;
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private static final int STATE_EXPANDED = 3;
	private int mState = STATE_ONSCREEN;
	int mScrollY;
	private int mMinRawY = 0;
	private int rawY;
	private boolean noAnimation = false;
	JSONObject jObject;
	private int mQuickReturnHeight;
	private RelativeLayout mQuickReturnView;
	TextView name,comment,logical, ilogical, reply;
	private TranslateAnimation anim;
	View mHeader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		comm_id = getIntent().getIntExtra("comm_id", 0);
		setContentView(R.layout.quick_return);
		LayoutInflater inflater = getLayoutInflater();
		mHeader = inflater.inflate(R.layout.ladder, null);
		mPlaceHolder = mHeader.findViewById(R.id.placeholder);
		mQuickReturnView = (RelativeLayout) findViewById(R.id.sticky);
		name=(TextView) findViewById(R.id.commentorName);
		comment=(TextView) findViewById(R.id.comm);
		logical=(TextView) findViewById(R.id.ivUpvote);
		ilogical=(TextView) findViewById(R.id.ivDownvote);
		reply=(TextView) findViewById(R.id.ivReply);
		
		Intent i = getIntent();
		list = (QuickReturnListView) findViewById(R.id.quickList);
		params = new ArrayList<NameValuePair>();
		name.setText(i.getStringExtra("name"));
		comment.setText(i.getStringExtra("comment"));
		logical.setText(i.getStringExtra("logical"));
		ilogical.setText(i.getStringExtra("ilogical"));
		reply.setText(i.getStringExtra("reply"));
		
		
		params.add(new BasicNameValuePair("comment_id", comm_id+ ""));
		goc = new GetReply(params, url);
		goc.execute();
	}
	
	public void setList(){
		list.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mQuickReturnHeight = mQuickReturnView.getHeight();
						list.computeScrollY();
						mCachedVerticalScrollRange = list.getListHeight();
					}
				});
		list.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mScrollY = 0;
				int translationY = 0;

				if (list.scrollYIsComputed()) {
					mScrollY = list.getComputedScrollY();
				}

				rawY = mPlaceHolder.getTop()
						- Math.min(
								mCachedVerticalScrollRange
										- list.getHeight(), mScrollY);

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
						System.out.println("test3");
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:

					if (translationY > 0) {
						translationY = 0;
						mMinRawY = rawY - mQuickReturnHeight;
					}

					else if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}

					else if (translationY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;

					} else if (mQuickReturnView.getTranslationY() != 0
							&& !noAnimation) {
						noAnimation = true;
						anim = new TranslateAnimation(0, 0,
								-mQuickReturnHeight, 0);
						anim.setFillAfter(true);
						anim.setDuration(250);
						mQuickReturnView.startAnimation(anim);
						anim.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								noAnimation = false;
								mMinRawY = rawY;
								mState = STATE_EXPANDED;
							}
						});
					}
					break;

				case STATE_EXPANDED:
					if (rawY < mMinRawY - 2 && !noAnimation) {
						noAnimation = true;
						anim = new TranslateAnimation(0, 0, 0,
								-mQuickReturnHeight);
						anim.setFillAfter(true);
						anim.setDuration(250);
						anim.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								noAnimation = false;
								mState = STATE_OFFSCREEN;
							}
						});
						mQuickReturnView.startAnimation(anim);
					} else if (translationY > 0) {
						translationY = 0;
						mMinRawY = rawY - mQuickReturnHeight;
					}

					else if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}

					else if (translationY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					} else {
						mMinRawY = rawY;
					}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	   	getMenuInflater().inflate(R.menu.edit_text,menu);
	   	View view = menu.findItem(R.id.action_comment).getActionView();
	   	View v = view.findViewById(R.id.post);
	   	v.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
	// TODO Auto-generated method stub
	Log.d("Pressed","Post Button Clicked");
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
			jObject= func.getJSONfromURL(url, paramets);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			try{
			if(jObject.getInt("success")==1){
				commentList.add(new Comment("Sandeep", et.getText().toString(),0 ));
				et.setText("");
				
				adapter.notifyDataSetChanged();
				setList();
			}
			}catch(Exception e){
				
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
			adapter = new CommentsAdapter(ReplyCommentQuickReturn.this, commentList);
			list.setAdapter(adapter);
			setList();
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
			//adapter = new CommentsAdapter("HARISH", "DO");
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
//		MyCommentHolder holder;

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
			comment = commList.get(position);
			holder = new MyCommentHolder(v, comm_id, 0, 0, 0);
			holder.comment.setText(comment.getComment());
			holder.logical.setVisibility(View.GONE);
			holder.illogical.setVisibility(View.GONE);
			holder.reply.setVisibility(View.GONE);
			return v;
		}
	}
//	
	class MyCommentHolder {

		TextView name, comment, logical, illogical, subcomm,reply;
		int com_id, logicals, illogicals;

		MyCommentHolder(View view, int com_id, int uV, int dV, int replies) {
			name = (TextView) view.findViewById(R.id.commentorName);
			comment = (TextView) view.findViewById(R.id.comm);
			logical = (TextView) view.findViewById(R.id.ivUpvote);
			illogical = (TextView) view.findViewById(R.id.ivDownvote);
			this.com_id = com_id;
			logicals = uV;
			illogicals = dV;
			reply=(TextView) view.findViewById(R.id.ivReply);
		}
	}
}