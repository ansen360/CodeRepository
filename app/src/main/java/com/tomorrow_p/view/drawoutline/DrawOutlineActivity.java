package com.tomorrow_p.view.drawoutline;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;

import com.tomorrow_p.R;

public class DrawOutlineActivity extends Activity {
	private DrawOutlineView drawOutlineView;
	private Bitmap sobelBm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_draw_outline);
		// 将Bitmap压缩处理，防止OOM
		Bitmap bm = CommenUtils.getRatioBitmap(this, R.drawable.draw_outline_test, 100, 100);
		// 返回的是处理过的Bitmap
		sobelBm = SobelUtils.Sobel(bm);

		final Bitmap paintBm = CommenUtils.getRatioBitmap(this, R.drawable.draw_outline_paint, 10,
				20);	drawOutlineView = (DrawOutlineView) findViewById(R.id.outline);
				drawOutlineView.setPaintBm(paintBm);  
	}

	// 根据Bitmap信息，获取每个位置的像素点是否需要绘制
	// 使用boolean数组而不是int[][]主要是考虑到内存的消耗
	private boolean[][] getArray(Bitmap bitmap) {
		boolean[][] b = new boolean[bitmap.getWidth()][bitmap.getHeight()];

		for (int i = 0; i < bitmap.getWidth(); i++) {
			for (int j = 0; j < bitmap.getHeight(); j++) {
				if (bitmap.getPixel(i, j) != Color.WHITE)
					b[i][j] = true;
				else
					b[i][j] = false;
			}
		}
		return b;
	}

	boolean first = true;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (first) {
			first = false;
			drawOutlineView.beginDraw(getArray(sobelBm));
		} else
			drawOutlineView.reDraw(getArray(sobelBm));
		return true;
	}
}