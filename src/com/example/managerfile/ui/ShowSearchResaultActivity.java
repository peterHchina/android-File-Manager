package com.example.managerfile.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.managerfile.R;

import com.example.managerfile.adapter.GridAdapter;
import com.example.managerfile.adapter.ListAdapter;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.task.RefleshTheListDataTask;
import com.example.managerfile.util.OpenFile;

import android.os.Bundle;
import android.os.Environment;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.LinearLayout;

import android.widget.TextView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShowSearchResaultActivity extends Activity
{

	public ListView mListView;
	private String rootPath;
	public ListAdapter mListAdapter;
	public GridAdapter mGridAdapter;
	private Context mContext;
	public RefleshTheListDataTask refleshTheListDataTask;
	public List<Fileinfo> listFileinfos = new ArrayList<Fileinfo>();
	private ArrayList<String> mList;
	private LinearLayout mFileNoRecodeLayout;
	private TextView tvTitle;
	private TextView mTextView;
	private LinearLayout btnBack;
	public ListAdapter searchListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_resault_view);

		intpararm();

	}

	public void intpararm()
	{
		Intent intent = getIntent();
		mListView = (ListView) findViewById(R.id.files_list);
		mFileNoRecodeLayout = (LinearLayout) findViewById(R.id.empty_view);
		mContext = this;
		mList = intent.getStringArrayListExtra("SearchList");
		if (mList.size() == 0)
		{
			mListView.setVisibility(View.GONE);
			mFileNoRecodeLayout.setVisibility(View.VISIBLE);
		}else {
			mListView.setVisibility(View.VISIBLE);
			mFileNoRecodeLayout.setVisibility(View.GONE);
		}
		String keyWords = intent.getStringExtra("keyword");
		for (int i = 0; i < mList.size(); i++)
		{
			listFileinfos.add(new Fileinfo(mList.get(i)));
		}
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		String formateString = mContext.getResources().getString(R.string.search);
		tvTitle.setText(String.format(formateString, keyWords));
		mTextView = (TextView) findViewById(R.id.btnBack_text);
		mTextView.setText(R.string.back);
		btnBack = (LinearLayout) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

		searchListAdapter = new ListAdapter(mContext, listFileinfos);
		mFileNoRecodeLayout = (LinearLayout) findViewById(R.id.empty_view);
		rootPath = Environment.getExternalStorageDirectory().getPath();
		mListView.setAdapter(searchListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub

				OpenFile.openFile(new File(listFileinfos.get(position).getmPath()), mContext);

			}

		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				new SearchChoiceOprateDialog(mContext, listFileinfos.get(position), ShowSearchResaultActivity.this).initDialog();

				return false;
			}
		});

	}

	public void onBackPressed()
	{

		finish();

	}

	@Override
	public void finish()
	{
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

	}
}
