package cn.kloping.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ReadOutUtils {
    /**
     * setMode 0 当 写到 byte 13 10 作为 一行 win
     * setMode 1 当 写到 byte 10 作为 一行 liunx
     */
    public static class ReadOutputStream {
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
        public OutputStream os;
        private final List<String> que;
        private int mode = 0;

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

        public void setMode(int mode) {
            this.mode = mode;
        }

        private int i1;

        public int write(byte b) {
            baos.write(b);
            int i2 = i1;
            i1 = b;
            if (mode == 0) {
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
            } else if (mode == 1) {
                if (i1 == 10) {
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
                readOutputStream.write((byte) b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                os.write(b);
                for (byte b1 : b)
                    readOutputStream.write(b1);
            }

            @Override
            public void flush() throws IOException {
                os.flush();
            }

            @Override
            public void close() throws IOException {
                os.close();
            }
        });
        return readOutputStream;
    }
}
