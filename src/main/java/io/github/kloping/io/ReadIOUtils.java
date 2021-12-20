package io.github.kloping.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class ReadIOUtils {
    /**
     * setMode 0 当 写到 byte 13 10 作为 一行 win
     * setMode 1 当 写到 byte 10 作为 一行 liunx
     */
    public static class ReadOutputStreamImpl implements ReadOutputStream {
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
        public OutputStream os;
        private int mode = 0;

        public OutputStream getOs() {
            return os;
        }

        public Object[] getQue() {
            return que.toArray();
        }

        private ArrayBlockingQueue<String> que = new ArrayBlockingQueue<>(10);

        private ReadOutputStreamImpl() {
        }

        public void setOs(OutputStream os) {
            this.os = os;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        private int i1;

        private int write(byte b) {
            baos.write(b);
            int i2 = i1;
            i1 = b;
            if (mode == 0) {
                if (i1 == 10 && i2 == 13) {
                    try {
                        String line = baos.toString("utf-8");
                        line = line.substring(0, line.length() - 2);
                        baos.reset();
                        que.put(line);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (mode == 1) {
                if (i1 == 10) {
                    try {
                        String line = baos.toString("utf-8");
                        line = line.substring(0, line.length() - 1);
                        baos.reset();
                        que.put(line);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return i1;
        }

        private int index = 0;

        public String readLine() {
            try {
                return que.take();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void clearCache() {
            que.clear();
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
    public static ReadOutputStreamImpl connectOs(OutputStream os) {
        ReadOutputStreamImpl readOutputStream = new ReadOutputStreamImpl();
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
    //================================================================

}
