package com.example.managerfile.ui;

import java.io.File;
import java.util.List;
import java.util.Stack;

import com.example.managerfile.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerfile.ui.operate.DialogCompressFiles;

import com.example.managerfile.ui.operate.DialogCancelCollectFile;
import com.example.managerfile.ui.operate.DialogDeleteFile;
import com.example.managerfile.ui.operate.DialogRenameFile;
import com.example.managerfile.ui.operate.DialogShareFile;
import com.example.managerfile.ui.operate.DialogShowFileAttribute;
import com.example.managerfile.model.Check;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.provider.common.CollectFileDao;

public class CollectionFileOprateDialog
{
	private Context mContext;
	String[] mMenu;
	private File mFile;
	private Fileinfo mFileinfo;
	private DetailCategoryFileinfo mDetailCategoryFileinfo;
	int res;
	private List<DetailCategoryFileinfo> list;
	CollectFileDao collectFileDao;
	private int fileType;
	private ListView listView;
	private LinearLayout mFileNoRecodeLayout;
	private TextView tvTitle;
	private FrameLayout mSearchBar;
	private Stack<Check> checks;
	private SharedPreferences settingPreferences;

	public CollectionFileOprateDialog(Context context, ListView listView, List<DetailCategoryFileinfo> list, DetailCategoryFileinfo file, LinearLayout layout, TextView tvTitle,
			FrameLayout frameLayout, int fileType, Stack<Check> t, SharedPreferences sharedPreferences)
	{
		mContext = context;
		mDetailCategoryFileinfo = file;
		mFileinfo = new Fileinfo(mDetailCategoryFileinfo.getPath());
		collectFileDao = new CollectFileDao(mContext);
		this.fileType = fileType;
		this.listView = listView;
		this.list = list;
		checks = t;
		this.tvTitle = tvTitle;
		mFileNoRecodeLayout = layout;
		mSearchBar = frameLayout;
		settingPreferences = sharedPreferences;
		if (mFileinfo.isDrectory()) {
			if (fileType == 8) {
				mMenu = mContext.getResources().getStringArray(R.array.menu5);
			}
			else {
				mMenu = mContext.getResources().getStringArray(R.array.menu4);
			}
		}
		else {
			if (fileType == 8) {
				mMenu = mContext.getResources().getStringArray(R.array.menu3);
			}
			else {
				mMenu = mContext.getResources().getStringArray(R.array.menu2);
			}
		}

	}

	public int initDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(mFileinfo.getmName());
		if (mFileinfo.isDrectory() && fileType == 8) {
			dialog.setItems(mMenu, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dia, int which)
				{
					// TODO Auto-generated method stub
					switch (which)
					{

					case 0:
						new DialogDeleteFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, mSearchBar, fileType, checks).init2();
						break;
					case 1:
						new DialogCompressFiles(mContext, mFileinfo).init2();
						break;
					case 2:
						new DialogRenameFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, mSearchBar, fileType, checks).init1();
						break;
					case 3:
						new DialogCancelCollectFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, mSearchBar, fileType, checks).init();
						break;
					case 4:
						new DialogShowFileAttribute(mContext, mFileinfo).init();
						break;
					default:
						break;
					}

				}
			});
		}
		else if (fileType == 8) {
			dialog.setItems(mMenu, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dia, int which)
				{
					// TODO Auto-generated method stub
					switch (which)
					{

					case 0:
						new DialogDeleteFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, mSearchBar, fileType, checks).init2();
						break;
					case 1:
						new DialogShareFile(mContext, mFileinfo).init();
						break;
					case 2:
						new DialogCompressFiles(mContext, mFileinfo).init2();
						break;
					case 3:
						new DialogRenameFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, mSearchBar, fileType, checks).init1();
						break;
					case 4:
						new DialogCancelCollectFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, mSearchBar, fileType, checks).init();
						break;
					case 5:
						new DialogShowFileAttribute(mContext, mFileinfo).init();
						break;
					default:
						break;
					}

				}
			});
		}
		else if (mFileinfo.isDrectory() && fileType != 8) {
			dialog.setItems(mMenu, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dia, int which)
				{
					// TODO Auto-generated method stub
					switch (which)
					{

					case 0:
						new DialogDeleteFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, mSearchBar, checks, settingPreferences).init3();
						break;
					case 1:
						new DialogCompressFiles(mContext, mSearchBar, list, listView, mFileinfo.getmPath(), mFileNoRecodeLayout, settingPreferences, checks).init3();
						break;
					case 2:
						new DialogRenameFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, mSearchBar, checks, settingPreferences).init2();
						break;
					case 3:
						new DialogShowFileAttribute(mContext, mFileinfo).init();
						break;
					default:
						break;
					}

				}
			});
		}
		else {
			dialog.setItems(mMenu, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dia, int which)
				{
					// TODO Auto-generated method stub
					switch (which)
					{

					case 0:
						new DialogDeleteFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, mSearchBar, checks, settingPreferences).init3();
						break;
					case 1:
						new DialogShareFile(mContext, mFileinfo).init();
						break;
					case 2:
						new DialogCompressFiles(mContext, mSearchBar, list, listView, mFileinfo.getmPath(), mFileNoRecodeLayout, settingPreferences, checks).init3();
						break;
					case 3:
						new DialogRenameFile(mContext, listView, list, mFileinfo, mFileNoRecodeLayout, mSearchBar, checks, settingPreferences).init2();
						break;
					case 4:
						new DialogShowFileAttribute(mContext, mFileinfo).init();
						break;
					default:
						break;
					}

				}
			});
		}
		dialog.show();
		return res;
	}

	public void cancleCollectFile(Fileinfo fileinfo)
	{
		if (collectFileDao.delete(fileinfo.getmPath()) != -100) {
			Toast.makeText(mContext, R.string.cancel_collect_success, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(mContext, R.string.cancel_collect_fail, Toast.LENGTH_SHORT).show();
		}
	}
}
