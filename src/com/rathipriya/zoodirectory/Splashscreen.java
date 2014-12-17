package com.rathipriya.zoodirectory;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Splashscreen extends ActionBarActivity {
	String hello="Hello";
	protected int _splashTime=4000;
	MediaPlayer mp        = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//action bar color
		android.app.ActionBar bar=getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#246B47")));
		managerOfSound();
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(Splashscreen.this, GridviewActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, _splashTime);




	}//oncreate end

	private void managerOfSound() {
		if (mp != null) {
			mp.reset();
			mp.release();
		}
		mp = MediaPlayer.create(this, R.raw.spashvoice);

		mp.start();
	}



}
