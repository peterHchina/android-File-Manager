package com.example.managerfile.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipException;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.tools.zip.ZipEntry;

public class ZipUtil
{
	static final int BUFFER = 1024;

	/**
	 * 批量压缩文件（夹）
	 * 
	 * @param resFileList
	 *            要压缩的文件（夹）列表
	 * @param zipFile
	 *            生成的压缩文件
	 * @throws Exception
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile) throws Exception
	{
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), BUFFER));
		zipout.setEncoding("GBK");
		for (File resFile : resFileList)
		{
			zip(zipout, resFile, "");
		}
		zipout.close();
	}

	/**
	 * 压缩一个文件（夹）
	 * 
	 * @param resFileList
	 *            要压缩的文件（夹）列表
	 * @param zipFile
	 *            生成的压缩文件
	 * @throws Exception
	 */
	public static void zipFile(File destfile, File inputfile) throws Exception
	{
		File inputFile = inputfile;
		// 创建压缩文件
		File destFile = destfile;
		// 递归压缩方法
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destFile));
		// 设置压缩编码.设置为GBK后在windows下就不会乱码了，如果要放到Linux或者Unix下就不要设置了
		out.setEncoding("GBK");
		zip(out, inputFile, "");
		System.out.println("zip done");
		out.close();
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception
	{
		System.out.println("Zipping   " + f.getName()); // 记录日志，开始压缩
		if (f.isDirectory())
		{ // 如果是文件夹，则获取下面的所有文件
			File[] fl = f.listFiles();
			if (base.length() > 0)
			{

				out.putNextEntry(new ZipEntry(base + "/"));// 此处要将文件写到文件夹中只用在文件名前加"/"再加文件夹名
			}

			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++)
			{
				zip(out, fl[i], base + fl[i].getName());
			}
		}
		else
		{ // 如果是文件，则压缩
			if (base.equals(""))
			{
				out.putNextEntry(new ZipEntry(f.getName()));//只有一个文件
			}
			else
			{
				out.putNextEntry(new ZipEntry(base)); // 生成下一个压缩节点
			}

			FileInputStream in = new FileInputStream(f); // 读取文件内容
			int len;
			byte[] buf = new byte[BUFFER];
			while ((len = in.read(buf, 0, BUFFER)) != -1)
			{
				out.write(buf, 0, len); // 写入到压缩包
			}
			in.close();
			out.closeEntry();
		}
	}

	/**
	 * 解压缩zip文件
	 * 
	 * @param fileName
	 *            要解压的文件名 包含路径 如："c:\\test.zip"
	 * @param filePath
	 *            解压后存放文件的路径 如："c:\\temp"
	 * @throws Exception
	 */
	public static void unZip(String fileName, String destFilePath) throws Exception
	{
		ZipFile zipFile = new ZipFile(fileName, "GBK"); // 以“GBK”编码创建zip文件，用来处理winRAR压缩的文件。
		Enumeration emu = zipFile.getEntries();
		while (emu.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry) emu.nextElement();
			if (entry.isDirectory())
			{
				File dir = new File(destFilePath + entry.getName());
				if (!dir.exists())
				{
					dir.mkdirs();
				}
				continue;
			}
			BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

			File file = new File(destFilePath + entry.getName());
			File parent = file.getParentFile();
			if (parent != null && (!parent.exists()))
			{
				parent.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
			byte[] buf = new byte[BUFFER];
			int len = 0;
			while ((len = bis.read(buf, 0, BUFFER)) != -1)
			{
				fos.write(buf, 0, len);
			}
			bos.flush();
			bos.close();
			bis.close();
			System.out.println("解压文件：" + file.getName());
		}
		zipFile.close();
	}

}
