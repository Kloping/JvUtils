package cn.kloping.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {

    /**
     * 从文件中获取 字符数组 一行一个元素
     *
     * @param path
     * @return strs
     */
    public static String[] getStringsFromFile(String path) {
        try {
            List<String> ls = new LinkedList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                ls.add(line);
            }
            br.close();
            return ls.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从File获取String
     *
     * @param path
     * @return
     */
    public static String getStringFromFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从文件中获取byte数组
     *
     * @param path
     * @return
     */
    public static byte[] getBytesFromFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将一串字符写入 File
     *
     * @param str
     * @param path
     * @param name
     */
    public static void putStringInFile(String str, String path, String name) {
        try {
            File file = new File(path, name);
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一串字符写入 File
     *
     * @param str
     * @param file
     */
    public static void putStringInFile(String str, File file) {
        try {
            if (file == null) return;
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
