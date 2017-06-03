package org.code.common;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Config {

    private static Config config = null;
    private File sd = Environment.getExternalStorageDirectory();
    private String path = sd.getPath() + "/moco/data";
    private static Properties p = null;

    public static Config getInstance() {
        if (config == null) {
            config = new Config();
            config.init();
        }
        return config;
    }

    // 读
    public void init() {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();

        File f = new File(path + "/project.properties");
        if (!f.exists()) {
            put("volume", "10");
            put("update", "false");
            put("article", "false");
        }
    }

    public String get(String key) {
        p = new Properties();
        try {
            InputStream in = new FileInputStream(path + "/project.properties");
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = p.getProperty(key);
        if (result == null) result = "";
        return result;
    }


    // 写
    public void put(String key, String value) {
        p = new Properties();
        try {
            InputStream in = new FileInputStream(path + "/project.properties");
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.setProperty(key, value);
        OutputStream fos;
        try {
            fos = new FileOutputStream(path + "/project.properties");
            p.store(fos, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
