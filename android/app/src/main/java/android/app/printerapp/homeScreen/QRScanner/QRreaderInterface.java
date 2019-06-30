package android.app.printerapp.homeScreen.QRScanner;


import com.google.android.gms.vision.CameraSource;

public interface QRreaderInterface
{

    interface rViewInterface
    {
        void startReading();
        void stopReading();
    }

    interface rControllerToViewInterface
    {
        void displayCamera(final CameraSource cameraSource);
        void sendMessage(int message);
        void showProgressBar();
        void dismissProgressBar();
    }

} //End of class
