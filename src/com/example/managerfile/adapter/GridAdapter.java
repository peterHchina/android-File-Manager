package com.example.managerfile.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.managerfile.R;

import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.task.ApkIconLoadTask;
import com.example.managerfile.task.ImageLoad4HeadTask;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.OpenFile;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter
{

	private Context mContext;
	public List<Fileinfo> listItems = new ArrayList<Fileinfo>();
	int phSize;
	protected static final int PHO_DIP = 45;
	public float PIX_SCALE;
	Resources res;
	private int SrcollState = 0;
	public static final int ITEM_VIEW_PIC = 0;
	public static final int ITEM_VIEW_NOT_PIC = 1;
	private static ExecutorService FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	public boolean isEdit = false;

	public GridAdapter(Context context, List<Fileinfo> objects)
	{
		listItems = objects;
		mContext = context;
		res = mContext.getResources();

	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		if (listItems != null)
		{
			return listItems.size();
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
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent)
	{
		// TODO Auto-generated method stub

		View view = convertview;
		Fileinfo mFileMap = listItems.get(position);
		if (getItemViewType(position) == ITEM_VIEW_PIC)
		{
			view = fillInPicView(convertview, mFileMap, position);
		}
		else
		{
			view = fillInNotPicView(convertview, mFileMap, position);
		}
		return view;
	}

	private View fillInPicView(View convertview, Fileinfo map, int postion)
	{
		viewHolder holder;
		if (convertview == null)
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertview = inflater.inflate(R.layout.grid_file_item, null);
			holder = new viewHolder(convertview);
			convertview.setTag(holder);

		}
		holder = (viewHolder) convertview.getTag();
		if (holder == null || map == null)
		{
			return convertview;
		}
		holder.reset();
		String filename = map.getmName();
		String stroageString = map.getmSize();
		String filedateString = map.getmDate();
		String path = map.getmPath();
		if (isEdit)
		{
			holder.mCheckBox.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.mCheckBox.setVisibility(View.GONE);
		}
		if (map.isCheck())
		{
			holder.mCheckBox.setChecked(true);
		}
		else
		{
			holder.mCheckBox.setChecked(false);
		}
		holder.mFileName.setText(filename);

		holder.mFileIcon.setImageDrawable(mContext.getResources().getDrawable(OpenFile.SeekIconForFileThroughFilename(filename, path)));
		holder.task = new ImageLoad4HeadTask(mContext, holder.mFileIcon, path, filename);
		holder.task.executeOnExecutor(FULL_TASK_EXECUTOR);
		// if (FileUtil.getExtensionWithoutDot(filename).equals("apk"))
		// {
		// holder.apktask = new ApkIconLoadTask(mContext, path,
		// holder.mFileIcon);
		// holder.apktask.executeOnExecutor(FULL_TASK_EXECUTOR);
		// }
		return convertview;
	}

	private View fillInNotPicView(View convertview, Fileinfo map, int postion)
	{
		viewHolder holder;
		if (convertview == null)
		{

			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertview = inflater.inflate(R.layout.grid_file_item, null);
			holder = new viewHolder(convertview);
			convertview.setTag(holder);

		}
		holder = (viewHolder) convertview.getTag();
		if (holder == null || map == null)
		{
			return convertview;
		}
		holder.reset();
		if (isEdit)
		{
			holder.mCheckBox.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.mCheckBox.setVisibility(View.GONE);
		}
		if (map.isCheck())
		{
			holder.mCheckBox.setChecked(true);
		}
		else
		{
			holder.mCheckBox.setChecked(false);
		}
		String filename = map.getmName();

		String path = map.getmPath();

		holder.mFileName.setText(filename);

		holder.mFileIcon.setImageDrawable(mContext.getResources().getDrawable(OpenFile.SeekIconForFileThroughFilename(filename, path)));
		if (FileUtil.getExtensionWithoutDot(filename).equals("apk"))
		{
			holder.apktask = new ApkIconLoadTask(mContext, path, holder.mFileIcon);
			holder.apktask.executeOnExecutor(FULL_TASK_EXECUTOR);
		}
		return convertview;
	}

	private class viewHolder
	{

		ImageView mFileIcon;
		TextView mFileName;
		ApkIconLoadTask apktask;
		ImageLoad4HeadTask task;
		CheckBox mCheckBox;

		public viewHolder(View paramView)
		{

			mFileIcon = (ImageView) paramView.findViewById(R.id.icon);
			mFileName = (TextView) paramView.findViewById(R.id.file_name);
			mCheckBox = (CheckBox) paramView.findViewById(R.id.checkbox_item);

		}

		public void reset()
		{
			if (task != null)
			{
				task.cancel(true);
				task = null;
			}
			if (apktask != null)
			{
				apktask.cancel(true);
				apktask = null;
			}

		}
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	public int getItemViewType(int paramInt)
	{
		Fileinfo map = listItems.get(paramInt);
		String filename = map.getmName();
		if (FileUtil.getExtensionWithoutDot(filename).equals("jpg") || FileUtil.getExtensionWithoutDot(filename).equals("png") || FileUtil.getExtensionWithoutDot(filename).equals("gif"))
		{
			return ITEM_VIEW_PIC;
		}
		return ITEM_VIEW_NOT_PIC;
	}

	public List<Fileinfo> GetCheckItems()
	{
		List<Fileinfo> mList = new ArrayList<Fileinfo>();
		for (int i = 0; i < listItems.size(); i++)
		{
			if (listItems.get(i).isCheck())
			{
				mList.add(listItems.get(i));
				listItems.get(i).setCheck(false);
			}

		}
		notifyDataSetChanged();
		return mList;
	}

	public int getCheckedItemsNumber()
	{
		int count = 0;
		for (int i = 0; i < listItems.size(); i++)
		{
			if (listItems.get(i).isCheck())
			{
				count++;
			}
		}
		return count;
	}
	
	public void MakeAllItemsSelected()
	{
		for (int i = 0; i < listItems.size(); i++)
		{
			listItems.get(i).setCheck(true);

		}
		notifyDataSetChanged();
	}

	public void MakeAllItemsUnselected()
	{
		for (int i = 0; i < listItems.size(); i++)
		{
			listItems.get(i).setCheck(false);

		}
		notifyDataSetChanged();
	}
}
