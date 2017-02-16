package com.jamendo.renhaojie.hear.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by Ren Haojie on 2017/2/8.
 */

public class FileUtil {

    private static String privateDir = "/hear/";

    public static boolean isExists(Context context, String fileName) {
        String dir = getDir(context);
        String filePath = dir + fileName;

        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isExists(String fullPath) {
        File file = new File(fullPath);
        return file.exists();
    }

    public static String getDir(Context context) {
        String dir;

        if (isExternalAvaialable()) {
            dir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) + privateDir;
        } else {
            dir = context.getFilesDir() + privateDir;
        }
        return dir;
    }

    public static String getFilePath(Context context, String fileName) {
        return getDir(context) + fileName;
    }

    public static void saveFile(ResponseBody responseBody, String dirPath, String fileName, Object busSubscription) {

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

//        File file = new File(dir, fileName);
//        BufferedSource source = responseBody.source();
//        try {
//            BufferedSink sink = Okio.buffer(Okio.sink(file));
//            sink.writeAll(source);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        InputStream inputStream = null;
        byte[] buffer = new byte[1024];
        int length;

        FileOutputStream outputStream = null;

        try {
            inputStream = responseBody.byteStream();
            File file = new File(dir, fileName);
            if (file.exists()) {
                inputStream.skip(file.length());
            }

            outputStream = new FileOutputStream(file);
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            RxBus.getInstance().unSubscribe(busSubscription);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean isExternalAvaialable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
