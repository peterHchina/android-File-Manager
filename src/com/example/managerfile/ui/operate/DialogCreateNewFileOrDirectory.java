package com.example.managerfile.ui.operate;

import java.io.File;
import java.io.IOException;

import com.example.managerfile.R;
import com.example.managerfile.ui.FileManagerActivity;

import android.R.raw;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DialogCreateNewFileOrDirectory
{
	Context mContext;
	private String[] items;
	private EditText mEditText;
	private String filename;
	private boolean isDir=false;
	private String mpath;
	private File file;
	Editable editable;
	public int res=0;
	FileManagerActivity mFileManagerActivity;
	
    public DialogCreateNewFileOrDirectory (Context context,File currentFile)
	{
		mContext=context;
		items=mContext.getResources().getStringArray(R.array.create);
		mpath=currentFile.getAbsolutePath();
		
		
	}
    
    public DialogCreateNewFileOrDirectory (Context context,File currentFile,FileManagerActivity fileManager)
	{
		mContext=context;
		items=mContext.getResources().getStringArray(R.array.create);
		mpath=currentFile.getAbsolutePath();
		mFileManagerActivity=fileManager;
		
	}
    
    public void init(){
    	LayoutInflater inflater=LayoutInflater.from(mContext);
    	LinearLayout vLayout=(LinearLayout)inflater.inflate(R.layout.dialog_create_file, null);
    	mEditText=(EditText)vLayout.findViewById(R.id.file_name);
    	editable=mEditText.getText();
    	mEditText.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			@Override
			public void onFocusChange(View view, boolean hasFocus)
			{
				// TODO Auto-generated method stub
				if (hasFocus)
				{
//					Log.i("77777777777777777777777", mpath);
					//Selection.selectAll(editable);
					Selection.setSelection(editable, editable.length());
				}else {
					
				}
			}
		});
    	
    	
    	AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
    	dialog.setTitle(R.string.create_question);
    	dialog.setSingleChoiceItems(items, 0, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface arg0, int which)
			{
				// TODO Auto-generated method stub
				if (which==0)
				{
//					mEditText.setHint(R.string.new_file_f);
					mEditText.setText(R.string.new_file_f);
				}
				else {
					isDir=true;
//					mEditText.setHint(R.string.new_file_d);
					mEditText.setText(R.string.new_file_d);
				}
				
			}
		});
    	dialog.setView(vLayout);
//    	setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    	dialog.setPositiveButton(R.string.ok, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				filename=mEditText.getText().toString().trim();
				
				if (!filename.isEmpty())
				{
					filename=mpath+File.separator+filename;
					file=new File(filename);
					
//					Log.i("_---------------------", filename);
					res=1;
				}else {
					Toast.makeText(mContext, R.string.dir_had_exits, Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (file.exists())
				{
					if (isDir)
					{
						Toast.makeText(mContext, R.string.dir_had_exits, Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(mContext, R.string.file_had_exits, Toast.LENGTH_SHORT).show();
					}
				}else {
					if (!isDir)
					{
						try
						{
							if (!file.createNewFile())
							{
								Toast.makeText(mContext, R.string.file_created_failed, Toast.LENGTH_SHORT).show();
							}
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						file.mkdir();
						res=1;
					}
					
				}
				mFileManagerActivity.reflashPath(mpath);
				
			}
	
		});
    	dialog.setNegativeButton(R.string.cancel, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				
			}
		});
    	dialog.show();
    }
    
}
