package edu.fsu.cs.muppetlabs.defriendly;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnTouchListener;

import com.facebook.*;
import com.facebook.android.Facebook;
import com.facebook.model.*;
import com.facebook.widget.FriendPickerFragment;


public class GetUsers extends Activity {
	TextView firstName0,
		firstName1,
		firstName2,
		firstName3,
		lastName0,
		lastName1,
		lastName2,
		lastName3,
		firstNameHeading,
		lastNameHeading,
		selectedFirstName,
		selectedLastName,
		incorrectCountView,
		correctCountView;

	boolean firstNameSelected,
		lastNameSelected;
	
	ImageView nextButton,
			  nextButton_disabled;
	
	static String myToken;
	String preferences;
	static String theToken;
	GraphUser[] myFriends;
	int[] friends;
	TextView[] firstT =  new TextView[4];
	TextView[] lastT = new TextView[4];
	int[] wrongChosenNumbers = new int[5];
	int players = 0;
	int lastChosen;
	ImageView user_picture;
	String first, last;
	int wrongFriendsCount, rightFriendsCount, numFriendsSeen, rightFriendsCountInRow , counterInRow;
	String id;
	Bitmap[] wrongPeoplePictures = new Bitmap[5];
	String[] names = new String[5];
	 Bitmap mIcon1 = null;
	
	private TextView textViewResults;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.userquiz);
	    
	    nextButton_disabled = (ImageView) findViewById(R.id.nextButton_disabled);
	    nextButton = (ImageView) findViewById(R.id.nextButton);
	    nextButton.setOnTouchListener(new OnTouchListener(){

	    	@Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                	case MotionEvent.ACTION_DOWN :
                		nextButton.setImageResource(R.drawable.defriendly_next_button_pressed);
                		break;
                	case MotionEvent.ACTION_UP :
                		nextButton.setImageResource(R.drawable.defriendly_next_button);
                		break;
                }
                return false;
            }

        });
	    
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    firstName0 = (TextView) findViewById(R.id.firstName0);
	    firstName1 = (TextView) findViewById(R.id.firstName1);
		firstName2 = (TextView) findViewById(R.id.firstName2);
		firstName3 = (TextView) findViewById(R.id.firstName3);
		lastName0 = (TextView) findViewById(R.id.lastName0);
		lastName1 = (TextView) findViewById(R.id.lastName1);
		lastName2 = (TextView) findViewById(R.id.lastName2);
		lastName3 = (TextView) findViewById(R.id.lastName3);
		Typeface namesFont = Typeface.createFromAsset(getAssets(), "LANENAR_.ttf");
		firstName0.setTypeface(namesFont);
	    firstName1.setTypeface(namesFont);
		firstName2.setTypeface(namesFont);
		firstName3.setTypeface(namesFont);
		lastName0.setTypeface(namesFont);
		lastName1.setTypeface(namesFont);
		lastName2.setTypeface(namesFont);
		lastName3.setTypeface(namesFont);
		
		firstNameHeading = (TextView) findViewById(R.id.firstNameHeading);
		lastNameHeading = (TextView) findViewById(R.id.lastNameHeading);
		Typeface nameHeadingFont = Typeface.createFromAsset(getAssets(), "UbuntuTitling-Bold.ttf");
		firstNameHeading.setTypeface(nameHeadingFont);
		lastNameHeading.setTypeface(nameHeadingFont);
		
		selectedFirstName = (TextView) findViewById(R.id.selectedFirstName);
		selectedLastName = (TextView) findViewById(R.id.selectedLastName);
		
		firstNameSelected = false;
		lastNameSelected = false;
		
		incorrectCountView = (TextView) findViewById(R.id.wrongCounterNumer);
		correctCountView = (TextView) findViewById(R.id.rightCounter);
	    
		firstT [0] = firstName0;
		firstT [1] = firstName1;
		firstT [2] = firstName2;
		firstT [3] = firstName3;
		
		lastT [0] = lastName0;
		lastT [1] = lastName1;
		lastT [2] = lastName2;
		lastT [3] = lastName3;
	    
		//initializing counters;
		wrongFriendsCount = 0;
		rightFriendsCount = 0;
		rightFriendsCountInRow = 0;
		numFriendsSeen = 0;
		counterInRow = 3;
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
		               // TextView welcome = (TextView) findViewById(R.id.welcome);
		               // welcome.setText("Hello " + user.getName() + "!");
		                Log.i("Hello", "Working");
		              }	// end if
		            }	// end on completed
		          }); // end execute on request;
		        } // end session if opened
		      }// end void call
		    }); // end open active session
	    
	    //doBatchRequest();
	    
	}
	 	
	public void getPictureAndInfo(View v)
	{
		authenticate();
		RefreshViews();
		//String first, last;
		String fakefirst[] = new String[3];
		String fakelast[] = new String[3];
		int fakeChosen;
		int position = players;
		int[]copy = new int[position];
		
		players++;
		if(players == myFriends.length){
			players = 0;
			position = players;
			players++;
		}
		
		if(position > 0){
			copy = (int[])friends.clone();
		}
		
		friends = new int[players];
		
		for(int i = 0; i < position; i++){
			friends[i] = copy[i];
		}
		
		Random generator = new Random();
		//boolean duplicate = false;
		lastChosen = generator.nextInt(myFriends.length);
		while(isDuplicate(lastChosen)){
			lastChosen = generator.nextInt(myFriends.length);
		}
		friends[position] = lastChosen;
		
	
		//get all the info
		id = myFriends[lastChosen].getId().toString();
		first = myFriends[lastChosen].getFirstName().toString();
		last = myFriends[lastChosen].getLastName().toString();
		
		
		for(int i = 0; i < 3; i++){
			fakeChosen = generator.nextInt(myFriends.length);
			fakefirst[i] = myFriends[fakeChosen].getFirstName().toString();
			fakelast[i] = myFriends[fakeChosen].getLastName().toString();
			Log.i("fakes", fakefirst[i]+" "+fakelast[i]);
		}
		int rand = generator.nextInt(4);
		int j = 0;
		for(int i = 0; i < 3; i++){
			 j = (rand + i) % 4;
			firstT[j].setText(fakefirst[i]);
		}
		firstT[(j+1)%4].setText(first);
		rand = generator.nextInt(3);
		for(int i = 0; i < 3; i++){
			j = (rand + i) % 4;
			lastT[j].setText(fakelast[i]);
		}
		lastT[(j+1)%4].setText(last);
		
		 user_picture=(ImageView)findViewById(R.id.profilePicture);
		 URL img_value = null;
		 try {
			img_value = new URL("http://graph.facebook.com/"+id+"/picture?type=large");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 user_picture.setImageBitmap(mIcon1);
		
		firstNameSelected = false;
		lastNameSelected = false;
		 
	}
	
	public boolean isDuplicate(int num){
		if(players > 0){
			for(int i = 0; i > players; i++){
				if(friends[i] == lastChosen){
					return true;
				}
			}
		}
		return false;
		
	}
	
	private void makeFriendsRequest()
	{
		final View  v =  null;
		Request myFriendsRequest =  Request.newMyFriendsRequest(Session.getActiveSession(), new Request.GraphUserListCallback(){
			
			@Override
			public void onCompleted(List<GraphUser> users, Response response) {
				// TODO Auto-generated method stub
				if (response.getError() == null) {
	                // Handle response
	            }
				
				myFriends = users.toArray(new GraphUser[users.size()]);
				getPictureAndInfo(v);
				
			}
		});
		
		Bundle requestParams = myFriendsRequest.getParameters();
	    requestParams.putString("fields", "name,first_name,last_name,id");
	    myFriendsRequest.setParameters(requestParams);
	    myFriendsRequest.executeAsync();
		
	}
	
	public void FirstNameClicked(View v) {
		Log.i("Clicked", "firstname");
		FirstNamesVisisble();
		firstNameSelected = true;
		
		if(firstNameSelected && lastNameSelected){
			nextButton_disabled.setVisibility(View.INVISIBLE);
			nextButton.setVisibility(View.VISIBLE);
		}
		selectedFirstName.setTextAppearance(getApplicationContext(), R.style.normalText);
		selectedFirstName.setTextColor(Color.parseColor("#000000"));
		
		if(v == firstName0) {
				firstName0.setVisibility(View.INVISIBLE);
				selectedFirstName.setText(firstName0.getText().toString());		
		}
		else if(v == firstName1) {
				firstName1.setVisibility(View.INVISIBLE);
				selectedFirstName.setText(firstName1.getText().toString());
		}
		else if(v == firstName2) {
				firstName2.setVisibility(View.INVISIBLE);
				selectedFirstName.setText(firstName2.getText().toString());
		}
		else if(v == firstName3) {
				firstName3.setVisibility(View.INVISIBLE);
				selectedFirstName.setText(firstName3.getText().toString());
		}
	}
	
	public void LastNameClicked(View v) {
		Log.i("Clicked", "lastname");
		LastNamesVisisble();
		lastNameSelected = true;
		if(firstNameSelected && lastNameSelected){
			nextButton_disabled.setVisibility(View.INVISIBLE);
			nextButton.setVisibility(View.VISIBLE);
		}
		selectedLastName.setTextAppearance(getApplicationContext(), R.style.normalText);
		selectedLastName.setTextColor(Color.parseColor("#000000"));
		
		if(v == lastName0) {
				lastName0.setVisibility(View.INVISIBLE);
				selectedLastName.setText(lastName0.getText().toString());
		}
		else if(v == lastName1) {
				lastName1.setVisibility(View.INVISIBLE);
				selectedLastName.setText(lastName1.getText().toString());
		}
		else if(v == lastName2) {
				lastName2.setVisibility(View.INVISIBLE);
				selectedLastName.setText(lastName2.getText().toString());
		}
		else if(v == lastName3) {
				lastName3.setVisibility(View.INVISIBLE);
				selectedLastName.setText(lastName3.getText().toString());
		}
	}
	
	public void FirstNamesVisisble(){
		firstName0.setVisibility(View.VISIBLE);
	    firstName1.setVisibility(View.VISIBLE);
		firstName2.setVisibility(View.VISIBLE);
		firstName3.setVisibility(View.VISIBLE);
	}
	
	public void LastNamesVisisble(){
		lastName0.setVisibility(View.VISIBLE);
		lastName1.setVisibility(View.VISIBLE);
		lastName2.setVisibility(View.VISIBLE);
		lastName3.setVisibility(View.VISIBLE);
	}

	public void authenticate()
	{
		
		if (firstNameSelected && lastNameSelected )
		{
			if ((first.equals(selectedFirstName.getText().toString()) && last.equals(selectedLastName.getText().toString())) == false)
			{
				wrongChosenNumbers[wrongFriendsCount] = lastChosen;
				wrongPeoplePictures[wrongFriendsCount] = mIcon1;
				wrongFriendsCount++;
				
				
				if (wrongFriendsCount == 3)		// changed this to 3 was 5
				{
					fifthWrong(wrongChosenNumbers);
				}
				
				incorrectCountView.setText(String.valueOf(wrongFriendsCount));
				
				rightFriendsCountInRow = 0;
				// add it to array of wrong people.
			}
			else
			{
				rightFriendsCount++;
				rightFriendsCountInRow++;
				
				correctCountView.setText(String.valueOf(rightFriendsCount));
				if (rightFriendsCountInRow >= counterInRow)
				{
					counterInRow = counterInRow + 5;
					Toast.makeText(getApplicationContext(), rightFriendsCountInRow + " matches in a row", Toast.LENGTH_SHORT).show();	
				}
			}	
		}
	}
	
	public void RefreshViews() {
		FirstNamesVisisble();
		LastNamesVisisble();
		selectedFirstName.setText(R.string.SelectedFirstNamePlaceholder);
		selectedLastName.setText(R.string.SelectedLastNamePlaceholder);
		nextButton_disabled.setVisibility(View.VISIBLE);
		nextButton.setVisibility(View.INVISIBLE);
		
		selectedFirstName.setTextAppearance(getApplicationContext(), R.style.italicText);
		selectedFirstName.setTextColor(Color.parseColor("#9A9DEE"));
		selectedLastName.setTextAppearance(getApplicationContext(), R.style.italicText);
		selectedLastName.setTextColor(Color.parseColor("#9A9DEE"));
	}
	
	public void fifthWrong(int[] wrongPersons){
		String[] id = new String[5];
		
		//for(int i = 0; i < 5; i++){
		//	id[0] = myFriends[wrongPersons[i]].getId().toString();
		//	names[0] = myFriends[wrongPersons[i]].getName().toString();
		//}
		
		names[0] = myFriends[wrongPersons[0]].getName();
		names[1] = myFriends[wrongPersons[1]].getName();
		names[2] = myFriends[wrongPersons[2]].getName();
		//names[3] = myFriends[wrongPersons[3]].getName();
		//names[4] = myFriends[wrongPersons[4]].getName();
				
				
		Intent intent = new Intent(GetUsers.this, EndGame.class);
		//Bundle bundle = new Bundle();
		//bundle.putParcelableArray("pictures", wrongPeoplePictures);
		//bundle.putStringArray("names", names);
		
		intent.putExtra("names", names);
		//intent.putExtra("pictures", wrongPeoplePictures);
		//intent.putExtra("bundle", bundle);               
		intent.putExtra("image1", wrongPeoplePictures[0]);
		intent.putExtra("image2", wrongPeoplePictures[1]);
		intent.putExtra("image3", wrongPeoplePictures[2]);
		//intent.putExtra("image4", wrongPeoplePictures[3]);
		//intent.putExtra("image5", wrongPeoplePictures[4]);
		startActivity(intent);
	}
	
	
}		// end class everything.
