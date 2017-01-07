package com.example.managerfile.util;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.util.HashMap;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LoadImage
{

	public String image_path;
	int phSize;
	protected static final int PHO_DIP = 45;
	public float PIX_SCALE;
	Resources res;
	private HashMap<String, SoftReference<Drawable>> imageCache=new HashMap<String, SoftReference<Drawable>>();

	private static LoadImage instance;
	public LoadImage(Context mcontext)
	{
		
		res = mcontext.getResources();
		PIX_SCALE =res.getDisplayMetrics().density;
//		ICON_PIX = (int) (ICON_DIP * PIX_SCALE + 0.5f);
		phSize = (PHO_DIP * (int) (PIX_SCALE + 0.5f));
	}

	public Drawable loadImage(final String image_path, final ImageCallBack callBack)
	{

		if (imageCache.containsKey(image_path))
		{
			SoftReference<Drawable> drawableReference=imageCache.get(image_path);
			Drawable drawable=drawableReference.get();
			if (drawable!=null)
			{
				return drawable;
			}
		}
		
		final Handler handler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Drawable drawable = (Drawable) msg.obj;
				callBack.getDrawable(drawable);
			}

		};

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Log.i("test34545^^^^^^^^^^^^^^^^^^^^^^^^^", image_path);
				try {
					Drawable drawable = SmallImage(image_path);		
					
					Message message = Message.obtain();
					imageCache.put(image_path, new SoftReference<Drawable>(drawable));
					message.obj = drawable;
					handler.sendMessage(message);
				} 
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				Drawable drawable = null;
//				try
//				{
//					drawable = SmallImage(image_path);
//				}
//				catch (Exception e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				finally
//				{
//					Message message = Message.obtain();
//					message.obj = drawable;
//					handler.sendMessage(message);
//				}

			}
		}).start();
		
		return null;
	}

	public Drawable SmallImage(String path) throws Exception
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
			drawable = new BitmapDrawable(res, b);
			return drawable;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public interface ImageCallBack
	{
		public void getDrawable(Drawable drawable);
	}
	
	public static LoadImage getInstance(Context mcontext)
	{
		if(instance==null)
		{
			instance=new LoadImage(mcontext);
		}
		return instance;
	}

}
