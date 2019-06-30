package android.app.printerapp.homeScreen.textScanner;


import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.activities.MainActivity;
import android.app.printerapp.activities.buildMainActivity;
import android.app.printerapp.util.ui.enums;
import android.content.Intent;

import java.lang.ref.WeakReference;

public class textScannerController implements textScannerInterfaces.tsModelInterface, textScannerInterfaces.tsViewInterface
{
    //region Class Variables and Constructor
    private Activity activity;
    private textScannerInterfaces.tsControllerToModelInterface model;
    private WeakReference<textScannerInterfaces.tsControllerToViewInterface> view;

    public textScannerController(textScannerInterfaces.tsControllerToViewInterface view, Activity activity)
    {
        this.activity   = activity;
        this.view       = new WeakReference<textScannerInterfaces.tsControllerToViewInterface>(view);
        this.model      = new textScannerModel(this,activity);
    }

    //endregion

    //region Complying with textScannerInterfaces.tsModelInterface Methods
    @Override
    public void sendMessageToUser(int message)
    {
        // Notify view to send message to user
        this.view.get().sendMessageToUser(message);
    }

    @Override
    public void validQrcode(enums.qrCodeType codeType)
    {
        // Local variables
        Intent intent = null;

        // Stop the spinner
        this.view.get().stopProgressDialog();

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

    //region Complying with textScannerInterfaces.tsViewInterface Methods
    @Override
    public void qrcodeReceived(String qrcode)
    {
        // Notify view to start progress dialog
        this.view.get().startProgressDialog();

        // Check the structure of the qrcode provided
        if(qrcode.matches(this.activity.getString(R.string.qrcodeRegex)))
        {
            // Pass it to the model
            this.model.qrcodeReceived(qrcode);
        }
        else
        {
            // Notify the user that the qrcode is invalid
            this.view.get().sendMessageToUser(R.string.invalidqrcode);
        }
    }

    //endregion

}
