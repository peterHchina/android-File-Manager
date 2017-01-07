package com.example.managerfile.util;

import java.io.File;

import android.R.raw;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import com.example.managerfile.R;

public class OpenFile
{
	private String filePath = null;;

	public OpenFile()
	{

	}

	// public OpenFile(String filString) {
	// filePath = filString;
	// }
	//
	// File file = new File(filePath);

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	private static String getMIMEType(File file)
	{
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	/**
	 * 打开文件
	 * 
	 * @param file
	 */
	public static void openFile(File file, Context mcontext)
	{
		// Uri uri = Uri.parse("file://"+file.getAbsolutePath());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType(file);
		// 设置intent的data和Type属性。
		intent.setDataAndType(/* uri */Uri.fromFile(file), type);
		// 跳转
		try {
			mcontext.startActivity(intent);
		}
		catch (Exception exception) {

		}

	}

	public static Integer SeekIconForFileThroughFilename(String filename, String filepath)
	{
		FileUtil fileUtils = new FileUtil();
		File file = new File(filepath);
		String extension = fileUtils.getExtensionWithoutDot(filename);
		if (filepath != null && file.isDirectory()) {
			return R.drawable.folder;
		}
		// else if (file.length() == 0)
		// {
		// return R.drawable.unknown_file1;
		// }
		else if (extension.equals("doc") || extension.equals("docx")) {
			return R.drawable.doc_file;
		}
		else if (extension.equals("ppt") || extension.equals("pptx")) {
			return R.drawable.ppt_file;
		}
		else if (extension.equals("jpg") || extension.equals("png")) {
			return R.drawable.image_file;
		}
		else if (extension.equals("pdf")) {
			return R.drawable.pdf_file;
		}
		else if (extension.equals("txj")) {
			return R.drawable.font_file;
		}
		else if (extension.equals("text") || extension.equals("txt") || extension.equals("log")) {
			return R.drawable.txt_file;
		}
		else if (extension.equals("xls") || extension.equals("xlsx")) {
			return R.drawable.xls_file;
		}
		else if (extension.equals("rar") || extension.equals("zip") || extension.equals("tar.gz")) {
			return R.drawable.compressed_file;
		}
		else if (extension.equals("mp3") || extension.equals("ogg") || extension.equals("wmv") || extension.equals("acc")) {
			return R.drawable.audio_file;
		}
		else if (extension.equals("rmvb") || extension.equals("avi") || extension.equals("mp4") || extension.equals("3gp") || extension.equals("mkv")) {
			return R.drawable.video_file;
		}
		else if (extension.equals("apk")) {
			return R.drawable.apk_file;
		}
		else if (extension.equals("html") || extension.equals("xml")) {
			return R.drawable.html_file;
		}
		else if (extension.equals("csv")) {
			return R.drawable.csv_file;
		}
		else if (extension.equals("vcf")) {
			return R.drawable.vcf_file;
		}
		else if (extension.equals("")) {
			return R.drawable.unknown_file;
		}
		else {
			return R.drawable.unknown_file;
		}

	}

	public static Integer SeekIconForFileThroughFilename(String filepath)
	{
		File file = new File(filepath);
		String extension = FileUtil.getExtensionWithoutDot(filepath);
		if (filepath != null && file.isDirectory()) {
			return R.drawable.folder;
		}
		else if (extension.equals("doc") || extension.equals("docx")) {
			return R.drawable.doc_file;
		}
		else if (extension.equals("ppt") || extension.equals("pptx")) {
			return R.drawable.ppt_file;
		}
		else if (extension.equals("jpg") || extension.equals("png")) {
			return R.drawable.image_file;
		}
		else if (extension.equals("pdf")) {
			return R.drawable.pdf_file;
		}
		else if (extension.equals("txj")||extension.equals("ttf")) {
			return R.drawable.font_file;
		}
		else if (extension.equals("iuz")) {
			return R.drawable.iuz_file;
		}
		else if (extension.equals("itz")||extension.equals("mtz")) {
			return R.drawable.itz_file;
		}
		else if (extension.equals("text") || extension.equals("txt") || extension.equals("log")) {
			return R.drawable.txt_file;
		}
		else if (extension.equals("xls") || extension.equals("xlsx")) {
			return R.drawable.xls_file;
		}
		else if (extension.equals("rar") || extension.equals("zip") || extension.equals("tar.gz")) {
			return R.drawable.compressed_file;
		}
		else if (extension.equals("mp3") || extension.equals("ogg") || extension.equals("wav") || extension.equals("acc") || extension.equals("ape")) {
			return R.drawable.audio_file;
		}
		else if (extension.equals("rmvb") || extension.equals("avi") || extension.equals("mp4") || extension.equals("3gp") || extension.equals("mkv") || extension.equals("wmv")) {
			return R.drawable.video_file;
		}
		else if (extension.equals("apk")) {
			return R.drawable.apk_file;
		}
		else if (extension.equals("html") || extension.equals("xml")) {
			return R.drawable.html_file;
		}
		else if (extension.equals("csv")) {
			return R.drawable.csv_file;
		}
		else if (extension.equals("vcf")) {
			return R.drawable.vcf_file;
		}
		else if (extension.equals("")) {
			return R.drawable.unknown_file;
		}
		else {
			return R.drawable.unknown_file;
		}

	}

	public static Integer SeekDetailAttributeForFileThroughFilename(String mFilename)
	{

		String extension = FileUtil.getExtensionWithoutDot(mFilename);
		if (extension.equals("doc") || extension.equals("docx")) {
			return R.string.document_file;
		}
		else if (extension.equals("ppt") || extension.equals("pptx")) {
			return R.string.document_file;
		}
		else if (extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg") || extension.equals("gif")) {
			return R.string.png_file;
		}
		else if (extension.equals("pdf")) {
			return R.string.document_file;
		}
		else if (extension.equals("txj") || extension.equals("ttf")) {
			return R.string.font_file;
		}
		else if (extension.equals("text") || extension.equals("txt")) {
			return R.string.text_file;
		}
		else if (extension.equals("log")) {
			return R.string.log_file;
		}
		else if (extension.equals("xls") || extension.equals("xlsx")) {
			return R.string.document_file;
		}
		else if (extension.equals("rar") || extension.equals("zip") || extension.equals("tar.gz")) {
			return R.string.compressed_file;
		}
		else if (extension.equals("mp3") || extension.equals("ogg") || extension.equals("wmv") || extension.equals("acc")) {
			return R.string.music_file;
		}
		else if (extension.equals("rmvb") || extension.equals("avi") || extension.equals("mp4") || extension.equals("3gp") || extension.equals("mkv")) {
			return R.string.vido_file;
		}
		else if (extension.equals("apk")) {
			return R.string.apk_file;
		}
		else if (extension.equals("html")) {
			return R.string.net_file;
		}
		else if (extension.equals("xml")) {
			return R.string.web_file;
		}
		else if (extension.equals("csv")) {
			return R.string.csv_file;
		}
		else if (extension.equals("vcf")) {
			return R.string.vcf_file;
		}
		else if (extension.equals("dat") || extension.equals("sh")) {
			return R.string.dat_file;
		}
		else if (extension.equals("")) {
			return R.string.unknowfile;
		}
		else {
			return R.string.unknowfile;
		}

	}

	public static Integer SeekDetailIconForFileThroughFilename(String filename)
	{
		FileUtil fileUtils = new FileUtil();
		String extension = fileUtils.getExtensionWithoutDot(filename);
		if (extension.equals("docx")) {
			return R.drawable.docx;
		}
		if (extension.equals("log")) {
			return R.drawable.log;
		}
		else if (extension.equals("doc")) {
			return R.drawable.doc;
		}
		else if (extension.equals("dat")) {
			return R.drawable.dat;
		}
		else if (extension.equals("pptx")) {
			return R.drawable.pptx;
		}
		else if (extension.equals("ppt")) {
			return R.drawable.ppt;
		}
		else if (extension.equals("jpg") || extension.equals("jpeg")) {
			return R.drawable.jpeg;
		}
		else if (extension.equals("png")) {
			return R.drawable.png;
		}
		else if (extension.equals("gif")) {
			return R.drawable.gif;
		}
		else if (extension.equals("bmp")) {
			return R.drawable.bmp;
		}
		else if (extension.equals("pdf")) {
			return R.drawable.pdf;
		}
		else if (extension.equals("apk")) {
			return R.drawable.apk;
		}
		else if (extension.equals("text") || extension.equals("txt")) {
			return R.drawable.text;
		}
		else if (extension.equals("xlsx")) {
			return R.drawable.xlsx;
		}
		else if (extension.equals("xls")) {
			return R.drawable.xls;
		}
		else if (extension.equals("rar")) {
			return R.drawable.rar;
		}
		else if (extension.equals("zip")) {
			return R.drawable.zip;
		}
		else if (extension.equals("7zip")) {
			return R.drawable.file_7zip;
		}
		else if (extension.equals("mp3")) {
			return R.drawable.mp3;
		}
		else if (extension.equals("ogg")) {
			return R.drawable.ogg;
		}
		else if (extension.equals("acc")) {
			return R.drawable.acc;
		}
		else if (extension.equals("wmv")) {
			return R.drawable.wmv;
		}
		else if (extension.equals("rmvb")) {
			return R.drawable.rmvb;
		}
		else if (extension.equals("3gp")) {
			return R.drawable.file_3gp;
		}
		else if (extension.equals("avi")) {
			return R.drawable.avi;
		}
		else if (extension.equals("mp4")) {
			return R.drawable.mp4;
		}
		else if (extension.equals("mkv")) {
			return R.drawable.mkv;
		}
		else if (extension.equals("xml")) {
			return R.drawable.xml;
		}
		else if (extension.equals("html")) {
			return R.drawable.html;
		}
		else if (extension.equals("csv")) {
			return R.drawable.csv_file;
		}
		else if (extension.equals("vcf")) {
			return R.drawable.vcf_file;
		}
		else if (extension.equals("")) {
			return R.drawable.unknow;
		}
		else {
			return R.drawable.unknow;
		}

	}

	// 建立一个MIME类型与文件后缀名的匹配表
	private final static String[][] MIME_MapTable =
	{
			// {后缀名， MIME类型}
	{ ".3gp", "video/3gpp" },
	{ ".apk", "application/vnd.android.package-archive" },
	{ ".asf", "video/x-ms-asf" },
	{ ".avi", "video/x-msvideo" },
	{ ".bin", "application/octet-stream" },
	{ ".bmp", "image/bmp" },
	{ ".c", "text/plain" },
	{ ".class", "application/octet-stream" },
	{ ".conf", "text/plain" },
	{ ".cpp", "text/plain" },
	{ ".doc", "application/msword" },
	{ ".docx", "application/msword" },
	{ ".exe", "application/octet-stream" },
	{ ".gif", "image/gif" },
	{ ".gtar", "application/x-gtar" },
	{ ".gz", "application/x-gzip" },
	{ ".h", "text/plain" },
	{ ".htm", "text/html" },
	{ ".html", "text/html" },
	{ ".jar", "application/java-archive" },
	{ ".java", "text/plain" },
	{ ".jpeg", "image/jpeg" },
	{ ".jpg", "image/jpeg" },
	{ ".js", "application/x-javascript" },
	{ ".log", "text/plain" },
	{ ".m3u", "audio/x-mpegurl" },
	{ ".m4a", "audio/mp4a-latm" },
	{ ".m4b", "audio/mp4a-latm" },
	{ ".m4p", "audio/mp4a-latm" },
	{ ".m4u", "video/vnd.mpegurl" },
	{ ".m4v", "video/x-m4v" },
	{ ".mov", "video/quicktime" },
	{ ".mp2", "audio/x-mpeg" },
	{ ".mp3", "audio/x-mpeg" },
	{ ".mp4", "video/mp4" },
	{ ".mpc", "application/vnd.mpohun.certificate" },
	{ ".mpe", "video/mpeg" },
	{ ".mpeg", "video/mpeg" },
	{ ".mpg", "video/mpeg" },
	{ ".mpg4", "video/mp4" },
	{ ".mpga", "audio/mpeg" },
	{ ".msg", "application/vnd.ms-outlook" },
	{ ".ogg", "audio/ogg" },
	{ ".pdf", "application/pdf" },
	{ ".png", "image/png" },
	{ ".pps", "application/vnd.ms-powerpoint" },
	{ ".ppt", "application/vnd.ms-powerpoint" },
	{ ".pptx", "application/vnd.ms-powerpoint" },
	{ ".prop", "text/plain" },
	{ ".rar", "application/x-rar-compressed" },
	{ ".rc", "text/plain" },
	{ ".rmvb", "audio/x-pn-realaudio" },
	{ ".rtf", "application/rtf" },
	{ ".sh", "text/plain" },
	{ ".tar", "application/x-tar" },
	{ ".tgz", "application/x-compressed" },
	{ ".txt", "text/plain" },
	{ ".wav", "audio/x-wav" },
	{ ".wma", "audio/x-ms-wma" },
	{ ".wmv", "audio/x-ms-wmv" },
	{ ".dat", "text/plain" },
	{ ".sh", "text/plain" },
	{ ".py", "text/plain" },
	{ ".m", "text/plain" },
	{ ".wps", "application/vnd.ms-works" },
			// {".xml", "text/xml"},
	{ ".xml", "text/plain" },
	{ ".z", "application/x-compress" },
	{ ".zip", "application/zip" },
	{ "", "*/*" } };
}
