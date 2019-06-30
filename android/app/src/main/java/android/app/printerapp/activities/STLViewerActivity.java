package android.app.printerapp.activities;

import android.app.printerapp.R;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.enums;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class STLViewerActivity extends AppCompatActivity
{
    private String filename;
    private String TAG = "STLViewerActivity.class";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stlviewer);

        // Hiding the action bar
        this.hideActionBar();
    }

    private void hideActionBar()
    {
        ActionBar bar = this.getSupportActionBar();
        if(bar!=null)
        {
            bar.hide();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        Log.i(TAG, "Fragment Attached");

        if(filename == null)
        {
          this.filename = this.getIntent().getStringExtra(enums.bundleKeys.filenameComm.getValue());
        }

        Log.i(TAG, "Filename to be sent = "+ filename);

        // Send a bundle to the fragement
        Bundle bundle = new Bundle();
        bundle.putString(enums.bundleKeys.filenameComm.getValue(),this.filename);
        fragment.setArguments(bundle);
    }

} //End of class
