package com.cth.wechat.ui;

import android.os.Bundle;
import android.widget.EditText;
import cn.bmob.v3.listener.UpdateListener;

import com.cth.wechat.R;
import com.cth.wechat.bean.User;
import com.cth.wechat.view.HeaderLayout.onRightImageButtonClickListener;


/**
 * �����ǳƺ��Ա�
 * 
 * @ClassName: SetNickAndSexActivity
 * @Description: TODO
 */
public class UpdateInfoActivity extends BaseActivity {

	EditText edit_nick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_updateinfo);
		initView();
	}

	private void initView() {
		initTopBarForBoth("�޸��ǳ�", R.drawable.base_action_bar_true_bg_selector,
				new onRightImageButtonClickListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						String nick = edit_nick.getText().toString();
						if (nick.equals("")) {
							showToast("����д�ǳ�!");
							return;
						}
						updateInfo(nick);
					}
				});
		edit_nick = (EditText) findViewById(R.id.edit_nick);
	}

	/** �޸�����
	  * updateInfo
	  * @Title: updateInfo
	  * @return void
	  * @throws
	  */
	private void updateInfo(String nick) {
		final User user = userManager.getCurrentUser(User.class);
		User u = new User();
		u.setNick(nick);
		u.setHight(110);
		u.setObjectId(user.getObjectId());
		u.update(this, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				final User c = userManager.getCurrentUser(User.class);
				showToast("�޸ĳɹ�:"+c.getNick()+",height = "+c.getHight());
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("onFailure:" + arg1);
			}
		});
	}
}
