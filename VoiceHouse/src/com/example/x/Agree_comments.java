package com.example.x;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.x.Agree_Fragment.Comment;
import com.example.x.Agree_Fragment.CommentsAdapter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Agree_comments extends Fragment
{	
	LinearLayout linearlayout;
	ArrayList<Comment> comments;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.agree_fragment_layout, container,false);
		linearlayout = (LinearLayout)view.findViewById(R.id.lp);
		return view;
	}
	class GetComments extends AsyncTask<Void, Void, Void> {
		List<NameValuePair> paramets;
		String url="http://192.168.0.108/X/getallcomments.php";
		public GetComments(List<NameValuePair> params, String url) {
			//this.paramets = params;
			paramets = new ArrayList<NameValuePair>();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONfunction func = new JSONfunction();
			paramets.add(new BasicNameValuePair("discussion_id",  "1"));
			JSONObject jObject = func.getJSONfromURL(url, paramets);
			comments=new ArrayList<Comment>();
			Log.d("JSON oBJECT",jObject.toString());
			try {
				JSONArray arra = (JSONArray) jObject.get("comments");
				Log.d("arra", arra.toString());
				for (int i = 0; i < arra.length(); i++) 
				{
					jObject = arra.getJSONObject(i);
					//SingleCommentView singlevieww = new SingleCommentView(getActivity(), jObject);
					//linearlayout.addView(singlevieww);
					Log.e("adding views","adding");
				}
			} catch (Exception e) {
				Log.e("arrayList", e.toString());
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//list.setAdapter(adapter);
			Log.e("1","1");
			super.onPostExecute(result);
		}
	}
}
