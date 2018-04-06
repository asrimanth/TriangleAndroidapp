package com.example.srimanth.triangleandroidapp.views;


import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.srimanth.triangleandroidapp.ColorBall;
import com.example.srimanth.triangleandroidapp.R;

/**
 * DrawView class holds corner objects and handle them by drawing
 * overlay.
 *
 * @author Chintan Rathod (http://chintanrathod.com)
 */
public class ResizableView extends View {

    Point point1, point3;
    Point point2, point4;

    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    int groupId = -1;
    private ArrayList < ColorBall > colorballs = new ArrayList <> ();
    // array that holds the balls
    private int balID = 0;
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;

    public ResizableView(Context context) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        // setting the start point for the balls
        point1 = new Point();
        point1.x = 50;
        point1.y = 20;

        point2 = new Point();
        point2.x = 150;
        point2.y = 20;

        point3 = new Point();
        point3.x = 150;
        point3.y = 120;

        point4 = new Point();
        point4.x = 50;
        point4.y = 120;

        // declare each ball with the ColorBall class
        colorballs.add(new ColorBall(context, R.drawable.blue_circle, point1));
        colorballs.add(new ColorBall(context, R.drawable.blue_circle, point2));
        colorballs.add(new ColorBall(context, R.drawable.blue_circle, point3));
        colorballs.add(new ColorBall(context, R.drawable.blue_circle, point4));

    }

    public ResizableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ResizableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        // setting the start point for the balls
        point1 = new Point();
        point1.x = 50;
        point1.y = 20;

        point2 = new Point();
        point2.x = 150;
        point2.y = 20;

        point3 = new Point();
        point3.x = 150;
        point3.y = 120;

        point4 = new Point();
        point4.x = 50;
        point4.y = 120;

        // declare each ball with the ColorBall class
        colorballs.add(new ColorBall(context, R.drawable.blue_circle, point1));
        colorballs.add(new ColorBall(context, R.drawable.blue_circle, point2));
        colorballs.add(new ColorBall(context, R.drawable.blue_circle, point3));
        colorballs.add(new ColorBall(context, R.drawable.blue_circle, point4));

    }

    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(0xFFCCCCCC); //if you want another background color

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#55000000"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        // mPaint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);

        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#55FFFFFF"));

        if (groupId == 1) {
            canvas.drawRect(point1.x + colorballs.get(0).getWidthOfBall() / 2,
                    point3.y + colorballs.get(2).getWidthOfBall() / 2, point3.x + colorballs.get(2).getWidthOfBall() / 2, point1.y + colorballs.get(0).getWidthOfBall() / 2, paint);
        } else {
            canvas.drawRect(point2.x + colorballs.get(1).getWidthOfBall() / 2,
                    point4.y + colorballs.get(3).getWidthOfBall() / 2, point4.x + colorballs.get(3).getWidthOfBall() / 2, point2.y + colorballs.get(1).getWidthOfBall() / 2, paint);
        }

        // draw the balls on the canvas
        for (ColorBall ball: colorballs) {
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    new Paint());
        }
    }

    // events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventAction) {

            case MotionEvent.ACTION_DOWN:
                // touch down so check if the finger is on
                // a ball
                balID = -1;
                groupId = -1;
                for (ColorBall ball: colorballs) {
                    // check if inside the bounds of the ball (circle)
                    // get the center for the ball
                    int centerX = ball.getX() + ball.getWidthOfBall();
                    int centerY = ball.getY() + ball.getHeightOfBall();
                    paint.setColor(Color.CYAN);
                    // calculate the radius from the touch to the center of the ball
                    double radCircle = Math.sqrt((double)(((centerX - X) * (centerX - X)) + (centerY - Y) * (centerY - Y)));

                    if (radCircle < ball.getWidthOfBall()) {

                        balID = ball.getID();
                        if (balID == 1 || balID == 3) {
                            groupId = 2;
                            canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                    paint);
                        } else {
                            groupId = 1;
                            canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                    paint);
                        }
                        invalidate();
                        break;
                    }
                    invalidate();
                }

                break;

            case MotionEvent.ACTION_MOVE:
                // touch drag with the ball
                // move the balls the same as the finger
                if (balID > -1) {
                    colorballs.get(balID).setX(X);
                    colorballs.get(balID).setY(Y);

                    paint.setColor(Color.CYAN);

                    if (groupId == 1) {
                        colorballs.get(1).setX(colorballs.get(0).getX());
                        colorballs.get(1).setY(colorballs.get(2).getY());
                        colorballs.get(3).setX(colorballs.get(2).getX());
                        colorballs.get(3).setY(colorballs.get(0).getY());
                        canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                paint);
                    } else {
                        colorballs.get(0).setX(colorballs.get(1).getX());
                        colorballs.get(0).setY(colorballs.get(3).getY());
                        colorballs.get(2).setX(colorballs.get(3).getX());
                        colorballs.get(2).setY(colorballs.get(1).getY());
                        canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                paint);
                    }

                    invalidate();
                }

                break;

            case MotionEvent.ACTION_UP:
                // touch drop - just do things here after dropping

                break;
        }
        // redraw the canvas
        invalidate();
        return true;

    }

}