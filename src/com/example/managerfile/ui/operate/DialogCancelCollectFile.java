package com.example.managerfile.ui.operate;

import java.util.List;
import java.util.Stack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.managerfile.provider.common.CollectFileDao;
import com.example.managerfile.R;
import com.example.managerfile.model.Check;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.task.GetFileListTask;

public class DialogCancelCollectFile
{
	private Context mContext;
	private Fileinfo mFileinfo;
	private int fileType;
	private ListView listView;
	private LinearLayout mFileNoRecodeLayout;
	private TextView tvTitle;
	private FrameLayout mSearchBar;
	private List<DetailCategoryFileinfo> list;
	private Stack<Check> checks;
	private String stringFormate;

	public DialogCancelCollectFile(Context context, ListView listView, List<DetailCategoryFileinfo> list, Fileinfo file, LinearLayout layout, TextView tvTitle, FrameLayout frameLayout, int fileType,
			Stack<Check> t)
	{
		mContext = context;
		mFileinfo = file;
		this.fileType = fileType;
		this.list = list;
		this.listView = listView;
		this.mSearchBar = frameLayout;
		mFileNoRecodeLayout = layout;
		checks = t;
		this.tvTitle = tvTitle;

	}

	public void init()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.cancel_collect);
		if (mFileinfo != null) {

			if (!mFileinfo.isDrectory()) {
				stringFormate = mContext.getResources().getString(R.string.delete_collect_dir);

				builder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
			}
			else {
				stringFormate = mContext.getResources().getString(R.string.delete_collect_file);
				builder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
			}
		}
		builder.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				delateCollectRocode();
				GetFileListTask mGetFileListTask = new GetFileListTask(listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, fileType, mSearchBar, checks, mContext, false, false);
				mGetFileListTask.execute();
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		builder.show();
	}

	private void delateCollectRocode()
	{
		CollectFileDao mDao = new CollectFileDao(mContext);
		mDao.delete(mFileinfo.getmPath());
	}
}
