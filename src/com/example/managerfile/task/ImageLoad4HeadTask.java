package com.example.managerfile.task;

import java.io.File;

import com.example.managerfile.cache.ImageCache;
import com.example.managerfile.cache.wrap.ImageInfo;
import com.example.managerfile.cache.wrap.ImageInfo.ImageType;
import com.example.managerfile.util.OpenFile;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoad4HeadTask extends AsyncTask<Void, Void, Bitmap>
{
	int phSize;
	Resources res;
	protected static final int PHO_DIP = 45;
	public float PIX_SCALE;
	private Bitmap bitmap;

	private ImageCache imageCache;

	private ImageInfo imageInfo;

	private ImageView imageView;

	private String url = null;
	private String filename = null;
	private Context mContext;

	public ImageLoad4HeadTask(Context context, ImageView iv, String url, String filename)
	{
		if (iv == null || url == null)
		{
			throw new IllegalArgumentException("ImageView or URL is null!");
		}
		this.imageView = iv;
		this.url = url;
		this.filename = filename;
		imageCache = ImageCache.getInstance();
		imageInfo = new ImageInfo(url, ImageType.Head);
		mContext = context;
		res = mContext.getResources();
		PIX_SCALE = res.getDisplayMetrics().density;
		// ICON_PIX = (int) (ICON_DIP * PIX_SCALE + 0.5f);
		phSize = (PHO_DIP * (int) (PIX_SCALE + 0.5f));

	}

	protected Bitmap doInBackground(Void... params)
	{
		try
		{
			bitmap = imageCache.get(imageInfo);
			if (bitmap == null)
			{
				Log.v("ImageLoad4HeadTask", "ImageLoad4HeadTask");
				bitmap = SmallImage(url);

				if (bitmap != null)
				{

					imageCache.put(imageInfo, bitmap);
				}
			}
		}
		catch (Exception e)
		{

		}
		return bitmap;
	}

	protected void onPostExecute(Bitmap paramBitmap)
	{
		super.onPostExecute(paramBitmap);
		if (paramBitmap != null)
		{
			BitmapDrawable bd = new BitmapDrawable(paramBitmap);
			imageView.setImageDrawable(bd);
			// imageView.setImageBitmap(paramBitmap);
		}
	}

	protected void onPreExecute()
	{
		super.onPreExecute();
		Drawable headBackground = null;
		Bitmap headBitmap = null;

		imageView.setImageResource(OpenFile.SeekIconForFileThroughFilename( url));

		bitmap = imageCache.getBitmapFromCache(imageInfo);
		if (bitmap != null)
		{
			cancel(true);
			onPostExecute(bitmap);
		}
	}

	public Bitmap SmallImage(String path) throws Exception
	{

		BitmapFactory.Options opt = new BitmapFactory.Options();
		Drawable drawable;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);
		opt.inJustDecodeBounds = false;
		// Log.d(tag, "phSize: " + phSize + " pix_scale: " + PIX_SCALE);
		if (opt.outWidth > opt.outHeight)
		{
			opt.inSampleSize = opt.outWidth / phSize;

		}
		else
		{
			opt.inSampleSize = opt.outHeight / phSize;
		}
		Bitmap b;
		try
		{
			b = BitmapFactory.decodeFile(path, opt);
			return b;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
