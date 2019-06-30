package android.app.printerapp.activities;

import android.Manifest;
import android.app.printerapp.R;
import android.app.printerapp.displayingData.CMBuildData.buildDataController;
import android.app.printerapp.postPrinting.RetrieveAllTests.partTestController;
import android.app.printerapp.displayingData.dataFragment;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.enums;
import android.app.printerapp.viewer.ViewerMainFragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;

public class MainActivity extends FragmentActivity
{
    //region Class variables
    private FragmentTabHost mTabHost;
    private final String BUILD_TAB= "BUILD_TAB";
    private final String TEST_TAB= "TEST_TAB";
    private final String VIEWER_TAB= "VIEWER_TAB";
    //endregion

    //region Activity Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Requesting permission to read external storage
        if(!isPermissionGranted())
        {
            this.askPermission();
        }
        else
        {
            // Initialize Variables
            this.initializeVariables();

            // Add fragments
            this.setUptabs();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        // Send arguments to the respective fragment
        this.sendArgumentsToFragments(fragment);
    }
    //endregion

    //region Helper Methods - General
    private void initializeVariables()
    {
        this.mTabHost   = (FragmentTabHost)findViewById(android.R.id.tabhost);
    }
    //endregion

    //region Helper Methods - Setting Tabs
    private void setUptabs()
    {
        // Setup the tab host
        this.mTabHost.setup(this,getSupportFragmentManager(), android.R.id.tabcontent);

        // Add tabs to tab host
        this.addTabs();
    }

    private void addTabs()
    {
        this.mTabHost.addTab(mTabHost.newTabSpec(this.BUILD_TAB).setIndicator("Build", null), dataFragment.class, null);

        // Adding the Post Printing Tab
        this.mTabHost.addTab(mTabHost.newTabSpec(this.TEST_TAB).setIndicator("Tests", null), dataFragment.class, null);

        // Adding the viewer
        this.mTabHost.addTab(mTabHost.newTabSpec(this.VIEWER_TAB).setIndicator("Viewer", null), ViewerMainFragment.class, null);
    }

    private void sendArgumentsToFragments(Fragment fragment)
    {
        // Creating bundle keys
        String bundleKeyPersonality = enums.bundleKeys.personalityComm.getValue();
        String bundleKeyController  = enums.bundleKeys.controllerComm.getValue();
        String bundleKeyFilename    = enums.bundleKeys.filenameComm.getValue();

        if (fragment.getClass() == dataFragment.class && fragment.getTag().equals(this.BUILD_TAB)) {
            // Creating Personalities
            Personality buildDataPersonality        = new Personality("B123", enums.typeOfData.buildData.getValue());

            // Creating controllers
            buildDataController controllerBuildData = new buildDataController((dataInterfaces.dfControllerToViewInterface)fragment, this);

            // Creating bundles
            Bundle bundle1 = new Bundle();

            // Putting personalities to respective bundles
            bundle1.putSerializable(bundleKeyPersonality,buildDataPersonality);

            // Putting controller in respective bundles
            bundle1.putSerializable(bundleKeyController,controllerBuildData);

            // Assigning bundle to respective fragments
            fragment.setArguments(bundle1);
        } else if (fragment.getClass() == dataFragment.class && fragment.getTag().equals(this.TEST_TAB)) {
            // Creating Personalities
            Personality testDataPersonality = new Personality("B123", enums.typeOfData.partTests.getValue());

            // Creating controllers
            partTestController controllerBuildData = new partTestController((dataInterfaces.dfControllerToViewInterface)fragment, this);

            // Creating bundles
            Bundle bundle = new Bundle();

            // Putting personalities to respective bundles
            bundle.putSerializable(bundleKeyPersonality,testDataPersonality);

            // Putting controller in respective bundles
            bundle.putSerializable(bundleKeyController,controllerBuildData);

            // Assigning bundle to respective fragments
            fragment.setArguments(bundle);
        }
    }

    //endregion

    //region Helper Methods - Handling permission
    private static final int MY_REQUEST_CODE = 99;
    private boolean isPermissionGranted()
    {
        boolean result = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        return result;
    }

    //Request  permission
    private void askPermission()
    {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
    }

    // Handling permission solution
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_REQUEST_CODE:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("Permission", "Permission Granted");

                    // Code for opening file
                }
                else
                {
                    Log.i("Permission", "Permission Denied");

                }
                return;
            }
        }
    }
    //endregion
}

//endregion
