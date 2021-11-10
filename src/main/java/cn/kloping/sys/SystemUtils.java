package cn.kloping.sys;

import cn.kloping.io.ReadIOUtils;

import java.io.*;

import static cn.kloping.file.FileUtils.testFile;

public class SystemUtils {

    public static void setOutToFile(String path) throws IOException {
        setOutToFile(path, false);
    }

    public static void setOutToFile(String path, boolean append) throws IOException {
        File file;
        testFile(file = new File(path));
        ReadIOUtils.ReadOutputStream ros = ReadIOUtils.connectOs(System.out);
        System.setOut(new PrintStream(ros.getOs()));
        new Thread(() -> {
            try {
                PrintWriter pw = new PrintWriter(new FileOutputStream(file, append));
                String line = null;
                while (true) {
                    line = ros.readLine();
                    pw.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void setErrToFile(String path, boolean append) throws IOException {
        File file;
        testFile(file = new File(path));
        ReadIOUtils.ReadOutputStream ros = ReadIOUtils.connectOs(System.err);
        System.setErr(new PrintStream(ros.getOs()));
        new Thread(() -> {
            try {
                PrintWriter pw = new PrintWriter(new FileOutputStream(file, append));
                String line = null;
                while (true) {
                    line = ros.readLine();
                    pw.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void setErrToFile(String path) throws IOException {
        setErrToFile(path, false);
    }
}
