package com.example.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class profileView extends Activity {

	 ListView lv;
	
	JSONObject jsonobject;
	InputStream is;
	String result, name;
	String[] choice_name;
	JSONObject jArray;
	JSONArray jsonarray;
	public int client_id;
	int nmbr_of_ilogical, nmbr_of_logical, nmbr_of_comments;
	TextView tvlogicals, tvillogicals,tvcomments,tvrating,tvname;
	double rating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profileview);
		tvillogicals = (TextView) findViewById(R.id.textView3);
		tvlogicals = (TextView) findViewById(R.id.textView2);
		tvcomments=(TextView) findViewById(R.id.textView4);
		tvrating=(TextView) findViewById(R.id.textView5);
		tvname=(TextView) findViewById(R.id.textView1);
		lv = (ListView) findViewById(R.id.listView1);
		client_id = 3;

		AsyncTask<String, String, String> a1 = new DownloadJSON();
		a1.execute();

	}
String rating(double i)
{
	double rate=i;
	String ratng="";
	if(i<1&&i>0)
	{
		ratng="Reticent";
	}
	if(i<2&&i>1)
	{
		ratng="Ambivert";
	}
	if(i<3&&i>2 )
	{
		ratng="Eloquent";
	}
	
	return ratng;
	
}
	class DownloadJSON extends AsyncTask<String, String, String> {
		private boolean runningone = true;

		@Override
		protected void onPostExecute(String args) {
			Log.d("Response:", jsonarray.toString());
			lv.setAdapter(new ArrayAdapter<String>(profileView.this,
					R.layout.list_item, R.id.textView1, choice_name));
			tvlogicals.setText("Up votes:" + nmbr_of_logical);
			tvillogicals.setText("Down votes:" + nmbr_of_ilogical);
			tvcomments.setText("Comments:"+nmbr_of_comments);
			tvrating.setText(""+rating(rating));
		tvname.setText(""+name);
		}

		@Override
		protected String doInBackground(String... params) {
			JSONParse jsonParser = new JSONParse(); // userchoices[spinner.getSelectedItemPosition()]
			// Building Parameters
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("client_id", "3"));
			jsonobject = jsonParser
					.makeHttpRequest(
							"http://192.168.0.108/X/fetchinterest.php", "POST",
							params1);
			try {
				// Locate the NodeList name

				jsonarray = jsonobject.getJSONArray("discussion_table");
			JSONObject jsonobject1 = jsonarray.getJSONObject(0);
					JSONArray jsonarr=jsonobject1.getJSONArray("choice_name");
					choice_name = new String[jsonarr.length()];
					for (int i = 0; i <= jsonarr.length()-1; i++) 
					{
					choice_name[i]=jsonarr.getString(i);
						System.out.println("choice:"+choice_name[i]);
					}
					name = jsonobject1.getString("name");
					nmbr_of_ilogical = jsonobject1.getInt("nmbr_of_ilogical");
					nmbr_of_logical = jsonobject1.getInt("nmbr_of_logical");
					rating = jsonobject1.getDouble("rating");
				} catch (Exception e) {
				// Log.d("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;

		}

	}
}
