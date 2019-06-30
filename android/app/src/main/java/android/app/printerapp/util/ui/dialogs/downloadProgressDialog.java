package android.app.printerapp.util.ui.dialogs;


import android.app.AlertDialog;
import android.app.printerapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class downloadProgressDialog
{
    // region Class variables and constructor
    private TextView textViewProgressPercentage;
    private ProgressBar progressBar;
    private Context context;
    private AlertDialog alertDialog;

    public downloadProgressDialog(Context context)
    {
        this.context    = context;
    }
    //endregion

    //region Public Methods
    public void show()
    {
        // Get the custom view
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.download_file_progress, null);

        // Initialize variables
        this.progressBar                = dialogView.findViewById(R.id.downloadSTLProgressBar);
        this.textViewProgressPercentage = dialogView.findViewById(R.id.textviewDownloadProgressSTL);

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
        if(this.alertDialog != null)
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

    public void setProgressPercentage(int percentage)
    {
        if(this.progressBar!= null && this.textViewProgressPercentage != null)
        {
            this.progressBar.setProgress(percentage);
            this.textViewProgressPercentage.setText(String.valueOf(percentage)+"%");
        }
    }

}
