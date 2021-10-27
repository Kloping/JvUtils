package cn.kloping.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {

    public static String[] getStringsFromFile(String path) {
        try {
            List<String> ls = new LinkedList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            String line = null;
            while ((line = br.readLine()) != null) {
                ls.add(line);
            }
            return ls.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringFromFile(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("//")) continue;
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getBytesFromFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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
}
