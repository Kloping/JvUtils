package cn.kloping.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static cn.kloping.judge.Judge.isNotNull;

public class FileUtils {

    /**
     * 检测文件 若不存在则创建
     *
     * @param filePath
     * @return 是否存在
     * @throws IOException
     */
    public static boolean testFile(String filePath) throws IOException {
        if (!isNotNull(filePath)) return false;
        File file = new File(filePath);
        boolean k = file.exists();
        if (k) return true;
        file.getParentFile().mkdirs();
        file.createNewFile();
        return k;
    }

    /**
     * 检测文件 若不存在则创建
     *
     * @param file
     * @return 是否存在
     * @throws IOException
     */
    public static boolean testFile(File file) throws IOException {
        if (!isNotNull(file)) return false;
        boolean k = file.exists();
        if (k) return true;
        file.getParentFile().mkdirs();
        file.createNewFile();
        return k;
    }

    /**
     * 从文件中获取 字符数组 一行一个元素
     *
     * @param path
     * @return strs
     */
    public static String[] getStringsFromFile(String path) {
        try {
            if (!isNotNull(path)) return null;
            testFile(path);
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
     * @param path 路径
     * @return str
     */
    public static String getStringFromFile(String path) {
        try {
            if (!isNotNull(path)) return null;
            testFile(path);
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
     * 从File获取String 并 过滤 以 filter 开头的行元素
     *
     * @param path   路径
     * @param filter 过滤 以其开头的
     * @return
     */
    public static String getStringFromFile(String path, String filter) {
        try {
            if (!isNotNull(path)) return null;
            testFile(path);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith(filter)) continue;
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从文件中获取byte数组
     *
     * @param path path
     * @return byte[]
     */
    public static byte[] getBytesFromFile(String path) {
        try {
            if (!isNotNull(path)) return null;
            testFile(path);
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
     * @param str  str
     * @param path path
     * @param name name
     */
    public static void putStringInFile(String str, String path, String name) {
        try {
            if (!isNotNull(str, path, name)) return;
            File file = new File(path, name);
            testFile(file);
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
     * @param str  str
     * @param file file
     */
    public static void putStringInFile(String str, File file) {
        try {
            if (file == null) return;
            if (!isNotNull(str, file)) return;
            testFile(file);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建缓存文件
     *
     * @param bytes 数据
     * @return tempFile
     * @throws IOException
     */
    public static File createTempFile(byte[] bytes) throws IOException {
        File file = File.createTempFile("temp" + UUID.randomUUID(), "");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
        return file;
    }

    /**
     * 创建缓存文件
     *
     * @param bytes  数据
     * @param suffix 后缀
     * @return tempFile
     * @throws IOException
     */
    public static File createTempFile(byte[] bytes, String suffix) throws IOException {
        File file = File.createTempFile("temp-" + UUID.randomUUID(), suffix);
        return file = writeBytesToFile(bytes, file);
    }

    /**
     * 将数据写到File
     *
     * @param bytes 数据
     * @param file  文件
     * @return 文件
     * @throws IOException
     */
    public static File writeBytesToFile(byte[] bytes, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
        return file;
    }
}
