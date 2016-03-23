package com.cth.wechat.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.db.BmobDB;

import com.cth.wechat.R;
import com.cth.wechat.adapter.NewFriendsAdapter;
import com.cth.wechat.view.dialog.DialogTips;

public class NewFriendsActivity extends BaseActivity implements
		OnItemLongClickListener {

	ListView listView;

	NewFriendsAdapter adapter;

	String from = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friends);
		from = getIntent().getStringExtra("from");
		initView();
	}

	private void initView() {
		initTopBarForLeft("������");
		listView = (ListView) findViewById(R.id.list_new_friend);
		listView.setOnItemLongClickListener(this);
		adapter = new NewFriendsAdapter(this, BmobDB.create(this)
				.queryBmobInviteList());
		listView.setAdapter(adapter);
		if (from == null) {// ������֪ͨ���ĵ������λ�����һ��
			listView.setSelection(adapter.getCount());
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		BmobInvitation invite = (BmobInvitation) adapter.getItem(position);
		showDeleteDialog(position, invite);
		return true;
	}

	public void showDeleteDialog(final int position, final BmobInvitation invite) {
		DialogTips dialog = new DialogTips(this, invite.getFromname(),
				"ɾ����������", "ȷ��", true, true);
		// ���óɹ��¼�
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				deleteInvite(position, invite);
			}
		});
		// ��ʾȷ�϶Ի���
		dialog.show();
		dialog = null;
	}

	/**
	 * ɾ������ deleteRecent
	 * 
	 * @param @param recent
	 * @return void
	 * @throws
	 */
	private void deleteInvite(int position, BmobInvitation invite) {
		adapter.remove(position);
		BmobDB.create(this).deleteInviteMsg(invite.getFromid(),
				Long.toString(invite.getTime()));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (from == null) {
			startAnimActivity(MainActivity.class);
		}
	}

}
