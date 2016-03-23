package com.cth.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cth.wechat.ui.fragment.ChatFragment;
import com.cth.wechat.ui.fragment.ContactsFragment;
import com.cth.wechat.ui.fragment.MeFragment;

public class MyFragmentAdapter extends FragmentPagerAdapter {
	private List<Fragment> listFragments;
	private Fragment fragment;

	public MyFragmentAdapter(FragmentManager fm) {
		super(fm);
		createFragment();
	}

	private void createFragment() {
		listFragments = new ArrayList<Fragment>();
		fragment = new ChatFragment();
		listFragments.add(fragment);
		fragment = new ContactsFragment();
		listFragments.add(fragment);
		fragment = new MeFragment();
		listFragments.add(fragment);
	}

	public List<Fragment> getListFragments() {
		return listFragments;
	}
	@Override
	public Fragment getItem(int position) {

		return listFragments.get(position);
	}

	@Override
	public int getCount() {
		return listFragments.size();
	}

}
