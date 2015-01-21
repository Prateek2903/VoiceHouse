package com.example.x;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class SingleComment extends RelativeLayout {
	public SingleComment(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initVieww();
	}

	LayoutInflater inflater = null;
	TextView name, comment, upvotes, downvotes, reply;
	int uVotes, dVotes, replies, commentId, discId, clientId;
	String Comment, Name, disc;
	ImageView profilePic;
	JSONObject obj;
	Context c;
	String temp;
	
	private void initVieww() {
		// TODO Auto-generated method stub
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.single_comment, this, true);
		name = (TextView) findViewById(R.id.commentorName);
		comment = (TextView) findViewById(R.id.comm);
		upvotes = (TextView) findViewById(R.id.ivUpvote);
		downvotes = (TextView) findViewById(R.id.ivDownvote);
		reply = (TextView) findViewById(R.id.ivReply);
	}
}
