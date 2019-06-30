package android.app.printerapp.magicsScreenshot;


import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.BuildFiles;
import android.app.printerapp.backend.models.Part;
import android.app.printerapp.backend.s3Downloader.S3Download;
import android.app.printerapp.backend.s3Downloader.s3downloadInterface;
import android.app.printerapp.util.ui.FileHelper;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.enums;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class magicsScreenshotActivity extends AppCompatActivity implements s3downloadInterface
{
    //region Class variables
    private final String TAG = "MagicsSSActivity.class";
    private String buildID;
    private TextView textViewProgress;
    private ProgressBar progressBar;
    private ImageView imageViewMagicsScreenshot;
    private String imageFileName;
    private S3Download<magicsScreenshotActivity> downloader;
    private ActionBar bar;
    //endregion

    //region Activity Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magics_screenshot);

        // Initialize variables
        this.initializeVariables();

        // Hide the action bar
        this.bar.hide();

        // Download the Build Data
        this.downloadPicture();
    }
    //endregion

    //region Complying with s3downloadInterface Methods
    @Override
    public void downloadComplete()
    {
        // Show the picture
        this.showPicture();
    }

    @Override
    public void downloadFailure(int message)
    {
        // Display the message to the textview
        this.textViewProgress.setText("Download Failed");
    }

    @Override
    public void progressReport(int percentage)
    {
        // Display the percentage in teh progress bar
        this.progressBar.setProgress(percentage);

        // Display the percentage to the textview
        this.textViewProgress.setText(String.valueOf(percentage)+"%");
    }

    //endregion

    //region Downloading picture from S3
    private void downloadPicture()
    {
        // Get the context
        Context context = this;

        // Get the token
        String token = ((MyApplication)this.getApplication()).getToken();

        // Get an endpoint
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<BuildFiles> call = endpoint.getBuildFiles(this.buildID,token);

        //Enqueue the call
        call.enqueue(new Callback<BuildFiles>()
        {
            @Override
            public void onResponse(Call<BuildFiles> call, Response<BuildFiles> response)
            {
                if(response.body()!= null)
                {
                    // Get the Magics object
                    if(response.body().getMagic()!=null && response.body().getMagic().size()>0)
                    {
                        // Get the file name and store it locally
                        imageFileName = response.body().getMagic().get(0).getMagicScreenshot();

                        // Get the list of parts associated to the build
                        List<Part> listOfParts = response.body().getPart();

                        Log.i(TAG, "Image Filename = "+ imageFileName);

                        if(FileHelper.doesFileExists(context,imageFileName,enums.FileDownloadType.MagicsScreenShot))
                        {
                            Log.i(TAG, "File exists.");

                            // Show the picture
                            showPicture();
                        }
                        else
                        {
                            Log.i(TAG, "File does not exists. Downloading...");

                            // Download the file from S3
                            if(downloader!=null)
                            {
                                downloader.downloadFile(imageFileName, enums.FileDownloadType.MagicsScreenShot);
                            }

                        }

                        // Make configurations in this activity based on what type of ID was scanned
                        makeConfigurationForPartsMagicsID(listOfParts);

                    }
                }
            }

            @Override
            public void onFailure(Call<BuildFiles> call, Throwable t)
            {
                Log.i(TAG, "onFailure(): "+ t.getMessage());
            }
        });
    }
    //endregion

    //region Helper Methods
    private void showPicture()
    {
        // Make the progress bar GONE
        this.progressBar.setVisibility(View.GONE);

        // Make the text view for the progress bar GONE
        this.textViewProgress.setVisibility(View.GONE);

        try
        {
            String path = Environment.getExternalStorageDirectory()+getString(R.string.pathForMSFile);
            File file = new File(path, imageFileName);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            this.imageViewMagicsScreenshot.setImageBitmap(bitmap);
            this.imageViewMagicsScreenshot.setVisibility(View.VISIBLE);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void makeConfigurationForPartsMagicsID(List<Part> list)
    {

        // Get the build id
        String qrcode = ((MyApplication)getApplication()).getQrcode();

        Log.i(TAG, "@makeConfigurationFor(): qrcode: " + qrcode);

        // Only perform the following code if the qr code scanned corresponds to a part qrcode
        if(qrcode.contains("P"))
        {
            String magicsID = null;

            // Get the magics ID for the part scanned
            for(int i = 0 ; i < list.size(); i++)
            {
                if(qrcode.equals(list.get(i).getQRCode()))
                {
                    magicsID = list.get(i).getMagicID();
                    break;
                }
            }

            // Display the magics ID in the action bar
            if(bar != null && magicsID != null)
            {
                // Show the bar
                bar.show();

                // String Title to show
                String title = "Part " + qrcode + " has a Magics ID of "+ magicsID;

                // Show the title
                bar.setTitle(title);
            }
            else
            {
                Log.i(TAG, "@makeConfigurationFor(): magicsID = "+ magicsID);
            }
        }
    }

    private void initializeVariables()
    {
        this.bar                        = this.getSupportActionBar();
        this.buildID                    = this.getIntent().getStringExtra(enums.qrCodeType.Build.getValue());
        this.progressBar                = this.findViewById(R.id.downloadProgressBar);
        this.textViewProgress           = this.findViewById(R.id.textviewDownloadProgress);
        this.imageViewMagicsScreenshot  = this.findViewById(R.id.imageViewMagicsScreenshot);
        this.downloader                 = new S3Download<magicsScreenshotActivity>(this, this);
    }
    //endregion

}
