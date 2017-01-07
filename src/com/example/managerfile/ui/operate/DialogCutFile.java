package com.example.managerfile.ui.operate;

import java.util.ArrayList;
import java.util.List;

import com.example.managerfile.adapter.GridAdapter;
import com.example.managerfile.adapter.PasteGridAdapter;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.ui.FileManagerActivity;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class DialogCutFile
{
	private Context mContext;
	private Fileinfo mFileinfo;
	private FileManagerActivity mFileManagerActivity;
	private List<Fileinfo> mList;

	public DialogCutFile(Context context, Fileinfo fileinfo, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		mFileinfo = fileinfo;
		mFileManagerActivity = fileManagerActivity;
	}

	public DialogCutFile(Context context, List<Fileinfo> fileinfo, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		mList = fileinfo;
		mFileManagerActivity = fileManagerActivity;
	}

	public void init()
	{
		mFileManagerActivity.mPasteBodeLayout.setVisibility(View.VISIBLE);
		if (mFileManagerActivity.mFileinfos.size() == 0)
		{
			mFileManagerActivity.mFileinfos.add(mFileinfo);
		}
		else
		{
			boolean is = false;
			for (int i = 0; i < mFileManagerActivity.mFileinfos.size(); i++)
			{
				if (mFileManagerActivity.mFileinfos.get(i).getmPath().equals(mFileinfo.getmPath()))
				{
					is = true;
					break;
				}

			}
			if (!is)
			{
				mFileManagerActivity.mFileinfos.add(mFileinfo);
			}

		}
		if (mFileManagerActivity.mFileinfos.size() != 0)
		{
			mFileManagerActivity.adapter = new PasteGridAdapter(mContext, mFileManagerActivity.mFileinfos);

			mFileManagerActivity.mPasteGridView.setAdapter(mFileManagerActivity.adapter);
			mFileManagerActivity.adapter.NofityAll();
		}
	}

	public void init1()
	{
		mFileManagerActivity.mPasteBodeLayout.setVisibility(View.VISIBLE);
		mFileManagerActivity.mOpearte.setVisibility(View.GONE);
		if (mFileManagerActivity.mFileinfos.size() != 0)
		{
			mFileManagerActivity.adapter = new PasteGridAdapter(mContext, mFileManagerActivity.mFileinfos);
			mFileManagerActivity.mPasteGridView.setAdapter(mFileManagerActivity.adapter);
			mFileManagerActivity.adapter.NofityAll();
		}
	}
}
