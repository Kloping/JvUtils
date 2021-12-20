package io.github.kloping.io;

import java.io.OutputStream;
import java.util.List;

public interface ReadOutputStream {
    OutputStream getOs();

    Object[] getQue();

    void setOs(OutputStream os);

    void setMode(int mode);

    String readLine();

    void clearCache();
}
