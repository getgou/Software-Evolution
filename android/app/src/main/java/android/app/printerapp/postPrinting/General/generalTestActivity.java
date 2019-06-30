package android.app.printerapp.postPrinting.General;

import android.app.printerapp.R;
import android.app.printerapp.adapters.viewPagerAdapter;
import android.app.printerapp.displayingData.dataFragment;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.enums;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
public class generalTestActivity extends AppCompatActivity {

    private GridView grid;
    private dataFragment generalTestDataFragment;
    private viewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_test2);

        // Hide action bar
        ActionBar bar = this.getSupportActionBar();
        if(bar!=null) {
            bar.hide();
        }

        this.initializeVariables();
    }

    private void configureFragments(Fragment fragment) {
        // Creating bundle keys
        String bundleKeyPersonality = enums.bundleKeys.personalityComm.getValue();
        String bundleKeyController  = enums.bundleKeys.controllerComm.getValue();

        Personality buildDataPersonality    = new Personality("B123", enums.typeOfData.generalTest.getValue());

        // Creating controllers
        generalTestController controllerBuildData = new generalTestController((dataInterfaces.dfControllerToViewInterface) fragment, this);

        // Creating bundles
        Bundle bundle = new Bundle();

        // Putting controller in respective bundles
        bundle.putSerializable(bundleKeyPersonality,buildDataPersonality);

        bundle.putSerializable(bundleKeyController,controllerBuildData);


        // Assigning bundle to respective fragments
        fragment.setArguments(bundle);
    }

    private void initializeVariables() {
        // Fragments
        this.generalTestDataFragment = new dataFragment();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        this.configureFragments(fragment);
    }
}
