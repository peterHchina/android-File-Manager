package com.example.managerfile.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.zip.Inflater;

import org.apache.http.impl.client.TunnelRefusedException;

import com.example.managerfile.R;
import com.example.managerfile.R.layout;
import com.example.managerfile.R.menu;
import com.example.managerfile.adapter.ActionAdapter;
import com.example.managerfile.adapter.GridAdapter;
import com.example.managerfile.adapter.ListAdapter;
import com.example.managerfile.adapter.PasteGridAdapter;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.task.CopyListFileTask;
import com.example.managerfile.task.CopySingleFileTask;
import com.example.managerfile.task.DeleteGridViewItemTask;
import com.example.managerfile.task.RefleshTheListDataTask;
import com.example.managerfile.task.SearchFileTask;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.OpenFile;
import com.example.managerfile.util.SortChineseFileName;
import com.example.managerfile.model.MemoryCountAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.example.managerfile.ui.DialogMenuActivity;
import com.example.managerfile.ui.barcode.CaptureActivity;
import com.example.managerfile.ui.operate.DialogCompressFiles;
import com.example.managerfile.ui.operate.DialogCopyFile;
import com.example.managerfile.ui.operate.DialogCreateNewFileOrDirectory;
import com.example.managerfile.ui.operate.DialogCutFile;
import com.example.managerfile.ui.operate.DialogDeleteFile;
import com.example.managerfile.ui.operate.DialogShowPhoneStroageState;
import com.example.managerfile.ui.operate.DialogSortFile;

public class FileManagerActivity extends Activity
{

	public ListView mListView;
	public GridView mGridView;
	private String rootPath;
	private File root;
	private File currentParent;
	private File[] currentFiles;
	private int mCount = 0;
	public ListAdapter mListAdapter;
	public GridAdapter mGridAdapter;
	private Context mContext;
	private boolean isShowHideFile;
	private boolean isSwitchStute;
	private static FileManagerActivity instance;
	private LinearLayout mFileNoRecodeLayout;
	private TextView mFilePath;
	private int ListPosition = 0;
	private int ListTop = 0;
	public RefleshTheListDataTask refleshTheListDataTask;
	public List<Fileinfo> listFileinfos = new ArrayList<Fileinfo>();
	private Stack<MemoryCountAdapter> mStack;
	public MemoryCountAdapter memoryCount;
	private SharedPreferences settingPreferences;
	private static final String SHARED_PREFS_NAME = "com.FileManger_preferences";
	private static final String SETTING_HIDE_FILE = "com.FileManger.HideFile";
	private static final String SETTING_SWITCH_STAUTE = "com.FileManger.Switch";
	private SharedPreferences.Editor editor;
	private int sortModel = 0;
	private PopupWindow mPopupWindow;
	private int height;
	private EditText mSearchInputEdit = null;
	private ImageView mDeleteBtn = null;
	private TextView mSearchBtn = null;
	private ImageView mBarcodeBtn = null;
	private String defaultWords = null;
	private InputMethodManager mInputMethodManager = null;
	private String key;
	public LinearLayout mPasteBodeLayout;
	public Button mPasteButton;
	public Button mClearButton;
	public GridView mPasteGridView;
	public List<Fileinfo> mFileinfos = new ArrayList<Fileinfo>();
	public LinearLayout mHidePasteBodeLayout;
	private ImageView mUpImageView;
	private ImageView mDownImageView;
	private boolean State = false;
	public PasteGridAdapter adapter = null;
	private String dstPath = "AppBack";
	private static final String SETTING_BACK_PATH = "com.FileManger.path";
	public LinearLayout mOpearte;
	public Button mActionButton;
	public Button mChoiceAll;
	public Button mCancelButton;
	public Boolean isEdit = false;
	public boolean isClickAll = false;
	private PopupWindow pwMyPopWindow;// popupwindow
	private ListView lvPopupList;// popupwindow中的ListView
	private final int NUM_OF_VISIBLE_LIST_ROWS = 4;
	private LinearLayout mAdLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		settingPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		isShowHideFile = settingPreferences.getBoolean(SETTING_HIDE_FILE, false);
		editor = settingPreferences.edit();
		editor.putString(SETTING_BACK_PATH, dstPath);
		editor.commit();
		intpararm();

	}

	public void intpararm()
	{
		if (mInputMethodManager == null) {
			mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		mListView = (ListView) findViewById(R.id.files_list);
		mGridView = (GridView) findViewById(R.id.files_grid);
		mFilePath = (TextView) findViewById(R.id.file_path);
		mStack = new Stack<MemoryCountAdapter>();
		mPasteBodeLayout = (LinearLayout) findViewById(R.id.paste_bode);
		mPasteButton = (Button) findViewById(R.id.paste_button);
		mOpearte = (LinearLayout) findViewById(R.id.opearte);
		mOpearte.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub

			}
		});
		mCancelButton = (Button) findViewById(R.id.cancel);
		mCancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub

				// if (!mListAdapter.isEdit)
				// {
				mOpearte.setVisibility(View.GONE);
				mListAdapter.isEdit = !isEdit;
				isEdit = !isEdit;
				mListAdapter.MakeAllItemsUnselected();
				mGridAdapter.MakeAllItemsUnselected();
				if (!State) {

					int on = mListAdapter.getCheckedItemsNumber();
					if (on == 0) {
						mActionButton.setClickable(false);
						mActionButton.setBackgroundResource(R.drawable.search_btn_disable);
					}
					else {
						mActionButton.setClickable(true);
						mActionButton.setBackgroundResource(R.drawable.btn_opearte_bg);
					}
				}
				else {
					int on = mListAdapter.getCheckedItemsNumber();
					if (on == 0) {
						mActionButton.setClickable(false);
					}
					else {
						mActionButton.setClickable(true);
					}
				}
			}
		});
		mChoiceAll = (Button) findViewById(R.id.all_choice);
		mChoiceAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				if (!isClickAll) {
					mListAdapter.MakeAllItemsSelected();
					mGridAdapter.MakeAllItemsSelected();
					mChoiceAll.setText(mContext.getResources().getString(R.string.choice_all_not));
					isClickAll = !isClickAll;
				}
				else {
					mListAdapter.MakeAllItemsUnselected();
					mGridAdapter.MakeAllItemsUnselected();
					isClickAll = !isClickAll;
					mChoiceAll.setText(mContext.getResources().getString(R.string.choice_all));

				}

				if (!State) {

					int on = mListAdapter.getCheckedItemsNumber();
					if (on == 0) {
						mActionButton.setClickable(false);
					}
					else {
						mActionButton.setClickable(true);
					}
				}
				else {
					int on = mGridAdapter.getCheckedItemsNumber();
					if (on == 0) {
						mActionButton.setClickable(false);
					}
					else {
						mActionButton.setClickable(true);
					}
				}
			}
		});
		mActionButton = (Button) findViewById(R.id.action);
		mActionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				ActionPopWindows();
				if (pwMyPopWindow.isShowing()) {

					pwMyPopWindow.dismiss();// 关闭
				}
				else {

					pwMyPopWindow.showAsDropDown(mActionButton);// 显示
				}

			}
		});
		mPasteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				if (mFileinfos.size() != 0) {
					CopyListFileTask mCopyFileTask = new CopyListFileTask(mContext, mFileinfos, new Fileinfo(currentParent.getPath()), FileManagerActivity.this);
					mCopyFileTask.execute();
				}

			}
		});
		mClearButton = (Button) findViewById(R.id.clear_button);
		mClearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				mPasteBodeLayout.setVisibility(View.GONE);
				mFileinfos.clear();
			}
		});

		mPasteGridView = (GridView) findViewById(R.id.paste_grid);
		mPasteGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				final int count = position;
				AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
				mBuilder.setTitle(R.string.delete_paste_item);
				mBuilder.setItems(R.array.paste, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int which)
					{
						// TODO Auto-generated method stub

						switch (which)
						{
						case 0:
							CopySingleFileTask mCopySingleFileTask = new CopySingleFileTask(mContext, mFileinfos.get(count), new Fileinfo(currentParent.getPath()), FileManagerActivity.this);
							mCopySingleFileTask.execute();
							break;
						case 1:
							DeleteGridViewItemTask mDeleteGridViewItem = new DeleteGridViewItemTask(count, FileManagerActivity.this, mContext);
							mDeleteGridViewItem.execute();
							break;
						default:
							break;
						}
						dialogInterface.dismiss();

					}
				});
				mBuilder.show();
				return false;
			}
		});

		mHidePasteBodeLayout = (LinearLayout) findViewById(R.id.hide_paste);
		mUpImageView = (ImageView) findViewById(R.id.up);
		mDownImageView = (ImageView) findViewById(R.id.down);
		mHidePasteBodeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				if (State) {
					mUpImageView.setVisibility(View.VISIBLE);
					mDownImageView.setVisibility(View.INVISIBLE);
					mPasteGridView.setVisibility(View.GONE);
					State = !State;
				}
				else {
					mDownImageView.setVisibility(View.VISIBLE);
					mUpImageView.setVisibility(View.INVISIBLE);
					mPasteGridView.setVisibility(View.VISIBLE);
					State = !State;
				}
			}
		});
		isSwitchStute = settingPreferences.getBoolean(SETTING_SWITCH_STAUTE, true);
		memoryCount = new MemoryCountAdapter(0, 0);
		if (isSwitchStute) {
			mListView.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.GONE);
		}
		else {
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
		}
		mFileNoRecodeLayout = (LinearLayout) findViewById(R.id.empty_view);
		rootPath = Environment.getExternalStorageDirectory().getPath();
		root = new File(rootPath);
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			height = getResources().getDimensionPixelSize(x);
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		mContext = this;
		if (root.exists()) {
			currentParent = root;
			currentFiles = root.listFiles();
			if (currentFiles == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				View view = inflater.inflate(R.layout.sdcard_umount_state, null);
				Toast toast = new Toast(mContext);
				toast.setView(view);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
				finish();
			}
			FindAllFile(currentFiles);

		}

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub

				if (!isEdit) {

					if (listFileinfos.get(position).isDrectory()) {

						// File[] tem = new
						// File(listFileinfos.get(position).getmPath()).listFiles();
						File[] tem = GetCurrentFiles(listFileinfos.get(position).getmPath());
						// 获取用户单击的列表项对应的文件夹，设为当前的父文件夹
						currentParent = new File(listFileinfos.get(position).getmPath());
						// 保存当前的父文件夹内的全部文件和文件夹
						currentFiles = tem;
						ListPosition = mListView.getFirstVisiblePosition();
						Log.i("test1+++++++++++++++++++++++++++", ":" + ListPosition);
						View v = mListView.getChildAt(0);
						ListTop = (v == null) ? 0 : v.getTop();
						// ListTop=view.getScrollY();
						Log.i("test2+++++++++++++++++++++++++++", ":" + ListTop);
						memoryCount.setListPosition(0);
						memoryCount.setListTop(0);
						mStack.push(new MemoryCountAdapter(ListPosition, ListTop));
						// 再次更新ListView
						FindAllFile(currentFiles);

					}
					else {
						OpenFile.openFile(new File(listFileinfos.get(position).getmPath()), mContext);
					}
					mCount = 0;
				}
				else {
					if (!mListAdapter.listItems.get(position).isCheck()) {
						mListAdapter.listItems.get(position).setCheck(true);
					}
					else {
						mListAdapter.listItems.get(position).setCheck(false);
					}
					mListAdapter.notifyDataSetChanged();
					int on = mListAdapter.getCheckedItemsNumber();
					if (on == 0) {
						mActionButton.setClickable(false);
					}
					else {
						mActionButton.setClickable(true);
					}
				}
			}

		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				if (!isEdit) {
					if (listFileinfos.get(position).isDrectory()) {

						// File[] tem = new
						// File(listFileinfos.get(position).getmPath()).listFiles();
						File[] tem = GetCurrentFiles(listFileinfos.get(position).getmPath());
						// 获取用户单击的列表项对应的文件夹，设为当前的父文件夹
						currentParent = new File(listFileinfos.get(position).getmPath());
						// 保存当前的父文件夹内的全部文件和文件夹
						currentFiles = tem;
						// if (currentFiles.length == 0)
						// {
						// mFileNoRecodeLayout.setVisibility(View.VISIBLE);
						// mGridView.setVisibility(View.GONE);
						// }
						// 再次更新ListView
						ListPosition = mListView.getFirstVisiblePosition();
						View v = mListView.getChildAt(0);
						ListTop = (v == null) ? 0 : v.getTop();
						FindAllFile(currentFiles);

						memoryCount.setListPosition(0);
						memoryCount.setListTop(0);
						mStack.push(new MemoryCountAdapter(position, ListTop));
					}
					else {
						OpenFile.openFile(new File(listFileinfos.get(position).getmPath()), mContext);
					}
					mCount = 0;
				}
				else {
					if (!mGridAdapter.listItems.get(position).isCheck()) {
						mGridAdapter.listItems.get(position).setCheck(true);
					}
					else {
						mGridAdapter.listItems.get(position).setCheck(false);
					}
					mGridAdapter.notifyDataSetChanged();
					int on = mGridAdapter.getCheckedItemsNumber();
					if (on == 0) {
						mActionButton.setClickable(false);
					}
					else {
						mActionButton.setClickable(true);
					}
				}
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				if (!isEdit) {

					new ChoiceOprateDialog(mContext, listFileinfos.get(position), FileManagerActivity.this).initDialog();
				}
				return false;
			}
		});

		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				if (!isEdit) {

					new ChoiceOprateDialog(mContext, listFileinfos.get(position), FileManagerActivity.this).initDialog();
				}
				return false;
			}
		});

	}

	private void FindAllFile(File[] files)
	{

		listFileinfos.clear();
		try {
			mFilePath.setText(currentParent.getCanonicalPath().toString());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (files == null) {
			mFileNoRecodeLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.GONE);
			return;
		}
		if (FileUtil.AfterDirectoryFileCount(files) == 0) {
			mFileNoRecodeLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.GONE);
			return;
		}
		refleshTheListDataTask = new RefleshTheListDataTask(listFileinfos, files, currentParent, isShowHideFile, FileManagerActivity.this, mContext, settingPreferences);
		refleshTheListDataTask.execute();

	}

	public void getParentsFile()
	{

		if (mFileNoRecodeLayout.getVisibility() == View.VISIBLE) {
			mFileNoRecodeLayout.setVisibility(View.GONE);
		}
		if (!isSwitchStute) {
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);

		}
		if (isSwitchStute) {
			mGridView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

		}
		try {
			if (!currentParent.getCanonicalPath().equals(Environment.getExternalStorageDirectory().getPath())) {

				// 获取上一级目录
				memoryCount = mStack.pop();
				currentParent = currentParent.getParentFile();
				// 列出当前目录下的所有文件
				currentFiles = currentParent.listFiles();
				// 再次更新ListView
				FindAllFile(currentFiles);

			}
			else {
				Toast.makeText(mContext, R.string.pressed_twice_outline, Toast.LENGTH_SHORT).show();
				mCount += 1;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_MENU && isEdit) {
			// 监控/拦截菜单键
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
		case R.id.create_settings:

			DialogCreateNewFileOrDirectory mCreateNewFileOrDirectory = new DialogCreateNewFileOrDirectory(mContext, currentParent, this);
			mCreateNewFileOrDirectory.init();
			break;
		case R.id.search_settings:
			SearchFile();
			break;
		case R.id.switch_settings:
			if (isSwitchStute) {
				// currentFiles = currentParent.listFiles();
				mListView.setVisibility(View.GONE);
				mGridView.setVisibility(View.VISIBLE);
				isSwitchStute = false;
				FindAllFile(currentFiles);
				editor.putBoolean(SETTING_SWITCH_STAUTE, isSwitchStute);
			}
			else {

				// currentFiles = currentParent.listFiles();
				mListView.setVisibility(View.VISIBLE);
				mGridView.setVisibility(View.GONE);
				isSwitchStute = true;
				FindAllFile(currentFiles);
				editor.putBoolean(SETTING_SWITCH_STAUTE, isSwitchStute);

			}
			editor.commit();
			break;

		case R.id.sector_settings:
			Sort();
			break;
		case R.id.action_settings:
			Setting();
			break;

		case R.id.action_editting:
			Editting();
		default:
			break;
		}
		return true;

	}

	public void ManagerApp()
	{
		Intent intent = new Intent();
		intent.setClass(mContext, ApkPackageManagerActivity.class);
		startActivity(intent);
	}

	public void Setting()
	{
		Intent intent = new Intent();
		intent.setClass(mContext, FileMangerSettingActivity.class);
		startActivityForResult(intent, 0);
	}

	public void Sort()
	{
		DialogSortFile mDialogSortFile = new DialogSortFile(mContext, FileManagerActivity.this, settingPreferences, currentParent);
		mDialogSortFile.init();
	}

	public void ShowStorageState()
	{
		DialogShowPhoneStroageState mDialogShowPhoneStroageState = new DialogShowPhoneStroageState(mContext);
		mDialogShowPhoneStroageState.init();
	}

	public void SearchFile()
	{
		// Intent intent = new Intent();
		// intent.setClass(mContext, FileMangerSearchActivity.class);
		// startActivity(intent);
		getPopupWindowInstance();
		mPopupWindow.showAtLocation(mFilePath, Gravity.TOP, 0, 400);
		mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void Editting()
	{
		mOpearte.setVisibility(View.VISIBLE);
		isEdit = true;
		mListAdapter.isEdit = isEdit;
		mListAdapter.notifyDataSetChanged();
		mGridAdapter.isEdit = isEdit;
		mGridAdapter.notifyDataSetChanged();
		mActionButton.setClickable(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0) {

			boolean isShowHideFile1 = data.getBooleanExtra("isShowHideFile", true);
			if (isShowHideFile != isShowHideFile1) {
				isShowHideFile = isShowHideFile1;
				FindAllFile(currentFiles);
			}

		}
	}

	public void onBackPressed()
	{
		// if (mPopupWindow != null)
		// {
		// mPopupWindow.setFocusable(false);
		// mPopupWindow.dismiss();
		//
		// }
		// else
		{
			getParentsFile();
			if (mCount >= 2) {

				finish();
			}
		}

	}

	public static FileManagerActivity getInstance()
	{
		if (instance == null) {
			instance = new FileManagerActivity();
		}
		return instance;
	}

	@Override
	public void finish()
	{
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onDestroy()
	{
		// 释放资源，原finalize()方法名修改为close()
		super.onDestroy();
	}

	public void reflashPath(String path)
	{
		if (path.isEmpty()) {
			return;
		}
		else {
			currentParent = new File(path);
			currentFiles = currentParent.listFiles();
			FindAllFile(currentFiles);
		}

	}

	private File[] GetCurrentFiles(String path)
	{
		if (isShowHideFile) {
			List<File> currentFiles = new ArrayList<File>();
			File[] files = new File(path).listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isHidden()) {
					continue;
				}
				currentFiles.add(files[i]);
			}

			return (File[]) currentFiles.toArray(new File[currentFiles.size()]);
		}
		else {
			return new File(path).listFiles();
		}
	}

	/*
	 * 获取PopupWindow实例
	 */
	private void getPopupWindowInstance()
	{
		if (null != mPopupWindow) {
			mPopupWindow.dismiss();
			return;
		}
		else {
			initPopuptWindow();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();

	}

	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow()
	{
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.search_pop_windows, null);
		LinearLayout blankLinearLayout = (LinearLayout) popupWindow.findViewById(R.id.pop_windows_other);
		blankLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				mPopupWindow.dismiss();
			}
		});
		mSearchInputEdit = (EditText) popupWindow.findViewById(R.id.search_key_word);
		mSearchInputEdit.setFocusable(true);
		key = mSearchInputEdit.getEditableText().toString().trim();
		defaultWords = mContext.getResources().getString(R.string.search_hide);
		mSearchInputEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2)
			{
				// TODO Auto-generated method stub
				return false;
			}
		});
		mDeleteBtn = (ImageView) popupWindow.findViewById(R.id.close_btn);
		mDeleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				mSearchInputEdit.setText(null);
				if (mInputMethodManager == null) {
					mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				}
				mInputMethodManager.showSoftInput(mSearchInputEdit, InputMethodManager.SHOW_FORCED);
				mSearchInputEdit.setHint(defaultWords);
			}
		});
		mSearchBtn = (TextView) popupWindow.findViewById(R.id.search_btn);
		String able = mContext.getResources().getConfiguration().locale.getCountry();
		if (!able.equals("CN")) {
			mSearchBtn.setTextSize(mContext.getResources().getDimension(R.dimen.hotword_fontsize_en));
		}

		mSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				String keywords = mSearchInputEdit.getEditableText().toString().trim();
				if (keywords.length() > 0) {
					mPopupWindow.dismiss();
					new SearchFileTask(currentParent.getPath(), keywords, mContext).execute();

				}
				else {
					Toast.makeText(mContext, R.string.error_blank, Toast.LENGTH_SHORT).show();
					return;
				}

			}
		});
		mBarcodeBtn = (ImageView) popupWindow.findViewById(R.id.barcode_btn);
		mBarcodeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, CaptureActivity.class);
				startActivity(intent);
			}
		});

		mSearchInputEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub

				if (key == null || key.length() == 0) {

				}
			}
		});
		mSearchInputEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub
				if (s.toString().trim().length() <= 0) {
					mDeleteBtn.setVisibility(View.GONE);
					mSearchBtn.setEnabled(false);
					mSearchBtn.setEnabled(false);

				}
				else {
					mSearchBtn.setEnabled(true);
					mDeleteBtn.setVisibility(View.VISIBLE);
					mDeleteBtn.setEnabled(true);

				}
			}
		});

		// 创建一个PopupWindow
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		mPopupWindow = new PopupWindow(popupWindow, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight() - height);
		mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// mInputMethodManager.showSoftInput(mSearchInputEdit,
		// InputMethodManager.SHOW_FORCED);

	}

	public void ActionPopWindows()
	{
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View layout = layoutInflater.inflate(R.layout.action_pop_windows, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		String[] itemsStrings = mContext.getResources().getStringArray(R.array.action);
		ActionAdapter actionAdapter = new ActionAdapter(mContext, itemsStrings);
		lvPopupList.setAdapter(actionAdapter);
		pwMyPopWindow = new PopupWindow(layout);
		pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		// 控制popupwindow的宽度和高度自适应
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth() + 300);
		pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 10) * NUM_OF_VISIBLE_LIST_ROWS);

		// 控制popupwindow点击屏幕其他地方消失
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);
		int on = mListAdapter.getCheckedItemsNumber();
		if (on == 0) {
			mActionButton.setClickable(false);
		}
		else {
			mActionButton.setClickable(true);
		}
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				List<Fileinfo> mList = mListAdapter.GetCheckItems();
				switch (position)
				{
				case 0:
					mFileinfos = mList;
					new DialogCopyFile(mContext, mFileinfos, FileManagerActivity.this).init1();
					mOpearte.setVisibility(View.GONE);
					mListAdapter.isEdit = !isEdit;
					mGridAdapter.isEdit = !isEdit;
					isEdit = !isEdit;
					mListAdapter.MakeAllItemsUnselected();
					mGridAdapter.MakeAllItemsUnselected();
					break;
				case 1:
					new DialogDeleteFile(mContext, mList, FileManagerActivity.this).init();
					mOpearte.setVisibility(View.GONE);
					mListAdapter.isEdit = !isEdit;
					mGridAdapter.isEdit = !isEdit;
					isEdit = !isEdit;
					mListAdapter.MakeAllItemsUnselected();
					mGridAdapter.MakeAllItemsUnselected();
					break;
				case 2:
					mFileinfos = mList;
					Log.i("size+++++++++++++++++++++++++++++++++++++++++++++++", ":" + mFileinfos.size());
					new DialogCutFile(mContext, mFileinfos, FileManagerActivity.this).init1();
					mOpearte.setVisibility(View.GONE);
					mListAdapter.isEdit = !isEdit;
					mGridAdapter.isEdit = !isEdit;
					isEdit = !isEdit;
					mListAdapter.MakeAllItemsUnselected();
					mGridAdapter.MakeAllItemsUnselected();
					break;
				case 3:
					new DialogCompressFiles(mContext, mList, FileManagerActivity.this).init();
					break;
				case 4:

					break;
				default:
					break;
				}
				pwMyPopWindow.dismiss();

			}

		});
	}

	private List<Fileinfo> GetChoicedItemsIsCut(List<Fileinfo> mList)
	{
		if (mList == null || mList.size() == 0) {
			return null;
		}

		for (int i = 0; i < mList.size(); i++) {
			mList.get(i).setCut(true);
		}
		return mList;
	}
}
