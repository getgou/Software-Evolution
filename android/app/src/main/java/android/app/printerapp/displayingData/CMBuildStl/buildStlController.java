package android.app.printerapp.displayingData.CMBuildStl;

import android.app.Activity;
import android.app.printerapp.activities.STLViewerActivity;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.dialogs.downloadProgressDialog;
import android.app.printerapp.util.ui.enums;
import android.content.Intent;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

public class buildStlController implements Serializable, dataInterfaces.buildStlModelInterface, dataInterfaces.dfViewInterface
{
    //region Class variables and Constructor
    //Class variables
    private transient dataInterfaces.buildStlControllerToModelInterface model;
    private transient WeakReference<dataInterfaces.dfControllerToViewInterface> view;
    private transient Activity activity;
    private transient downloadProgressDialog progressDialog;

    //Class constructor
    public buildStlController(dataInterfaces.dfControllerToViewInterface view, Activity activity)
    {
        this.activity       = activity;
        this.model          = new buildStlModel(this, activity);
        this.view           = new WeakReference<dataInterfaces.dfControllerToViewInterface>(view);
        this.progressDialog = new downloadProgressDialog(activity);
    }
    //endregion

    //region Complying with dataInterfaces.buildStlModelInterface Methods

    @Override
    public void sendMessageToUser(int message)
    {
        // Stop the progress dialog
        if(this.progressDialog.isDialogShown())
        {
            this.progressDialog.dismiss();
        }

        this.view.get().sendMessageToUser(message);
    }

    @Override
    public void displayPersonality(LinkedHashMap<String, String> data)
    {
        this.view.get().displayPersonality(data);
    }

    @Override
    public void changeContent(int position, String newContent)
    {
        // Not used
    }

    @Override
    public void openSTLViewer(String fileName)
    {
        //Stop the progress dialog
        if(this.progressDialog.isDialogShown())
        {
            this.progressDialog.dismiss();
        }
        
        // Notify the viewer to open the the given filename
        Intent intent = new Intent(this.activity, STLViewerActivity.class);
        intent.putExtra(enums.bundleKeys.filenameComm.getValue(),fileName);
        this.activity.startActivity(intent);
    }

    @Override
    public void displayProgress(int percentage)
    {
        this.progressDialog.setProgressPercentage(percentage);
    }

    //endregion

    //region Complying with dataInterfaces.dfViewInterface Methods
    @Override
    public void getMapWithData(Personality personality)
    {
        this.model.getMapWithData(personality);
    }

    @Override
    public void itemClicked(int position, String key)
    {
        // TODO: 11/29/17 Start the download progress dialog
        if(!this.progressDialog.isDialogShown())
        {
            this.progressDialog.show();
        }
        
        // Notify model of the file that needs to be downloaded
        this.model.itemClicked(position, key);
    }

    //endregion

} //End of class
