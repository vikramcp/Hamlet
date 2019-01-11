package in.technogenie.hamlet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

import in.technogenie.hamlet.beans.CustomerVO;
import in.technogenie.hamlet.fragment.MembersBusinessFragment;
import in.technogenie.hamlet.fragment.MembersFragment;

public class MembersTabsAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    List<CustomerVO> customers = null;
    List categoryList = null;

    public MembersTabsAdapter(FragmentManager fm, int NoofTabs, List<CustomerVO> customers) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
        this.customers = customers;
    }

    public MembersTabsAdapter(FragmentManager fm, int NoofTabs, List<CustomerVO> customers, List categoryList) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
        this.customers = customers;
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Log.d("MembersTabsAdapter", "MembersFragment View Triggered.." + position);
                MembersFragment home = MembersFragment.newInstance(customers);
                return home;
            case 1:
                Log.d("MembersTabsAdapter", "MembersBusinessFragment View Triggered.." + position);
                MembersBusinessFragment biz = MembersBusinessFragment.newInstance(customers, categoryList);
                return biz;
            default:
                MembersFragment home3 = new MembersFragment();
                return home3;
        }
    }


    public int getCount() {
        return mNumOfTabs;
    }
}
