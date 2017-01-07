package com.example.managerfile.model;

import android.R.integer;

public class DetailCategoryFileinfo 
{

	private int id;
	private String title;
	private String displayName;
	private String mimeType;
	private String path;
	private long Size;
	private int number;
	private String date;
	private String Stroage;
	boolean isCheck = false;
	boolean isDrectory = false;
	boolean isCut = false;
	public DetailCategoryFileinfo()
	{
		super();
	}

	public DetailCategoryFileinfo(int id, String title, String displayName, String mimeType, String path, long Size)
	{
		this.id = id;
		this.title = title;
		this.displayName = displayName;
		this.mimeType = mimeType;
		this.path = path;
		this.Size = Size;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public long getSize()
	{
		return Size;
	}

	public void setSize(long size)
	{
		Size = size;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getStroage()
	{
		return Stroage;
	}

	public void setStroage(String stroage)
	{
		Stroage = stroage;
	}

	public boolean isCheck()
	{
		return isCheck;
	}

	public void setCheck(boolean isCheck)
	{
		this.isCheck = isCheck;
	}

	public boolean isDrectory()
	{
		return isDrectory;
	}

	public void setDrectory(boolean isDrectory)
	{
		this.isDrectory = isDrectory;
	}

	public boolean isCut()
	{
		return isCut;
	}

	public void setCut(boolean isCut)
	{
		this.isCut = isCut;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

}
