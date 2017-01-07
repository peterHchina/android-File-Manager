package com.example.managerfile.util;

import java.util.Comparator;

import com.example.managerfile.model.Fileinfo;

public class SortByFileDate implements Comparator<Fileinfo>
{

	@Override
	public int compare(Fileinfo fileinfo1, Fileinfo fileinfo2)
	{
		// TODO Auto-generated method stub
		if (fileinfo2.isDrectory())
		{
			if (!fileinfo1.isDrectory())
			{
				return 1;
			}

			if (fileinfo1.getmLastModifyTime().compareTo(fileinfo2.getmLastModifyTime()) > 0)
			{
				return 1;
			}
			else
			{
				return -1;
			}

			// return this.mName.compareTo(another.mName);
		}
		if (fileinfo1.isDrectory())
		{
			return -1;
		}
		else
		{
			if (fileinfo1.getmLastModifyTime().compareTo(fileinfo2.getmLastModifyTime()) > 0)
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
		// return this.mName.compareTo(another.mName);

	}

}
