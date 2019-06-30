package android.app.printerapp.activities;

import android.app.printerapp.util.ui.Log;
import android.app.printerapp.R;
import android.app.printerapp.adapters.viewPagerAdapter;
import android.app.printerapp.displayingData.CMBuildData.buildDataController;
import android.app.printerapp.displayingData.CMBuildHistory.buildHistoryController;
import android.app.printerapp.displayingData.CMBuildStl.buildStlController;
import android.app.printerapp.displayingData.dataFragment;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.enums;
import android.app.printerapp.util.ui.viewPagerAnimation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class buildMainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
{

    //region Class variables
    private ViewPager mViewPager;
    private viewPagerAdapter pagerAdapter;
    private ActionBar bar;

    // Fragments
    private dataFragment buildDataFragment;
    //private dataFragment buildHistoryFragment;
    private dataFragment buildSTLFragment;
    //endregion

    //region Activity lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_main);

        // Initialize variables
        this.initializeVariables();

        // Set listeners
        this.setListeners();

        // Configuring fragments
        this.configureFragments();

        // Configure view pager
        this.configuringViewPager();

        // Initial title configuration
        this.initialTitleSetup();

    }
    //endregion

    //region Complying with ViewPager.OnPageChangeListener Methods
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position)
    {
        String title = "";

        if(position == enums.typeOfData.buildData.getValue())
        {
            // Get  title
            title = getResources().getString(R.string.buildData);
        }
        else if (position == enums.typeOfData.buildHistory.getValue())
        {
            // Get  title
            title = getResources().getString(R.string.buildHistory);
        }
        else if (position == enums.typeOfData.buildStls.getValue())
        {
            // Get  title
            title = getResources().getString(R.string.buildSTL);
        }

        // Set the title
        Log.i("Title", title);
        this.bar.setTitle(title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //endregion

    //region Helper Methods - General
    private void initializeVariables()
    {
        // Fragments
        this.buildDataFragment      = new dataFragment();
        //this.buildHistoryFragment   = new dataFragment();
        this.buildSTLFragment       = new dataFragment();

        // List of Fragments
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(enums.typeOfData.buildData.getValue(),this.buildDataFragment);
        //list.add(enums.typeOfData.buildHistory.getValue(),this.buildHistoryFragment);
        list.add(enums.typeOfData.buildStls.getValue(),this.buildSTLFragment);

        // viewpager adapter
        this.pagerAdapter = new viewPagerAdapter(getSupportFragmentManager(),list);

        //View pager
        this.mViewPager = (ViewPager) findViewById(R.id.viewPagerScanner);

        // Others
        this.bar    = this.getSupportActionBar();

    }

    private void configureFragments()
    {
        // Creating bundle keys
        String bundleKeyPersonality = enums.bundleKeys.personalityComm.getValue();
        String bundleKeyController  = enums.bundleKeys.controllerComm.getValue();

        // Creating Personalities
        Personality buildDataPersonality    = new Personality("B123", enums.typeOfData.buildData.getValue());
        Personality buildHistoryPersonality = new Personality("B123", enums.typeOfData.buildHistory.getValue());
        Personality buildSTLPersonality     = new Personality("B123", enums.typeOfData.buildStls.getValue());

        // Creating controllers
        buildDataController controllerBuildData         = new buildDataController(this.buildDataFragment, this);
        //buildHistoryController controllerBuildHistory   = new buildHistoryController(this.buildHistoryFragment, this);
        buildStlController controllerBuildStl           = new buildStlController(this.buildSTLFragment, this);

        // Creating bundles
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();

        // Putting personalities to respective bundles
        bundle1.putSerializable(bundleKeyPersonality,buildDataPersonality);
        bundle2.putSerializable(bundleKeyPersonality,buildHistoryPersonality);
        bundle3.putSerializable(bundleKeyPersonality,buildSTLPersonality);

        // Putting controller in respective bundles
        bundle1.putSerializable(bundleKeyController,controllerBuildData);

        //bundle2.putSerializable(bundleKeyController,controllerBuildHistory);
        bundle3.putSerializable(bundleKeyController,controllerBuildStl);

        // Assigning bundle to respective fragments
        this.buildDataFragment.setArguments(bundle1);
        //this.buildHistoryFragment.setArguments(bundle2);
        this.buildSTLFragment.setArguments(bundle3);

    }

    private void configuringViewPager()
    {
        // Set adapter
        this.mViewPager.setAdapter(this.pagerAdapter);

        // Set animation type
        this.mViewPager.setPageTransformer(true ,new viewPagerAnimation());
    }

    private void setListeners()
    {
        this.mViewPager.addOnPageChangeListener(this);
    }

    private void initialTitleSetup()
    {
        this.bar.setTitle(getResources().getString(R.string.buildData));
    }
    //endregion


} //End of class
