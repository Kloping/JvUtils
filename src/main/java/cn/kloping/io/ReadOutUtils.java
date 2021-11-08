package cn.kloping.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * 使用示例
 * 如果 格式乱 请点开
 * ```
 *
 *         ReadOutputStream readOutputStream = connectIO(System.out);
 *
 *         System.setOut(new PrintStream(readOutputStream.getOs()));
 *
 *         new Thread(() -> {
 *             try {
 *                 PrintWriter pw = new PrintWriter(new FileOutputStream("./out.txt"), true);
 *                 while (true) {
 *                     String m1 = readOutputStream.readLine();
 *                     pw.println(m1);
 *                 }
 *             } catch (FileNotFoundException e) {
 *                 e.printStackTrace();
 *             }
 *         }).start();
 *
 *         new Thread(() -> {
 *             Scanner sc = new Scanner(System.in);
 *             while (true) {
 *                 String line = sc.nextLine();
 *                 System.out.println(line);
 *             }
 *         }).start();
 * ```
 */
public class ReadOutUtils {

    public static class ReadOutputStream {
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
        public OutputStream os;
        private final List<String> que;

        public OutputStream getOs() {
            return os;
        }

        public List<String> getQue() {
            return que;
        }

        public ReadOutputStream() {
            que = Collections.synchronizedList(new LinkedList<>());
        }

        public void setOs(OutputStream os) {
            this.os = os;
        }

        private int i1;

        public int write(byte b) {
            baos.write(b);
            int i2 = i1;
            i1 = b;
            if (i1 == 10 && i2 == 13) {
                try {
                    String line = baos.toString("utf-8").trim();
                    baos.reset();
                    synchronized (que) {
                        que.add(line);
                        que.notifyAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return i1;
        }

        private int index = 0;

        public String readLine() {
            if (que.size() == index++)
                synchronized (que) {
                    try {
                        que.wait();
                        return que.get(que.size() - 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            return que.get(que.size() - 1);
        }
    }

    /**
     * 将一个输出流 转为 一个可读的 ReadOutputStream
     * ReadOutputStream.readLine() 方法
     * 处理后的 OutputStream 用 ReadOutputStream.getOs() 获得
     *
     * @param os 输出流
     * @return 实例
     */
    public static ReadOutputStream connectIO(OutputStream os) {
        ReadOutputStream readOutputStream = new ReadOutputStream();
        readOutputStream.setOs(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                os.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                os.write(b);
            }

            @Override
            public void flush() throws IOException {
                os.flush();
            }

            @Override
            public void close() throws IOException {
                os.close();
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                os.write(b, off, len);
                if ((off < 0) || (off > b.length) || (len < 0) ||
                        ((off + len) > b.length) || ((off + len) < 0)) {
                    throw new IndexOutOfBoundsException();
                } else if (len == 0) {
                    return;
                }
                for (int i = 0; i < len; i++) {
                    readOutputStream.write(b[off + i]);
                }
            }
        });
        return readOutputStream;
    }
}
