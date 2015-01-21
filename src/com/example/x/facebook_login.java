package com.example.x;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class facebook_login extends Activity {
 private Session.StatusCallback sessionStatusCallback;
 private Session currentSession;
GraphUser u;
Request request ;
 private ImageButton login;
 String name,id,email;
 private Button publishButton;
JSONObject jsonobject;
private ProgressDialog pDialog;
AsyncTask<String, Void,String> a1;
SharedPreferences client_id;
SharedPreferences.Editor editor;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.login);
  a1 = new upload();
  
  client_id = getSharedPreferences("client_id", Context.MODE_PRIVATE);
  editor = client_id.edit();
  // create instace for sessionStatusCallback
  sessionStatusCallback = new Session.StatusCallback() {

   @Override
   public void call(Session session, SessionState state,
     Exception exception) {
    onSessionStateChange(session, state, exception);

   }
  };

  // login button
  login = (ImageButton) findViewById(R.id.authButton);
  login.setOnClickListener(new OnClickListener() {

   @Override
   public void onClick(View v) {
    
    connectToFB();
   
   }
  });
//  publishButton=(Button) findViewById(R.id.publishButton);
//publishButton.setOnClickListener(new OnClickListener() {
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		publishStory();
//	}
//});
  
  

 }

 /**
  * Connects the user to facebook
  */
 public void connectToFB() {

  List<String> permissions = new ArrayList<String>();
  permissions.add("publish_stream");
  permissions.add("email");

  currentSession = new Session.Builder(this).build();
  currentSession.addCallback(sessionStatusCallback);
  
  Session.OpenRequest openRequest = new Session.OpenRequest(
    facebook_login.this);
  openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
  openRequest.setRequestCode(Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE);
  openRequest.setPermissions(permissions);
  currentSession.openForPublish(openRequest);
  
 }

 /**
  * this method is used by the facebook API
  */
 @Override
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  if (currentSession != null) {
   currentSession.onActivityResult(this, requestCode, resultCode, data);
   
  }
 }

 /**
  * manages the session state change. This method is called after the
  * <code>connectToFB</code> method.
  * 
  * @param session
  * @param state
  * @param exception
  */
 private void onSessionStateChange(Session session, SessionState state,
   Exception exception) {
  if (session != currentSession) {
   return;
  }

  if (state.isOpened()) {
	  makeMeRequest(currentSession);
	  
	//a1.execute();
   // Log in just happened.
  // Toast.makeText(getApplicationContext(), "session opened",
  //   Toast.LENGTH_SHORT).show();
  } else if (state.isClosed()) {
   // Log out just happened. Update the UI.
//   Toast.makeText(getApplicationContext(), "session closed",
//     Toast.LENGTH_SHORT).show();
  }
 }

 /**
  * Publishes story on the logged user's wall
  */
// public void publishStory() {
//  Bundle params = new Bundle();
//  params.putString("name", "test-name");
//  params.putString("message", "test-caption");
//  params.putString("description", "test-description");
//  params.putString("link", "http://curious-blog.blogspot.com");
//  Session.setActiveSession(currentSession);
//
//  RequestsDialogBuilder feedDialog = (new WebDialog.RequestsDialogBuilder(this));
//  feedDialog.setMessage("Get a life dude..");
//  feedDialog.setTo(u.getId());
//   feedDialog.setOnCompleteListener(new OnCompleteListener() {
//
//     @Override
//     public void onComplete(Bundle values,
//       FacebookException error) {
//      if (error == null) {
//       // When the story is posted, echo the success
//       // and the post Id.
//       final String postId = values.getString("post_id");
//       if (postId != null) {
//        // do some stuff
//       } else {
//        // User clicked the Cancel button
//        Toast.makeText(getApplicationContext(),
//          "Publish cancelled", Toast.LENGTH_SHORT)
//          .show();
//       }
//      } else if (error instanceof FacebookOperationCanceledException) {
//       // User clicked the "x" button
//       Toast.makeText(getApplicationContext(),
//         "Publish cancelled", Toast.LENGTH_SHORT)
//         .show();
//      } else {
//       // Generic, ex: network error
//       Toast.makeText(getApplicationContext(),
//         "Error posting story", Toast.LENGTH_SHORT)
//         .show();
//      }
//     }
//
//    }).build().show();
 

// }
 private void makeMeRequest(final Session session) {
	// Make an API call to get user data and define a
	// new callback to handle the response.
	request = Request.newMeRequest(session,
	new Request.GraphUserCallback() {
	@Override
	public void onCompleted(GraphUser user, Response response) {
	// If the response is successful
	if (session == currentSession) {
	if (user != null) {
	Session.setActiveSession(currentSession);
	u=user;
	name=u.getName();
	id=u.getId();
	email=u.getProperty("email").toString();
	Toast.makeText(facebook_login.this, "Name:"+name+"id:"+id, Toast.LENGTH_LONG).show();
	a1.execute();
	}
	}
	if (response.getError() != null) {
	// Handle errors, will do so later.
	}
	}
	});
	request.executeAsync();
	
}
	
	private class upload extends AsyncTask<String, Void,String>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(facebook_login.this);
			pDialog.setMessage("Saving the Data,Please Wait..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			//Log.d("Create Response", "json");
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			if(u!=null)
			{
			name=u.getName();
			id=u.getId();
			email=u.getProperty("email").toString();
			}else
			{
				try {
					wait(2000);
					name=u.getName();
					id=u.getId();
					email=u.getProperty("email").toString();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JSONParse jsonParser = new JSONParse();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name",name));
			params.add(new BasicNameValuePair("client_id",id));
			params.add(new BasicNameValuePair("email",email));
			jsonobject = jsonParser.makeHttpRequest("http://192.168.0.108/X/clientdata.php", "POST", params);
			String status="Success";
			return  status;
		}
		protected void onPostExecute(String result) {
			// dismiss the dialog once done
				pDialog.dismiss();
				editor.putString("client_id", id);
				editor.commit();
                //pageeditor.putString("page", "3");
                //pageeditor.commit();
				startActivity(new Intent(facebook_login.this,HelloGridView.class));
				//finish();
		}	
	}
	
}
//name=u.getName();
//id=u.getId();
//email=u.getProperty("email").toString();