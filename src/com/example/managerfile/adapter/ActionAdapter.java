package com.example.managerfile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.managerfile.R;;

public class ActionAdapter extends BaseAdapter
{
	private String[] actionItem;
	private Context mContext;
	private LayoutInflater inflater;

	public ActionAdapter(Context contexts, String[] item)
	{

		mContext = contexts;
		actionItem = item;
		inflater = LayoutInflater.from(contexts);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		if (actionItem != null)
		{
			return actionItem.length;
		}
		else
		{
			return 0;
		}

	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		if (actionItem != null)
		{
			return actionItem[position];
		}
		else
		{
			return null;
		}

	}

	@Override
	public long getItemId(int id)
	{
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.action_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if (holder == null || actionItem == null)
		{
			return null;

		}

		holder.mTextView1.setText(actionItem[position]);
		return convertView;
	}

	private class ViewHolder
	{
		TextView mTextView1;

		public ViewHolder(View parView)
		{
			mTextView1 = (TextView) parView.findViewById(R.id.action);
		}
	}

}
