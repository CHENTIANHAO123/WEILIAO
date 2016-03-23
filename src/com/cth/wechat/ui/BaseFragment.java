package com.cth.wechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

import com.bmob.utils.BmobLog;
import com.cth.wechat.CustomApplication;
import com.cth.wechat.R;
import com.cth.wechat.view.HeaderLayout;
import com.cth.wechat.view.HeaderLayout.HeaderStyle;
import com.cth.wechat.view.HeaderLayout.onLeftImageButtonClickListener;
import com.cth.wechat.view.HeaderLayout.onRightImageButtonClickListener;

public class BaseFragment extends Fragment {

	public BmobChatManager chatManager;
	public BmobUserManager userManager;
	public HeaderLayout headerLayout;
	
	public CustomApplication mApplication;
	
	protected View centenView;
	public LayoutInflater layoutInflater;
	
	private Handler mHandler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		subInit();
	}
	
	public void subInit(){
		mApplication = CustomApplication.getInstance();
		userManager = BmobUserManager.getInstance(getActivity());
		chatManager = BmobChatManager.getInstance(getActivity());
		layoutInflater = LayoutInflater.from(getActivity());
	}
	
	public void runOnWorkThread( Runnable action ){
		new Thread(action).start();
	}
	
	public void runOnUIThread( Runnable action ){
		mHandler.post(action);
	}
	
	Toast mToast;

	public void showToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}
	
	public void ShowLog(String msg){
		BmobLog.i(msg);
	}
	
	public View findViewById(int paramInt) {
		return getView().findViewById(paramInt);
	}
	
	/**
	 * 只有title initTopBarLayoutByTitle
	 * @Title: initTopBarLayoutByTitle
	 * @throws
	 */
	public void initTopBarForOnlyTitle(String titleName) {
		headerLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		headerLayout.init(HeaderStyle.DEFAULT_TITLE);
		headerLayout.setDefaultTitle(titleName);
	}

	/**
	 * 初始化标题栏-带左右按钮
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBoth(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener) {
		headerLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		headerLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		headerLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
		headerLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}

	/**
	 * 只有左边按钮和Title initTopBarLayout
	 * 
	 * @throws
	 */
	public void initTopBarForLeft(String titleName) {
		headerLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		headerLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
		headerLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
	}
	
	/** 右边+title
	  * initTopBarForRight
	  * @return void
	  * @throws
	  */
	public void initTopBarForRight(String titleName,int rightDrawableId,
			onRightImageButtonClickListener listener) {
		headerLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		headerLayout.init(HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
		headerLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}
	
	// 左边按钮的点击事件
	public class OnLeftButtonClickListener implements
			onLeftImageButtonClickListener {

		@Override
		public void onClick() {
			getActivity().finish();
		}
	}
	
	/**
	 * 动画启动页面 startAnimActivity
	 * @throws
	 */
	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}
	
	public void startAnimActivity(Class<?> cla) {
		getActivity().startActivity(new Intent(getActivity(), cla));
	}

	

	
}
