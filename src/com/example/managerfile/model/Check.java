package com.example.managerfile.model;

import android.R.integer;

public class Check
{

	String filePath;
	int poisition = 0;
	int listTop = 0;

	public Check(String filepath, int poisition)
	{
		filePath = filepath;
		this.poisition = poisition;
	}

	public Check(String filepath, int poisition, int listTop)
	{
		filePath = filepath;
		this.poisition = poisition;
		this.listTop = listTop;
	}

	public Check(String filepath)
	{
		filePath = filepath;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public int getPoisition()
	{
		return poisition;
	}

	public void setPoisition(int poisition)
	{
		this.poisition = poisition;
	}

	public int getListTop()
	{
		return listTop;
	}

	public void setListTop(int listTop)
	{
		this.listTop = listTop;
	}

}
