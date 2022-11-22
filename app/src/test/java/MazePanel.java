import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MazePanel extends View {
    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;

    public MazePanel(Context context) {
        super(context);

    }
//should not call onDraw or onMeasure
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas); //should use the canvas.drawbitMap to draw onto the canvas
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    //needs to be able to draw rectangles and red ball
}
