package com.example.managerfile.ui.operate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.example.managerfile.R;
import com.example.managerfile.model.ApkInfo;
import com.example.managerfile.ui.ApkPackageManagerActivity;
import com.example.managerfile.ui.ApplicationManagerActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class DialogManageApp
{
	Context mContext;
	private String[] mMangerStrings;
	private PackageInfo mPackageInfo;
	private ApkInfo mApkInfo;
	private static final int BUFF_SIZE = 8192;
	private PackageManager mPackageManager;
	private static final String BACKUPUP_DIR = "/sdcard/AppBackup";
	public static final String APP_PKG_PREFIX = "com.android.settings.";
	public static final String APP_PKG_NAME = APP_PKG_PREFIX + "ApplicationPkgName";
	private final static String apkPath = "/data/app/";
	private String dstPath;
	private String dstPath1 = "/sdcard/AppBack/";
	private ApkPackageManagerActivity mActivity;
	private SharedPreferences settingPreferences;
	private SharedPreferences.Editor editor;
	private static final String SETTING_BACK_PATH = "com.FileManger.path";
	private ApplicationManagerActivity mActivity1;

	public DialogManageApp(Context context, PackageInfo packageInfo, ApkInfo apkInfo, PackageManager packageManager)
	{
		mContext = context;
		mApkInfo = apkInfo;
		mPackageInfo = packageInfo;
		mPackageManager = packageManager;
		mMangerStrings = mContext.getResources().getStringArray(R.array.apkmenu);
		settingPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

		dstPath = "/sdcard/" + settingPreferences.getString(SETTING_BACK_PATH, "AppBack");

	}

	public DialogManageApp(Context context, PackageInfo packageInfo, ApkInfo apkInfo, PackageManager packageManager, ApkPackageManagerActivity activity)
	{
		mContext = context;
		mApkInfo = apkInfo;
		mPackageInfo = packageInfo;
		mPackageManager = packageManager;
		mActivity = activity;
		mMangerStrings = mContext.getResources().getStringArray(R.array.apkmenu);
		settingPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		dstPath = "/sdcard/" + settingPreferences.getString(SETTING_BACK_PATH, "AppBack");

	}

	public DialogManageApp(Context context, ApkInfo apkInfo, PackageManager packageManager, ApkPackageManagerActivity activity)
	{
		mContext = context;
		mApkInfo = apkInfo;
		mPackageManager = packageManager;
		mActivity = activity;
		mMangerStrings = mContext.getResources().getStringArray(R.array.apkmenu);
		settingPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

		dstPath = "/sdcard/" + settingPreferences.getString(SETTING_BACK_PATH, "AppBack");

	}

	public DialogManageApp(Context context, ApkInfo apkInfo, PackageManager packageManager, ApplicationManagerActivity activity)
	{
		mContext = context;
		mApkInfo = apkInfo;
		mPackageManager = packageManager;
		mActivity1 = activity;
		mMangerStrings = mContext.getResources().getStringArray(R.array.apkmenu);
		settingPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		dstPath = "/sdcard/" + settingPreferences.getString(SETTING_BACK_PATH, "AppBack");

	}

	public void init()
	{
		File f = new File(dstPath);
		if (!(f.exists())) {
			f.mkdirs();
		}
		AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
		mDialog.setTitle(mApkInfo.name());
		mDialog.setItems(mMangerStrings, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int which)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (which)
				{
				case 0:

					intent = mPackageManager.getLaunchIntentForPackage(mApkInfo.getmPackageNmae());

					if (intent == null) {
						return;
					}

					mContext.startActivity(intent);
					break;
				case 1:

					Uri uri = Uri.fromParts("package", mApkInfo.getmApplicationInfo().packageName, null);
					intent = new Intent(Intent.ACTION_DELETE, uri);
					mContext.startActivity(intent);
					// mActivity.RefashData();
					break;
				case 2:
					String backApk = mApkInfo.getmPackagePath();
					try {
						new File(dstPath).mkdirs();
						if (!dstPath.endsWith("/"))
							dstPath = dstPath + "/";
						copyFile(new File(backApk), new File(dstPath + "/" + mApkInfo.name() + ".apk"));
						String stringFormate = mContext.getResources().getString(R.string.backup_success);
						Toast.makeText(mContext, String.format(stringFormate, mApkInfo.name()), Toast.LENGTH_SHORT).show();
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}
					break;
				case 3:
					final int level = Build.VERSION.SDK_INT;

					if (level < 9) {
						intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
						intent.putExtra(APP_PKG_NAME, mApkInfo.getmApplicationInfo().packageName);
						intent.putExtra("pkg", mApkInfo.getmApplicationInfo().packageName);
					}
					else {
						String details = "android.settings.APPLICATION_DETAILS_SETTINGS";
						intent.setAction(details);
						uri = Uri.fromParts("package", mApkInfo.getmPackageNmae(), null);
						intent.setData(uri);
					}
					mContext.startActivity(intent);
					break;
				default:
					break;
				}

			}
		});
		mDialog.show();
	}

	public int copyFile(File src, File dst) throws InterruptedException
	{
		int ret = 0;
		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		if (src == null) {
			return -1;
		}
		try {
			fIn = new FileInputStream(src);
			fOut = new FileOutputStream(dst);
			in = new BufferedInputStream(fIn, BUFF_SIZE);
			out = new BufferedOutputStream(fOut, BUFF_SIZE);
			byte[] bytes = new byte[BUFF_SIZE];
			int length;
			while ((length = in.read(bytes)) != -1) {
				out.write(bytes, 0, length);
			}
			out.flush();

		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if (fIn != null)
					fIn.close();
				if (fOut != null)
					fOut.close();
				return ret;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	class APKTimerTask extends TimerTask
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			mActivity.RefashData();

		}
	};
}
