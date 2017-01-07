package com.example.managerfile.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.managerfile.R;
import com.example.managerfile.task.SearchFileTask;
import com.example.managerfile.ui.barcode.CaptureActivity;

public class FileMangerSearchActivity extends Activity implements OnClickListener, TextWatcher, OnEditorActionListener,
		OnItemClickListener, OnTouchListener
{

	public static final String TAG = "SearchListActivity";

	public static final int MSG_BASE = 0;
	public static final int MSG_LOAD_DATA_COMPLETE = MSG_BASE + 1;
	public static final int MSG_LOAD_DATA_FAILED = MSG_BASE + 2;
	public static final String SUGGESTION_WORD = "word";

	private EditText mSearchInputEdit = null;
	private ImageView mDeleteBtn = null;
	private TextView mSearchBtn = null;
	private ImageView mBarcodeBtn = null;

	private Context mContext = null;

	private ArrayList<String> sugList = null;
	private ArrayList<String> historyList = null;
	private LinearLayout mContent = null;
	private InputMethodManager mInputMethodManager = null;;
	private ContentResolver cr = null;
	private LayoutInflater mInflater = null;

	public static int NUM_COLUMN = 4;
	private boolean mFirstLoaded = false;

	private static boolean isRegisted = false;
	private Cursor dataCursor = null;

	private final static int TOKEN_DATA_QUERY = 0;

	private String mInputMethod = "setDropDownAlwaysVisible";
	private TextView mHotListTitle = null;
	private int line = 0;
	private String hotWord = null;
	private SharedPreferences prefs = null;
	private String defaultWords = null;
	private static final int DLG_CLEAR_HISTORY = 0;
	private boolean firstBack = true;
	private ListView mHistoryListView = null;

	private boolean mFirstQuery = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.search_list_activity);
		mContext = this;
		mInflater = LayoutInflater.from(mContext);
		mContent = (LinearLayout) findViewById(R.id.content);
		mSearchInputEdit = (EditText) findViewById(R.id.search_key_word);
		mHistoryListView = (ListView) findViewById(R.id.pop_list);
		mHistoryListView.setOnTouchListener(this);
		mSearchInputEdit.setOnEditorActionListener(this);
		mDeleteBtn = (ImageView) findViewById(R.id.close_btn);
		mDeleteBtn.setOnClickListener(this);
		mSearchBtn = (TextView) findViewById(R.id.search_btn);
		String able = mContext.getResources().getConfiguration().locale.getCountry();
		if (!able.equals("CN"))
		{
			mSearchBtn.setTextSize(mContext.getResources().getDimension(R.dimen.hotword_fontsize_en));
		}

		mSearchBtn.setOnClickListener(this);
		mBarcodeBtn = (ImageView) findViewById(R.id.barcode_btn);
		mBarcodeBtn.setOnClickListener(this);

		mSearchInputEdit.setOnClickListener(this);

		Log.i(TAG, "hotWord = " + hotWord);
		defaultWords = mContext.getResources().getString(R.string.search_hide);
		if (hotWord != null && !hotWord.equals(defaultWords))
		{
			mSearchBtn.setEnabled(true);
		}
		else
		{
			mSearchBtn.setEnabled(false);
		}
		mSearchInputEdit.setHint(hotWord);

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		String key = mSearchInputEdit.getEditableText().toString().trim();
		if (v == mDeleteBtn)
		{
			mSearchInputEdit.setText(null);
			if (mInputMethodManager == null)
			{
				mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			}
			mInputMethodManager.showSoftInput(mSearchInputEdit, InputMethodManager.SHOW_FORCED);
		}
		else if (v == mSearchBtn)
		{
			if (key == null || key.toString().trim().length() <= 0)
			{
				defaultWords = mContext.getResources().getString(R.string.search_hide);

			}
			else
			{
				if (key.contains("/"))
				{
					Toast.makeText(mContext, R.string.error_string, Toast.LENGTH_SHORT).show();
					return;
				}
				new SearchFileTask(null, key, mContext);
			}

		}
		else if (v == mBarcodeBtn)
		{
			Intent intent = new Intent();
			intent.setClass(this, CaptureActivity.class);
			startActivity(intent);

		}
		else if (v == mSearchInputEdit)
		{
			Log.i(TAG, "key = " + key);
			if (key == null || key.length() == 0)
			{

			}
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		// TODO Auto-generated method stub
		if (s.toString().trim().length() <= 0)
		{
			mDeleteBtn.setVisibility(View.GONE);
			mHistoryListView.setVisibility(View.GONE);
			mSearchBtn.setEnabled(false);
			// mHotListTitle.setText(R.string.hot_list_title);
			defaultWords = mContext.getResources().getString(R.string.search_hide);
			if (hotWord != null && !hotWord.equals(defaultWords))
			{
				mSearchBtn.setEnabled(true);
			}
			else
			{
				mSearchBtn.setEnabled(false);
			}

		}
		else
		{
			String keywords = mSearchInputEdit.getEditableText().toString().trim();
			mHistoryListView.setAdapter(null);
			mSearchBtn.setEnabled(true);
			mDeleteBtn.setVisibility(View.VISIBLE);
			mDeleteBtn.setEnabled(true);

		}

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mSearchInputEdit.addTextChangedListener(this);
	}

}
