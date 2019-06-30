package android.app.printerapp.util.ui;

import android.app.printerapp.R;
import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileHelper
{
    public static boolean doesFileExists(Context context, String filename, enums.FileDownloadType filetype)
    {
        String path = null;

        // Get a file path
        if(filetype == enums.FileDownloadType.MagicsScreenShot)
        {
            path = Environment.getExternalStorageDirectory()+ context.getString(R.string.pathForMSFile);
        }
        else if (filetype == enums.FileDownloadType.STLFile)
        {
            path = Environment.getExternalStorageDirectory()+ context.getString(R.string.pathForSTLFile);
        }

        if(path != null)
        {
            File file = new File(path, filename);

            if(file.exists())
            {
                return true;
            }

            return false;
        }

        return false;
    }
}
