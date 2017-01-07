package com.example.managerfile.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.managerfile.adapter.AppManagerGridAdapter;
import com.example.managerfile.model.ApkInfo;
import com.example.managerfile.ui.ApplicationManagerActivity;
import com.example.managerfile.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

public class ApplicationGettedFromSystemTask extends AsyncTask<Void, Void, Boolean>
{
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private final static int SYS_AND_USER = 0;
	private final static int ONLY_SYS = 1;
	private final static int ONLY_USER = 2;
	private final static int REFRESH = 3;
	private PackageManager mPackageManager;
	private ProgressDialog mDialog;
	private ArrayList<ApkInfo> mApkInfos;
	private int showWhat = ONLY_USER;
	private Context mContext;
	private List<PackageInfo> apkInfos;
	private GridView apkView;
   private ApplicationManagerActivity mApplicationManagerActivity;
	public ApplicationGettedFromSystemTask(Context context,GridView apkView,ApplicationManagerActivity activity)
	{
		mContext = context;
		mPackageManager = mContext.getPackageManager();
		this.apkView=apkView;
		mApplicationManagerActivity=activity;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	private void getInfosByType(List<PackageInfo> apkInfos)
	{

		if (apkInfos == null)
			return;

		mApkInfos = new ArrayList<ApkInfo>();
		if (mApkInfos.size() != 0) {
			mApkInfos.clear();
		}

		String packagename;
		String packagepath;
		ApplicationInfo packageApplicationInfo;
		String name;
		Drawable icon;
		PackageInfo apk;
		boolean isSys = false;
		for (int i = 0; i < apkInfos.size();) {
			apk = apkInfos.get(i);
			isSys = apk.applicationInfo.sourceDir.startsWith("/system");

			switch (showWhat)
			{
			case SYS_AND_USER:
				break;
			case ONLY_SYS:
				if (!isSys) {
					apkInfos.remove(apk);
					continue;
				}
				break;
			case ONLY_USER:
				if (isSys) {
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
			Collections.sort(mApkInfos);
		}
	}

	@Override
	protected Boolean doInBackground(Void... arg0)
	{
		// TODO Auto-generated method stub
		getInfosByType(getInfos());
		if (mApkInfos.size() == 0 || mApkInfos == null) {
			return false;
		}
		return true;
	}

	private List<PackageInfo> getInfos()
	{
		// TODO Auto-generated method stub

		apkInfos = new ArrayList<PackageInfo>();
		if (apkInfos.size() != 0) {
			apkInfos.clear();
		}
		apkInfos = mPackageManager.getInstalledPackages(0);
		return apkInfos;
	}

	protected void onPostExecute(Boolean res)
	{
		if (mDialog != null) {
			mDialog.cancel();
		}
		if (res) {
        AppManagerGridAdapter adapter=new AppManagerGridAdapter(mContext, mApkInfos);
        apkView.setAdapter(adapter);
        mApplicationManagerActivity.setmApkInfos(mApkInfos);
		}
		else {
			Toast.makeText(mContext, mContext.getResources().getString(R.string.app_search_error), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onPreExecute()
	{
		String str = mContext.getResources().getString(R.string.scaning);
		mDialog = ProgressDialog.show(mContext, null, str);
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(mOnCancelListener);
	}

}
