package ntu.bustiming;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.json.JSONArray;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;
    Context mContext;
    NearbyFragment tab1;
    FavoriteFragment tab2;
    RouteFragment tab3;


    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context mcontext) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mContext = mcontext;
        tab1 = new NearbyFragment();
        tab2 = new FavoriteFragment();
        tab3 = new RouteFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            //tab1 = new NearbyFragment();
            return tab1;
        }
        else if (position == 1)        // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            //tab2 = new FavoriteFragment();
            //tab2.refreshData();
            return tab2;
        }
        else{
            //tab3 = new RouteFragment();
            return tab3;
        }
    }
    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
