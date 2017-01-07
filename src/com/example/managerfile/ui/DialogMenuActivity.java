package com.example.managerfile.ui;

import java.io.File;

import com.example.managerfile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.managerfile.adapter.DialogMenuAdapter;

public class DialogMenuActivity extends Dialog implements DialogInterface
{

	private File mFile;
	private Context mContext;
	private LinearLayout mLayout;
	private ListView mListView;
	private TextView mTextView;
	private DialogMenuAdapter mListAdapter;
	public DialogMenuActivity(Context context,File file)
	{
		super(context);
		mContext=context;
		mFile=file;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater mInflater= LayoutInflater.from(mContext);
		AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
		LinearLayout mLayout=(LinearLayout)mInflater.inflate(R.layout.menu_dialog,null,false);
		mListView=(ListView)mLayout.findViewById(R.id.choice_mode);
		mTextView=(TextView)mLayout.findViewById(R.id.file_name);
		mTextView.setText(file.getName());
		mListAdapter=new DialogMenuAdapter(mFile, mContext);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				if (position==1)
				{
					
				}else if (position==2) {
					
				}else if (position==3) {
					
				}else if (position==4) {
					
				}else if (position==5) {
					
				}else {
					
				}
				DialogMenuActivity.this.dismiss();
				
			}
		});
		
		setContentView(mLayout);
		// TODO Auto-generated constructor stub
	}

	
	
}
