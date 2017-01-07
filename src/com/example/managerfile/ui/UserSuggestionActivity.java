package com.example.managerfile.ui;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.managerfile.R;

public class UserSuggestionActivity extends Activity implements OnClickListener
{

	private final static int MSG_BASE = 0;
	private final static int MSG_COMMIT_COMMENT = MSG_BASE + 1;
	private final static int MSG_COMMIT_COMMENT_SUCCESS = MSG_BASE + 2;
	private final static int MSG_COMMIT_COMMENT_FAILED = MSG_BASE + 3;

	private EditText mSuggestionInput = null;
	private EditText mContactInput = null;

	private Context mContext = null;
	private Button mCommitBtn = null;

	// private BackgroundHandler mBackgroundHandler = null;
	private HandlerThread mBackgroundThread = null;
	private UIHandler mUIHandler = null;
	private static final int DLG_LOADING = 0;
	private LayoutInflater mInflater = null;
	private AlertDialog mProgressDialog = null;
	private boolean mWaitingForResult = false;
	private TextView tvTitle;
	private TextView mTextView;
	private LinearLayout btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setBackgroundDrawableResource(android.R.color.white);
		setContentView(R.layout.user_suggestion_activity);
		mContext = this;
		mInflater = LayoutInflater.from(mContext);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.suggest);
		mTextView = (TextView) findViewById(R.id.btnBack_text);
		mTextView.setText(R.string.back);
		btnBack = (LinearLayout) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				((Activity) mContext).finish();
			}
		});
		mSuggestionInput = (EditText) findViewById(R.id.suggestion_text);
		mContactInput = (EditText) findViewById(R.id.contact_text);
		mCommitBtn = (Button) findViewById(R.id.commit_btn);
		mCommitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if (mWaitingForResult)
		{
			return;
		}
		if (mBackgroundThread == null)
		{
			mBackgroundThread = new HandlerThread("com.vivo.game.FEED_BACK_COMMENT");
			mBackgroundThread.start();
			// mBackgroundHandler = new BackgroundHandler(
			// mBackgroundThread.getLooper());
		}
		if (mUIHandler == null)
		{
			mUIHandler = new UIHandler(getMainLooper());
		}
		mWaitingForResult = true;
		checkInfo();
	}

	private void checkInfo()
	{
		String sugInputText = mSuggestionInput.getEditableText().toString().trim();
		String contactInputText = mContactInput.getEditableText().toString().trim();
		int sugSize = 0;
		int contactSize = 0;
		if (sugInputText != null)
		{
			sugSize = sugInputText.toString().trim().length();
			if (sugSize <= 0)
			{
				mWaitingForResult = false;
				Toast.makeText(mContext, R.string.suggestion_tips, Toast.LENGTH_SHORT).show();
				return;
			}
		}
		else
		{
			mWaitingForResult = false;
			Toast.makeText(mContext, R.string.suggestion_tips, Toast.LENGTH_SHORT).show();
			return;
		}
		if (contactInputText != null)
		{
			contactSize = contactInputText.trim().length();
			if (contactSize <= 0)
			{
				mWaitingForResult = false;
				Toast.makeText(mContext, R.string.contact_tips, Toast.LENGTH_SHORT).show();
				return;
			}
		}
		else
		{
			mWaitingForResult = false;
			Toast.makeText(mContext, R.string.contact_tips, Toast.LENGTH_SHORT).show();
			return;
		}
		if (sugInputText != null && sugSize > 0 && contactInputText != null && contactSize > 0)
		{
			if (emailFormat(contactInputText) || isMobileNum(contactInputText))
			{
				showLoadingMsg();
				// mBackgroundHandler.sendEmptyMessage(MSG_COMMIT_COMMENT);
			}
			else
			{
				mWaitingForResult = false;
				Toast.makeText(mContext, R.string.contact_check, Toast.LENGTH_SHORT).show();
				return;
			}

		}

	}

	// private void commit() {
	// String sugInputText =
	// mSuggestionInput.getEditableText().toString().trim();
	// String contactInputText =
	// mContactInput.getEditableText().toString().trim();
	// HashMap<String, String> params = new HashMap<String, String>();
	// params.put(DataLoader.SUGGESTION_CONTENT, sugInputText);
	// params.put(DataLoader.CONTACT_CONTENT, contactInputText);
	// mHttpConnect.connect(DataLoader.USER_SUGGESTION_URL,
	// params,HttpConnect.CONNECT_TYPE_HTTPCLIENT_POST, 3, null,new
	// PostSuggestionResponed());
	// }

	public Dialog onCreateDialog(int id)
	{
		if (id == DLG_LOADING)
		{
			View progressDialogView = mInflater.inflate(R.layout.progress_dialog_vivo, null, false);
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setView(progressDialogView);
			TextView messageView = (TextView) progressDialogView.findViewById(R.id.message);
			messageView.setText(mContext.getString(R.string.commiting_wait));
			mProgressDialog = builder.create();
			mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				public void onDismiss(DialogInterface dialog)
				{
					mProgressDialog = null;
				}
			});
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					mProgressDialog = null;
					dismissLoadingMsg();
				}
			});
			mProgressDialog.setOnShowListener(new DialogInterface.OnShowListener()
			{
				public void onShow(DialogInterface dialog)
				{
				}
			});
			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
			{
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
				{
					return false;
				}
			});
			mProgressDialog.show();
			return mProgressDialog;
		}
		return null;
	}

	private void showLoadingMsg()
	{
		showDialog(DLG_LOADING);
	}

	private void dismissLoadingMsg()
	{
		try
		{
			dismissDialog(DLG_LOADING);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}

	// private class PostSuggestionResponed implements HttpResponed {
	//
	// @Override
	// public void respond(HttpConnect connect, Object connId, int
	// connStatus,String in) {
	// // TODO Auto-generated method stub
	// mWaitingForResult = false;
	// try {
	// JSONObject json = new JSONObject(in);
	// boolean result = JsonParser.getBoolean(BaseParse.BASE_RESULT,json);
	// String message = JsonParser.getRawString(BaseParse.BASE_MSG,json);
	// if (connStatus == HttpResponed.CONNECT_SUCCESS) {
	// if (result) {
	// mUIHandler.sendEmptyMessage(MSG_COMMIT_COMMENT_SUCCESS);
	// } else {
	// Message msg = mUIHandler.obtainMessage();
	// msg.what = MSG_COMMIT_COMMENT_FAILED;
	// msg.obj = message;
	// mUIHandler.sendMessage(msg);
	// }
	// connect.disconnect();
	// } else {
	// message = getString(R.string.commit_failed_network);
	// Message msg = mUIHandler.obtainMessage();
	// msg.what = MSG_COMMIT_COMMENT_FAILED;
	// msg.obj = message;
	// mUIHandler.sendMessage(msg);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// String message = getString(R.string.commit_failed_network);
	// Message msg = mUIHandler.obtainMessage();
	// msg.what = MSG_COMMIT_COMMENT_FAILED;
	// msg.obj = message;
	// mUIHandler.sendMessage(msg);
	// }
	//
	// }
	//
	// }

	// private class BackgroundHandler extends Handler {
	//
	// public BackgroundHandler(Looper looper) {
	// super(looper);
	// }
	//
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case MSG_COMMIT_COMMENT:
	// commit();
	// break;
	// default:
	// break;
	// }
	// }
	// }

	private class UIHandler extends Handler
	{

		public UIHandler(Looper looper)
		{
			super(looper);
		}

		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case MSG_COMMIT_COMMENT_SUCCESS:
				dismissLoadingMsg();
				String successMsg = mContext.getResources().getString(R.string.commit_success_msg);
				Toast t = Toast.makeText(mContext, successMsg, Toast.LENGTH_SHORT);
				t.show();
				finish();
				break;
			case MSG_COMMIT_COMMENT_FAILED:
				dismissLoadingMsg();
				String errorMsg = null;
				if (msg.obj == null)
				{
					errorMsg = mContext.getResources().getString(R.string.commit_failed_network);
				}
				else
				{
					errorMsg = (String) msg.obj;
				}
				Toast toast = Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT);
				toast.show();
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		if (mBackgroundThread != null)
		{
			mBackgroundThread.quit();
			mBackgroundThread = null;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		// mHttpConnect.disconnect();
	}

	private boolean emailFormat(String mail)
	{
		Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher mc = pattern.matcher(mail);
		return mc.matches();
	}

	public boolean isMobileNum(String num)
	{
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(num);
		return m.matches();
	}
}
