package android.app.printerapp.homeScreen.QRScanner;


import android.app.Activity;
import android.app.printerapp.activities.MainActivity;
import android.app.printerapp.activities.buildMainActivity;
import android.app.printerapp.homeScreen.textScanner.textScannerInterfaces;
import android.app.printerapp.homeScreen.textScanner.textScannerModel;
import android.app.printerapp.util.ui.enums;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.lang.ref.WeakReference;

public class QRreaderController implements QRreaderInterface.rViewInterface, textScannerInterfaces.tsModelInterface
{

    // region Class variables and constructor
    // Class variables
    private Activity activity;
    private WeakReference<QRreaderInterface.rControllerToViewInterface> view;
    private BarcodeDetector detector;
    private final CameraSource cameraSource;
    private Handler handler;
    private textScannerInterfaces.tsControllerToModelInterface model;

    // Class constructor
    public QRreaderController(Activity activity, QRreaderInterface.rControllerToViewInterface view)
    {
        this.activity           = activity;
        this.view               = new WeakReference<>(view);
        this.detector           = new BarcodeDetector.Builder(this.activity.getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        this.cameraSource       = new CameraSource.Builder(this.activity.getApplicationContext(), detector).setAutoFocusEnabled(true).build();
        this.handler            = new Handler(activity.getMainLooper());
        this.model      = new textScannerModel(this,activity);
    }
    //endregion

    //region Complying with readerInterface.rViewInterface Methods
    @Override
    public void startReading()
    {
        Log.i("QRCode", "Camera has started");

        // Notify view to display the camera
        this.view.get().displayCamera(this.cameraSource);

        // Initialize QR Code handler
        this.initializeQrCodeHandler();
    }

    @Override
    public void stopReading()
    {
        Log.i("QRCode", "Camera Stopped");

        // Stop the camera
        this.cameraSource.stop();

        // Releasing resources used by the detector
        this.detector.release();
    }

    //endregion

    //region Complying with textScannerInterfaces.tsModelInterface Methods
    @Override
    public void sendMessageToUser(int message)
    {
        // Send message to user
        this.view.get().sendMessage(message);

        // Dismiss the progress bar
        this.view.get().dismissProgressBar();

        // Start reading
        this.startReading();
    }

    @Override
    public void validQrcode(enums.qrCodeType codeType)
    {
        // Local variables
        Intent intent = null;

        // Stop the spinner
        this.view.get().dismissProgressBar();

        // Stop the reading
        this.stopReading();

        // Initializing the intent to the appropriate class based on the code.
        if(codeType.getValue().equals(enums.qrCodeType.Build.getValue()))
        {
            intent = new Intent(this.activity,buildMainActivity.class);
        }
        else if (codeType.getValue().equals(enums.qrCodeType.Part.getValue()))
        {
            intent = new Intent(this.activity,MainActivity.class);
        }
        else if(codeType.getValue().equals(enums.qrCodeType.Material.getValue()))
        {
            // Todo Create Class for material
        }

        // Initialize the intent
        if(intent != null)
        {
            this.activity.startActivity(intent);
        }
    }

    //endregion

    //region Helper Method - Barcode Detector initialization and Handler
    private void initializeQrCodeHandler()
    {
        Log.i("QRCode", "Reader initialized");

        // Notify the view to stop showing the dialog
        this.view.get().dismissProgressBar();

        // Determine what to do once the QR code has been detected
        this.detector.setProcessor(new Detector.Processor<Barcode>()
        {
            @Override
            public void release()
            {
                Log.i("QRCode", "release() @ setProcessor()");
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections)
            {
                Log.i("QRCode", "Reading ...");
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0)
                {
                    String qrCode = barcodes.valueAt(0).displayValue;

                    Log.i("QRCode", "Text Detected = "+ qrCode);

                    // ActionToPerfom outsie the UI Thread
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // Run action here
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    // Stop Reading
                                    stopReading();

                                    // Start the pogress dialog
                                    view.get().showProgressBar();

                                    // Notify the model of the received qrcode
                                    model.qrcodeReceived(qrCode);

                                }
                            });

                        }

                    }).start();
                }
            }
        });
    }
    //endregion


} //End of class
