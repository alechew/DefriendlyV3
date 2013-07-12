package edu.fsu.cs.muppetlabs.defriendly;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;



public class EndGame extends Activity {
	TextView FirstName,
	SecondName,
	ThirdName,
	FourthName,
	FifthName;
	ImageView FirstPic,
	SecondPic,
	ThirdPic,
	FourthPic,
	FifthPic;
	String[] names = new String[5];
	Bitmap[] pictures = new Bitmap[5];
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.enders_game);
	    
	    FirstName = (TextView)findViewById(R.id.wrong0);
		SecondName = (TextView)findViewById(R.id.wrong1);
		ThirdName = (TextView)findViewById(R.id.wrong2);
		//FourthName = (TextView)findViewById(R.id.wrong3);
		//FifthName = (TextView)findViewById(R.id.wrong4);
		
		FirstPic = (ImageView)findViewById(R.id.pic0);
		SecondPic = (ImageView)findViewById(R.id.pic1);
		ThirdPic = (ImageView)findViewById(R.id.pic2);
		//FourthPic = (ImageView)findViewById(R.id.pic3);
		//FifthPic = (ImageView)findViewById(R.id.pic4);
	    
		
	   Intent i = this.getIntent();
	  //Bundle extras=i.getBundleExtra("bundle");
	   
	   //String[] ids = i.getStringArrayExtra("ids");
	   //String[] ids = extras.getStringArray("ids");
	   //String[] names = new String[5];
	   names = i.getStringArrayExtra("names");
	   
	   //Bitmap[] pictures = new Bitmap[5];
	   //pictures = (Bitmap[]) extras.getParcelableArray("pictures");
		
	   pictures[0] = (Bitmap) i.getParcelableExtra("image1");
	   pictures[1] = (Bitmap) i.getParcelableExtra("image2");
	   pictures[2] = (Bitmap) i.getParcelableExtra("image3");
	   //pictures[3] = (Bitmap) i.getParcelableExtra("image4");
	   //pictures[4] = (Bitmap) i.getParcelableExtra("image5");
	    
		 FirstName.setText((String)names[0]);
		 SecondName.setText((String)names[1]);
		 ThirdName.setText((String)names[2]);
		 //FourthName.setText((String)names[3]);
		 //FifthName.setText((String)names[4]);
		 
		 
		 FirstPic.setImageBitmap(pictures[0]);
		 //SecondPic.setImageResource(R.drawable.com_facebook_button_check);
		 //ThirdPic.setImageResource(R.drawable.com_facebook_button_check);
		 SecondPic.setImageBitmap(pictures[1]);
		 ThirdPic.setImageBitmap(pictures[2]);
		 //FourthPic.setImageBitmap(pictures[3]);
		 //FifthPic.setImageBitmap(pictures[4]);
	}
	
}