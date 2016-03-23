package com.cth.wechat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.bmob.im.BmobChat;

import com.cth.wechat.R;
import com.cth.wechat.config.Config;

public class GuideActivity extends BaseActivity {
	
	private static final int GO_HOME = 100;
	private static final int GO_LOGIN = 200;
	
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		//Bmob SDK ≥ı ºªØ
		BmobChat.getInstance(this).init(Config.applicationId);
		if( userManager.getCurrentUser()!=null ){
			updateUserInfos();
			mHandler.sendEmptyMessageDelayed(GO_HOME,2000);
		}
		else{
			mHandler.sendEmptyMessageDelayed(GO_LOGIN,2000);
		}
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage( Message msg ){
			switch (msg.what) {
			case GO_HOME:
				startAnimActivity(MainActivity.class);
				finish();
				break;
			case GO_LOGIN:
				startAnimActivity(LoginActivity.class);
				finish();
				break;
			}
		}
	};
	
}
