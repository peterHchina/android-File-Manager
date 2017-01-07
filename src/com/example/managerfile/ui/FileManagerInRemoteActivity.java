package com.example.managerfile.ui;

import java.io.File;
import java.net.InetAddress;

import org.swift.ftp.Defaults;
import org.swift.ftp.Globals;
import org.swift.ftp.MyLog;
import org.swift.ftp.UiUpdater;

import com.example.managerfile.R;
import com.example.managerfile.service.FTPServerService;
import com.example.managerfile.ui.FileManagerTabActivity.IBackPressedListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FileManagerInRemoteActivity extends Activity implements IBackPressedListener
{
	private TextView ipText;
	private TextView instructionText;
	private TextView instructionTextPre;
	private View startStopButton;
	private Context mContext;
	protected MyLog myLog = new MyLog(this.getClass().getName());
	public Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_control_activity);
		mContext = FileManagerInRemoteActivity.this;
		handler = new Handler() {
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case 0: // We are being told to do a UI update
					// If more than one UI update is queued up, we only need to
					// do one.
					removeMessages(0);
					updateUi();
					break;
				case 1: // We are being told to display an error message
					removeMessages(1);
				}
			}
		};

		Context myContext = Globals.getContext();
		if (myContext == null) {
			myContext = this.getApplicationContext();
			if (myContext == null) {
				throw new NullPointerException("Null context!?!?!?");
			}
			Globals.setContext(myContext);
		}

		ipText = (TextView) findViewById(R.id.ip_address);
		instructionText = (TextView) findViewById(R.id.instruction);
		instructionTextPre = (TextView) findViewById(R.id.instruction_pre);
		startStopButton = findViewById(R.id.start_stop_button);
        startStopButton.setOnClickListener(startStopListener);
		updateUi();
		UiUpdater.registerClient(handler);
		
		// quickly turn on or off wifi.
		findViewById(R.id.wifi_state_image).setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onBack()
	{
		return false;
	}

	/**
	 * Whenever we regain focus, we should update the button text depending on
	 * the state of the server service.
	 */
	public void onStart()
	{
		super.onStart();
		UiUpdater.registerClient(handler);
		updateUi();
	}

	public void onResume()
	{
		super.onResume();

		UiUpdater.registerClient(handler);
		updateUi();
		// Register to receive wifi status broadcasts
		myLog.l(Log.DEBUG, "Registered for wifi updates");
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(wifiReceiver, filter);
	}

	/*
	 * Whenever we lose focus, we must unregister from UI update messages from
	 * the FTPServerService, because we may be deallocated.
	 */
	public void onPause()
	{
		super.onPause();
		UiUpdater.unregisterClient(handler);
		myLog.l(Log.DEBUG, "Unregistered for wifi updates");
		mContext.unregisterReceiver(wifiReceiver);
	}

	public void onStop()
	{
		super.onStop();
		UiUpdater.unregisterClient(handler);
	}

	public void onDestroy()
	{
		super.onDestroy();
		UiUpdater.unregisterClient(handler);
	}

	public void updateUi()
	{
		myLog.l(Log.DEBUG, "Updating UI", true);

		WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		int wifiState = wifiMgr.getWifiState();
		WifiInfo info = wifiMgr.getConnectionInfo();
		String wifiId = info != null ? info.getSSID() : null;
		boolean isWifiReady = FTPServerService.isWifiEnabled();
		setText(R.id.wifi_state, isWifiReady ? wifiId : getString(R.string.no_wifi_hint));
		if (!isWifiReady) {
		 findViewById(R.id.wifi_state1).setVisibility(View.VISIBLE);
		}
		ImageView wifiImg = (ImageView) findViewById(R.id.wifi_state_image);
		wifiImg.setImageResource(isWifiReady ? R.drawable.wifi_state4 : R.drawable.wifi_state0);

		boolean running = FTPServerService.isRunning();
		if (running) {
			myLog.l(Log.DEBUG, "updateUi: server is running", true);
			// Put correct text in start/stop button
			// Fill in wifi status and address
			InetAddress address = FTPServerService.getWifiIp();
			if (address != null) {
				String port = ":" + FTPServerService.getPort();
				ipText.setText("ftp://" + address.getHostAddress() + (FTPServerService.getPort() == 21 ? "" : port));

			}
			else {
				// could not get IP address, stop the service
				Context context = mContext.getApplicationContext();
				Intent intent = new Intent(context, FTPServerService.class);
				context.stopService(intent);
				ipText.setText("");
			}
		}

		startStopButton.setEnabled(isWifiReady);
		TextView startStopButtonText = (TextView) findViewById(R.id.start_stop_button_text);
		if (isWifiReady) {
			startStopButtonText.setText(running ? R.string.stop_server : R.string.start_server);
			startStopButton.setBackgroundResource(running ? R.drawable.btn_bottom_first : R.drawable.btn_bottom_last);
			startStopButtonText.setTextColor(running ? getResources().getColor(R.color.white) : getResources().getColor(R.color.white));
		}
		else {
			if (FTPServerService.isRunning()) {
				Context context = mContext.getApplicationContext();
				Intent intent = new Intent(context, FTPServerService.class);
				context.stopService(intent);
			}

			startStopButtonText.setText(R.string.no_wifi);
			startStopButtonText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			startStopButtonText.setTextColor(Color.GRAY);
		}

		ipText.setVisibility(running ? View.VISIBLE : View.INVISIBLE);
		instructionText.setVisibility(running ? View.VISIBLE : View.GONE);
		instructionTextPre.setVisibility(running ? View.GONE : View.VISIBLE);

	}

	private void setText(int id, String text)
	{
		TextView tv = (TextView) findViewById(id);
		tv.setText(text);
	}

	OnClickListener startStopListener = new OnClickListener() {

		@Override
		public void onClick(View arg0)
		{
			// TODO Auto-generated method stub
			Globals.setLastError(null);
			File chrootDir = new File(Defaults.chrootDir);
			if (!chrootDir.isDirectory())
				return;

			Context context = mContext.getApplicationContext();
			Intent intent = new Intent(context, FTPServerService.class);
			Globals.setChrootDir(chrootDir);
			if (!FTPServerService.isRunning()) {
				warnIfNoExternalStorage();

				if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
					startService(intent);

				}
			}
			else {
				stopService(intent);
			}

		}
	};

	private void warnIfNoExternalStorage()
	{
		String storageState = Environment.getExternalStorageState();
		if (!storageState.equals(Environment.MEDIA_MOUNTED)) {
			myLog.i("Warning due to storage state " + storageState);
			Toast toast = Toast.makeText(mContext, R.string.storage_warning, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
		public void onReceive(Context ctx, Intent intent)
		{
			myLog.l(Log.DEBUG, "Wifi status broadcast received");
			updateUi();
		}
	};

	boolean requiredSettingsDefined()
	{
		SharedPreferences settings = mContext.getSharedPreferences(Defaults.getSettingsName(), Defaults.getSettingsMode());
		String username = settings.getString("username", null);
		String password = settings.getString("password", null);
		if (username == null || password == null) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Get the settings from the FTPServerService if it's running, otherwise
	 * load the settings directly from persistent storage.
	 */
	SharedPreferences getSettings()
	{
		SharedPreferences settings = FTPServerService.getSettings();
		if (settings != null) {
			return settings;
		}
		else {
			return ((Activity) mContext).getPreferences(Activity.MODE_PRIVATE);
		}
	}
}
