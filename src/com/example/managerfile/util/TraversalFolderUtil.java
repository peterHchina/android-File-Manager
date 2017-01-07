package com.example.managerfile.util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 文件夹遍历
 * 
 * @author once
 * 
 */
public class TraversalFolderUtil
{

	// no recursion
	public static LinkedList<File> listLinkedFiles(String strPath)
	{
		LinkedList<File> list = new LinkedList<File>();
		File dir = new File(strPath);
		if (!dir.isDirectory())
		{
			return null;
		}
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++)
		{
			if (file[i].isDirectory())
				list.add(file[i]);
			else
				System.out.println(file[i].getAbsolutePath());
		}
		File tmp;
		while (!list.isEmpty())
		{
			tmp = (File) list.removeFirst();
			if (tmp.isDirectory())
			{
				file = tmp.listFiles();
				if (file == null)
					continue;
				for (int i = 0; i < file.length; i++)
				{
					if (file[i].isDirectory())
						list.add(file[i]);
					else
						System.out.println(file[i].getAbsolutePath());
				}
			}
			else
			{
				System.out.println(tmp.getAbsolutePath());
			}
		}
		return list;
	}

	// recursion
	public static ArrayList<File> listFiles(String strPath)
	{
		return refreshFileList(strPath);
	}

	public static ArrayList<File> refreshFileList(String strPath)
	{
		ArrayList<File> filelist = new ArrayList<File>();
		File dir = new File(strPath);
		File[] files = dir.listFiles();

		if (files == null)
			return null;
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isDirectory())
			{
				refreshFileList(files[i].getAbsolutePath());
			}
			else
			{
				if (files[i].getName().toLowerCase().endsWith("zip"))
					filelist.add(files[i]);
			}
		}
		return filelist;
	}
}