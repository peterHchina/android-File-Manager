package com.example.managerfile.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.managerfile.adapter.AppManagerGridAdapter;
import com.example.managerfile.model.ApkInfo;
import com.example.managerfile.receiver.AppInstallReceiver;
import com.example.managerfile.task.ApplicationGettedFromSystemTask;
import com.example.managerfile.ui.operate.DialogManageApp;
import com.example.managerfile.R;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationManagerActivity extends Activity
{

	private static final int BUFF_SIZE = 8192;
	private List<PackageInfo> apkInfos;
	private GridView apkView;
	private ArrayList<ApkInfo> mApkInfos;
	private PackageManager mPackageManager;
	private String[] apkmenu;
	private FileManagerActivity fileManager = null;
	private TextView tvTitleTextView;
	private Context mContext;
	private AppManagerGridAdapter mAppManagerGridAdapter;
	private Handler mHandler;
	private boolean flag = true;
	private int mPosition;
	private int preNumber;
	private int preUserAppNumber;
	private AppInstallReceiver mInstallReceiver;


	@Override
	public void onCreate(Bundle saveBundle)
	{
		super.onCreate(saveBundle);
		setContentView(R.layout.list_app_manger);
		mContext = this;
		mInstallReceiver = new AppInstallReceiver(ApplicationManagerActivity.this, mContext);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(mInstallReceiver, filter);
		init();

	}

	public void init()
	{
		tvTitleTextView = (TextView) findViewById(R.id.tvTitle);
		tvTitleTextView.setText(R.string.apk_manger);
		apkView = (GridView) findViewById(R.id.apk_grid);	
		mPackageManager = getPackageManager();
		RefashData();
		preNumber = mPackageManager.getInstalledPackages(0).size();
		apkView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub

				new DialogManageApp(mContext, mApkInfos.get(position), mPackageManager, ApplicationManagerActivity.this).init();
				mPosition = position;
			}
		});

	}

	public void RefashData()
	{
		new ApplicationGettedFromSystemTask(mContext, apkView, ApplicationManagerActivity.this).execute();
	}

	public ArrayList<ApkInfo> getmApkInfos()
	{
		return mApkInfos;
	}

	public void setmApkInfos(ArrayList<ApkInfo> mApkInfos)
	{
		this.mApkInfos = mApkInfos;
	}

	protected void onDestory()
	{
		unregisterReceiver(mInstallReceiver);
		super.onDestroy();
	}
}
