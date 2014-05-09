package com.pierceholdings.floatingcensor;

import java.util.ArrayList;
import java.util.List;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

 

public class MultiWindow extends StandOutWindow {


	@Override
	public String getAppName() {
		return "";
	}

	@Override
	public int getAppIcon() {
		return android.R.drawable.ic_menu_add;
	}

	@Override
	public String getTitle(int id) {
		return "";
	}

	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		// create a new layout from body.xml
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.simpleblank, frame, true);
	}

	// every window is initially same size
	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		return new StandOutLayoutParams(id, 800, 150,
				StandOutLayoutParams.AUTO_POSITION,
				StandOutLayoutParams.AUTO_POSITION, 100, 100);
	}

	// we want the system window decorations, we want to drag the body, we want
	// the ability to hide windows, and we want to tap the window to bring to
	// front
	@Override
	public int getFlags(int id) {
		return StandOutFlags.FLAG_DECORATION_SYSTEM
				| StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_DECORATION_MAXIMIZE_DISABLE
				| StandOutFlags.FLAG_WINDOW_HIDE_ENABLE
				| StandOutFlags.FLAG_DECORATION_CLOSE_DISABLE
				| StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP
				| StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE
				| StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE;
	}

	@Override
	public String getPersistentNotificationTitle(int id) {
		return getAppName() + " Running";
	}

	@Override
	public String getPersistentNotificationMessage(int id) {
		return "Click to add a new " + getAppName();
	}

	// return an Intent that creates a new MultiWindow
	@Override
	public Intent getPersistentNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), getUniqueId());
	}

	@Override
	public int getHiddenIcon() {
		return android.R.drawable.ic_menu_info_details;
	}

	@Override
	public String getHiddenNotificationTitle(int id) {
		return getAppName() + " Hidden";
	}

	@Override
	public String getHiddenNotificationMessage(int id) {
		return "Click to restore #" + id;
	}

	// return an Intent that restores the MultiWindow
	@Override
	public Intent getHiddenNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), id);
	}

	@Override
	public Animation getShowAnimation(int id) {
		if (isExistingId(id)) {
			// restore
			return AnimationUtils.loadAnimation(this,
					android.R.anim.slide_in_left);
		} else {
			// show
			return super.getShowAnimation(id);
		}
	}

	@Override
	public Animation getHideAnimation(int id) {
		return AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
	}

	@Override
	public List<DropDownListItem> getDropDownItems(int id) {
		List<DropDownListItem> items = new ArrayList<DropDownListItem>();
		items.add(new DropDownListItem(android.R.drawable.ic_menu_help,
				"Help", new Runnable() {

					@Override
					public void run() {
						Toast.makeText(
								MultiWindow.this,
								"Drag the bottom right corner to resize the box. Tap the eye icon to hide the box.",
								Toast.LENGTH_SHORT).show();
					}
				}));
		items.add(new DropDownListItem(android.R.drawable.btn_star,
				"Rate this app", new Runnable() {

					@Override
					public void run() {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    intent.setData(Uri.parse("market://details?id=com.pierceholdings.floatingcensor"));
					    startActivity(intent);
						}    
				}));
		items.add(new DropDownListItem(android.R.drawable.ic_dialog_alert,
				"Support the developer", new Runnable() {

					@Override
					public void run() {
						 donateClass();
						  
					}
				}));
		items.add(new DropDownListItem(android.R.drawable.ic_dialog_email,
				"Email Feedback", new Runnable() {

					@Override
					public void run() {
						Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					            "mailto","support@dontpause.me", null));
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Floating Censor Feedback");
					Intent new_intent = Intent.createChooser(emailIntent, "Send feedback...");
					new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					getApplicationContext().startActivity(new_intent);
					}
				}));
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean tutBox = prefs.getBoolean("tutBox", true);;
		if (tutBox) {
		items.add(new DropDownListItem(android.R.drawable.ic_menu_preferences,
				"Disable Tutorial Box", new Runnable() {

					@Override
					public void run() {
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						prefs.edit().putBoolean("tutBox", false).commit();
					}
		
		}));
		}
		
		return items;
	}

	protected void donateClass() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		   intent.setClass(MultiWindow.this, DonateActivity.class);
		   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   startActivity(intent); 
	}

	
}

