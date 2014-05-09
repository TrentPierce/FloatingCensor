package com.pierceholdings.floatingcensor;

import wei.mark.standout.StandOutWindow;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class StandOutExampleActivity extends Activity {
	protected static final String TAG = null;
	/** Called when the activity is first created. */
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		Intent intent = getIntent();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean tutBox = prefs.getBoolean("tutBox", true);
		if (tutBox) {
		StandOutWindow.closeAll(this, SimpleWindow.class);
		StandOutWindow
		.show(this, SimpleWindow.class, StandOutWindow.DEFAULT_ID);
		}
		StandOutWindow.closeAll(this, MultiWindow.class);

		// show a MultiWindow, SimpleWindow

		
		StandOutWindow.show(this, MultiWindow.class, StandOutWindow.DEFAULT_ID);
	//	StandOutWindow.show(this, MostBasicWindow.class,
	//			StandOutWindow.DEFAULT_ID);

		// show a MostBasicWindow. It is commented out because it does not
		// support closing.

		/*
		 * StandOutWindow.show(this, StandOutMostBasicWindow.class,
		 * StandOutWindow.DEFAULT_ID);
		 */

		finish();
	}
}