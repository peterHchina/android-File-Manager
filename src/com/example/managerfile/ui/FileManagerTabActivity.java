package com.example.managerfile.ui;

import com.example.managerfile.R;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class FileManagerTabActivity extends TabActivity
{

	private TabHost mTabHost;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private static Class<?> fileManagerArryClass[] =
	{ FileManagerCategoryActivity.class, FileManagerActivity.class, FileManagerInRemoteActivity.class, ApplicationManagerActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this;
		mLayoutInflater = LayoutInflater.from(this);
		initTableHost();

	}

	private void initTableHost()
	{
		TabSpec tabSpec;
		mTabHost = super.getTabHost();
		for (int i = 0; i < fileManagerArryClass.length; i++) {
			Intent intent1 = new Intent(this, fileManagerArryClass[i]);
			tabSpec = mTabHost.newTabSpec(fileManagerArryClass[i].getName());
			tabSpec.setIndicator(getTabItemView(i));
			tabSpec.setContent(intent1);
			mTabHost.addTab(tabSpec);
		}
		mTabHost.setCurrentTab(0);
		gestureDetector = new GestureDetector(new FileManagerTabGestureDetector(mTabHost));
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event)
			{
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	private View getTabItemView(int i)
	{
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		TextView textView = (TextView) view.findViewById(R.id.tab_text);
		String labels[] = getResources().getStringArray(R.array.file_tab_labels);
		textView.setText(labels[i]);

		return view;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public interface IBackPressedListener
	{
		/**
		 * 处理back事件。
		 * 
		 * @return True: 表示已经处理; False: 没有处理，让基类处理。
		 */
		boolean onBack();
	}

}
