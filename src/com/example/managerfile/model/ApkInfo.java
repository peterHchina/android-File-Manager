package com.example.managerfile.model;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class ApkInfo implements Comparable<ApkInfo>
{
	private Drawable icon;
	private String name;
	private String mPackageNmae;
	private String mPackagePath;
	private ApplicationInfo mApplicationInfo;

	public ApkInfo(Drawable icon, String name)
	{
		// TODO Auto-generated constructor stub
		this.icon = icon;
		this.name = name;
	}

	public ApkInfo(Drawable icon, String name, String packagename, String packagepath, ApplicationInfo applicationInfo)
	{
		// TODO Auto-generated constructor stub
		this.icon = icon;
		this.name = name;
		mPackageNmae = packagename;
		mPackagePath = packagepath;
		mApplicationInfo = applicationInfo;
	}

	public String name()
	{
		return name;
	}

	public Drawable icon()
	{
		return icon;
	}

	public String getmPackageNmae()
	{
		return mPackageNmae;
	}

	public void setmPackageNmae(String mPackageNmae)
	{
		this.mPackageNmae = mPackageNmae;
	}

	public String getmPackagePath()
	{
		return mPackagePath;
	}

	public void setmPackagePath(String mPackagePath)
	{
		this.mPackagePath = mPackagePath;
	}

	public ApplicationInfo getmApplicationInfo()
	{
		return mApplicationInfo;
	}

	public void setmApplicationInfo(ApplicationInfo mApplicationInfo)
	{
		this.mApplicationInfo = mApplicationInfo;
	}

	@Override
	public int compareTo(ApkInfo another)
	{
		// TODO Auto-generated method stub
		return name.compareTo(another.name);
	}

}
