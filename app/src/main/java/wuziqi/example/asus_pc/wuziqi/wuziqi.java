package wuziqi.example.asus_pc.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-pc on 2017/6/8.
 */

public class wuziqi extends View {


    private int mPanelWidth;
    private float mLineHeight;
    private int MaxLine = 10;
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private Paint mPaint;

    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;

    private float ratioPieOfLineHeight = 5 * 1.0f / 7;

    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();
    private boolean mIsWhite = true;

    private int MAX_IN_LINE = 5;
    private int number=0;
    private int time=0;
    private NumberComput mnumberComput;


    public wuziqi(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.mipmap.bai);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.mipmap.hei);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(mIsGameOver!=true&&number>0){
                        try {
                            Thread.sleep(1000);
                            time++;
                            mnumberComput.getTime(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int WidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int WidthMode = MeasureSpec.getMode(widthMeasureSpec);

        int HeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int HeightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.e("AAA", "size:" + WidthSize + "::" + HeightSize);
        int width = Math.min(WidthSize, HeightSize);

        if (WidthMode == MeasureSpec.UNSPECIFIED) {
            Log.e("AAA", "WidthMode:" + "UNSPECIFIED");
            width = HeightSize;
        } else if (HeightMode == MeasureSpec.UNSPECIFIED) {
            Log.e("AAA", "HeightMode:" + "UNSPECIFIED");
            width = WidthSize;
        }

        setMeasuredDimension(width, width);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MaxLine;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBorad(canvas);
        drawPieces(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);
        if (whiteWin || blackWin) {
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;
            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
            mOverListener.GameOver(number,time,text);
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
           boolean win= checkHorizontal(x, y, points);
            if(win) return true;
            win= checkVetical(x, y, points);
            if(win) return true;
            win= checkLeftDiagonal(x, y, points);
            if(win) return true;
            win= checkRightDiagonal(x, y, points);
            if(win) return true;
        }
        return false;
    }

    /**
     * @param x
     * @param y
     * @param points
     */
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_IN_LINE) return true;
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_IN_LINE) return true;
        return false;
    }

    /**
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkVetical(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x , y-i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_IN_LINE) return true;
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x , y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_IN_LINE) return true;
        return false;
    }

    /**
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x+i , y-i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_IN_LINE) return true;
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x-i , y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_IN_LINE) return true;
        return false;
    }

    /**
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x-i , y-i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_IN_LINE) return true;
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x+i , y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_IN_LINE) return true;
        return false;
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece
                    , (whitePoint.x + (1 - ratioPieOfLineHeight) / 2) * mLineHeight
                    , (whitePoint.y + (1 - ratioPieOfLineHeight) / 2) * mLineHeight, null);
        }
        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point BlackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (BlackPoint.x + (1 - ratioPieOfLineHeight) / 2) * mLineHeight,
                    (BlackPoint.y + (1 - ratioPieOfLineHeight) / 2) * mLineHeight, null);
        }

    }

    private void drawBorad(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;
        for (int i = 0; i < MaxLine; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int Y = (int) ((0.5 + i) * lineHeight);

            canvas.drawLine(startX, Y, endX, Y, mPaint);
            canvas.drawLine(Y, startX, Y, endX, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) return false;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);
            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            if (mIsWhite) {
                mWhiteArray.add(p);
            } else {
                mBlackArray.add(p);
            }
            mIsWhite = !mIsWhite;
            number++;
            if(mnumberComput!=null) {
                mnumberComput.num(number);
            }
        }
        invalidate();
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private static final String INSTANCE="instance";
    private static final String INSTANCE_GAME_OVER="instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY="instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY="instance_black_array";
    private static final String INSTANCE_ISWHITE="instance_white";
    private static final String INSTANCE_NUMBER="instance_number";

    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        bundle.putInt(INSTANCE_NUMBER,number);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle= (Bundle) state;
            mIsGameOver=bundle.getBoolean(INSTANCE_GAME_OVER);
            mIsWhite=bundle.getBoolean(INSTANCE_ISWHITE);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            number=bundle.getInt(INSTANCE_NUMBER);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void start(){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver=false;
        mIsWhiteWinner=false;
        number=0;
        time=0;
        mnumberComput.num(time);
        invalidate();
    }

    public interface NumberComput{
        public  void num(int number);
        public  void getTime(int time);
    }
    public void setNunberComput(NumberComput mnumberComput){
        this.mnumberComput=mnumberComput;
    }
    private OverListener mOverListener;
    public void setOverListener(OverListener overListener){
        this.mOverListener=overListener;
    }
    public interface OverListener{
        public void GameOver(int number,int tiem,String Win);
    }
}
