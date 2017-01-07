package com.example.managerfile.ui;

import java.util.HashMap;
import com.example.managerfile.R;
import com.example.managerfile.model.CategoryFileinfo;
import com.example.managerfile.provider.common.CollectFileDao;
import com.example.managerfile.util.AllFileUtil;
import com.example.managerfile.util.AllFileUtil.FileCategory;
import com.example.managerfile.util.AllFileUtil.SDCardInfo;
import com.example.managerfile.util.FileUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FileManagerCategoryActivity extends Activity implements OnClickListener
{
	private HashMap<AllFileUtil.FileCategory, Integer> categoryIndex = new HashMap<AllFileUtil.FileCategory, Integer>();

	private RelativeLayout mFirstRelativeLayout;
	private RelativeLayout mSecondRelativeLayout;
	private RelativeLayout mThirdRelativeLayout;
	private LinearLayout mBarLinearLayout;
	private LinearLayout mNoViewLayout;

	private TextView mMusicCount;
	private TextView mApkCount;
	private TextView mCompressCount;
	private TextView mPictureCount;
	private TextView mDoucumentCount;
	private TextView mVideoCount;
	private TextView mThemeCount;
	private TextView mCollectionCount;

	private RelativeLayout mThemeView;
	private RelativeLayout mMusicView;
	private RelativeLayout mVideoView;
	private RelativeLayout mpictureView;
	private RelativeLayout mDocumentView;
	private RelativeLayout mApkView;
	private RelativeLayout mCollectionView;
	private RelativeLayout mCompressView;
	private Context mContext;
	private CategoryBar mCategoryBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_manager_tab_view);
		init();
		bindView();
		registerFileWitcherReceiver();

	}

	private void init()
	{
		mFirstRelativeLayout = (RelativeLayout) findViewById(R.id.first_resion);
		mSecondRelativeLayout = (RelativeLayout) findViewById(R.id.second_resion);
		mThirdRelativeLayout = (RelativeLayout) findViewById(R.id.third_resion);
		mBarLinearLayout = (LinearLayout) findViewById(R.id.bar_view);
		mNoViewLayout = (LinearLayout) findViewById(R.id.no_views);
		mThemeView = (RelativeLayout) findViewById(R.id.theme);
		mThemeCount = (TextView) findViewById(R.id.theme_count);
		mMusicView = (RelativeLayout) findViewById(R.id.music);
		mMusicCount = (TextView) findViewById(R.id.music_count);
		mVideoView = (RelativeLayout) findViewById(R.id.video);
		mVideoCount = (TextView) findViewById(R.id.video_count);
		mpictureView = (RelativeLayout) findViewById(R.id.picture);
		mPictureCount = (TextView) findViewById(R.id.picture_count);
		mDocumentView = (RelativeLayout) findViewById(R.id.documents);
		mDoucumentCount = (TextView) findViewById(R.id.documents_count);
		mCompressView = (RelativeLayout) findViewById(R.id.compress);
		mCompressCount = (TextView) findViewById(R.id.compress_count);
		mApkView = (RelativeLayout) findViewById(R.id.apk);
		mApkCount = (TextView) findViewById(R.id.apk_count);
		mCollectionView = (RelativeLayout) findViewById(R.id.collection);
		mCollectionCount = (TextView) findViewById(R.id.collection_count);
		mContext = this;
		mThemeView.setOnClickListener(this);
		mMusicView.setOnClickListener(this);
		mApkView.setOnClickListener(this);
		mCollectionView.setOnClickListener(this);
		mCollectionView.setOnClickListener(this);
		mCompressView.setOnClickListener(this);
		mDocumentView.setOnClickListener(this);
		mVideoView.setOnClickListener(this);
		mpictureView.setOnClickListener(this);
		setupCategoryInfo();
	}

	private void bindView()
	{
		SDCardInfo sdCardInfo = AllFileUtil.getSDCardInfo();
		CollectFileDao dao = new CollectFileDao(mContext);
		if (sdCardInfo != null) {
			mCategoryBar.setFullValue(sdCardInfo.total);
			setTextView(R.id.sd_card_capacity, getString(R.string.sd_card_size, FileUtil.FormetFileSize(sdCardInfo.total)));
			setTextView(R.id.sd_card_available, getString(R.string.sd_card_available, FileUtil.FormetFileSize(sdCardInfo.free)));
		}
		AllFileUtil.refreshCategoryInfo(mContext);
		long size = 0;
		for (AllFileUtil.FileCategory fc : AllFileUtil.sCategories) {
			CategoryFileinfo categoryInfo = AllFileUtil.getCategoryInfos().get(fc);
			setCategoryCount(fc, categoryInfo.getCount());

			// other category size should be set separately with calibration
			if (fc == AllFileUtil.FileCategory.Other)
				continue;
			setCategorySize(fc, categoryInfo.getSize());
			setCategoryBarValue(fc, categoryInfo.getSize());
			size += categoryInfo.getSize();
		}
		if (sdCardInfo != null) {
			long otherSize = sdCardInfo.total - sdCardInfo.free - size;
			setCategorySize(FileCategory.Other, otherSize);
			setCategoryBarValue(FileCategory.Other, otherSize);
		}

		if (dao != null) {
			setCategoryCount(FileCategory.Favorite, dao.getAllCollectFiles().size());
		}
		// setCategoryCount(FileCategory.Favorite, mFavoriteList.getCount());

		if (mCategoryBar.getVisibility() == View.VISIBLE) {
			mCategoryBar.startAnimation();
		}
	}

	private void registerFileWitcherReceiver()
	{
		FileWitcherReceiver mFileWitcherReceiver = new FileWitcherReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		intentFilter.addDataScheme("file");
		registerReceiver(mFileWitcherReceiver, intentFilter);
	}

	private void updateUI()
	{
		boolean sdCardReady = FileUtil.isSDCardReady();
		if (sdCardReady) {
			if (mNoViewLayout.getVisibility() == View.VISIBLE) {
				mNoViewLayout.setVisibility(View.GONE);
			}
			if (mFirstRelativeLayout.getVisibility() == View.GONE) {
				mFirstRelativeLayout.setVisibility(View.VISIBLE);
			}
			if (mSecondRelativeLayout.getVisibility() == View.GONE) {
				mSecondRelativeLayout.setVisibility(View.VISIBLE);
			}
			if (mThirdRelativeLayout.getVisibility() == View.GONE) {
				mThirdRelativeLayout.setVisibility(View.VISIBLE);
			}
			if (mNoViewLayout.getVisibility() == View.GONE) {
				mNoViewLayout.setVisibility(View.VISIBLE);
			}
			bindView();
		}
		else {
			mNoViewLayout.setVisibility(View.VISIBLE);
			mFirstRelativeLayout.setVisibility(View.GONE);
			mSecondRelativeLayout.setVisibility(View.GONE);
			mThirdRelativeLayout.setVisibility(View.GONE);
			mBarLinearLayout.setVisibility(View.GONE);
		}
	}

	private void setCategoryCount(AllFileUtil.FileCategory fc, long count)
	{
		TextView view = (TextView) getCategoryCountId(fc);
		if (view == null)
			return;
		view.setText("(" + count + ")");
	}

	private View getCategoryCountId(AllFileUtil.FileCategory fc)
	{
		switch (fc)
		{
		case Music:
			return mMusicCount;
		case Video:
			return mVideoCount;
		case Picture:
			return mPictureCount;
		case Theme:
			return mThemeCount;
		case Doc:
			return mDoucumentCount;
		case Zip:
			return mCompressCount;
		case Apk:
			return mApkCount;
		case Favorite:
			return mCollectionCount;
		default:
			break;
		}
		return null;
	}

	private void setupCategoryInfo()
	{

		mCategoryBar = (CategoryBar) findViewById(R.id.category_bar);
		int[] imgs = new int[]
		{ R.drawable.music_file_size, R.drawable.video_file_size, R.drawable.picture_file_size, R.drawable.theme_file_size, R.drawable.doc_file_size, R.drawable.pressed_file_size, R.drawable.apk_file_size, R.drawable.other_file_size };

		for (int i = 0; i < imgs.length; i++) {
			mCategoryBar.addCategory(imgs[i]);
		}

		for (int i = 0; i < AllFileUtil.sCategories.length; i++) {
			categoryIndex.put(AllFileUtil.sCategories[i], i);
		}
	}

	@Override
	public void onClick(View view)
	{
		// TODO Auto-generated method stub
		int id = view.getId();
		Intent intent = new Intent();
		switch (id)
		{
		case R.id.music:
			intent.putExtra("type", 1);
			break;
		case R.id.documents:
			intent.putExtra("type", 7);
			break;
		case R.id.video:
			intent.putExtra("type", 2);
			break;
		case R.id.picture:
			intent.putExtra("type", 3);
			break;
		case R.id.theme:
			intent.putExtra("type", 4);
			break;
		case R.id.apk:
			intent.putExtra("type", 5);
			break;
		case R.id.compress:
			intent.putExtra("type", 6);
			break;
		case R.id.collection:
			intent.putExtra("type", 8);
			break;
		default:
			break;
		}
		if (intent.getIntExtra("type", 0) > 0) {
			intent.setClass(mContext, ShowCategoryFilesActivity.class);
			startActivity(intent);
		}

	}

	private void setCategorySize(AllFileUtil.FileCategory fc, long size)
	{
		int txtId = 0;
		int resId = 0;
		switch (fc)
		{
		case Music:
			txtId = R.id.category_legend_music;
			resId = R.string.music;
			break;
		case Video:
			txtId = R.id.category_legend_video;
			resId = R.string.video;
			break;
		case Picture:
			txtId = R.id.category_legend_picture;
			resId = R.string.picture;
			break;
		case Theme:
			txtId = R.id.category_legend_theme;
			resId = R.string.theme;
			break;
		case Doc:
			txtId = R.id.category_legend_document;
			resId = R.string.document;
			break;
		case Zip:
			txtId = R.id.category_legend_zip;
			resId = R.string.compress_bag;
			break;
		case Apk:
			txtId = R.id.category_legend_apk;
			resId = R.string.apk;
			break;
		case Other:
			txtId = R.id.category_legend_other;
			resId = R.string.other;
			break;
		}

		if (txtId == 0 || resId == 0)
			return;

		setTextView(txtId, getString(resId) + ":" + FileUtil.FormetFileSize(size));
	}

	private void setTextView(int id, String t)
	{
		TextView text = (TextView) findViewById(id);
		text.setText(t);
	}

	private void setCategoryBarValue(AllFileUtil.FileCategory f, long size)
	{
		if (mCategoryBar == null) {
			mCategoryBar = (CategoryBar) findViewById(R.id.category_bar);
		}
		mCategoryBar.setCategoryValue(categoryIndex.get(f), size);
	}

	private class FileWitcherReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED) || action.equals(Intent.ACTION_MEDIA_MOUNTED) || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
				updateUI();
			}
		}

	}
}
