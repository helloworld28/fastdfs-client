/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.vo.FileInfo;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.io.OutputStream;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-10 06:00:28
 */
public class Download2StreamCommand extends DownloadCommand {
    private OutputStream stream;

    public Download2StreamCommand(FileInfo fileInfo, int offset, int downloadLength, OutputStream stream) {
        super(fileInfo, offset, downloadLength);
        this.stream = stream;
    }

    public Download2StreamCommand(FileInfo fileInfo, OutputStream stream) {
        super(fileInfo);
        this.stream = stream;
    }

    @Override
    public int getBufferSize() {
        return 256 * 1024;
    }

    @Override
    protected void handleBody(byte[] buffer, Result<byte[]> result) throws IOException {
        stream.write(buffer);
    }
}
