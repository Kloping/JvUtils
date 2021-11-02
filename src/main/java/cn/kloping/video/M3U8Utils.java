package cn.kloping.video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * m3u8下载工具
 * 也可用于 多个连接内容合并
 */
public class M3U8Utils implements Runnable {
    private ExecutorService threads;
    private String[] urls;
    private CountDownLatch cdl;
    private int threadNum = 10;
    private PrintStream out = System.out;
    private File outFile;

    /**
     * 构造参数
     *
     * @param urls    从m3u8文件提取出的下载地址s
     * @param outFile 输出文件
     */
    public M3U8Utils(String[] urls, File outFile) {
        this.outFile = outFile;
        this.urls = urls;
    }

    /**
     * 构造参数
     *
     * @param urls      从m3u8文件提取出的下载地址s
     * @param threadNum 最大线程数量 默认10
     * @param outFile   输出文件
     */
    public M3U8Utils(String[] urls, int threadNum, File outFile) {
        this.outFile = outFile;
        this.threadNum = threadNum;
        this.urls = urls;
    }

    /**
     * 构造参数
     *
     * @param m3u8Url m3u8下载地址
     * @param outFile 输出文件
     * @throws IOException
     */
    public M3U8Utils(String m3u8Url, File outFile) throws IOException {
        this.outFile = outFile;
        this.urls = cn.kloping.url.UrlUtils.getStringFromHttpUrl(true, m3u8Url).split("\n");
    }

    /**
     * 构造
     *
     * @param m3u8Url   m3u8下载地址
     * @param threadNum 线程数量
     * @param outFile   输出文件
     * @throws IOException
     */
    public M3U8Utils(String m3u8Url, int threadNum, File outFile) throws IOException {
        this.outFile = outFile;
        this.urls = cn.kloping.url.UrlUtils.getStringFromHttpUrl(true, m3u8Url).split("\n");
        this.threadNum = threadNum;
    }

    /**
     * 设置线程数量 必须在 run之前 否则不生效
     *
     * @param threadNum
     */
    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        threads = Executors.newFixedThreadPool(threadNum);
        cdl = new CountDownLatch(urls.length);
        byte[][] bytess = new byte[urls.length][];
        for (String s1 : urls) {
            int finalI = getIndex();
            threads.execute(() -> {
                out.println("开始下载第" + finalI);
                byte[] bytes = cn.kloping.url.UrlUtils.getBytesFromHttpUrl(s1);
                bytess[finalI] = bytes;
                out.println("完成下载第" + finalI);
                cdl.countDown();
            });
        }
        try {
            cdl.await();
            out.println("合并文件");
            FileOutputStream fos = new FileOutputStream(outFile);
            for (byte[] bytes : bytess) {
                fos.write(bytes);
            }
            fos.close();
            out.println("完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
            out.println("未知异常");
        } catch (IOException e) {
            e.printStackTrace();
            out.println("未知异常");
        }
    }

    /**
     * 设置输出提示 默认 系统System.out
     *
     * @param out
     */
    public void setOut(PrintStream out) {
        this.out = out;
    }

    /**
     * 设置输出文件
     * 必须在 run之前 否则不生效
     *
     * @param outFile
     */
    public void setOutFile(File outFile) {
        this.outFile = outFile;
    }

    private int i = 0;

    public synchronized int getIndex() {
        return i++;
    }
}
