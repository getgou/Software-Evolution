package android.app.printerapp.homeScreen;

import android.app.printerapp.R;
import android.app.printerapp.adapters.viewPagerAdapter;
import android.app.printerapp.homeScreen.QRScanner.QRreaderFragment;
import android.app.printerapp.homeScreen.QRScanner.box;
import android.app.printerapp.homeScreen.textScanner.textScannerFragment;
import android.app.printerapp.util.ui.viewPagerAnimation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class homeScreenActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
{

    //region Class Variables
    private ViewPager mViewPager;
    private viewPagerAdapter pagerAdapter;
    private box Box;

    // Fragments
    private QRreaderFragment scannerFragment;
    private textScannerFragment textScannerFragment;

    //endregion
    
    //region Activity Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Set the initial configurations of the activity
        this.initialActivityConfigurations();
        
        // Initialize variables
        this.initializeVariables();

        // Set listeners
        this.setListeners();

        // Configure view pager
        this.configuringViewPager();

        // Add camera boundary box
        this.addBox();
    }
    //endregion

    //region Complying with ViewPager.OnPageChangeListener Methods
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        // Not required
    }

    @Override
    public void onPageSelected(int position)
    {
       if(position == 0) // QR Scanner fragment
       {
            // Show box
           this.Box.setVisibility(View.VISIBLE);

           //Restart the camera
           this.scannerFragment.reStartCamera();
       }
       else if(position == 1) // Text Scanner
       {
           // Hide Box
           this.Box.setVisibility(View.INVISIBLE);

           // Notify the Scanner to release camera resources
           this.scannerFragment.stopCamera();
       }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        // Not required
    }

    //endregion

    //region Helper Method - General

    private void addBox()
    {
        addContentView(this.Box, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private void initialActivityConfigurations()
    {
        ActionBar bar = this.getSupportActionBar();

        if(bar != null)
        {
            bar.hide();
        }
    }

    private void initializeVariables()
    {
        // Fragments
        this.scannerFragment = new QRreaderFragment();
        this.textScannerFragment = new textScannerFragment();

        // List of Fragments
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(this.scannerFragment);
        list.add(this.textScannerFragment);

        // viewpager adapter
        this.pagerAdapter = new viewPagerAdapter(getSupportFragmentManager(),list);

        //View pager
        this.mViewPager = (ViewPager) findViewById(R.id.viewPagerScanner);

        // Camera Box
        this.Box = new box(this);
    }

    private void setListeners()
    {
        this.mViewPager.addOnPageChangeListener(this);
    }

    private void configuringViewPager()
    {
        // Set adapter
        this.mViewPager.setAdapter(this.pagerAdapter);

        // Set animation type
        this.mViewPager.setPageTransformer(true ,new viewPagerAnimation());
    }
    //endregion


} //End of class
