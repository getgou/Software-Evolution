package android.app.printerapp.displayingData.CMBuildStl;


import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.util.ui.FileHelper;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.BuildFiles;
import android.app.printerapp.backend.models.Part;
import android.app.printerapp.backend.s3Downloader.S3Download;
import android.app.printerapp.backend.s3Downloader.s3downloadInterface;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class buildStlModel implements dataInterfaces.buildStlControllerToModelInterface, s3downloadInterface
{

    //region Class variables and Class contructor
    //Class variables
    private static final String TAG = "buildStlModel.class";
    private dataInterfaces.buildStlModelInterface controller;
    private Activity activity;
    private String filename;

    public buildStlModel(dataInterfaces.buildStlModelInterface controller, Activity activity)
    {
        this.controller = controller;
        this.activity   = activity;
    }

    //endregion

    //region Complying with dataInterfaces.buildStlControllerToModelInterface Methods
    @Override
    public void getMapWithData(Personality personality)
    {
        // Get token
        String token = ((MyApplication)activity.getApplication()).getToken();

        // Get the build ID
        String qrcode = ((MyApplication)activity.getApplication()).getQrcode();

        // Get an endpoint
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<BuildFiles> call = endpoint.getBuildFiles(qrcode,token);

        // Enqueue the call
        call.enqueue(new Callback<BuildFiles>()
        {
            @Override
            public void onResponse(Call<BuildFiles> call, Response<BuildFiles> response)
            {
                if (response.body() == null)
                {
                    // Notify controller that a problem occurred
                    controller.sendMessageToUser(R.string.unableToGetFileData);

                    // Send an empty list of item to be displayed
                    controller.displayPersonality(getItemsMap(null, qrcode));
                }
                else
                {
                    // Get the data from the API
                    BuildFiles data = response.body();

                    // Send the data to the controller
                    controller.displayPersonality(getItemsMap(data,qrcode));
                }
            }

            @Override
            public void onFailure(Call<BuildFiles> call, Throwable t)
            {
                // Notify controller that a problem occurred
                controller.sendMessageToUser(R.string.unableToGetFileData);

                // Send an empty list of item to be displayed
                controller.displayPersonality(getItemsMap(null, qrcode));

            }
        });
    }

    @Override
    public void itemClicked(int position, String key)
    {
        // Store the name of the file locally
        this.filename   = key;

        // Download the stl file
        S3Download<buildStlModel> downloader = new S3Download<>(this.activity,this);

        // Call the method for downloading STL file
        if(FileHelper.doesFileExists(this.activity,key,enums.FileDownloadType.STLFile))
        {
            Log.i(TAG, "File exists.");

            // Notify controller that download is complete
            this.controller.openSTLViewer(key);
        }
        else
        {
            Log.i(TAG, "File does not exists. Downloading...");
            downloader.downloadFile(key, enums.FileDownloadType.STLFile);
        }
    }

    //endregion

    //region Complying with the s3downloadInterface Methods
    @Override
    public void downloadComplete()
    {
        // Notify the controller to open the STL file, after download is complete
        this.controller.openSTLViewer(this.filename);
    }

    @Override
    public void downloadFailure(int message)
    {
        // Notify controller that there was a failure
        this.controller.sendMessageToUser(message);
    }

    @Override
    public void progressReport(int percentage)
    {
        // Notify the controller of the progress
        this.controller.displayProgress(percentage);
    }

    //endregion

    //region Helper Methods
    private LinkedHashMap<String, String> getItemsMap(BuildFiles data, String qrCode)
    {
        // List of item name
        LinkedHashMap<String, String> itemsName = new LinkedHashMap<>();

        if(data != null)
        {
            // Get the list of parts
            ArrayList<Part> listOfParts = new ArrayList<>(data.getPart());

            // Populate the map that shows the data to display
            for(int i = 0; i<listOfParts.size(); i++)
            {
                itemsName.put(listOfParts.get(i).getStlFileName(),"IMG");
            }

        }

        return itemsName;
    }
    //endregion

} //End of class
