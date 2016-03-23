package com.cth.wechat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.utils.BmobLog;
import com.cth.wechat.R;
import com.cth.wechat.bean.User;
import com.cth.wechat.config.Constants;
import com.cth.wechat.util.CommonUtils;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	EditText userName, userPassword, userPassword2;
	Button registerButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initTopBarForLeft("ע��");
		init();
	}

	public void init() {
		userName = (EditText) findViewById(R.id.et_username);
		userPassword = (EditText) findViewById(R.id.et_password);
		userPassword2 = (EditText) findViewById(R.id.et_email);
		registerButton = (Button) findViewById(R.id.btn_register);
		registerButton.setOnClickListener(this);
	}

	public void registering() {
		String userString = userName.getText().toString();
		String passwordString = userPassword.getText().toString();
		String passwordString2 = userPassword2.getText().toString();

		if (TextUtils.isEmpty(userString)) {
			showToast("��������û���Ϊ�ա�");
			return;
		}
		if (TextUtils.isEmpty(passwordString)) {
			showToast("�����������Ϊ�ա�");
			return;
		}
		if (TextUtils.isEmpty(passwordString2)) {
			showToast("������ȷ���������롣");
			return;
		}
		if (!(passwordString.equals(passwordString2))) {
			showToast("��������������벻һ�¡�");
			return;
		}
		boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
		if (!isNetConnected) {
			showToast(R.string.network_tips);
			return;
		}

		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("����ע�ᡣ");
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		final User user = new User();
		user.setSex(true);
		user.setUsername(userString);
		user.setPassword(passwordString);
		user.setDeviceType("android");
		user.setInstallId(BmobInstallation.getInstallationId(this));
		user.signUp(this, new SaveListener() {
			@Override
			public void onSuccess() {
				dialog.dismiss();
				showToast("ע��ɹ���");
				userManager.bindInstallationForRegister(user.getUsername());
				sendBroadcast(new Intent( Constants.ACTION_REGISTER_SUCCESS_FINISH ));
				Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				showToast("ע��ʧ��,��������û����ѱ�ע�ᡣ");
				dialog.dismiss();
				BmobLog.i(arg1);
			}
		});
	}

	@Override
	public void onClick(View v) {
		registering();
	}
}
