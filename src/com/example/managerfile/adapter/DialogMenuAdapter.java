package com.example.managerfile.adapter;

import java.io.File;

import com.example.managerfile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogMenuAdapter extends BaseAdapter
{

	String[] mMenu;
	Context mContext;

	public DialogMenuAdapter(File mFile, Context context)
	{

		mContext = context;
		mMenu=mContext.getResources().getStringArray(R.array.menu);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mMenu.length;
	}

	@Override
	public Object getItem(int poistion)
	{
		// TODO Auto-generated method stub
		return mMenu[poistion];
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int positon, View convertview, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		View view = convertview;
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		ViewHolder holder;
		if (view == null)
		{

			view = mInflater.inflate(R.layout.dialog_list_item, null, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}
		holder.mTextView.setText(mMenu[positon]);

		return view;
	}

	class ViewHolder
	{
		TextView mTextView;

		ViewHolder(View view)
		{
			mTextView = (TextView) view.findViewById(R.id.choice);
		}
	}

}
