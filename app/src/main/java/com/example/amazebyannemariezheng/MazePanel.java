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

    private static final int MAIN_COLOR = greenWM; //new Color(0.4f, 0.4f, 1.0f);
    // fixed configuration for circle surrounding arms
    private static final int CIRCLE_HIGHLIGHT = ColorUtils.blendARGB(Color.parseColor("#115740"), Color.BLACK, 0.2f);
    //Color.decode("#115740").darker();// = new Color(1.0f, 1.0f, 1.0f, 0.8f);
    private static final int CIRCLE_SHADE = ColorUtils.blendARGB(Color.parseColor("#115740"), Color.WHITE, 0.2f);
    //Color.decode("#115740").brighter(); //new Color(0.0f, 0.0f, 0.0f, 0.2f);
    // fixed configuration for letters used to indicate direction
    private static final int MARKER_COLOR = Color.BLACK;



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

        addMarker(200,200,"0v0");

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

    /**
     * MazeColors enumerates color choices for specific purposes.
     * The prefix indicates which class or feature uses it.
     */
    public enum MazeColors {BACKGROUND_TOP, BACKGROUND_BOTTOM,
        MAP_DEFAULT, MAP_WALL_DEFAULT, MAP_WALL_SEENBEFORE, MAP_CURRENTLOCATION, MAP_SOLUTION,
        COMPASSROSE_MAIN_COLOR, COMPASSROSE_CIRCLE_HIGHLIGHT, COMPASSROSE_CIRCLE_SHADE,
        COMPASSROSE_MARKER_COLOR_DEFAULT, COMPASSROSE_MARKER_COLOR_CURRENTDIRECTION, COMPASSROSE_BACKGROUND,
        TITLE_LARGE, TITLE_SMALL, TITLE_DEFAULT, FRAME_OUTSIDE, FRAME_MIDDLE, FRAME_INSIDE,
        FIRSTPERSON_DEFAULT} ;

    // address needs from Map.java
    // requires predefined color for white (seen), grey for other walls, red for currentlocation, yellow for solution

    public int getColor(MazeColors color) {
        switch (color) {
            // unused color settings for FirstPersonView, background
            // original color choices for background black on top, dark gray at bottom
            case BACKGROUND_TOP:
                return Color.BLACK; // unused, just for completeness
            case BACKGROUND_BOTTOM:
                return Color.DKGRAY; // unused, just for completeness
            // color settings for Map
            case MAP_DEFAULT:
                return Color.WHITE;
            case MAP_WALL_DEFAULT:
                return Color.GRAY;
            case MAP_WALL_SEENBEFORE:
                return Color.WHITE;
            case MAP_CURRENTLOCATION:
                return Color.RED;
            case MAP_SOLUTION:
                return Color.YELLOW;
            // color settings for CompassRose
            case COMPASSROSE_MAIN_COLOR:
                return MAIN_COLOR;
            case COMPASSROSE_CIRCLE_HIGHLIGHT:
                return CIRCLE_HIGHLIGHT;
            case COMPASSROSE_CIRCLE_SHADE:
                return CIRCLE_SHADE;
            case COMPASSROSE_MARKER_COLOR_DEFAULT:
                return goldWM;
            case COMPASSROSE_MARKER_COLOR_CURRENTDIRECTION:
                return MARKER_COLOR;
            case COMPASSROSE_BACKGROUND:
                return Color.WHITE;
            // color settings for SimpleScreens
            case TITLE_DEFAULT:
                return blackWM;
            case TITLE_LARGE:
                return goldWM;
            case TITLE_SMALL:
                return greenWM;
            case FRAME_OUTSIDE:
                return greenWM;
            case FRAME_MIDDLE:
                return goldWM;
            case FRAME_INSIDE:
                return Color.WHITE;
            // color settings for FirstPersonView
            case FIRSTPERSON_DEFAULT:
                return Color.WHITE;
            default:
                break;
        }
        return Color.WHITE; // this is a mistake if you get here!!!
    }

}
