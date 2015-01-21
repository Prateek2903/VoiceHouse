package com.example.x;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.widget.ProfilePictureView;

@SuppressWarnings("deprecation")
public class LoginFacebook extends Activity //implements LocationListener
{

	// Your Facebook APP ID
	private static String APP_ID = "397229567106750"; 
		// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	ImageView profileimg;
	// private static String url_register="http://192.168.122.1/Sangha/reg.php";
	private static final String TAG_SUCCESS = "success";
	private ProgressDialog pDialog;
	Bitmap bmp = null;
	JSONObject json;
	double lat=0, lon=0;
	Context context;
	// JSONParser jsonParser = new JSONParser();
	String id;
	// Buttons
	ImageButton authbtn;
	ProfilePictureView pf;
	LocationListener locationListener;
	LocationManager lm;
	List<Address> user;
	JSONObject jsonobject;
	AsyncTask<String, Void, String> a1;
	SharedPreferences page;
	SharedPreferences.Editor pageeditor;
	 String loc;
	 SharedPreferences clientid;
	 SharedPreferences.Editor clientideditor;
	
	String name,latitude,longitude,email;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		authbtn = (ImageButton) findViewById(R.id.authButton);
		page = getSharedPreferences("page", Context.MODE_PRIVATE);
		pageeditor = page.edit();
		getActionBar().hide();
		clientid = getSharedPreferences("clientid", Context.MODE_PRIVATE);
		clientideditor =clientid.edit();
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255,127,8,149)));
		a1 = new upload();
		/**
		 * Login button Click event
		 * */
		authbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Image Button", "button Clicked");
				loginToFacebook();
			}
		});

	}
	

	public void loginToFacebook() {

		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);

			Log.d("FB Sessions", "" + facebook.isSessionValid());
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new DialogListener() {

						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@Override
						public void onComplete(Bundle values) {
							
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

							getProfileInformation();
							
							 
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});
		} else {
			getProfileInformation();
			
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	@SuppressWarnings("deprecation")
	public void getProfileInformation() {

		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					// Facebook Profile JSON data
					JSONObject profile = new JSONObject(json);

					// getting name of the user
					name = profile.getString("name");

					// getting email of the user
					email = profile.getString("email");
					id = profile.getString("id");
					clientideditor.putString("clientid", id);
					clientideditor.commit();
						
					Log.d("message facebook" , name+" "+email+" "+id);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							a1.execute();
							
							//Toast.makeText(LoginFacebook.this,"Name:"+name+"  Email:"+email+"  Id:"+id, Toast.LENGTH_LONG).show();							
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onIOException(IOException e, Object state) {
			}
			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}
			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}
			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}
	
	private class upload extends AsyncTask<String, Void,String>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginFacebook.this);
			pDialog.setMessage("Saving the Data,Please Wait..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			//Log.d("Create Response", "json");
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			JSONParse jsonParser = new JSONParse();
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name",name));
			params.add(new BasicNameValuePair("client_id",id));
			params.add(new BasicNameValuePair("email",email));
			jsonobject = jsonParser.makeHttpRequest("http://voicehouse.in/php/clientdata.php", "POST", params);
			String status="Success";
			return  status;
		}
		protected void onPostExecute(String result) {
			// dismiss the dialog once done
				pDialog.dismiss();
				pageeditor.putString("page", "3");
				pageeditor.commit();
				startActivity(new Intent(LoginFacebook.this,HelloGridView.class));
				overridePendingTransition(R.anim.slidetop,R.anim.slidebottom);
			
				//overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
				finish();
		}	
	}
}