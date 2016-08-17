package com.tomorrow_p.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 根据文件路径构建 Android Intent
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    /*      获取SD卡路径        */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /*      判断SD卡是否可用       */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return true;
        return false;
    }

    /**
     * 获取SD卡剩余空间
     */
    public static long getSDFreeSize() {
        if (isSDCardAvailable()) {
            StatFs statFs = new StatFs(getSDCardPath());

            long blockSize = statFs.getBlockSize();

            long freeBlocks = statFs.getAvailableBlocks();
            return freeBlocks * blockSize;
        }

        return 0;
    }

    /**
     * 获取SD卡的总容量
     */
    public static long getSDAllSize() {
        if (isSDCardAvailable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数
     *
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 拷贝文件，通过返回值判断是否拷贝成功
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static boolean copyFile(String sourcePath, String targetPath) {
        boolean isOK = false;
        if (!TextUtils.isEmpty(sourcePath) && !TextUtils.isEmpty(targetPath)) {
            File sourcefile = new File(sourcePath);
            File targetFile = new File(targetPath);
            if (!sourcefile.exists()) {
                return false;
            }
            if (sourcefile.isDirectory()) {
                isOK = copyDir(sourcefile, targetFile);
            } else if (sourcefile.isFile()) {
                if (!targetFile.exists()) {
                    createFile(targetPath);
                }
                FileOutputStream outputStream = null;
                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(sourcefile);
                    outputStream = new FileOutputStream(targetFile);
                    byte[] bs = new byte[1024];
                    int len;
                    while ((len = inputStream.read(bs)) != -1) {
                        outputStream.write(bs, 0, len);
                    }
                    isOK = true;
                } catch (Exception e) {
                    Logger.i(TAG, e.getLocalizedMessage());
                    Logger.i(TAG, e.getLocalizedMessage());
                    isOK = false;
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            Logger.i(TAG, e.getLocalizedMessage());
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            Logger.i(TAG, e.getLocalizedMessage());
                        }
                    }
                }
            }
            return isOK;
        }
        return false;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (!file.exists()) {
                return false;
            }
            try {
                file.delete();
            } catch (Exception e) {
                Logger.i(TAG, e.getLocalizedMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 统计文件夹文件的大小
     */
    public static long getSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                // 获取文件大小
                File[] fl = file.listFiles();
                long ss = 0;
                for (File f : fl)
                    ss += getSize(f);
                return ss;
            } else {
                long ss = (long) file.length();
                return ss; // 单位制bytes
            }
        } else {
            // System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0;
        }
    }

    /**
     * 把bytes转换成MB
     */
    public static String getTrafficStr(long total) {
        DecimalFormat format = new DecimalFormat("##0.0");
        if (total < 1024 * 1024) {
            return format.format(total / 1024f) + "KB";
        } else if (total < 1024 * 1024 * 1024) {
            return format.format(total / 1024f / 1024f) + "MB";
        } else if (total < 1024 * 1024 * 1024 * 1024) {
            return format.format(total / 1024f / 1024f / 1024f) + "GB";
        } else {
            return "统计错误";
        }
    }

    /**
     * 删除文件夹里面的所以文件
     */
    public static void deleteDir(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                } else {
                    deleteDir(files[i]);
                }
            }
        }
    }

    /**
     * 剪切文件，将文件拷贝到目标目录，再将源文件删除
     */
    public static boolean cutFile(String sourcePath, String targetPath) {
        boolean isSuccessful = copyFile(sourcePath, targetPath);
        if (isSuccessful) {
            // 拷贝成功则删除源文件
            return deleteFile(sourcePath);
        }
        return false;
    }

    /**
     * 拷贝目录
     */
    public static boolean copyDir(File sourceFile, File targetFile) {
        if (sourceFile == null || targetFile == null) {
            return false;
        }
        if (!sourceFile.exists()) {
            return false;
        }
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        // 获取目录下所有文件和文件夹的列表
        File[] files = sourceFile.listFiles();
        if (files == null || files.length < 1) {
            return false;
        }
        File file = null;
        StringBuffer buffer = new StringBuffer();
        boolean isSuccessful = false;
        // 遍历目录下的所有文件文件夹，分别处理
        for (int i = 0; i < files.length; i++) {
            file = files[i];
            buffer.setLength(0);
            buffer.append(targetFile.getAbsolutePath()).append(File.separator).append(file.getName());
            if (file.isFile()) {
                // 文件直接调用拷贝文件方法
                isSuccessful = copyFile(file.getAbsolutePath(), buffer.toString());
                if (!isSuccessful) {
                    return false;
                }
            } else if (file.isDirectory()) {
                // 目录再次调用拷贝目录方法
                copyDir(file, new File(buffer.toString()));
            }
        }
        return true;
    }

    /**
     * 剪切目录，先将目录拷贝完后再删除源目录
     */
    public static boolean cutDir(String sourceDir, String targetDir) {
        File sourceFile = new File(sourceDir);
        File targetFile = new File(targetDir);
        boolean isCopySuccessful = copyDir(sourceFile, targetFile);
        if (isCopySuccessful) {
            return deleteDir(sourceDir);
        }
        return false;
    }

    /**
     * 删除目录
     */
    public static boolean deleteDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            return false;
        }
        File[] files = file.listFiles();
        boolean isSuccessful = false;
        if (files.length == 0) {
            file.delete();
            return true;
        }
        // 对所有列表中的路径进行判断是文件还是文件夹
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                isSuccessful = deleteDir(files[i].getAbsolutePath());
            } else if (files[i].isFile()) {
                isSuccessful = deleteFile(files[i].getAbsolutePath());
            }
            if (!isSuccessful) {
                // 如果有删除失败的情况直接跳出循环
                break;
            }
        }
        if (isSuccessful) {
            file.delete();
        }
        return isSuccessful;
    }

    /**
     * 将流写入指定文件
     */
    public static boolean stream2File(InputStream inputStream, String path) {
        File file = new File(path);
        boolean isSuccessful = true;
        FileOutputStream fileOutputStream = null;
        try {
            if (!file.exists()) {
                File file2 = file.getParentFile();
                file2.mkdirs();
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            byte[] bs = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(bs)) != -1) {
                fileOutputStream.write(bs, 0, length);
            }
        } catch (Exception e) {
            Logger.i(TAG, e.getLocalizedMessage());
            isSuccessful = false;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Logger.i(TAG, e.getLocalizedMessage());
            }
        }
        return isSuccessful;
    }

    /**
     * 创建目录
     */
    public static void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 修改文件读写权限
     */
    public static void chmodFile(String fileAbsPath, String mode) {
        String cmd = "chmod " + mode + " " + fileAbsPath;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            Logger.i(TAG, e.getLocalizedMessage());
        }
    }

    /**
     * 将object对象写入outFile文件
     */
    public static void writeObject2File(String outFile, Object object, Context context) {
        ObjectOutputStream out = null;
        FileOutputStream outStream = null;
        try {
            File dir = context.getDir("cache", Context.MODE_PRIVATE);
            outStream = new FileOutputStream(new File(dir, outFile));
            out = new ObjectOutputStream(new BufferedOutputStream(outStream));
            out.writeObject(object);
            out.flush();
        } catch (Exception e) {
            Logger.i(TAG, e.getLocalizedMessage());
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    Logger.i(TAG, e.getLocalizedMessage());
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Logger.i(TAG, e.getLocalizedMessage());
                }
            }
        }
    }

    /**
     * 从outFile文件读取对象
     */
    public static Object readObjectFromPath(String filePath, Context context) {
        Object object = null;
        ObjectInputStream in = null;
        FileInputStream inputStream = null;
        try {
            File dir = context.getDir("cache", Context.MODE_PRIVATE);
            File f = new File(dir, filePath);
            if (f == null || !f.exists()) {
                return null;
            }
            inputStream = new FileInputStream(new File(dir, filePath));
            in = new ObjectInputStream(new BufferedInputStream(inputStream));
            object = in.readObject();
        } catch (Exception e) {
            Logger.i(TAG, e.getLocalizedMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Logger.i(TAG, e.getLocalizedMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Logger.i(TAG, e.getLocalizedMessage());
                }
            }

        }
        return object;
    }

    /**
     * 读取指定路径下的文件内容
     *
     * @param path
     * @return 文件内容
     */
    public static String readFile(String path) {
        BufferedReader br = null;
        try {
            File myFile = new File(path);
            br = new BufferedReader(new FileReader(myFile));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            Logger.i(TAG, e.getLocalizedMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.i(TAG, e.getLocalizedMessage());
                }
            }
        }
        return null;
    }

    /**
     * 创建文件，并修改读写权限
     */
    public static File createFile(String filePath, String mode) {
        File desFile = null;
        try {
            String desDir = filePath.substring(0, filePath.lastIndexOf(File.separator));
            File dir = new File(desDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            chmodFile(dir.getAbsolutePath(), mode);
            desFile = new File(filePath);
            if (!desFile.exists()) {
                desFile.createNewFile();
            }
            chmodFile(desFile.getAbsolutePath(), mode);
        } catch (Exception e) {
            Logger.i(TAG, e.getLocalizedMessage());
        }
        return desFile;
    }

    /**
     * 根据指定路径，创建父目录及文件
     *
     * @param filePath
     * @return File 如果创建失败的话，返回null
     */
    public static File createFile(String filePath) {
        return createFile(filePath, "755");
    }

    /*      获取系统存储路径        */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /*      获取外部存储路径        */
    public static String getExternalStorageDirectoryPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                Logger.d(TAG, "FILE is exist");
                bitmap = BitmapFactory.decodeFile(pathString);
            } else {
                Logger.d(TAG, "FILE is not exist");
            }
        } catch (Exception e) {
            Logger.e(TAG, "error: " + e.getMessage());
        }
        return bitmap;
    }

    /**
     * 读取asset目录下文件。
     */
    public static String readFile(Context mContext, String file, String code) {
        int len = 0;
        byte[] buf = null;
        String result = "";
        try {
            InputStream in = mContext.getAssets().open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            result = new String(buf, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*  ==============================  构建 Android Intent  ==============================  */

    /*      判断intent是否可用        */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    public static Intent openFile(String filePath) {

        File file = new File(filePath);
        if (!file.exists())
            return null;
        /* 取得扩展 */
        String end = filePath.substring(filePath.lastIndexOf(".") + 1,
                filePath.length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt") || end.equals("pptx")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc") || end.equals("docx")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else if (end.equals("zip")) {
            return getZipFileIntent(filePath);
        } else if (end.equals("rar")) {
            return getRarFileIntent(filePath);
        } else {
            return getAllIntent(filePath);
        }
    }

    /**
     * 获取用于打开APK文件的intent
     */
    public static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    /**
     * 获取用于打开APK文件的intent
     */
    public static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取用于打开VIDEO文件的intent
     */
    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * 获取用于打开AUDIO文件的intent
     */
    public static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * 获取用于打开Html文件的intent
     */
    public static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * 获取用于打开图片文件的intent
     */
    public static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 获取用于打开PPT文件的intent
     */
    public static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /**
     * 获取用于打开Excel文件的intent
     */
    public static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * 获取用于打开Word文件的intent
     */
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * 获取用于打开CHM文件的intent
     */
    public static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * 获取用于打开文本文件的intent
     */
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    /**
     * 获取用于打开PDF文件的intent
     */
    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * 获取用于打开ZIP文件的intent
     */
    public static Intent getZipFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/zip");
        return intent;
    }

    /**
     * 获取用于打开ZIP文件的intent
     */
    public static Intent getRarFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/rar");
        return intent;
    }

}
