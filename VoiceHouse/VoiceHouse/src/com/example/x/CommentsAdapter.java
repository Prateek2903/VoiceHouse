package com.example.x;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter {

	Context context;
	Comment comment;
	ArrayList<Comment> commentsList;
	JSONObject jsonObject = new JSONObject();
	int like, dislike;
	MyCommentHolder holder;

	public CommentsAdapter(Context c, JSONArray al) {
		// TODO Auto-generated constructor stub
		context = c;
		commentsList = new ArrayList<Comment>();
		for (int i = 0; i < al.length(); i++) {
			try {
				jsonObject = (JSONObject) al.get(i);
				commentsList.add(new Comment("Sandeep", jsonObject
						.getString("comment"), jsonObject.getInt("comment_id"),
						jsonObject.getInt("nmbr_of_logical"), jsonObject
								.getInt("nmbr_of_ilogical"), 1));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commentsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return commentsList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.single_comment, parent, false);
		comment = commentsList.get(position);
		holder = new MyCommentHolder(v, comment.getComment_id(),
				comment.getNmbr_of_logical(), comment.getNmbr_of_ilogical(),
				comment.getNmbr_of_comments());
		holder.name.setText(comment.getName());
		holder.comment.setText(comment.getComment());
		holder.logical.setText(comment.getNmbr_of_logical() + "");
		holder.illogical.setText(comment.nmbr_of_ilogical + "");
		holder.subcomm.setText(comment.getNmbr_of_comments() + "");
		
		/*
		holder.uVote.setTag(holder);
		holder.dVote.setTag(holder);
		holder.reply.setTag(holder);
		v.setTag(holder);

		holder.uVote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCommentHolder mch = (MyCommentHolder) v.getTag();
				if (v.getAlpha() == 0.5) {
					v.setAlpha(1);
					int likes = Integer.parseInt(mch.logical.getText().toString())+ 1;
					mch.logical.setText(likes + "");
				} else {
					v.setAlpha((float) 0.5);
					int likes = Integer.parseInt(mch.logical.getText().toString())- 1;
					mch.logical.setText(likes + "");
				}
			}
		});

		holder.dVote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCommentHolder mch = (MyCommentHolder) v.getTag();
				if (v.getAlpha() == 0.5) {
					v.setAlpha(1);
					int unlikes = Integer.parseInt(mch.illogical.getText().toString()) + 1;
					mch.illogical.setText(unlikes + "");

				} else {
					v.setAlpha((float) 0.5);
					int unlikes =Integer.parseInt(mch.illogical.getText().toString())- 1;
					mch.illogical.setText(unlikes + "");
				}
			}
		});

		holder.reply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCommentHolder mch = (MyCommentHolder) v.getTag();
				Intent i = new Intent(context, ReplyComment.class);
				i.putExtra("comm_id", mch.com_id);
				context.startActivity(i);
			}
		});
		*/

		return v;
	}

	class MyCommentHolder {

		TextView name, comment, logical, illogical, subcomm;
		//ImageView dp, uVote, dVote, reply;
		int com_id, logicals, illogicals; //, subcomms;

		MyCommentHolder(View view, int com_id, int uV, int dV, int replies) {
			name = (TextView) view.findViewById(R.id.commentorName);
			comment = (TextView) view.findViewById(R.id.comm);
			logical = (TextView) view.findViewById(R.id.noUpvotes);
			illogical = (TextView) view.findViewById(R.id.noDownvotes);
		//	subcomm = (TextView) view.findViewById(R.id.noComms);
			this.com_id = com_id;
		/*	uVote = (ImageView) view.findViewById(R.id.ivUpvote);
			dVote = (ImageView) view.findViewById(R.id.ivDownvote);
			reply = (ImageView) view.findViewById(R.id.ivComment);
			*/
			logicals = uV;
			illogicals = dV;
		//	subcomms = replies;
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
}
