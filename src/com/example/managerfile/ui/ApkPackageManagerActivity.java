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

import com.example.managerfile.R;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.managerfile.adapter.AppManagerGridAdapter;
import android.widget.Toast;
import com.example.managerfile.adapter.AppManagerGridAdapter;
import com.example.managerfile.model.ApkInfo;
import com.example.managerfile.ui.operate.DialogManageApp;

public class ApkPackageManagerActivity extends Activity
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
	private final static int SYS_AND_USER = 0;
	private final static int ONLY_SYS = 1;
	private final static int ONLY_USER = 2;
	private final static int REFRESH = 3;
	private int showWhat = ONLY_USER;
	private Handler mHandler;
	private boolean flag = true;
	private UpdateTimeThread mTimeThread;
	private int mPosition;
	private int preNumber;
	private int preUserAppNumber;

	@Override
	public void onCreate(Bundle saveBundle)
	{
		super.onCreate(saveBundle);
		setContentView(R.layout.list_app_manger);
		mContext = this;
		init();
	}

	public void init()
	{
		tvTitleTextView = (TextView) findViewById(R.id.tvTitle);
		tvTitleTextView.setText(R.string.apk_manger);
		apkView = (GridView) findViewById(R.id.apk_grid);
		mPackageManager = getPackageManager();
		// mApkInfos = new ArrayList<ApkInfo>();
		RefashData();
		preNumber = mPackageManager.getInstalledPackages(0).size();		
		mTimeThread = new UpdateTimeThread();
		mTimeThread.start();
		apkView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				new DialogManageApp(mContext, mApkInfos.get(position), mPackageManager, ApkPackageManagerActivity.this).init();
				mPosition = position;
			}
		});

		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				// apkInfos = mPackageManager.getInstalledPackages(0);
				// getInfos(apkInfos);
				// mAppManagerGridAdapter.notifyDataSetChanged();
				RefashData();

			}

		};

	}

	public void RefashData()
	{
		apkInfos = new ArrayList<PackageInfo>();
		if (apkInfos.size() != 0)
		{
			apkInfos.clear();
		}
		apkInfos = mPackageManager.getInstalledPackages(0);
		getInfos(apkInfos);
		mAppManagerGridAdapter = new AppManagerGridAdapter(mContext, mApkInfos);
		if (preNumber > apkInfos.size())
		{
			apkView.setAdapter(mAppManagerGridAdapter);
			apkView.setSelection(mPosition);
			preNumber = apkInfos.size();
		}

	}

	private void getInfos(List<PackageInfo> apkInfos)
	{

		if (apkInfos == null)
			return;

		mApkInfos = new ArrayList<ApkInfo>();
		if (mApkInfos.size() != 0)
		{
			mApkInfos.clear();
		}

		String packagename;
		String packagepath;
		ApplicationInfo packageApplicationInfo;
		String name;
		Drawable icon;
		PackageInfo apk;
		boolean isSys = false;
		for (int i = 0; i < apkInfos.size();)
		{
			apk = apkInfos.get(i);
			isSys = apk.applicationInfo.sourceDir.startsWith("/system");

			switch (showWhat)
			{
			case SYS_AND_USER:
				break;
			case ONLY_SYS:
				if (!isSys)
				{
					apkInfos.remove(apk);
					continue;
				}
				break;
			case ONLY_USER:
				if (isSys)
				{
					apkInfos.remove(apk);
					continue;
				}
				break;
			default:
				break;
			}
			i++;
			name = (String) mPackageManager.getApplicationLabel(apk.applicationInfo);
			icon = mPackageManager.getApplicationIcon(apk.applicationInfo);
			packagename = apk.packageName;
			packagepath = apk.applicationInfo.sourceDir;
			packageApplicationInfo = apk.applicationInfo;
			mApkInfos.add(new ApkInfo(icon, name, packagename, packagepath, packageApplicationInfo));
			Log.i("Test1", "++++++++++++++++++++++++++++" + mApkInfos.size());
			Collections.sort(mApkInfos);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		/**/
		menu.add(0, 0, SYS_AND_USER, mContext.getResources().getString(R.string.all_apk));
		menu.add(0, 0, ONLY_SYS, mContext.getResources().getString(R.string.system_apk));
		menu.add(0, 0, ONLY_USER, mContext.getResources().getString(R.string.user_apk));
		menu.add(0, 0, REFRESH, mContext.getResources().getString(R.string.user_apk));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int what = item.getOrder();
		switch (what)
		{
		case SYS_AND_USER:
			showWhat = SYS_AND_USER;
			RefashData();
			break;
		case ONLY_SYS:
			showWhat = ONLY_SYS;
			RefashData();
			break;
		case ONLY_USER:
			showWhat = ONLY_USER;
			RefashData();
			break;
		case REFRESH:
			RefashData();
			break;
		}
		return true;
	}

	public int copyFile(File src, File dst) throws InterruptedException
	{
		int ret = 0;
		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		if (src == null)
		{
			return -1;
		}
		try
		{
			fIn = new FileInputStream(src);
			fOut = new FileOutputStream(dst);
			in = new BufferedInputStream(fIn, BUFF_SIZE);
			out = new BufferedOutputStream(fOut, BUFF_SIZE);
			byte[] bytes = new byte[BUFF_SIZE];
			int length;
			while ((length = in.read(bytes)) != -1)
			{
				out.write(bytes, 0, length);
			}
			out.flush();

		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (fIn != null)
					fIn.close();
				if (fOut != null)
					fOut.close();
				return ret;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	@Override
	public void onBackPressed()
	{

		flag = false;
		mTimeThread.interrupt();
		finish();
	}

	public class UpdateTimeThread extends Thread
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			while (flag)
			{

				try
				{
					sleep(1000);
					Log.i("test*********************************", "");
					mHandler.sendEmptyMessage(0);

				}
				catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	}
}
