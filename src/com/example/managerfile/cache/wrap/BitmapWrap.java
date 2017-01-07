package com.example.managerfile.cache.wrap;

import android.graphics.Bitmap;
import android.util.Log;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class BitmapWrap extends Wrap<Bitmap> {
	private static final int REFERENCE_DIVIDER_SIZE = 120000;

	private Reference<Bitmap> ref;

	// private Bitmap bitmap;

	public BitmapWrap(Bitmap paramBitmap) {
		setWrap(paramBitmap);
		setHit(1);
	}

	public Bitmap getWrap() {
		return ref.get();
	}
	
	public boolean isValid(){
		return ref.get()!=null?true:false;
	}

	public void setWrap(Bitmap paramBitmap) {
		int size = 0;
		if (paramBitmap != null) {
			int j = paramBitmap.getWidth();
			int k = paramBitmap.getHeight();
			size = j * k;
		}

		// bitmap = paramBitmap;
		if (size > REFERENCE_DIVIDER_SIZE) {
			ref = new WeakReference<Bitmap>(paramBitmap);
		} else {
			ref = new SoftReference<Bitmap>(paramBitmap);
		}
	}
}
