package com.example.managerfile.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.model.Videoinfo;
import com.example.managerfile.task.ApkIconLoadTask;
import com.example.managerfile.task.ImageLoad4HeadTask;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.OpenFile;
import com.example.managerfile.R;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore.Video;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter
{
	private Context mContext;
	public List<Videoinfo> listItems = new ArrayList<Videoinfo>();
	int phSize;
	protected static final int PHO_DIP = 45;
	public float PIX_SCALE;
	Resources res;
	private int SrcollState = 0;
	public static final int ITEM_VIEW_PIC = 0;
	public static final int ITEM_VIEW_NOT_PIC = 1;
	private static ExecutorService FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	private String mfolderFormate;
	public boolean isEdit = false;
	private LinearLayout mFileNoRecodeLayout;
	public VideoListAdapter(Context context, List<Videoinfo> objects,LinearLayout layout)
	{
		listItems = objects;
		mContext = context;
		res = mContext.getResources();
		mFileNoRecodeLayout=layout;
		mfolderFormate = mContext.getResources().getString(R.string.number_folder_caintins);
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
		Videoinfo mFileMap = listItems.get(position);
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

	private View fillInPicView(View convertview, Videoinfo map, int postion)
	{
		viewHolder holder;
		if (convertview == null)
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertview = inflater.inflate(R.layout.list_file_item, null);
			holder = new viewHolder(convertview);
			convertview.setTag(holder);

		}
		holder = (viewHolder) convertview.getTag();
		if (holder == null || map == null)
		{
			return convertview;
		}
		holder.reset();
		String filename = map.getTitle();
		String stroageString = map.getStroage();
		String filedateString = map.getDate();
		String path = map.getPath();

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
		holder.mFileDate.setText(filedateString);
		holder.mFileName.setText(filename+FileUtil.getExtensionWithDot(path));
		if (map.isDrectory())
		{
			holder.mFileCount.setVisibility(View.VISIBLE);
			holder.mFileCount.setText(String.format(mfolderFormate, FileUtil.GetAfterDirectoryFileCounts(map.getPath())));
		}
		else
		{
			holder.mFileCount.setVisibility(View.GONE);
		}
		holder.mFileStroage.setText(stroageString);
		holder.mFileIcon.setImageDrawable(mContext.getResources().getDrawable(OpenFile.SeekIconForFileThroughFilename(map.getPath())));
		holder.task = new ImageLoad4HeadTask(mContext, holder.mFileIcon, map.getThumbPath(), filename);
		holder.task.executeOnExecutor(FULL_TASK_EXECUTOR);
		if (FileUtil.getExtensionWithoutDot(filename).equals("apk"))
		{
			holder.apktask = new ApkIconLoadTask(mContext, path, holder.mFileIcon);
			holder.apktask.executeOnExecutor(FULL_TASK_EXECUTOR);
		}
		return convertview;
	}

	private View fillInNotPicView(View convertview, Videoinfo map, int postion)
	{
		viewHolder holder;
		if (convertview == null)
		{

			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertview = inflater.inflate(R.layout.list_file_item, null);
			holder = new viewHolder(convertview);
			convertview.setTag(holder);

		}
		holder = (viewHolder) convertview.getTag();
		if (holder == null || map == null)
		{
			return convertview;
		}
		holder.reset();
		String filename = map.getTitle();
		String stroageString = map.getStroage();
		String filedateString = map.getDate();
		String path = map.getPath();
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
		if (map.isDrectory())
		{
			holder.mFileCount.setVisibility(View.VISIBLE);
			holder.mFileCount.setText(String.format(mfolderFormate, FileUtil.GetAfterDirectoryFileCounts(map.getPath())));
		}
		else
		{
			holder.mFileCount.setVisibility(View.GONE);
		}
		holder.mFileDate.setText(filedateString);
		holder.mFileName.setText(filename+FileUtil.getExtensionWithDot(path));
		holder.mFileStroage.setText(stroageString);
		holder.mFileIcon.setImageDrawable(mContext.getResources().getDrawable(OpenFile.SeekIconForFileThroughFilename(path)));
		if (map.getThumbPath()!=null&&!map.getThumbPath().equals("")) {
			holder.mFileIcon.setImageURI(Uri.parse(map.getThumbPath()));  
		}
//		holder.task = new ImageLoad4HeadTask(mContext, holder.mFileIcon, map.getThumbPath(), map.getThumbPath());
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
		TextView mFileStroage;
		TextView mFileDate;
		TextView mFileCount;
		ImageLoad4HeadTask task;
		ApkIconLoadTask apktask;
		CheckBox mCheckBox;

		public viewHolder(View paramView)
		{
			mFileStroage = (TextView) paramView.findViewById(R.id.files_stroage);
			mFileIcon = (ImageView) paramView.findViewById(R.id.icon);
			mFileName = (TextView) paramView.findViewById(R.id.file_name);
			mFileDate = (TextView) paramView.findViewById(R.id.file_modify);
			mFileCount = (TextView) paramView.findViewById(R.id.files_counts);
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
		Videoinfo map = listItems.get(paramInt);
		String filename = map.getTitle();
		if (FileUtil.getExtensionWithoutDot(filename).equals("jpg") || FileUtil.getExtensionWithoutDot(filename).equals("png") || FileUtil.getExtensionWithoutDot(filename).equals("gif"))
		{
			return ITEM_VIEW_PIC;
		}
		return ITEM_VIEW_NOT_PIC;
	}

	public void NofityAll()
	{
		notifyDataSetChanged();
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

	public List<Videoinfo> GetCheckItems()
	{
		List<Videoinfo> mList = new ArrayList<Videoinfo>();
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
}
