package android.app.printerapp.util.ui.dialogs;

import android.app.AlertDialog;
import android.app.printerapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class waitingDialog
{
    // region Class variables and Constructor
    // Class variables
    private Context context;
    private AlertDialog alertDialog;

    // Class constructor
    public waitingDialog(Context context)
    {
        this.context    = context;
    }
    //endregion

    //region Public Methods
    public void show()
    {
        // Get the custom view
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.waiting_dialog, null);

        // Get a builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the alert dialog view
        builder.setView(dialogView);

        // Create alert dialog
        this.alertDialog = builder.create();
        this.alertDialog.setCancelable(true);
        this.alertDialog.setCanceledOnTouchOutside(false);
        this.alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Show it
        this.alertDialog.show();

    }

    public void dismiss()
    {
        if(this.alertDialog!=null)
        {
            this.alertDialog.dismiss();
        }
    }

    public Boolean isDialogShown()
    {
        if(this.alertDialog!=null)
        {
            return this.alertDialog.isShowing();
        }

        return false;
    }

    //endregion

}
