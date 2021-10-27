package cn.kloping.url;

import java.io.*;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UrlUtils {
    /**
     * 从网络上获取字符
     *
     * @param k   忽略 #开头的文字
     * @param url 网址
     * @return 字符
     */
    public static String getStringFromHttpUrl(boolean k, String url) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                if (k && line.trim().startsWith("#"))
                    continue;
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从网络上获取bytes
     *
     * @param url 网址
     * @return byte数组
     */
    public static byte[] getBytesFromHttpUrl(String url) {
        try {
            InputStream is = new URL(url).openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024 * 1024];
            int len = -1;
            while ((len = is.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final ExecutorService threads = Executors.newFixedThreadPool(50);

    public static void downloadFile(String urlStr, String fileName) {
        threads.execute(() -> {
            try {
                System.out.println("下载=>" + fileName);
                URL url = new URL(urlStr);
                InputStream is = url.openStream();
                byte[] bytes = new byte[1024 * 1024];
                int len = -1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = is.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                }
                is.close();
                File file = new File(fileName);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
