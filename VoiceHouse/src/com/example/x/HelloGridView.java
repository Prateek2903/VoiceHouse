package com.example.x;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

public class HelloGridView extends Activity {
	JSONfunction func = null;
	JSONObject jObject = null;
	GridView gv;
	Context context;
	public static MenuItem vi;
	Menu mymenu;
	ImageAdapter helloview;
	SharedPreferences client_id;
	SharedPreferences choices;
	SharedPreferences.Editor editor;
	ProgressDialog pDialog;
	public static String[] ctgrynamelist = { "Business", "Entertainment",
			"Finance", "Food", "Politics", "Sports", "Technology", "Travel" };
	public static int[] ctgryImages = { R.drawable.business,
			R.drawable.entertainment, R.drawable.finance, R.drawable.food,
			R.drawable.politics, R.drawable.sports, R.drawable.technology,
			R.drawable.travel };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		client_id = getSharedPreferences("client_id", Context.MODE_PRIVATE);
		gv = (GridView) findViewById(R.id.gridview);
		helloview = new ImageAdapter(this, ctgrynamelist, ctgryImages,HelloGridView.this);
		gv.setAdapter(helloview);
		choices = getSharedPreferences("choices", Context.MODE_PRIVATE);
		editor = choices.edit();
		getActionBar().setTitle("Select max 4");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		getMenuInflater().inflate(R.layout.menu, menu);
		helloview.mymenu = menu;
		vi = (MenuItem) menu.findItem(R.id.next);
		//
		helloview.vi = (MenuItem) vi;
		if (helloview.count > 0) 
			
		{
			Log.d("counnnndqqqqqqddtt", helloview.count+"");
			(vi).setVisible(true);
		} 
		else 
		{
			(vi).setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);

	}

	public void menu() {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.next) {
			if (helloview.choices != null) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("interest_id",
						helloview.choice));
				params.add(new BasicNameValuePair("client_id",client_id.getString("client_id", "1")));
				SendInterest si = new SendInterest(params);
				si.execute();
			}
		}
		return true;
	}

	class SendInterest extends AsyncTask<Void, Void, Void> {
		List<NameValuePair> paramets;
		String url = "http://192.168.0.108/X/saveinterest.php";
		public SendInterest(List<NameValuePair> params) {
			this.paramets = params;
		}
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(HelloGridView.this);
			pDialog.setMessage("Saving the Data,Please Wait..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			func = new JSONfunction();
			Log.d("choices", " "+paramets);
			jObject = func.getJSONfromURL(url, paramets);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			editor.putString("choices", helloview.choices);
			editor.commit();
			try {
				if(jObject.getString("success").equals("1")){
					Intent i = new Intent(HelloGridView.this,MainActivity.class);
					startActivity(i);
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}