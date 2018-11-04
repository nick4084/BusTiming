package ntu.bustiming.control;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * This class is a page adapter that handles the pages attached to the sliding tab
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;
    Context mContext;
    NearbyFragment tab1;
    FavoriteFragment tab2;
    RouteFragment tab3;

    /**
     * Cnnstructor method
     * @param fm fragment manager instance
     * @param mTitles array of tab titles
     * @param mNumbOfTabsumb number of tabs
     * @param mcontext applicaiton context
     */
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context mcontext) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mContext = mcontext;
        tab1 = new NearbyFragment();
        tab2 = new FavoriteFragment();
        tab3 = new RouteFragment();
    }

    /**
     * get the fragment instance by tab position
     * @param position index
     * @return fragment
     */
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

    /**
     * return the page title by tab index
     * @param position index
     * @return position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    /**
     * get the number of tabs
     * @return number of tabs
     */
    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
