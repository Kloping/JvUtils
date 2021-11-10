package cn.kloping.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadUtils {

    /**
     * 读取所有的字节
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] readAll(InputStream is) throws IOException {
        if (is == null) return null;
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        while ((len = is.read(bytes)) != -1)
            baos.write(bytes);
        return baos.toByteArray();
    }

    /**
     * 读取所有字节转为指定 格式 字符串
     *
     * @param is
     * @param character
     * @return
     * @throws IOException
     */
    public static String readAll(InputStream is, String character) throws IOException {
        if (is == null) return null;
        if (character == null) character = "utf-8";
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        while ((len = is.read(bytes)) != -1)
            baos.write(bytes);
        return baos.toString(character);
    }
}
