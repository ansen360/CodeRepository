package org.code.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.ScrollView;

import org.code.activity.TestActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ansen on 2017/8/31 16:06.
 *
 * @E-mail: ansen360@126.com
 * @Blog: "http://blog.csdn.net/qq_25804863"
 * @Github: "https://github.com/ansen360"
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: org.code.common
 * @Description: TODO
 */
public class ScrollableViewUtil {
    private static final int DELAY = 2;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private boolean isEnd;


    public void start(final ScrollView view, final ImageView testImage, final OnScrollFinishedListener listener) {
        // 模拟一个按下事件,按下位置为屏幕中心
        final MotionEvent motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, view.getWidth() / 2, view.getHeight() / 2, 0);
        view.dispatchTouchEvent(motionEvent);
        motionEvent.setAction(MotionEvent.ACTION_MOVE);
        // 触发移动事件的最小距离为 ViewConfiguration.get(view.getContext()).getScaledTouchSlop() + 1
        motionEvent.setLocation(motionEvent.getX(), motionEvent.getY() - (ViewConfiguration.get(view.getContext()).getScaledTouchSlop() + 1));
        view.dispatchTouchEvent(motionEvent);
        motionEvent.setLocation(motionEvent.getX(), view.getHeight() / 2);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isEnd) {
                    //停止时正好一屏则全部绘制，否则绘制部分
                    if ((view.getHeight() / 2 - (int) motionEvent.getY()) % view.getHeight() == 0) {
                        Bitmap bitmap = rec(view);
                        bitmaps.add(bitmap);
                        listener.onFinish(bitmap);
                    } else {
                        Bitmap origBitmap = rec(view);
//                        view.setDrawingCacheEnabled(true);
//                        view.buildDrawingCache();
//                        Bitmap b1 = view.getDrawingCache();
//                        testImage.setImageBitmap(b1);
                        int y1 = (int) motionEvent.getY();
                        int y = view.getHeight() / 2 - y1;    // 滑出屏幕的长度
                        Bitmap bitmap = Bitmap.createBitmap(origBitmap, 0, view.getHeight() - y % view.getHeight(), view.getWidth(), y % view.getHeight());

                        Log.d("ansen", "y1: " + y1 + " y: " + y + " %: " + y % view.getHeight() + " wi: " + view.getHeight());
                        bitmaps.add(bitmap);
//                        testImage.setImageBitmap(bitmap);
                        listener.onFinish(bitmap);
//                        origBitmap.recycle();
                    }
                    //最后一张可能高度不足view的高度
                    int h = view.getHeight() * (bitmaps.size() - 1);
                    Bitmap bitmap = bitmaps.get(bitmaps.size() - 1);
                    h = h + bitmap.getHeight();
                    Bitmap result = Bitmap.createBitmap(view.getWidth(), h, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    for (int i = 0; i < bitmaps.size(); i++) {
                        Bitmap b = bitmaps.get(i);
                        canvas.drawBitmap(b, 0, i * view.getHeight(), null);
//                        b.recycle();
                    }
                    testImage.setImageBitmap(result);
                    listener.onFinish(result);
                    return;
                }
                Log.d("ansen", "move: " + (view.getHeight() / 2 - (int) motionEvent.getY()) + " height: " + view.getHeight() + ((view.getHeight() / 2 - (int) motionEvent.getY()) % view.getHeight() == 0));
                if ((view.getHeight() / 2 - (int) motionEvent.getY()) % view.getHeight() == 0) {    // 判断是否滑出一屏(包含第一屏)
                    Bitmap bitmap = rec(view);
                    bitmaps.add(bitmap);
                    testImage.setImageBitmap(bitmap);
                    listener.onFinish(bitmap);
                }
                motionEvent.setAction(MotionEvent.ACTION_MOVE);
                //模拟每次向上滑动一个像素，这样可能导致滚动特别慢，实际使用时可以修改该值，但判断是否正好滚动了
                //一屏幕就不能简单的根据 (view.getHeight() / 2 - (int) motionEvent.getY()) % view.getHeight() == 0 来确定了。
                //可以每次滚动n个像素，当发现下次再滚动n像素时就超出一屏幕时可以改变n的值，保证下次滚动后正好是一屏幕，
                //这样就可以根据(view.getHeight() / 2 - (int) motionEvent.getY()) % view.getHeight() == 0来判断要不要截屏了。
                motionEvent.setLocation((int) motionEvent.getX(), (int) motionEvent.getY() - 3);
                if (view.getChildAt(0).getHeight() - view.getHeight()
                        == view.getScrollY()) {
                    isEnd = true;
                    Log.d("ansen", "" + motionEvent.getY() + view.getBottom());
                }
                view.dispatchTouchEvent(motionEvent);
                view.postDelayed(this, DELAY);
            }
        }, DELAY);
    }


    public void stop() {
        isEnd = true;
    }

    static boolean a = false;

    private Bitmap rec(View view) {
        Bitmap film;
        if (a) {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            film = view.getDrawingCache();
        } else {

            film = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();

            canvas.setBitmap(film);

            view.draw(canvas);
        }
        a = !a;
        return film;
    }

    public interface OnScrollFinishedListener {
        void onFinish(Bitmap bitmap);
    }
}
