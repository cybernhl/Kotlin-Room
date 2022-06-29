package com.guadou.kt_demo.demo.demo5_network_request;

import android.content.Context;
import android.util.Log;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 *
 */
public class FileIOUtils {


    public void writeSth() {

//        InputStream is = httpResult.getInputStream();
//        FileOutputStream fos = null;
//
//        try {
//            fos = new FileOutputStream(file, true);
//
//            byte[] bytes = new byte[1024 * 2];
//            int len;
//
//            while ((len = is.read(bytes)) != -1) {
//                fos.write(bytes, 0, len);
//                fos.flush();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            IOUtils.close(fos);
//            IOUtils.close(is);
//        }

    }


    public static void writeText(Context context, String fileName, String content) {

        if (content == null) content = "";

        try {

            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(content.getBytes());

            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String read(Context context, String fileName) {
        try {

            FileInputStream in = context.openFileInput(fileName);
            BufferedInputStream bis = new BufferedInputStream(in);
            return readInStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    private static String readInStream(BufferedInputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        }
        return null;

    }

    public static boolean copyFile(Context context, String fileName) {

        try {
            FileInputStream in = context.openFileInput(fileName);
            BufferedInputStream bis = new BufferedInputStream(in);

            File file = context.getExternalFilesDir("copy");
            File newFile = new File(file, "copy.txt");
            OutputStream out = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(out);

            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }

            bis.close();
            bos.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean copyFile2(Context context, String fileName) {


        try {
            String file = context.getFilesDir().getAbsolutePath() + "/" + fileName;
            FileReader fr = new FileReader(file);


            File file2 = context.getExternalFilesDir("copy2");
            File newFile = new File(file2, "copy2.txt");
            FileWriter fw = new FileWriter(newFile);

            int len = -1;
            char[] buffer = new char[1024];
            while ((len = fr.read(buffer)) != -1) {
                fw.write(buffer, 0, len);
            }

            fr.close();
            fw.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }



    public static void readLine(Context context, String fileName){
        String filePath = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(filePath);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            // 循环读取文件中的每一行并打印
            while ((line = bufferedReader.readLine()) != null) {
                YYLogUtils.i(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
