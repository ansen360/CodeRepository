package com.ansen.common;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class LruCacheUtil {

	private static final String TAG = "LruCacheUtil";

	public static final String BLUR = "_blur";

	private int MAXMEMONRY = (int) (Runtime.getRuntime().maxMemory() / 1024);
	private static LruCache<String, Bitmap> mMemoryCache;

	private LruCacheUtil() {
		if (mMemoryCache == null)
			mMemoryCache = new LruCache<String, Bitmap>(1024) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					// 重写此方法来衡量每张图片的大小，默认返回图片数量。
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}

				@Override
				protected void entryRemoved(boolean evicted, String key,
						Bitmap oldValue, Bitmap newValue) {
					Logger.d(TAG, "soft cacheis full , clear the oldValue \r\nkey====="+key);
					synchronized (oldValue) {
						oldValue.recycle();
						oldValue = null;
					}
				}
			};
	}

	private static LruCacheUtil object = null;

	public synchronized static LruCacheUtil getInstance() {
		if (object == null || mMemoryCache == null) {
			object = new LruCacheUtil();
		}
		return object;
	}

	public void clearCache() {
		if (mMemoryCache != null) {
			if (mMemoryCache.size() > 0) {
				Logger.d(TAG, "mMemoryCache.size() " + mMemoryCache.size());
				mMemoryCache.evictAll();
				Logger.d(TAG, "mMemoryCache.size()" + mMemoryCache.size());
			}
			mMemoryCache = null;
		}
	}

	public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		Logger.d(TAG, "mMemoryCache.size() " + mMemoryCache.size());
		if (mMemoryCache.get(key) == null) {
			if (key != null && bitmap != null)
				mMemoryCache.put(key, bitmap);
		} else
			Logger.w(TAG, "the res is aready exits");
	}

	public synchronized Bitmap getBitmapFromMemCache(String key) {
		if (key != null) {
			Bitmap bm = mMemoryCache.get(key);
			return bm;
		}
		return null;
	}

	/**
	 * 移除缓存
	 * 
	 * @param key
	 */
	public synchronized void removeImageCache(String key) {
		if (key != null) {
			if (mMemoryCache != null) {
				Bitmap bm = mMemoryCache.remove(key);
				if (bm != null)
					bm.recycle();
			}
		}
	}
}
