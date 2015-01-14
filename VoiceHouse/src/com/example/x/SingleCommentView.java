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

public class SingleCommentView extends RelativeLayout {

	LayoutInflater inflater = null;
	TextView name, comment, upvotes, downvotes, reply;
	int uVotes, dVotes, replies, commentId, discId, clientId;
	String Comment, Name, disc;
	ImageView profilePic;
	JSONObject obj;
	Context c;
	
	String temp;
	public SingleCommentView(Context context, Object object, String disc) {
		super(context);
		// TODO Auto-generated constructor stub
		c = context;
		obj = (JSONObject) object;
		this.disc = disc;
		try {
			uVotes = obj.getInt("nmbr_of_logical");
			dVotes = obj.getInt("nmbr_of_ilogical");
			replies = obj.getInt("nmbr_of_reply");
			Comment = obj.getString("comment");
			Name = obj.getString("name");
			commentId = obj.getInt("comment_id");
			discId = obj.getInt("discussion_id");
			clientId = obj.getInt("client_id");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.single_comment, this, true);
		name = (TextView) findViewById(R.id.commentorName);
		comment = (TextView) findViewById(R.id.comm);
		upvotes = (TextView) findViewById(R.id.ivUpvote);
		downvotes = (TextView) findViewById(R.id.ivDownvote);
		reply = (TextView) findViewById(R.id.ivReply);
		name.setText(Name);
		if (Comment.length() > 200) {
			String s = Comment.substring(0, 199);
			comment.setText(s + "...");
		}else{
			comment.setText(Comment);
		}
		upvotes.setText(uVotes + " Logical");
		downvotes.setText(dVotes + " Illogical");
		reply.setText(replies + " Reply");
		
	}

	

}
