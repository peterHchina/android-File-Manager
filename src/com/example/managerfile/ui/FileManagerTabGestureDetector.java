package com.example.managerfile.ui;

import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.TabHost;

public class FileManagerTabGestureDetector extends SimpleOnGestureListener
{
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private TabHost tabHost;
	private int currentView = 0;
	private static int maxTabIndex = 3;

	public FileManagerTabGestureDetector(TabHost tabHost)
	{
		this.tabHost = tabHost;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		try {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				Log.i("test", "right");
				if (currentView == maxTabIndex) {
					currentView = 0;
				}
				else {
					currentView++;
				}
				tabHost.setCurrentTab(currentView);
			}
			else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				Log.i("test", "left");
				if (currentView == 0) {
					currentView = maxTabIndex;
				}
				else {
					currentView--;
				}
				tabHost.setCurrentTab(currentView);
			}
		}
		catch (Exception e) {
		}
		return false;
	}

}
