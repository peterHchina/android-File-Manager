package com.example.managerfile.model;

public class Musicinfo 
{
	private int id;
	private String title;
	private String path;
	private String displayName;
	private String mimeType;
	private String artist;
	private long Size;
	private String album;
	private long duration;

	
	private String date;
	private String Stroage;
	boolean isCheck = false;
	boolean isDrectory = false;
	boolean isCut = false;
	public Musicinfo()
	{
		super();
	}

	public Musicinfo(int id, String title, String path, String displayName, String mimeType, String artist, long Size, String album, long duration)
	{
		this.id = id;
		this.title = title;
		this.path = path;
		this.displayName = displayName;
		this.duration = duration;
		this.Size = Size;
		this.album = album;
		this.artist = artist;
		this.mimeType = mimeType;
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

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
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

	public String getArtist()
	{
		return artist;
	}

	public void setArtist(String artist)
	{
		this.artist = artist;
	}

	public boolean isCheck()
	{
		return isCheck;
	}

	public void setCheck(boolean isCheck)
	{
		this.isCheck = isCheck;
	}

	public long getSize()
	{
		return Size;
	}

	public void setSize(long size)
	{
		Size = size;
	}

	public String getStroage()
	{
		return Stroage;
	}

	public void setStroage(String stroage)
	{
		Stroage = stroage;
	}

	public String getAlbum()
	{
		return album;
	}

	public void setAlbum(String album)
	{
		this.album = album;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
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


}
