package com.example.amazebyannemariezheng;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MazePanel extends View {
    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;
    private int width=1500;
    private int height=1500;

    public MazePanel(Context context) {
        super(context);

        bitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bitmap);
        paint = new Paint();
        invalidate();
    }
//should not call onDraw or onMeasure
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas); //should use the canvas.drawbitMap to draw onto the canvas

//        canvas.drawBitmap(bitmap,(canvas.getWidth()),canvas.getHeight(), paint);
    }


    @Override
    protected void onMeasure(int width, int height) { //sets the Maze Panel height and width
        super.onMeasure(width, height);
        MazePanel.this.setMeasuredDimension(width, height);
    }

    public void createImage(Bitmap bitmap){

    }

    //needs to be able to draw rectangles and red ball
}
