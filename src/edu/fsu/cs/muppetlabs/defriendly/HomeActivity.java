package edu.fsu.cs.muppetlabs.defriendly;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.android.Facebook;
import com.facebook.model.*;
import com.facebook.widget.FriendPickerFragment;


public class HomeActivity extends Activity {
	ImageView startButton,
				buttonPlaceholder;
	
	static String myToken;
	String preferences;
	static String theToken;
	GraphUser[] myFriends = new GraphUser[5];
	
	int friendCounter;
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_home);
	    
	    buttonPlaceholder = (ImageView) findViewById(R.id.buttonPlaceholder);
	    startButton = (ImageView) findViewById(R.id.startButton);
	    startButton.setOnTouchListener(new OnTouchListener(){

	    	@Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                	case MotionEvent.ACTION_DOWN :
                		startButton.setImageResource(R.drawable.defriendly_start_button_pressed);
                		break;
                	case MotionEvent.ACTION_UP :
                		startButton.setImageResource(R.drawable.defriendly_start_button);
                		break;
                }
                return false;
            }

        });


	    // start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {
	        	myToken = session.getAccessToken();
	        	makeFriendsRequest();
	        	

	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	            	Typeface namesFont = Typeface.createFromAsset(getAssets(), "LANENAR_.ttf");
	            	
	            	buttonPlaceholder.setVisibility(View.INVISIBLE);
	            	startButton.setVisibility(View.VISIBLE);
	            	
	                TextView welcome = (TextView) findViewById(R.id.welcome);
	                welcome.setText(user.getFirstName() + ", you have");
	                TextView friendCountContinued = (TextView) findViewById(R.id.friendCountContinued);
	                friendCountContinued.setText(String.valueOf("friends."));
	                TextView welcomeContinued = (TextView) findViewById(R.id.welcomeContinued);
	                welcomeContinued.setText("Defriendly can help you with that.");
	                //Toast.makeText(getApplicationContext(), myFriends.length, Toast.LENGTH_LONG).show();
	                
	                TextView friendCount = (TextView) findViewById(R.id.friendCount);
	                friendCount.setText(String.valueOf(friendCounter));
	                //Toast.makeText(getApplicationContext(), String.valueOf(friendCounter), Toast.LENGTH_LONG).show();
	                
	        		friendCount.setTypeface(namesFont);
	                
	                Log.i("Hello", "Working");
	              }	// end if
	            }	// end on completedh
	          }); // end execute on request;
	        } // end session if opened
	      }// end void call
	    }); // end open active session
	    
	  //  SharedPreferences message = getSharedPreferences(preferences, 0);
		//theToken = message.getString("message", myToken);
	//	SharedPreferences.Editor editor = message.edit();
	//	editor.putString("message", theToken);
	//	editor.commit();
		//setting url with token.
		
	}
	
	private void makeFriendsRequest()
	{
		Request myFriendsRequest =  Request.newMyFriendsRequest(Session.getActiveSession(), new Request.GraphUserListCallback(){
			
			@Override
			public void onCompleted(List<GraphUser> users, Response response) {
				// TODO Auto-generated method stub
				if (response.getError() == null) {
	                // Handle response
	            }
				
				myFriends = users.toArray(new GraphUser[users.size()]);
				
				friendCounter = myFriends.length;
				
				TextView friendCount = (TextView) findViewById(R.id.friendCount);
				friendCount.setText(String.valueOf(friendCounter));
			}
		});
		
		//friendCounter = myFriends.length;
		//Toast.makeText(getApplicationContext(), myFriends.length, Toast.LENGTH_LONG).show();
		
		Bundle requestParams = myFriendsRequest.getParameters();
	    requestParams.putString("fields", "name,first_name,last_name,id");
	    myFriendsRequest.setParameters(requestParams);
	    myFriendsRequest.executeAsync();
		
	}
	
	
	public void startOther(View v)
	{
		Intent i = new Intent (HomeActivity.this, GetUsers.class);
		startActivity(i);
	}
	
	
	
	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }
	
	

}
