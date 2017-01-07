package com.example.managerfile.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.managerfile.R;
public class FileProgressDialog extends ProgressDialog
{
	private Context mContext;
	private String message=null;
    private TextView mTextView;
	public FileProgressDialog(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public FileProgressDialog(Context context, String message)
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
		setContentView(R.layout.define_progress_dialog);
		TextView mTextView=(TextView)findViewById(R.id.define_progress_msg);
		if (message==null)
		{
			mTextView.setVisibility(View.GONE);
		}else {
			mTextView.setVisibility(View.VISIBLE);
			mTextView.setText(message);
		}
//		define_progress_msg = (TextView) findViewById(R.id.define_progress_msg);
//		define_progress_msg.setText(message);
	}
}
