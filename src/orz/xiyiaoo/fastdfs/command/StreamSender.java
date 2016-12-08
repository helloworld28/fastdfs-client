/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import java.io.IOException;
import java.io.OutputStream;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 04:00:46
 */
public class StreamSender{
    private OutputStream out;

    public StreamSender(OutputStream out) {
        this.out = out;
    }
    public void write(int i) throws IOException {
        out.write(i);
    }
    public void write(byte[] bytes) throws IOException {
        out.write(bytes);
    }
    public void write(byte[] bytes, int offset, int length) throws IOException {
        out.write(bytes, offset, length);
    }
    public void flush() throws IOException {
        out.flush();
    }

}
