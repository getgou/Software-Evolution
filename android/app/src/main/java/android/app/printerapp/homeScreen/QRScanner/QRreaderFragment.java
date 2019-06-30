package android.app.printerapp.homeScreen.QRScanner;

import android.Manifest;
import android.app.printerapp.R;
import android.app.printerapp.util.ui.dialogs.waitingDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

public class QRreaderFragment extends Fragment implements QRreaderInterface.rControllerToViewInterface, SurfaceHolder.Callback
{

    //region Class variables and constructor
    //Class variables
    private SurfaceView SVCamera;
    private QRreaderInterface.rViewInterface controller;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private View viewOfInterest;
    private Boolean isNonePermissionViewShown = false;
    private waitingDialog dialogWaiting;

    public QRreaderFragment()
    {
        // Empty constructor
    }
    //endregion

    //region Fragment Lifecycle Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_qr_reader, container, false);

        // Initialize variables
        this.initializeVariables(view);

        // Set listeners
        this.setListeners();

        return view;
    }
    //endregion

    //region Complying with readerInterface.rControllerToViewInterface Methods
    @Override
    public void displayCamera(final CameraSource cameraSource)
    {
        Log.i("QRCode", "displayCamera():");
        try
        {
            cameraSource.start(SVCamera.getHolder());
        }
        catch (IOException io)
        {
            Log.i("QRCode", "IO Exception");
        }
    }

    @Override
    public void sendMessage(int message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar()
    {
        if(this.dialogWaiting != null)
        {
            if(this.dialogWaiting.isDialogShown())
            {
                Log.i("QRCode", "showProgressBar(): It is shown already");
            }
            else
            {
                Log.i("QRCode", "showProgressBar(): Not shown");
                Log.i("QRCode", "showProgressBar(): Showing the Progress bar");
                this.dialogWaiting.show();
            }

        }
    }

    @Override
    public void dismissProgressBar()
    {

        if(this.dialogWaiting != null)
        {
            if(this.dialogWaiting.isDialogShown())
            {
                Log.i("QRCode", "dismissProgressBar(): It is shown");
                Log.i("QRCode", "dismissProgressBar(): Dismissing the Progress bar");
                this.dialogWaiting.dismiss();
            }
            else
            {
                Log.i("QRCode", "dismissProgressBar(): Progress bar Not shown");
            }

        }
    }

    //endregion

    //region Helper Methods - General
    private void initializeVariables(View view)
    {
        this.SVCamera               = (SurfaceView) view.findViewById(R.id.surfaceViewCameraView);
        this.controller             = new QRreaderController(this.getActivity(), this);
        this.viewOfInterest         = getLayoutInflater().inflate(R.layout.camera_permission_denied,null);
        this.dialogWaiting          = new waitingDialog(this.getActivity());
    }

    private void setListeners()
    {
        this.SVCamera.getHolder().addCallback(this);
    }

    private void addNonePermissionView()
    {
        // Change the flag
        this.isNonePermissionViewShown = true;

        // Add the view
        getActivity().addContentView(this.viewOfInterest,new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void removeNonePermissionView()
    {
        Log.i("QRCode", "removeNonePermissionView: ");

        this.isNonePermissionViewShown = false;
        //((ViewGroup) viewOfInterest.getParent()).removeView(viewOfInterest);
        this.viewOfInterest.setVisibility(View.GONE);

    }

    private boolean isPermissionViewShown()
    {
        return this.isNonePermissionViewShown;
    }

    //endregion

    //region Complying with SurfaceHolder.Callback Methods
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.i("QRCode", "surfaceCreated()");

        // Check if permission for using camera has been granted
        if(this.isPermissionGranted())
        {
            // Start reader
            this.controller.startReading();
        }
        else
        {
            // Ask for permission
            this.askPermission();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        //Empty
        Log.i("QRCode", "surfaceChanged:");

        if(this.isPermissionGranted() && this.isPermissionViewShown())
        {
            // Remove the view
            this.removeNonePermissionView();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // Notify controller to stop the camera
        this.controller.stopReading();
    }
    //endregion

    //region Helper Methods - Requesting Permission to use camera
    private boolean isPermissionGranted()
    {

        boolean result = false;

        try
        {
            Log.i("Permission", "Inside try");
            result = (ActivityCompat.checkSelfPermission(this.getActivity().getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

        }
        catch(Exception e)
        {
            Log.i("Permission", "Error at try : "+ e.getMessage());
            return false;

        }

        return result;
    }

    //Request camera permission
    private void askPermission()
    {
        Log.i("QRCode", "askPermission()");
        // No explanation needed, we can request the permission.
        this.requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
    }

    // Handling permission solution
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        Log.i("QRCode", "onRequestPermission()");

        switch (requestCode)
        {
            case MY_CAMERA_REQUEST_CODE:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("QRCode", "Permission Granted");

                    if(isNonePermissionViewShown)
                    {
                        this.removeNonePermissionView();
                    }

                    // Request controller to start reading.
                    this.controller.startReading();

                }
                else
                {
                    Log.i("QRCode", "Permission Denied");

                    // Display layout indicating that permission was not granted
                    if(!this.isNonePermissionViewShown)
                    {
                        // Show none permission view
                        this.addNonePermissionView();
                    }
                }
                return;
            }
        }
    }

    //endregion

    //region Public Methods for receiving messages from parent Activity
    public void stopCamera()
    {
        if(this.isPermissionGranted())
        {
            this.controller.stopReading();
        }
        else
        {
            if(this.isNonePermissionViewShown)
            {
                // Hide the view
                this.viewOfInterest.setVisibility(View.GONE);
            }
        }
    }

    public void reStartCamera()
    {
        if (this.isPermissionGranted())
        {
            this.controller.startReading();
        }
        else
        {
            // Unhide the view
            this.viewOfInterest.setVisibility(View.VISIBLE);
        }
    }
    //endregion

} //End of class
