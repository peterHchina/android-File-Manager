package com.example.managerfile.model;

import android.R.integer;

public class MemoryCountAdapter
{

	private int ListTop=0;
	private int ListPosition;

	public MemoryCountAdapter( int listPosition,int listTop)
	{
		this.setListPosition(listPosition);
		this.setListTop(listTop);
	}

	public MemoryCountAdapter(int listPosition)
	{
		this.setListPosition(listPosition);

	}
	
	public MemoryCountAdapter()
	{
		this.setListPosition(0);

	}

	public int getListTop()
	{
		return ListTop;
	}

	public void setListTop(int listTop)
	{
		ListTop = listTop;
	}

	public int getListPosition()
	{
		return ListPosition;
	}

	public void setListPosition(int listPosition)
	{
		ListPosition = listPosition;
	}

}
