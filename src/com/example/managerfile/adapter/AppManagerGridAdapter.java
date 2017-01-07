package com.example.managerfile.adapter;

import java.util.List;

import com.example.managerfile.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.managerfile.model.ApkInfo;

public class AppManagerGridAdapter extends BaseAdapter
{
	protected Context mContext;
	protected List<ApkInfo> infos;
	protected LayoutInflater inflater;

	public AppManagerGridAdapter(Context context, List<ApkInfo> infos)
	{
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.infos = infos;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder=new ViewHolder();
			view = inflater.inflate(R.layout.grid_file_item, parent, false);
			holder.mApkIcon=(ImageView)view.findViewById(R.id.icon);
			holder.mApkName=(TextView)view.findViewById(R.id.file_name);
			view.setTag(holder);
		}else {
			holder=(ViewHolder)view.getTag();
		}
		holder.mApkIcon.setImageDrawable(infos.get(position).icon());
		holder.mApkName.setText(infos.get(position).name());
		return view;
	}

	private class ViewHolder
	{
		ImageView mApkIcon;
		TextView mApkName;
	}

}
