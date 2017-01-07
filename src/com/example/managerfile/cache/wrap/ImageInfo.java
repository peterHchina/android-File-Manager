package com.example.managerfile.cache.wrap;

import java.net.URLEncoder;

import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.StringUtil;



import android.text.TextUtils;



public class ImageInfo extends BaseModel{

	private static final long serialVersionUID = -7746645065690168828L;

	private static StringBuffer buffer = new StringBuffer();

	private String imageName;

	private ImageType imageType;

	private String URL;

	public enum ImageType {
		Head, Thumb, Middle, Origin, Temp
	}

	public ImageInfo(String url) {
		if (TextUtils.isEmpty(url))
			throw new IllegalArgumentException("url is null");
		this.URL = url;
		this.imageType = ImageType.Head;
		buffer.delete(0, buffer.length());
//		String fileExt = FileUtil.getFileExtensionFromUrl(url);
//		if (fileExt.equals("apk")||TextUtils.isEmpty(url)||fileExt.equals("")) {
//			fileExt = "jpg";
//		}
		String fileExt = "jpg";
		buffer.append(URLEncoder.encode(url.substring(0, url.length()-4)));
		buffer.append(".");
		buffer.append(fileExt);
		imageName = buffer.toString();
	}
	public ImageInfo(String url, ImageType type) {
		if (TextUtils.isEmpty(url) || type == null)
			throw new IllegalArgumentException("url is null");
		this.URL = url;
		this.imageType = type;
		buffer.delete(0, buffer.length());
		String fileExt = FileUtil.getFileExtensionFromUrl(url);
		if (StringUtil.isEmpty(fileExt)) {
			fileExt = "jpg";
		}
		buffer.append(URLEncoder.encode(url));
		buffer.append(".");
		buffer.append(fileExt);
		imageName = buffer.toString();
	}
	public boolean equals(Object other) {
		if (!(other instanceof ImageInfo)) {
			return false;
		}
		ImageInfo otherInfo = (ImageInfo) other;
		if (URL.equals(otherInfo.getURL()) && imageType == otherInfo.getImageType()) {
			return true;
		} else {
			return false;
		}	
	}

	public String getImageName() {
		return imageName;
	}

	public String getURL() {
		return URL;
	}

	public int hashCode() {
		int i = URL.hashCode() * 10;
		int j = imageType.hashCode();
		return i + j;
	}

	public void setImageName(String paramString) {
		imageName = paramString;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	public ImageType getImageType() {
		return imageType;
	}

	public String toString() {
		return "ImageInfo{Name:" + imageName + ",ImageType:" + imageType
				+ ",URL:" + URL + "}";
	}

}
