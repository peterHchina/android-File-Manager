package com.example.managerfile.ui.barcode;

import com.example.managerfile.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends Activity
{
	private WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		if (url != null && !"".equals(url))
		{
			webview = (WebView) findViewById(R.id.webview);
			// ����WebView���ԣ��ܹ�ִ��JavaScript�ű�
			webview.getSettings().setJavaScriptEnabled(true);
			// ����URL����
			webview.loadUrl(url);
			// ����web��ͼ�ͻ���
			webview.setWebViewClient(new MyWebViewClient());
		}
		else
		{
			Toast.makeText(WebViewActivity.this, "û�л�ȡ����ά����Ϣ��", Toast.LENGTH_SHORT).show();
		}

	}

	// ���û���
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack())
		{
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// web��ͼ�ͻ���
	public class MyWebViewClient extends WebViewClient
	{
		public boolean shouldOverviewUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}
	}
}