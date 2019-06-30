package android.app.printerapp;

import android.app.printerapp.viewer.DataStorage;
import android.app.printerapp.viewer.ViewerMainFragment;
import android.app.printerapp.viewer.ViewerSurfaceView;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Martin on 2017-10-11.
 */

public class StlSnapshooter {

    public static ViewerSurfaceView mSnapshotSurface;



    /**
     * This method is called from STlFile or GcodeFile when data is ready to render. Add the view to the layout.
     */
    public static void takeSnapshot (Context context) {
        List<DataStorage> list = new ArrayList<DataStorage>();
        View generatingProjectDialog = LayoutInflater.from(context).inflate(R.layout.dialog_loading_project, null);
        FrameLayout mSnapshotLayout = (FrameLayout) generatingProjectDialog.findViewById (R.id.framesnapshot);

        mSnapshotSurface = new ViewerSurfaceView (context, list, ViewerSurfaceView.NORMAL, ViewerMainFragment.DO_SNAPSHOT);
        mSnapshotSurface.setZOrderOnTop(true);
        mSnapshotLayout.addView(mSnapshotSurface);
    }

    /**
     * Creates the snapshot of the model
     */
    public static void saveSnapshot (final int width, final int height, final ByteBuffer bb ) {
        int screenshotSize = width * height;

        int pixelsBuffer[] = new int[screenshotSize];
        bb.asIntBuffer().get(pixelsBuffer);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixelsBuffer, screenshotSize-width, -width, 0, 0, width, height);
        pixelsBuffer = null;

        short sBuffer[] = new short[screenshotSize];
        ShortBuffer sb = ShortBuffer.wrap(sBuffer);
        bitmap.copyPixelsToBuffer(sb);

        //Making created bitmap (from OpenGL points) compatible with Android bitmap
        for (int i = 0; i < screenshotSize; ++i) {
            short v = sBuffer[i];
            sBuffer[i] = (short) (((v&0x1f) << 11) | (v&0x7e0) | ((v&0xf800) >> 11));
        }
        sb.rewind();
        bitmap.copyPixelsFromBuffer(sb);

        try {
            FileOutputStream fos = new FileOutputStream("pathToSnapshot.thumb"); // TODO: change path
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
