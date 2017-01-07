package com.example.managerfile.ui.barcode;

import com.example.managerfile.R;
import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback
{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private ImageView cancelScanButton;
	private LinearLayout btn_light_control;
	private Button btn_light_control1;
	private boolean isShow = false;
	private Context mContext;
	private ProgressBar pg;
	private ImageView iv_pg_bg_grey;
	private ImageView iv_big_circle;
	private ImageView iv_four_corner;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_diy);
		pg = (ProgressBar) findViewById(R.id.pg_camera_diy);
		iv_pg_bg_grey = (ImageView) findViewById(R.id.iv_camera_diy);
		iv_big_circle = (ImageView) findViewById(R.id.iv_camera_diy_circle);
		iv_four_corner = (ImageView) findViewById(R.id.iv_camera_diy_corner);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		btn_light_control1 = (Button) this.findViewById(R.id.btn_light_control1);
		btn_light_control = (LinearLayout) this.findViewById(R.id.btn_light_control);
		hasSurface = false;
		mContext = this;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface)
		{
			initCamera(surfaceHolder);
		}
		else
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

		// quit the scan view
//		cancelScanButton.setOnClickListener(new OnClickListener()
//		{
//
//			public void onClick(View v)
//			{
//				CaptureActivity.this.finish();
//			}
//		});
		btn_light_control.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				LightControl mLightControl = new LightControl();

				if (isShow)
				{
					isShow = false;
					btn_light_control.setBackgroundResource(R.drawable.sales_button_normal);
					btn_light_control1.setBackgroundResource(R.drawable.torch_off);
					mLightControl.turnOff();
				}
				else
				{
					isShow = true;
					btn_light_control.setBackgroundResource(R.drawable.sales_button_selected);
					btn_light_control1.setBackgroundResource(R.drawable.torch_on);
					mLightControl.turnOn();
				}

			}
		});
		btn_light_control1.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				LightControl mLightControl = new LightControl();

				if (isShow)
				{
					isShow = false;
					btn_light_control.setBackgroundResource(R.drawable.sales_button_normal);
					btn_light_control1.setBackgroundResource(R.drawable.torch_off);
					mLightControl.turnOff();
				}
				else
				{
					isShow = true;
					btn_light_control.setBackgroundResource(R.drawable.sales_button_selected);
					btn_light_control1.setBackgroundResource(R.drawable.torch_on);
					mLightControl.turnOn();
				}

			}
		});
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (handler != null)
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy()
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode)
	{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		Result obj = result;
		if (barcode != null)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			if (!obj.getBarcodeFormat().toString().substring(0, 1).equals("Q"))
			{
				dialog = new AlertDialog.Builder(mContext).setTitle(R.string.title_barcode).setMessage(obj.getText());
				dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

						Toast toast;

						toast = Toast.makeText(mContext, R.string.barcode_msg_close, Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();

						handler.restartPreviewAndDecode();
					}

				});
			}
			else
			{
				if (obj.getText().substring(0, 4).equals("http"))
				{
					final String Url = obj.getText().trim();
					dialog = new AlertDialog.Builder(mContext).setTitle(R.string.open_barcode).setMessage(obj.getText());
					dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface arg0, int arg1)
						{
							// TODO Auto-generated method stub
							Uri url = Uri.parse(Url);
							Intent it = new Intent(Intent.ACTION_VIEW, url);
							startActivity(it);
						}
					});
				}
				else
				{
					dialog = new AlertDialog.Builder(mContext).setTitle(R.string.qcode_barcode).setMessage(obj.getText());
				}

			}

			// .setView(layout)
			dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					dialog.dismiss();
					handler.restartPreviewAndDecode();
				}
			});
			dialog.create();
			dialog.show();
		}
		else
		{
			Toast toast = Toast.makeText(mContext, R.string.barcode_msg_no_sense, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

			handler.restartPreviewAndDecode();
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder)
	{
		try
		{
			CameraManager.get().openDriver(surfaceHolder);
		}
		catch (IOException ioe)
		{
			return;
		}
		catch (RuntimeException e)
		{
			return;
		}
		if (handler == null)
		{
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		if (!hasSurface)
		{
			hasSurface = true;
			initCamera(holder);
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView()
	{
		return viewfinderView;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void drawViewfinder()
	{
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound()
	{
		if (playBeep && mediaPlayer == null)
		{
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			}
			catch (IOException e)
			{
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate()
	{
		if (playBeep && mediaPlayer != null)
		{
			mediaPlayer.start();
		}
		if (vibrate)
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mediaPlayer.seekTo(0);
		}
	};

}