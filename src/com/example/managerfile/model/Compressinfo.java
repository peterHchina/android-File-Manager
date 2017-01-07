package com.example.managerfile.model;

public class Compressinfo extends Fileinfo
{

	private int id;
	private String title;
	private String displayName;
	private String mimeType;
	private String path;
	private long Size;

	public Compressinfo()
	{
		super();
	}

	public Compressinfo(int id, String title, String displayName, String mimeType, String path, long Size)
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

}
