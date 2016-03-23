package com.cth.wechat.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.SaveListener;

import com.cth.wechat.R;
import com.cth.wechat.bean.User;
import com.cth.wechat.config.Constants;
import com.cth.wechat.util.CommonUtils;

public class LoginActivity extends BaseActivity implements OnClickListener {

	EditText userName, userPassword;
	Button loginButton;
	TextView registerButton;
	
	private CustomReicever receiver = new CustomReicever();
	private IntentFilter intentFilter = new IntentFilter();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		registerReceiver(receiver, intentFilter);
	}

	public void init() {
		userName = (EditText) findViewById(R.id.et_username);
		userPassword = (EditText) findViewById(R.id.et_password);
		loginButton = (Button) findViewById(R.id.btn_login);
		registerButton = (TextView) findViewById(R.id.btn_register);
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		
		intentFilter.addAction(Constants.ACTION_REGISTER_SUCCESS_FINISH);
	}

	public void login() {
		String nameString = userName.getText().toString();
		String passwordString = userPassword.getText().toString();

		if (TextUtils.isEmpty(nameString)) {
			showToast(R.string.toast_error_username_null);
			return;
		}

		if (TextUtils.isEmpty(passwordString)) {
			showToast(R.string.toast_error_password_null);
			return;
		}

		User user = new User();
		user.setUsername(nameString);
		user.setPassword(passwordString);
		
		final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
		dialog.setMessage("正在登录。。。");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		userManager.login(user, new SaveListener() {
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						dialog.setMessage("正在获取好友列表。。。");
					}
				});
				updateUserInfos();
				dialog.dismiss();
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				dialog.dismiss();
				BmobLog.i(arg1);
				showToast("账号或者密码错误。");
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v == registerButton) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
		} else {
			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
			if(!isNetConnected){
				showToast(R.string.network_tips);
				return;
			}
			login();
		}
	}
	
	public class CustomReicever extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.ACTION_REGISTER_SUCCESS_FINISH)) {
				finish();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
