package android.app.printerapp.homeScreen.QRScanner;


import android.app.printerapp.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.view.View;

public class box extends View
{
    private Paint paint = new Paint();
    private Paint mTransparentPaint;
    private Paint mSemiBlackPaint;
    private Path mPath = new Path();

    public box(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        // Initialize the paints to be used
        this.initializePaints();

        //Position to draw the drawable
        int x0 = canvas.getWidth()/2;
        int y0 = canvas.getHeight()/2;
        int dx = canvas.getHeight()/4;
        int dy = canvas.getHeight()/3;

        float x1 = (float) x0;
        float y1 = (float) y0;
        float xd = (float) dx;
        float yd = (float) dy;

        // Reset the path
        this.mPath.reset();

        // Show the dashed square from drawable
        Drawable drawable = getResources().getDrawable(R.drawable.dotted_rectangle);
        drawable.setBounds(x0-dx, y0-dy, x0+dx, y0+dy);
        drawable.draw(canvas);

        // Set the transparent area
        this.mPath.addRect(x1-xd,y1-yd,x1+xd,y1+yd, Path.Direction.CCW);
        canvas.drawRect(x0-dx, y0-dy, x0+dx, y0+dy,mTransparentPaint);

        // Set the black background
        this.mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.parseColor("#4dffffff"));
    }

    private void initializePaints()
    {
        mTransparentPaint = new Paint();
        mTransparentPaint.setColor(Color.TRANSPARENT);
        mTransparentPaint.setStrokeWidth(10);

        mSemiBlackPaint = new Paint();
        mSemiBlackPaint.setColor(Color.TRANSPARENT);
        mSemiBlackPaint.setStrokeWidth(10);
    }


} //End of class
