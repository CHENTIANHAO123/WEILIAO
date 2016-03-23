package com.cth.wechat.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;

import com.cth.wechat.CustomApplication;
import com.cth.wechat.MyMessageReceiver;
import com.cth.wechat.R;
import com.cth.wechat.adapter.MyFragmentAdapter;
import com.cth.wechat.ui.fragment.ChatFragment;
import com.cth.wechat.ui.fragment.ContactsFragment;
import com.cth.wechat.view.BelowView;

public class MainActivity extends FragmentActivity implements OnClickListener,
		EventListener, OnPageChangeListener {

	private ViewPager viewPager;
	private MyFragmentAdapter adapter;
	private List<BelowView> mTabIndicator = new ArrayList<>();

	ImageView chat_tip, contact_tip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOverflowShowingAlways();
		getActionBar().setDisplayShowHomeEnabled(false);
		initNewMessageBroadCast();
		initTagMessageBroadCast();
		initTabIndicator();
		initData();
		BmobChat.getInstance(this).startPollService(20);
	}

	private void initData() {
		chat_tip = (ImageView) findViewById(R.id.iv_recent_tips);
		contact_tip = (ImageView) findViewById(R.id.iv_contact_tips);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter = new MyFragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(this);
	}

	private void initTabIndicator() {
		BelowView one = (BelowView) findViewById(R.id.tab_chat_fragment);
		BelowView two = (BelowView) findViewById(R.id.tab_two);
		BelowView three = (BelowView) findViewById(R.id.tab_three);

		mTabIndicator.add(one);
		mTabIndicator.add(two);
		mTabIndicator.add(three);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		one.setIconAlpha(1.0f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_friend:
			Intent intent = new Intent( MainActivity.this, AddFriendActivity.class );
			startActivity(intent);
			break;

		case R.id.action_scan:
			break;
			
		case R.id.action_feed:
			break;
			
		}
		return true;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (positionOffset > 0) {
			BelowView left = mTabIndicator.get(position);
			BelowView right = mTabIndicator.get(position + 1);

			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		resetOtherTabs();

		switch (v.getId()) {
		case R.id.tab_chat_fragment:
			mTabIndicator.get(0).setIconAlpha(1.0f);
			viewPager.setCurrentItem(0, false);
			break;
		case R.id.tab_two:
			mTabIndicator.get(1).setIconAlpha(1.0f);
			viewPager.setCurrentItem(1, false);
			break;
		case R.id.tab_three:
			mTabIndicator.get(2).setIconAlpha(1.0f);
			viewPager.setCurrentItem(2, false);
			break;
		}
	}

	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicator.size(); i++) {
			mTabIndicator.get(i).setIconAlpha(0);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (BmobDB.create(this).hasUnReadMsg()) {
			chat_tip.setVisibility(View.VISIBLE);
		} else {
			chat_tip.setVisibility(View.GONE);
		}

		if (BmobDB.create(this).hasNewInvite()) {
			contact_tip.setVisibility(View.VISIBLE);
		} else {
			contact_tip.setVisibility(View.GONE);
		}
		MyMessageReceiver.ehList.add(this);
		MyMessageReceiver.mNewNum = 0;

	}

	@Override
	protected void onPause() {
		super.onPause();
		MyMessageReceiver.ehList.remove(this);
	}

	@Override
	public void onAddUser(BmobInvitation arg0) {
		refreshInvite(arg0);
	}

	@Override
	public void onMessage(BmobMsg arg0) {
		// TODO Auto-generated method stub
		refreshNewMsg(arg0);
	}

	@Override
	public void onNetChange(boolean arg0) {
		// TODO Auto-generated method stub
		Toast toast = new Toast(this);
		toast.setText("您当前的网络状态不好。");
		toast.show();
	}

	@Override
	public void onOffline() {
		// TODO Auto-generated method stub
		Toast toast = new Toast(this);
		toast.setText("您的账号在别处登录。");
		toast.show();
	}

	@Override
	public void onReaded(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	private class NewBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			refreshNewMsg(null);
			abortBroadcast();
		}

	}

	public void refreshNewMsg(BmobMsg msg) {
		boolean isAllow = CustomApplication.getInstance().getSpUtil()
				.isAllowVoice();
		if (isAllow) {
			CustomApplication.getInstance().getMediaPlayer().start();
		}
		if (msg != null) {
			BmobChatManager.getInstance(MainActivity.this).saveReceiveMessage(
					true, msg);
		}
		if (viewPager.getCurrentItem() == 0) {
			((ChatFragment) adapter.getListFragments().get(0)).refresh();
		}
	}

	/**
	 * 刷新好友请求
	 * 
	 * @Title: notifyAddUser
	 * @Description: TODO
	 * @param @param message
	 * @return void
	 * @throws
	 */
	private void refreshInvite(BmobInvitation message) {
		boolean isAllow = CustomApplication.getInstance().getSpUtil()
				.isAllowVoice();
		if (isAllow) {
			CustomApplication.getInstance().getMediaPlayer().start();
		}
		contact_tip.setVisibility(View.VISIBLE);
		if (viewPager.getCurrentItem() == 1) {
			((ContactsFragment) adapter.getListFragments().get(1)).refresh();
		} else {
			// 同时提醒通知
			String tickerText = message.getFromname() + "请求添加好友";
			boolean isAllowVibrate = CustomApplication.getInstance()
					.getSpUtil().isAllowVibrate();
			BmobNotifyManager.getInstance(this).showNotify(isAllow,
					isAllowVibrate, R.drawable.ic_launcher, tickerText,
					message.getFromname(), tickerText.toString(),
					NewFriendsActivity.class);
		}
	}

	NewBroadcastReceiver newReceiver;

	public void initNewMessageBroadCast() {
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(
				BmobConfig.BROADCAST_NEW_MESSAGE);
		intentFilter.setPriority(3);
		registerReceiver(newReceiver, intentFilter);

	}

	TagBroadcastReceiver userReceiver;

	private void initTagMessageBroadCast() {
		// 注册接收消息广播
		userReceiver = new TagBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(
				BmobConfig.BROADCAST_ADD_USER_MESSAGE);
		// 优先级要低于ChatActivity
		intentFilter.setPriority(3);
		registerReceiver(userReceiver, intentFilter);
	}

	/**
	 * 标签消息广播接收者
	 */
	private class TagBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			BmobInvitation message = (BmobInvitation) intent
					.getSerializableExtra("invite");
			refreshInvite(message);
			// 终结广播
			abortBroadcast();
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowShowingAlways() {
		try {
			// true if a permanent menu key is present, false otherwise.
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(newReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(userReceiver);
		} catch (Exception e) {
		}
		// 取消定时检测服务
		BmobChat.getInstance(this).stopPollService();
	}
}
