package com.tomorrow_p.common;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapLruCache extends LruCache<String, Bitmap> {

	public BitmapLruCache(int maxSize) {
		super(4 * 1024 * 1024);
	}

	@Override
	protected int sizeOf(String key, Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
	}

}
