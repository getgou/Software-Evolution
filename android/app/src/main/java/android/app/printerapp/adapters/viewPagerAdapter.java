package android.app.printerapp.adapters;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class viewPagerAdapter extends FragmentPagerAdapter
{
    //region Class variables and constructor
    // Class variables
    private ArrayList<Fragment> listOfFragments;

    // Class constructor
    public viewPagerAdapter(FragmentManager fm, @NonNull ArrayList<Fragment> listOfFragments)
    {
        super(fm);
        this.listOfFragments = new ArrayList<>(listOfFragments);
    }
    //endregion

    @Override
    public Fragment getItem(int position)
    {
        if(this.listOfFragments != null)
        {
            return this.listOfFragments.get(position);
        }

        return null;
    }

    @Override
    public int getCount()
    {
        if(this.listOfFragments != null)
        {
            return this.listOfFragments.size();
        }

        return 0;

    }
} //End of class
