package com.example.x;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

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

public class fragment_a extends Fragment{
	
	
	AsyncTask<Void,Void,Void> getpost;
	LinearLayout lp;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_a, container,false);
		getpost = new GetPost();
		getpost.execute();
		lp = (LinearLayout)v.findViewById(R.id.linearlayout);
		return v;
	}
		
	public class GetPost extends AsyncTask<Void, Void, Void> {

		List<NameValuePair> paramets;
		String url = "http://192.168.0.108/X/getallcomments.php";

		public GetPost() {
			this.paramets = new ArrayList<NameValuePair>();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONfunction func = new JSONfunction();
			JSONObject jObject = func.getJSONfromURL(url, paramets);
			try {
				JSONArray array = (JSONArray) jObject.get("comments");
				for (int i = 0; i < array.length(); i++) {
					jObject = array.getJSONObject(i);
					SingleComment single = new SingleComment(getActivity());
					lp.addView(single);
				}

			} catch (Exception e) {
				Log.e("arrayList", e.toString());
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}


}


