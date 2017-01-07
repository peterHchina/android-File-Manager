package com.example.managerfile.model;

public class CategoryFileinfo
{
	private long count;
	private long size;

	public CategoryFileinfo()
	{
		super();
	}

	public CategoryFileinfo(int count, long size)
	{
		this.count = count;
		this.size = size;
	}

	public long getCount()
	{
		return count;
	}

	public void setCount(long count)
	{
		this.count = count;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}

}
