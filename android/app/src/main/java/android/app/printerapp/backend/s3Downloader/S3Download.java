package android.app.printerapp.backend.s3Downloader;

import android.app.printerapp.R;
import android.app.printerapp.util.ui.enums;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

public class S3Download<T>
{
    //region Class variables and Constructor
    private File downloadFromS3 = null;
    private String TAG = "S3Download.class";
    private Context context;
    private T delegate;

    public S3Download(Context context, T delegate)
    {
        this.context    = context;
        this.delegate   = delegate;
    }
    //endregion

    //region Method for downloading file
    public void downloadFile(String filename, enums.FileDownloadType fileType)
    {
        // Get the keys
        String accessKey    = this.context.getString(R.string.S3AccessKey);
        //String secreteKey   = this.context.getString(R.string.S3SecreteKey);
        String secreteKey ="o00SxWJpjoyS2h7GB3sKq1qlgSWE9B0RxlTi7XuF";

        // Generate the credentials
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secreteKey);

        // Instantiate an S3 client
        AmazonS3 s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));

        // Get a transfer utility
        TransferUtility transferUtility = new TransferUtility(s3, this.context);

        // Create a path variable
        String path = null;

        // Create the Path based on the filetype
        if(fileType == enums.FileDownloadType.MagicsScreenShot)
        {
            // Getting the path to download the file to
            path = Environment.getExternalStorageDirectory().getPath() + context.getString(R.string.pathForMSFile)+filename;
        }
        else if(fileType == enums.FileDownloadType.STLFile)
        {
            // Getting the path to download the file to
            path = Environment.getExternalStorageDirectory().getPath() + context.getString(R.string.pathForSTLFile)+filename;
        }

        Log.i(TAG,"Path = "+ path);

        // Ensure the path is not null
        if(path != null)
        {
            // Initialize the file object
            this.downloadFromS3 = new File(path);
        }
        else
        {
            Log.i(TAG,"Path is null");
        }

        // Creating an observer
        TransferObserver observer = null;

        // Ensuring the file is not null
        if(this.downloadFromS3 != null)
        {
            // Initialize the observer based on the fileType
            if(fileType == enums.FileDownloadType.MagicsScreenShot)
            {
                observer = transferUtility.download("elasticbeanstalk-eu-central-1-469547283381/magicscreenshot",filename,this.downloadFromS3);
            }
            else if (fileType == enums.FileDownloadType.STLFile)
            {
                observer = transferUtility.download("elasticbeanstalk-eu-central-1-469547283381/stl",filename,this.downloadFromS3);
            }

            // Observe the download
            this.transferObserverListener(observer);

        }
        else
        {
            Log.i(TAG,"The variable this.downloadFromS3 is null");
        }
    }
    //endregion

    //region Method for listening to the download process
    private void transferObserverListener(TransferObserver transferObserver)
    {
        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state)
            {
                Log.i(TAG, "State Change = " + state);

                if(state == TransferState.COMPLETED)
                {
                    Log.i(TAG, "Download Completed");

                    // Notify the controller that the download is complete
                    ((s3downloadInterface)delegate).downloadComplete();

                }
                else if (state == TransferState.FAILED)
                {
                    Log.i(TAG, "Download Failed");

                    // Notify the delegate that the download failed
                    ((s3downloadInterface)delegate).downloadFailure(R.string.unableTOGetMS);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal)
            {
                // Get the download percentage
                int percentage = (int) (bytesCurrent/bytesTotal * 100);

                // Inform the delegate of the progress
                ((s3downloadInterface)delegate).progressReport(percentage);
            }

            @Override
            public void onError(int id, Exception ex)
            {
                Log.i(TAG,"Error = "+ ex.getMessage());

                // Notify the delegate that the download failed
                ((s3downloadInterface)delegate).downloadFailure(R.string.unableTOGetMS);
            }

        });
    }
    //endregion

    //region Method for deleting content of the internal directory
    public void deleteAllFilesOfType(enums.FileDownloadType fileType)
    {
        // Initializing local variables
        String path = null;
        File directory;

        // Determining the path to be erased based on the file type
        if(fileType == enums.FileDownloadType.MagicsScreenShot)
        {
            path = Environment.getExternalStorageDirectory().getPath() + "/SWEREA/MagicsScreenshot";

        }
        else if(fileType == enums.FileDownloadType.STLFile)
        {
            path = Environment.getExternalStorageDirectory().getPath() + "/SWEREA/STLFile";
        }

        // Erasing the content of the directory
        if(path != null)
        {
            // Creating a directory
            directory = new File(path);

            // Ensuring the directory variable is in fact a directory
            if(directory.isDirectory())
            {
                Log.i(TAG,"It is a directory");

                // Getting the list of files found in the directory
                File[] children = directory.listFiles();

                Log.i(TAG,"Number of files = "+ children.length);

                // Erasing the files in the directory
                for(int i = 0 ; i< children.length; i++)
                {
                    File file = children[i];
                    file.delete();
                }
            }
            else
            {
                Log.i(TAG,"It is not a directory");
            }
        }
    }

    //endregion


}
