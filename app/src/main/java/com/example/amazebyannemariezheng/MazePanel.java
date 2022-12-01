package com.example.amazebyannemariezheng;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

public class MazePanel extends View implements P7PanelF22{
    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;
    private int width=1000;
    private int height=1000;

    private Context context;
    private AttributeSet attr;

    private static final int greenWM = Color.parseColor("#115740");
    private static final int goldWM = Color.parseColor("#916f41");
    private static final int blackWM = Color.parseColor("#222222");



    public MazePanel(Context context) {
        super(context);
        this.context = context;

        init(null);
    }

    public MazePanel(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.attr=attrs;

        init(attrs);
    }
//should not call onDraw or onMeasure
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas); //should use the canvas.drawbitMap to draw onto the canvas

        myTestImage(canvas);

//        canvas.drawBitmap(bitmap,canvas.getWidth(),canvas.getHeight(), paint);
    }


    @Override
    protected void onMeasure(int width, int height) { //sets the Maze Panel height and width
        super.onMeasure(width, height);
        setMeasuredDimension(this.width, this.height);

//        MazePanel.setMeasuredDimension(width, height);
    }



    private void init(@Nullable AttributeSet set) {
        bitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bitmap);
        paint = new Paint();
        invalidate();
    }


    private void myTestImage(Canvas c){
        canvas = c;
        addBackground(100);
        setColor(Color.YELLOW);
        addFilledRectangle(0, 0, 200, 500);

        addLine(0, 0, 500, 500);
        setColor(Color.RED);
        addFilledOval(300,150, 200, 200);

        setColor(Color.GREEN);
        addFilledOval(600,200, 300, 300);

        addMarker(200,200,"fuck this game");

        addLine(1000, 1000, 500, 500);

        int[] xCoordArray = new int[]{ 500,650,720,800};
        int[] yCoordArray = new int[]{ 500,800,850,500};
        addPolygon(xCoordArray,yCoordArray,4);

        setColor(Color.RED);
        int[] xCoordArray2 = new int[]{ 200,450,320,300};
        int[] yCoordArray2 = new int[]{ 400,300,550,300};
        addFilledPolygon(xCoordArray2,yCoordArray2,4);

        commit();

    }

    @Override
    public void commit() {
        //new view and redrawing
        invalidate();
    }

    @Override
    public boolean isOperational() {
        //to do ask teacher
        return false;
    }

    @Override
    public void setColor(int argb) {
        paint.setColor(argb);
    }

    @Override
    public int getColor() {
        return paint.getColor();
    }

    @Override
    public void addBackground(float percentToExit) {

        int blendedColor=ColorUtils.blendARGB(blackWM,goldWM,percentToExit/100);
        setColor(blendedColor);
        addFilledRectangle(0, 0, width, height/2);

        blendedColor=ColorUtils.blendARGB(Color.GRAY,greenWM,percentToExit/100);
        setColor(blendedColor);
        addFilledRectangle(0, height/2, width, height);
    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        //so top is x1 and bottom is x2 and height is x2-x1
        //left is y1 right is y2 and width is y2-y1

        paint.setStyle(Paint.Style.FILL);
        Rect r = new Rect(x,y,width+x,height+y);
        canvas.drawRect(r, paint);
    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        paint.setStyle(Paint.Style.FILL);
        Path polygon= new Path();
        polygon.reset(); //when reusing path for new call

        polygon.moveTo(xPoints[0], yPoints[0]);

        for (int i=1; i<nPoints; i++){
            polygon.lineTo(xPoints[i], yPoints[i]);
        }
        polygon.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(polygon, paint);
//paint does if set style of paint to filling
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        Path polygon= new Path();
        polygon.reset(); //when reusing path for new call

        polygon.moveTo(xPoints[0], yPoints[0]);

        for (int i=1; i<nPoints; i++){
            polygon.lineTo(xPoints[i], yPoints[i]);
        }
        polygon.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(polygon, paint);
    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        paint.setStrokeWidth(10f);
        canvas.drawLine((float)startX, (float)startY, (float)endX, (float)endY, paint);

    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(x, y, width+x, height+y, paint);
    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        paint.setStrokeWidth(10f);
        canvas.drawArc(x, y, width + x, height+y, startAngle, arcAngle, false, paint); //ask abt useCenter
    }

    @Override
    public void addMarker(float x, float y, String str) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        canvas.drawText(str, x, y, paint);
    }




    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue) {
    }
}
