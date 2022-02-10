package io.github.kloping.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author github-kloping
 */
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
            baos.write(bytes, 0, len);
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
        if (character == null) character = "utf-8";
        byte[] bytes = readAll(is);
        return new String(bytes, character);
    }

    private static final byte[] BUFFER = new byte[4096 * 1024];

    /**
     * 复制流
     *
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, false);
    }

    /**
     * 复制流
     *
     * @param input
     * @param output
     * @param autoClose 是否关闭
     * @throws IOException
     */
    public static void copy(InputStream input, OutputStream output, boolean autoClose) throws IOException {
        int bytesRead;
        while ((bytesRead = input.read(BUFFER)) != -1) {
            output.write(BUFFER, 0, bytesRead);
        }
        if (autoClose) {
            input.close();
            output.close();
        }
    }
}
