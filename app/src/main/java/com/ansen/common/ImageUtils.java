package com.ansen.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ansen on 2015/9/23.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.common
 * @Description: TODO
 */
public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();

        int[] pixels = new int[width * height];

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    public static Bitmap scaleBitmap(Bitmap bgimage, int newWidth, int newHeight) {
        int width = bgimage.getWidth();
        int height = bgimage.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    public static Bitmap compressBitmap(String fromFile, String toFile, int quality) {
        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
        return compressBitmap(bitmap, toFile, quality);
    }

    public static Bitmap compressBitmap(InputStream inputstream, String toFile, int quality) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputstream);
        return compressBitmap(bitmap, toFile, quality);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, String toFile, int quality) {
        Bitmap newBM = null;
        FileOutputStream out = null;
        try {
            File myCaptureFile = new File(toFile);
            if (!myCaptureFile.exists()) {
                File dir = myCaptureFile.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                myCaptureFile.createNewFile();
            }
            out = new FileOutputStream(myCaptureFile);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
            // Release memory resources
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            newBM = BitmapFactory.decodeFile(toFile);
        } catch (Exception e) {
            Logger.i(TAG, e.toString());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                Logger.i(TAG, e.toString());
            }
        }
        return newBM;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // 规避当创建图片失败时，Canvas绘制图片异常问题；直接返回原图片
        if (output == null) {
            return bitmap;
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getViewBitmap(View v) {

        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Bitmap decodeBitmap(Context context, File imageFile) {
        return decodeBitmap(context, imageFile, 32);
    }

    public static Bitmap decodeBitmap(Context context, File imageFile, int maxInSampleSize) {
        Bitmap bitmap = null;
        Options options = new Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inDither = true;
        options.inTempStorage = new byte[12 * 1024];
        options.inSampleSize = 1;

        // 设置最大缩放比例
        if (maxInSampleSize < 1) {
            maxInSampleSize = 64;
        }

        while (null == bitmap) {
            bitmap = getScaledBitmap(context, imageFile, options);

            options.inSampleSize = options.inSampleSize * 2;

            if (options.inSampleSize > maxInSampleSize * 2) {
                return null;
            }
        }
        return bitmap;
    }

    public static Bitmap getScaledBitmap(Context context, File imageFile, Options options) {
        Bitmap bitmap = null;
        // 获取资源图片
        FileInputStream fileInputStream = null;
        InputStream inputStream = null;
        try {
            fileInputStream = new FileInputStream(imageFile);
            inputStream = new BufferedInputStream(fileInputStream);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (OutOfMemoryError err) {
            Logger.i(TAG, "inSampleSize" + "-----" + options.inSampleSize + "-------" + err);
            return null;
        } catch (FileNotFoundException e) {
            Logger.i(TAG, e.getLocalizedMessage());
        } finally {
            closeInputStream(inputStream);
            closeInputStream(fileInputStream);
        }

        return bitmap;
    }

    private static void closeInputStream(InputStream stream) {
        if (null != stream) {
            try {
                stream.close();
            } catch (IOException e) {
                Logger.i(TAG, e.getLocalizedMessage());
            }
        }
    }

    /**
     * bitmap-->byte[]
     *
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * bytes[]->bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        BitmapFactory bitmapFactory = new BitmapFactory();
        Bitmap bitMap = bitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitMap;
    }

    /**
     * 保存图片到sd卡 /data/data/com.maizuo.main/files/
     *
     * @param bitmap
     * @param fileName
     * @return
     */
    public boolean saveImage(Context context, Bitmap bitmap, String fileName) {
        boolean bool = false;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            // if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // Logger.w(TAG, "Low free space onsd, do not cache");
            // return false;
            // }
            bos = new BufferedOutputStream(context.openFileOutput(fileName, 0));

            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bis = new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
            int b = -1;
            while ((b = bis.read()) != -1) {
                bos.write(b);
            }
            bool = true;
        } catch (Exception e) {
            bool = false;
            //itheima-debugLogger.i(TAG, "the local storage is not available");

        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                bool = false;
                //itheima-debugLogger.i(TAG, "the local storage is not available");

            }
        }
        return bool;
    }

    /**
     * 删掉图片
     *
     * @param fileName
     * @return
     */
    public boolean deleteImage(Context context, String fileName) {
        return context.deleteFile(fileName);
    }

    /**
     * 把bitmap转换成String
     *
     * @param bm
     * @return
     */
    public static String bitmapToString(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    // 放大缩小图片
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }

    // 获得bitmp的方法
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    public static Drawable BitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }


    // 获得带倒影的图片方法
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the draw_outline_paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    public static void writerFileForByteArray(byte[] data) {
        Bitmap $bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if ($bitmap == null)
            return;

        String filename = "/sdcard/xxx/" + System.currentTimeMillis() + ".png";

        File f = new File(filename);
        FileOutputStream fos = null;
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            $bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm, int cos) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, cos, baos);
        return baos.toByteArray();
    }

    public static void writerFileForBitmap(Bitmap bitmap) {

        String filename = "/sdcard/xxx/" + System.currentTimeMillis() + ".png";

        File f = new File(filename);
        FileOutputStream fos = null;
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 图片合成
     */
    private Bitmap createBitmap(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);//创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        //draw src into
        cv.drawBitmap(src, 0, 0, null);//在 0，0坐标开始画入src
        //draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);//在src的右下角画入水印
        //save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
        cv.restore();//存储
        return newb;
    }

    /**
     * 图片圆角
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 25;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 缩放、翻转和旋转图片
     *
     * @param bmpOrg
     * @param rotate
     * @return
     */
    private Bitmap gerZoomRotateBitmap(Bitmap bmpOrg, int rotate) {
        // 获取图片的原始的大小
        int width = bmpOrg.getWidth();
        int height = bmpOrg.getHeight();

        int newWidth = 300;
        int newheight = 300;
        // 定义缩放的高和宽的比例
        float sw = ((float) newWidth) / width;
        float sh = ((float) newheight) / height;
        // 创建操作图片的用的Matrix对象
        Matrix matrix = new Matrix();
        // 缩放翻转图片的动作
        // sw sh的绝对值为绽放宽高的比例，sw为负数表示X方向翻转，sh为负数表示Y方向翻转
        matrix.postScale(sw, sh);
        // 旋转30*
        matrix.postRotate(rotate);
        //创建一个新的图片
        Bitmap resizeBitmap = Bitmap.createBitmap(bmpOrg, 0, 0, width, height,
                matrix, true);
        return resizeBitmap;
    }

    /**
     * 图片旋转
     *
     * @param bmp    要旋转的图片
     * @param degree 图片旋转的角度，负值为逆时针旋转，正值为顺时针旋转
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bmp, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    /**
     * 图片缩放
     *
     * @param bm
     * @param scale 值小于则为缩小，否则为放大
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bm, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    /**
     * 图片缩放
     *
     * @param bm
     * @param w  缩小或放大成的宽
     * @param h  缩小或放大成的高
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bm, int w, int h) {
        Bitmap BitmapOrg = bm;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
    }

    /**
     * 图片反转
     *
     * @param flag 0为水平反转，1为垂直反转
     * @return
     */
    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        float[] floats = null;
        switch (flag) {
            case 0: // 水平反转
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1: // 垂直反转
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }

        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }

        return null;
    }

    /**
     * 获取保存图片的目录
     *
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     *
     * @param image
     * @return
     */
    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize 主流机型屏幕大小
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        //
        options.inPreferredConfig = Config.RGB_565; // 这里也可以是ARGB_8888
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     *
     * @return
     */
    public static String getAlbumName() {
        return "xxxx";
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			/*
             *
			 //对于inSampleSize计算也有不同的方式方法
			 // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
			   if (height > reqHeight || width > reqWidth) {

			         final int halfHeight = height / 2;
			         final int halfWidth = width / 2;

			         // 压缩比例值每次循环两倍增加,
			         // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
			         while ((halfHeight / inSampleSize) >= reqHeight
			                    && (halfWidth / inSampleSize) >= reqWidth) {
			              inSampleSize *= 2;
			        }
			  }*/
        }

        return inSampleSize;
    }
}
