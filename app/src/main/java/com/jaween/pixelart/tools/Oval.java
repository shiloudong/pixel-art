package com.jaween.pixelart.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.jaween.pixelart.tools.attributes.OvalToolAttributes;

/**
 * Created by ween on 10/19/14.
 */
public class Oval extends Tool {

    private static final int TOOL_ID = 3;
    public Oval(String name, Drawable icon) {
        super(name, icon, TOOL_ID);
        toolAttributes = new OvalToolAttributes();
    }

    private PointF start = new PointF();
    private RectF ovalBounds = new RectF();

    @Override
    protected void onStart(Bitmap bitmap, PointF event) {
        start.x = event.x;
        start.y = event.y;

        draw(canvas, bitmap, event);
    }

    @Override
    protected void onMove(Bitmap bitmap, PointF event) {
        // Locks the oval to a circle
        if (((OvalToolAttributes) toolAttributes).isCircleLocked()) {
            lockCircle(event);
        }

        draw(canvas, bitmap, event);
    }


    @Override
    protected void onEnd(Bitmap bitmap, PointF event) {
        // Locks the oval to a circle
        if (((OvalToolAttributes) toolAttributes).isCircleLocked()) {
            lockCircle(event);
        }

        draw(canvas, bitmap, event);
    }

    // TODO: Allow the coordinates of oval to go less than 0 in both x and y
    private void draw(Canvas canvas, Bitmap bitmap, PointF end) {
        ovalBounds.set(start.x, start.y, end.x, end.y);

        // Enables ovals to be drawn past the top and left canvas boundaries by modifying ovalBounds
        boundaryWorkAround(ovalBounds);

        canvas.setBitmap(bitmap);
        canvas.drawOval(ovalBounds, toolAttributes.getPaint());
    }

    // Locks the oval to a circle (modifies the input point)
    private void lockCircle(PointF end) {
        float dX = end.x - start.x;
        float dY = end.y - start.y;

        float ovalWidth = Math.abs(dX);
        float ovalHeight = Math.abs(dY);

        float diameter = Math.max(ovalWidth, ovalHeight);

        // The diameter of the circle is the larger of the oval's width or height
        if (dX > 0 && dY > 0) {
            // Lower right quadrant
            end.y = start.y + diameter;
            end.x = start.x + diameter;
        } else if (dX > 0 && dY < 0) {
            // Upper right quadrant
            end.y = start.y - diameter;
            end.x = start.x + diameter;
        } else if (dX < 0 && dY < 0) {
            // Upper left quadrant
            end.y = start.y - diameter;
            end.x = start.x - diameter;
        } else if (dX < 0 && dY > 0) {
            // Lower left quadrant
            end.y = start.y + diameter;
            end.x = start.x - diameter;
        }
    }

    // Canvas can't draw oval with edges inverted past the top and left boundaries, flips the edges to make it work
    private void boundaryWorkAround(RectF bounds) {
        if (bounds.right < bounds.left) {
            bounds.set(bounds.right, bounds.top, bounds.left, bounds.bottom);
        }
        if (bounds.bottom < bounds.top) {
            bounds.set(bounds.left, bounds.bottom, bounds.right, bounds.top);
        }
    }
}
