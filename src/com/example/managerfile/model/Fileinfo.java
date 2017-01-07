package com.example.managerfile.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Comparator;
import java.text.Collator;
import com.example.managerfile.util.FileUtil;

public class Fileinfo implements Comparable<Fileinfo>
{

	private String mName;
	private String mSize;
	private String mLength;
	private String mLastModifyTime;
	private String mPath;
	private String mDate;
	boolean isDrectory = false;
	boolean isCut = false;
	boolean isCheck = false;
	Collator cmpCollator = Collator.getInstance(java.util.Locale.CHINA);
	private FileUtil mFileUtil;

	public Fileinfo(String name, String size, String path, String date, boolean isdrectroy)
	{

		mName = name;
		mSize = size;
		mPath = path;
		mDate = date;
		isDrectory = isdrectroy;
		mFileUtil = new FileUtil();
	}
    public Fileinfo()
    {
    	super();
    }
	public Fileinfo(String path)
	{
		File file = new File(path);
		mFileUtil = new FileUtil();

		mName = file.getName();
		if (file.isDirectory())
		{
			// mFileUtil.GetDirectoryDetailAndSize(path);
			mSize = null;
			mLength = "0";
		}
		else
		{
			mLength = String.valueOf(file.length());
			mSize = FileUtil.FormetFileSize(file.length());
		}
		mLastModifyTime = String.valueOf(file.lastModified());
		mPath = path;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		mDate = dateFormat.format(new Date(file.lastModified()));
		isDrectory = file.isDirectory();

	}

	public String getmName()
	{
		return mName;
	}

	public void setmName(String mName)
	{
		this.mName = mName;
	}

	public String getmSize()
	{
		return mSize;
	}

	public void setmSize(String mSize)
	{
		this.mSize = mSize;
	}

	public String getmPath()
	{
		return mPath;
	}

	public void setmPath(String mPath)
	{
		this.mPath = mPath;
	}

	public String getmDate()
	{
		return mDate;
	}

	public void setmDate(String mDate)
	{
		this.mDate = mDate;
	}

	public boolean isDrectory()
	{
		return isDrectory;
	}

	public void setDrectory(boolean isDrectory)
	{
		this.isDrectory = isDrectory;
	}

	public String getmLength()
	{
		return mLength;
	}

	public void setmLength(String mLength)
	{
		this.mLength = mLength;
	}

	public String getmLastModifyTime()
	{
		return mLastModifyTime;
	}

	public void setmLastModifyTime(String mLastModifyTime)
	{
		this.mLastModifyTime = mLastModifyTime;
	}

	public boolean isCut()
	{
		return isCut;
	}

	public void setCut(boolean isCut)
	{
		this.isCut = isCut;
	}

	public boolean isCheck()
	{
		return isCheck;
	}

	public void setCheck(boolean isCheck)
	{
		this.isCheck = isCheck;
	}

	@Override
	public int compareTo(Fileinfo another)
	{
		// TODO Auto-generated method stub
		if (another.isDrectory)
		{
			if (!isDrectory)
			{
				return 1;
			}
			return this.mName.compareTo(another.mName);
		}
		if (isDrectory)
		{
			return -1;
		}
		return this.mName.compareTo(another.mName);
	}

}
