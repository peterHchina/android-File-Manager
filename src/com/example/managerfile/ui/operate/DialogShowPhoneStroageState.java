package com.example.managerfile.ui.operate;

import com.example.managerfile.R;
import com.example.managerfile.util.FileUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogShowPhoneStroageState
{
	private Context mContext;
	private LayoutInflater inflater;
	private TextView phoneInnerStroage;
	private TextView phoneOuterStroage;

	public DialogShowPhoneStroageState(Context context)
	{
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void init()
	{
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
		mBuilder.setTitle(R.string.stroage_satae);
		inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.list_phone_stroage, null);
		phoneInnerStroage = (TextView) view.findViewById(R.id.phone_inner_stroage);
		phoneOuterStroage = (TextView) view.findViewById(R.id.phone_sdcard_stroage);
		String formateString = mContext.getResources().getString(R.string.stroage_view);
		phoneInnerStroage.setText(formateString.format(formateString, FileUtil.FormetFileSize(FileUtil.getAvailableInternalMemorySize()), FileUtil.FormetFileSize(FileUtil.getTotalInternalMemorySize())));
		phoneOuterStroage.setText(formateString.format(formateString, FileUtil.FormetFileSize(FileUtil.getAvailableExternalMemorySize()), FileUtil.FormetFileSize(FileUtil.getTotalExternalMemorySize())));
		mBuilder.setView(view);
		mBuilder.setPositiveButton(R.string.ok, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				
			}
		});
		mBuilder.show();
	}
}
