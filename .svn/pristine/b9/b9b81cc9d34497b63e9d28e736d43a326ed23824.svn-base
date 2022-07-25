package com.capgemini.sesp.ast.android.ui.activity.receiver;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.capgemini.sesp.ast.android.BuildConfig;
import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Custom View to show GPS Reception animation
 *
 * @author Capgemini
 * @version 1.0
 * @see //AbstractFlowPage
 * @since 12th March, 2015
 */

public class GpsAnimationView extends View {
    private transient String mExampleString;
    private transient int mExampleColor = Color.RED;

    private transient float mExampleDimension = 0;
    private transient Drawable mExampleDrawable;

    private transient TextPaint mTextPaint;

    // Variables added for processing
    private transient int satWidth;
    private transient int satHeight;

    private transient int cellPhoneHeight;
    private transient int cellPhoneWidth;

    private transient Bitmap satelliteBitmap = null;
    private transient Bitmap cellPhoneBitmap = null;

    private transient Paint signalPaint = null;
    private transient Context context = null;

    // Draw initial curves
    private transient final RectF oval = new RectF();

    private transient float radius1 = 50;
    private transient float radius2 = 60;
    private transient float radius3 = 70;

    private transient int deltaRadius = 0;

    public int getDeltaRadius() {
        return deltaRadius;
    }

    public void setDeltaRadius(final int deltaRadius) {
        if (deltaRadius >= 300) {
            this.deltaRadius = deltaRadius;
        }
        invalidate();
    }

    public GpsAnimationView(final Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public GpsAnimationView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public GpsAnimationView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs, defStyle);
    }

    private void init(final AttributeSet attrs, final int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GpsAnimationView, defStyle, 0);

        mExampleString = a.getString(R.styleable.GpsAnimationView_exampleString);
        mExampleColor = a.getColor(R.styleable.GpsAnimationView_exampleColor, mExampleColor);

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing
        // with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(R.styleable.GpsAnimationView_exampleDimension, mExampleDimension);

        if (a.hasValue(R.styleable.GpsAnimationView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(R.styleable.GpsAnimationView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        signalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        signalPaint.setColor(this.context.getResources().getColor(R.color.colorHighlight));
        signalPaint.setStrokeWidth(3);
        signalPaint.setStyle(Paint.Style.STROKE);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        if (BuildConfig.DEBUG) {
            Log.d("GpsAnimationView-onDraw", "view effective width=" + contentWidth + " pixel");
            Log.d("GpsAnimationView-onDraw", "view effective height=" + contentHeight + " pixel");
        }

        processBitmapDisplay(contentHeight, contentWidth);

        if (satelliteBitmap != null) {
            canvas.drawBitmap(satelliteBitmap, 0, paddingTop, null);
            //satelliteCenter = new Point(satelliteBitmap.getWidth()/2, satelliteBitmap.getHeight()/2);
        }

        if (cellPhoneBitmap != null) {
            canvas.drawBitmap(cellPhoneBitmap, getWidth() - cellPhoneWidth/*(Float.valueOf(contentWidth/5)*2)*/,
                    getHeight() - cellPhoneHeight
                    /*viewHeight-(viewHeight/3)*/, null);
        }


        oval.set(satelliteBitmap.getWidth() / 2, satelliteBitmap.getHeight() / 2,
                satelliteBitmap.getWidth() / 2 + radius1 + deltaRadius, satelliteBitmap.getHeight() / 2 + radius1 + deltaRadius);
        canvas.drawArc(oval, -30, 120, false, signalPaint);

        oval.set(satelliteBitmap.getWidth() / 2, satelliteBitmap.getHeight() / 2,
                satelliteBitmap.getWidth() / 2 + radius2 + deltaRadius, satelliteBitmap.getHeight() / 2 + radius2 + deltaRadius);
        canvas.drawArc(oval, -30, 120, false, signalPaint);

        oval.set(satelliteBitmap.getWidth() / 2, satelliteBitmap.getHeight() / 2,
                satelliteBitmap.getWidth() / 2 + radius3 + deltaRadius, satelliteBitmap.getHeight() / 2 + radius3 + deltaRadius);
        canvas.drawArc(oval, -30, 120, false, signalPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop, paddingLeft
                    + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    private void processBitmapDisplay(final int contentHeight, final int contentWidth) {
        // Draw the satellite image
        try {
            int satelliteHeight = contentHeight / 3;
            int satelliteWidth = (contentWidth / 5) * 2;

            if (BuildConfig.DEBUG) {
                Log.d("GpsAnimationView-onDraw", "satellite width=" + satelliteWidth + " pixel");
                Log.d("GpsAnimationView-onDraw", "satellite height=" + satelliteHeight + " pixel");
            }

            // Process the image decoding only if
            if (satHeight == 0 || satWidth == 0) {
                // The view is resized or screen orientation is changed
                satelliteBitmap = AndroidUtilsAstSep.decodeBitmap(getResources(), R.drawable.satellite, satelliteHeight, satelliteWidth);
                Log.d("GpsAnimationView-onDraw", "Satellite bitmap successfully decoded");
                satHeight = satelliteBitmap.getHeight();
                satWidth = satelliteBitmap.getWidth();
            }

            // Draw the cell phone image
            if (BuildConfig.DEBUG) {
                Log.d("GpsAnimationView-onDraw", "satellite width=" + satelliteWidth + " pixel");
                Log.d("GpsAnimationView-onDraw", "satellite height=" + satelliteHeight + " pixel");
            }

            if (satelliteHeight != cellPhoneHeight
                    || satelliteWidth != cellPhoneWidth) {
                // The view is resized or screen orientation is changed
                cellPhoneBitmap = AndroidUtilsAstSep.decodeBitmap(getResources(), R.drawable.phone, satelliteHeight, satelliteWidth);
                Log.d("GpsAnimationView-onDraw", "Satellite bitmap successfully decoded");

                cellPhoneHeight = cellPhoneBitmap.getHeight();
                cellPhoneWidth = cellPhoneBitmap.getWidth();
            }
        } catch (Exception e) {
            writeLog("GpsAnimationView : processBitmapDisplay()", e);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this
     * string is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this
     * color is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view,
     * this dimension is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view,
     * this drawable is drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
