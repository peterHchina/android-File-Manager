package com.example.managerfile.listener;

import java.io.File;
import java.util.List;
import java.util.Stack;

import com.example.managerfile.R;
import com.example.managerfile.model.Check;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.task.RefleshCollectionFileTask;
import com.example.managerfile.util.OpenFile;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CollectedFileOnClickListener implements OnItemClickListener
{

	private Context mContext;
	private LinearLayout mNoRecodeLayout;
	private List<DetailCategoryFileinfo> mCategoryFileinfo;
    private FrameLayout mSearchBar;
    private ListView fileListView;
    private TextView mPath;
    private Stack<Check> mCheckList;
    private SharedPreferences mSharedPreferences;
	public CollectedFileOnClickListener(Context context, LinearLayout noView, FrameLayout frameLayout,ListView listView,List<DetailCategoryFileinfo> info,Stack<Check> list)
	{
		mContext = context;
		mNoRecodeLayout = noView;
		mCategoryFileinfo = info;
		mSearchBar=frameLayout;
		fileListView=listView;
	    mCheckList=list;
	}
	
	public CollectedFileOnClickListener(Context context, LinearLayout noView, FrameLayout frameLayout,ListView listView,List<DetailCategoryFileinfo> info,Stack<Check> list,SharedPreferences mSharedPreferences)
	{
		mContext = context;
		mNoRecodeLayout = noView;
		mCategoryFileinfo = info;
		mSearchBar=frameLayout;
		fileListView=listView;
	    mCheckList=list;
	    this.mSharedPreferences=mSharedPreferences;
	}
	@Override
	public void onItemClick(AdapterView<?> adapterView, View  view, int position, long id)
	{
		// TODO Auto-generated method stub
		DetailCategoryFileinfo fileinfo=mCategoryFileinfo.get(position);
		String path=fileinfo.getPath();
		View v = fileListView.getChildAt(0);
		int ListTop = (v == null) ? 0 : v.getTop();
         if (fileinfo.isDrectory() ) {
        	 mCheckList.push(new Check(path, fileListView.getFirstVisiblePosition(),ListTop));
			mSearchBar.setVisibility(View.VISIBLE);
			new RefleshCollectionFileTask(mContext,mSearchBar,fileListView,path,mNoRecodeLayout,mSharedPreferences,mCheckList).execute();
			
		}else {
			OpenFile.openFile(new File(path), mContext);
		}
	}

}
