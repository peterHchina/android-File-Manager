package com.example.managerfile.ui;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.zip.CheckedInputStream;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerfile.R;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.task.DeleteFileTask;
import com.example.managerfile.util.FileUtil;

public class FileMangerSettingActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{

	private String iPAddresString;
	private static final String SHARED_PREFS_NAME = "com.FileManger_preferences";
	private static final String SETTING_BACK_PATH = "com.FileManger.path";
	private static final String SETTING_HIDE_FILE = "com.FileManger.HideFile";
	private static final String SETTING_SHOW_IP = "com.FileManger.showIP";
	private static final String SETTING_SHOW_PHONE_STROAGE = "com.FileManger.showStroage";
	private static final String SETTING_LOCATION = "com.FileManger.HideFile";
	private static final String SETTING_CLEAR_DATA = "com.FileManger.clear";
	private static final String SETTING_SUGGESTION = "com.FileManger.suggestion";
	private SharedPreferences settingPreferences;
	private Preference setBackPath;
	private Preference setHideFile;
	private Preference setShowIp;
	private Preference update;
	private Preference suggest;
	private Preference clearData;
	private Context mContext;
	private String backupPathString;
	private EditText mEditText;
	private LayoutInflater inflater;
	private TextView tvTitle;
	private TextView mTextView;
	private LinearLayout btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager preferenceManager = getPreferenceManager();
		// preferenceManager.setSharedPreferencesName(SHARED_PREFS_NAME);
		// settingPreferences = getSharedPreferences(SHARED_PREFS_NAME,
		// MODE_PRIVATE);
		settingPreferences = preferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = settingPreferences.edit();
		setBackPath = (Preference) findPreference(SETTING_BACK_PATH);
		setHideFile = (CheckBoxPreference) findPreference(SETTING_HIDE_FILE);
		// setHideFile.setSelectable(settingPreferences.getBoolean(SETTING_LOCATION,
		// false));
		setShowIp = (Preference) findPreference(SETTING_SHOW_IP);
		clearData = (Preference) findPreference(SETTING_CLEAR_DATA);
		suggest = (Preference) findPreference(SETTING_SUGGESTION);
		mContext = this;
		inflater = LayoutInflater.from(mContext);
		View view1 = inflater.inflate(R.layout.include_micro_tecent_header, null, false);
		tvTitle = (TextView) view1.findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.setting);
		mTextView = (TextView) view1.findViewById(R.id.btnBack_text);
		mTextView.setText(R.string.back);
		btnBack = (LinearLayout) view1.findViewById(R.id.btnBack);
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		preferenceScreen.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		btnBack.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("isShowHideFile", settingPreferences.getBoolean(SETTING_HIDE_FILE, false));
				((Activity) mContext).setResult(RESULT_OK, intent);
				((Activity) mContext).finish();
			}
		});
		int height = mContext.getResources().getDimensionPixelSize(R.dimen.preference_title_height1);
		this.addContentView(view1, new LayoutParams(LayoutParams.MATCH_PARENT, height));
		backupPathString = settingPreferences.getString(SETTING_BACK_PATH, "AppBack");
		setBackPath.setSummary("/sdcard/" + backupPathString);
		setBackPath.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{

			@Override
			public boolean onPreferenceClick(Preference arg0)
			{
				// TODO Auto-generated method stub
				AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mContext);
				mDialogBuilder.setTitle(R.string.apk_files_back_up);
				View view = inflater.inflate(R.layout.item_set_backup_path, null, false);
				mEditText = (EditText) view.findViewById(R.id.backup_path);
				mEditText.setText(backupPathString);
				mDialogBuilder.setView(view);
				mDialogBuilder.setPositiveButton(R.string.ok, new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						// TODO Auto-generated method stub
						String tempPath = mEditText.getText().toString().trim();
						if (tempPath.length() > 0)
						{
							File file = new File("/sdcard/" + tempPath);
							if (file.exists())
							{
								Toast.makeText(mContext, R.string.error_path, Toast.LENGTH_SHORT).show();
							}
							else
							{
								file.mkdirs();
							}

							editor.putString(SETTING_BACK_PATH, mEditText.getText().toString().trim());
							editor.commit();
							setBackPath.setSummary("/sdcard/" + mEditText.getText().toString().trim());
						}
						else
						{
							Toast.makeText(mContext, R.string.error_input, Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
				mDialogBuilder.setNegativeButton(R.string.cancel, new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						// TODO Auto-generated method stub

					}
				});

				mDialogBuilder.show();
				return true;
			}
		});
		setShowIp.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{

			@Override
			public boolean onPreferenceClick(Preference arg0)
			{
				// TODO Auto-generated method stub
				AlertDialog.Builder diaBuilder = new AlertDialog.Builder(mContext);
				diaBuilder.setTitle(R.string.ip_adress);
				diaBuilder.setMessage(getIP());
				diaBuilder.setPositiveButton(R.string.ok, new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						// TODO Auto-generated method stub

					}
				});
				diaBuilder.show();
				return true;
			}
		});

		clearData.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{

			@Override
			public boolean onPreferenceClick(Preference arg0)
			{
				// TODO Auto-generated method stub
				AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
				mBuilder.setTitle(R.string.clear_back_up);
				mBuilder.setMessage(R.string.clear_all_back_data);
				mBuilder.setPositiveButton(R.string.ok, new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						// TODO Auto-generated method stub
                       String path="/sdcard/"+settingPreferences.getString(SETTING_BACK_PATH, "AppBack");
						if (path != null)
						{
							Fileinfo tempFileinfo = new Fileinfo(path);
							DeleteFileTask mDeleteFileTask = new DeleteFileTask(tempFileinfo, mContext);
							mDeleteFileTask.execute();
						}
						else
						{
							return;
						}
					}
				});
				mBuilder.setNegativeButton(R.string.cancel, new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						// TODO Auto-generated method stub

					}
				});
				mBuilder.show();
				return true;
			}
		});
		suggest.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{

			@Override
			public boolean onPreferenceClick(Preference arg0)
			{
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(mContext, UserSuggestionActivity.class);
				mContext.startActivity(intent);
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void GetIpAddress()
	{
		try
		{
			Socket socket = new Socket("www.baidu.com", 80);
			iPAddresString = socket.getLocalAddress().toString();
			Log.i("ip-----------------", ":" + iPAddresString);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// throw new RuntimeException();
		}
	}

	private String getIp()
	{
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();

		// 格式化IP address，例如：格式化前：1828825280，格式化后：192.168.1.109
		String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
		return ip;

	}

	private String getIP()
	{
		String IP = null;
		StringBuilder IPStringBuilder = new StringBuilder();
		try
		{
			Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements())
			{
				NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
				Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
				while (inetAddressEnumeration.hasMoreElements())
				{
					InetAddress inetAddress = inetAddressEnumeration.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
					{
						IPStringBuilder.append(inetAddress.getHostAddress().toString() + "\n");
					}
				}
			}
		}
		catch (SocketException ex)
		{

		}
		IP = IPStringBuilder.toString();
		return IP;
	}

	public void onBackPressed()
	{

		Intent intent = new Intent();
		intent.putExtra("isShowHideFile", settingPreferences.getBoolean(SETTING_HIDE_FILE, false));
		((Activity) mContext).setResult(RESULT_OK, intent);
		((Activity) mContext).finish();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		// TODO Auto-generated method stub

	}
}
