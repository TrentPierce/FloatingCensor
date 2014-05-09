package com.pierceholdings.floatingcensor;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class SimpleWindow extends StandOutWindow {

	@Override
	public String getAppName() {
		return "Tutorial Window";
	}

	@Override
	public int getAppIcon() {
		return android.R.drawable.ic_menu_close_clear_cancel;
	}

	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		// create a new layout from body.xml
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.simple, frame, true);
	}

	// the window will be centered
	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		return new StandOutLayoutParams(id, 800, 600,
				StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER);
	}

	// move the window by dragging the view
	@Override
	public int getFlags(int id) {
		return super.getFlags(id) | StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_DECORATION_MAXIMIZE_DISABLE
				| StandOutFlags.FLAG_DECORATION_CLOSE_DISABLE
				| StandOutFlags.FLAG_DECORATION_SYSTEM
				| StandOutFlags.FLAG_WINDOW_FOCUS_INDICATOR_DISABLE
				| StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE;
	}

	@Override
	public String getPersistentNotificationMessage(int id) {
		return "Click to close the Floating Censor";
	}

	@Override
	public Intent getPersistentNotificationIntent(int id) {
		return StandOutWindow.getCloseIntent(this, SimpleWindow.class, id);
	}
}
