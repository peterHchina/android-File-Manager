package com.example.managerfile.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.managerfile.R;

public class FileSearchProgressDialog extends ProgressDialog
{
	private Context mContext;
	private String message = null;
	private TextView mTextView;
	private TextView serachprocessTextView;
	public FileSearchProgressDialog(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public FileSearchProgressDialog(Context context, String message)
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_progress_dialog);
		TextView mTextView = (TextView) findViewById(R.id.define_progress_msg);
		serachprocessTextView = (TextView) findViewById(R.id.serach_processing_text);
		if (message == null)
		{
			mTextView.setVisibility(View.GONE);
		}
		else
		{
			mTextView.setVisibility(View.VISIBLE);
			mTextView.setText(message);
		}
		// define_progress_msg = (TextView)
		// findViewById(R.id.define_progress_msg);
		// define_progress_msg.setText(message);
	}

	public TextView getSerachprocessTextView()
	{
		return serachprocessTextView;
	}


}
