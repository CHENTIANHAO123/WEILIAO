package com.cth.wechat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;

import com.cth.wechat.CustomApplication;
import com.cth.wechat.R;
import com.cth.wechat.ui.BaseFragment;
import com.cth.wechat.ui.BlacklistActivity;
import com.cth.wechat.ui.InfoActivity;
import com.cth.wechat.ui.LoginActivity;
import com.cth.wechat.util.SharePreferenceUtil;

public class MeFragment extends BaseFragment implements OnClickListener {

	Button btn_logout;
	TextView tv_set_name;
	RelativeLayout layout_info, rl_switch_voice, rl_switch_vibrate,
			rl_switch_notification, layout_blacklist;
	ImageView iv_open_voice, iv_close_voice, iv_open_notification,
			iv_close_notification, iv_open_vibrate, iv_close_vibrate;
	View view1, view2;

	SharePreferenceUtil mShareUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mShareUtil = mApplication.getSpUtil();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_me, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_logout:
			CustomApplication.getInstance().logout();
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;

		case R.id.layout_blacklist:
			startActivity(new Intent(getActivity(), BlacklistActivity.class));
			break;

		case R.id.layout_info:
			Intent intent = new Intent(getActivity(), InfoActivity.class);
			intent.putExtra("from", "me");
			startActivity(intent);
			break;

		case R.id.rl_switch_notification:
			if (iv_open_notification.getVisibility() == View.VISIBLE) {
				iv_open_notification.setVisibility(View.INVISIBLE);
				iv_close_notification.setVisibility(View.VISIBLE);
				mShareUtil.setPushNotifyEnable(false);
				rl_switch_vibrate.setVisibility(View.GONE);
				rl_switch_voice.setVisibility(View.GONE);
				view1.setVisibility(View.GONE);
				view2.setVisibility(View.GONE);
			} else {
				iv_open_notification.setVisibility(View.VISIBLE);
				iv_close_notification.setVisibility(View.INVISIBLE);
				mShareUtil.setPushNotifyEnable(true);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				rl_switch_voice.setVisibility(View.VISIBLE);
				view1.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
			}

			break;

		case R.id.rl_switch_vibrate:
			if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_open_vibrate.setVisibility(View.INVISIBLE);
				iv_close_vibrate.setVisibility(View.VISIBLE);
				mShareUtil.setAllowVibrateEnable(false);
			} else {
				iv_open_vibrate.setVisibility(View.VISIBLE);
				iv_close_vibrate.setVisibility(View.INVISIBLE);
				mShareUtil.setAllowVibrateEnable(true);
			}
			break;
		case R.id.rl_switch_voice:
			if (iv_open_voice.getVisibility() == View.VISIBLE) {
				iv_open_voice.setVisibility(View.INVISIBLE);
				iv_close_voice.setVisibility(View.VISIBLE);
				mShareUtil.setAllowVoiceEnable(false);
			} else {
				iv_open_voice.setVisibility(View.VISIBLE);
				iv_close_voice.setVisibility(View.INVISIBLE);
				mShareUtil.setAllowVoiceEnable(true);
			}
			break;
		}
	}

	public void initView() {
		layout_blacklist = (RelativeLayout) findViewById(R.id.layout_blacklist);

		layout_info = (RelativeLayout) findViewById(R.id.layout_info);
		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_voice = (RelativeLayout) findViewById(R.id.rl_switch_voice);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_voice.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);

		iv_open_notification = (ImageView) findViewById(R.id.iv_open_notification);
		iv_close_notification = (ImageView) findViewById(R.id.iv_close_notification);
		iv_open_voice = (ImageView) findViewById(R.id.iv_open_voice);
		iv_close_voice = (ImageView) findViewById(R.id.iv_close_voice);
		iv_open_vibrate = (ImageView) findViewById(R.id.iv_open_vibrate);
		iv_close_vibrate = (ImageView) findViewById(R.id.iv_close_vibrate);
		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);

		tv_set_name = (TextView) findViewById(R.id.tv_set_name);
		btn_logout = (Button) findViewById(R.id.btn_logout);

		boolean isAllowNotify = mShareUtil.isAllowPushNotify();
		boolean isAllowVoice = mShareUtil.isAllowVoice();
		boolean isAllowVibrate = mShareUtil.isAllowPushNotify();

		if (isAllowVibrate) {
			iv_open_vibrate.setVisibility(View.VISIBLE);
			iv_close_vibrate.setVisibility(View.INVISIBLE);
		} else {
			iv_close_vibrate.setVisibility(View.VISIBLE);
			iv_open_vibrate.setVisibility(View.INVISIBLE);
		}

		if (isAllowVoice) {
			iv_open_voice.setVisibility(View.VISIBLE);
			iv_close_voice.setVisibility(View.INVISIBLE);
		} else {
			iv_close_voice.setVisibility(View.VISIBLE);
			iv_open_voice.setVisibility(View.INVISIBLE);
		}

		if (isAllowNotify) {
			iv_open_notification.setVisibility(View.VISIBLE);
			iv_close_notification.setVisibility(View.INVISIBLE);
		} else {
			iv_open_notification.setVisibility(View.VISIBLE);
			iv_close_notification.setVisibility(View.INVISIBLE);
		}

		btn_logout.setOnClickListener(this);
		layout_blacklist.setOnClickListener(this);
		layout_info.setOnClickListener(this);
	}

	/*
	 * 设置当前用户名。
	 */
	public void initData() {
		tv_set_name.setText(BmobUserManager.getInstance(getActivity())
				.getCurrentUser().getUsername());
	}
}
