package com.example.managerfile.listener;

import java.util.List;
import java.util.Stack;

import com.example.managerfile.model.Check;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.ui.CollectionFileOprateDialog;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class CollectedFileOnLongClickListener implements OnItemLongClickListener
{
	private Context mContext;
	private LinearLayout mNoRecodeLayout;
	private List<DetailCategoryFileinfo> mCategoryFileinfo;
	private FrameLayout mSearchBar;
	private ListView fileListView;
	private int fileType;
    private TextView tvTitle;
    private Stack<Check> mChecklList;
    private SharedPreferences mSharedPreferences;
	public CollectedFileOnLongClickListener(Context context, LinearLayout noView, FrameLayout frameLayout, ListView listView, List<DetailCategoryFileinfo> info)
	{
		mContext = context;
		mNoRecodeLayout = noView;
		mCategoryFileinfo = info;
		mSearchBar = frameLayout;
		fileListView = listView;
	}

	public CollectedFileOnLongClickListener(Context context, LinearLayout noView, FrameLayout frameLayout, TextView tvTitle,ListView listView, List<DetailCategoryFileinfo> info, int fileType,Stack<Check> t,SharedPreferences sharedPreferences)
	{
		mContext = context;
		mNoRecodeLayout = noView;
		mCategoryFileinfo = info;
		mSearchBar = frameLayout;
		fileListView = listView;
		this.fileType = fileType;
		this.tvTitle=tvTitle;
		mChecklList=t;
		mSharedPreferences=sharedPreferences;
	}
	public CollectedFileOnLongClickListener(Context context, LinearLayout noView, FrameLayout frameLayout,ListView listView, List<DetailCategoryFileinfo> info, int fileType,Stack<Check> t,SharedPreferences sharedPreferences)
	{
		mContext = context;
		mNoRecodeLayout = noView;
		mCategoryFileinfo = info;
		mSearchBar = frameLayout;
		fileListView = listView;
		this.fileType = fileType;
		mChecklList=t;
		mSharedPreferences=sharedPreferences;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
	{
		// TODO Auto-generated method stub

		if (fileType == 8) {
         new CollectionFileOprateDialog(mContext,fileListView,mCategoryFileinfo, mCategoryFileinfo.get(position),mNoRecodeLayout,tvTitle,mSearchBar,fileType,mChecklList,mSharedPreferences).initDialog();
		}
		else {
			 new CollectionFileOprateDialog(mContext,fileListView,mCategoryFileinfo, mCategoryFileinfo.get(position),mNoRecodeLayout,tvTitle,mSearchBar,0,mChecklList,mSharedPreferences).initDialog();
		}
		return false;
	}

}
