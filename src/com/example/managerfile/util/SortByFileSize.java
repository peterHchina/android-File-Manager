package com.example.managerfile.util;

import java.text.Collator;
import java.util.Comparator;

import com.example.managerfile.model.Fileinfo;

public class SortByFileSize implements Comparator<Fileinfo>
{
	Collator cmpCollator = Collator.getInstance();
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
			if (cmpCollator.compare(fileinfo1.getmLength(),fileinfo2.getmLength()) > 0)
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
			if (cmpCollator.compare(fileinfo1.getmLength(),fileinfo2.getmLength()) > 0)
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
