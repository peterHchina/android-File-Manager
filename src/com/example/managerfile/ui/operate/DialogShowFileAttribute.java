package com.example.managerfile.ui.operate;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.managerfile.R;
import com.example.managerfile.task.ApkIconLoadTask;
import com.example.managerfile.task.ImageLoad4HeadTask;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.OpenFile;
import com.example.managerfile.model.Fileinfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogShowFileAttribute
{
	Context mContext;
	Fileinfo mFile;
	private static ExecutorService FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();

	public DialogShowFileAttribute(Context context, Fileinfo file)
	{
		mContext = context;
		mFile = file;
	}

	public void init()
	{
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View mFileDetailView = inflater.inflate(R.layout.dialog_file_detail, null, false);
		ImageView mFileIcon = (ImageView) mFileDetailView.findViewById(R.id.file_icon);
		TextView mFilename = (TextView) mFileDetailView.findViewById(R.id.file_name);
		TextView mFileKinds = (TextView) mFileDetailView.findViewById(R.id.file_kinds);
		TextView mFileLocation = (TextView) mFileDetailView.findViewById(R.id.file_location);
		TextView mFileContent = (TextView) mFileDetailView.findViewById(R.id.file_contains);
		TextView mFileStroage = (TextView) mFileDetailView.findViewById(R.id.file_stroage);
		TextView mFileTime = (TextView) mFileDetailView.findViewById(R.id.file_last_modifed);
		LinearLayout mFileContentLayout = (LinearLayout) mFileDetailView.findViewById(R.id.contents_file);
		AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mContext);
		mDialogBuilder.setTitle(R.string.file_detail);
		mDialogBuilder.setView(mFileDetailView);
		mFilename.setText(mFile.getmName());
		mFileIcon.setImageResource(OpenFile.SeekIconForFileThroughFilename(mFile.getmName(), mFile.getmPath()));
		String mfileFormate = mContext.getResources().getString(OpenFile.SeekDetailAttributeForFileThroughFilename(mFile.getmName()));
		String mfolderFormate = mContext.getResources().getString(R.string.number_folder_contents);
		if (mFile.isDrectory())
		{
			FileUtil mFileUtil = new FileUtil();
			mFileUtil.GetDirectoryDetailAndSize(mFile.getmPath());
			mFileKinds.setText(R.string.folder);
			mFileContentLayout.setVisibility(View.VISIBLE);
			mFileContent.setText(String.format(mfolderFormate, mFileUtil.getDirNumber(), mFileUtil.getFileNumber()));
			mFileStroage.setText(FileUtil.FormetFileSize(mFileUtil.getFileSzie()));

		}
		else
		{
			ImageLoad4HeadTask mImageLoad4HeadTask;
			ApkIconLoadTask mApkIconLoadTask;
			mFileKinds.setText(String.format(mfileFormate, FileUtil.getExtensionWithoutDot(mFile.getmName()).toUpperCase()));
			mFileContentLayout.setVisibility(View.GONE);
			mFileStroage.setText(mFile.getmSize());
			if (FileUtil.getExtensionWithoutDot(mFile.getmName()).equals("apk"))
			{
				mApkIconLoadTask = new ApkIconLoadTask(mContext, mFile.getmPath(), mFileIcon);
				mApkIconLoadTask.executeOnExecutor(FULL_TASK_EXECUTOR);
			}

			if (FileUtil.getExtensionWithoutDot(mFile.getmName()).equals("jpg") || FileUtil.getExtensionWithoutDot(mFile.getmName()).equals("png") || FileUtil.getExtensionWithoutDot(mFile.getmName()).equals("gif"))
			{
				mImageLoad4HeadTask = new ImageLoad4HeadTask(mContext, mFileIcon, mFile.getmPath(), mFile.getmName());
				mImageLoad4HeadTask.executeOnExecutor(FULL_TASK_EXECUTOR);
			}

		}
		mFileTime.setText(mFile.getmDate());
		mFileLocation.setText(new File(mFile.getmPath()).getParent());
		mDialogBuilder.setPositiveButton(R.string.ok, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});

		mDialogBuilder.show();
	}
}
