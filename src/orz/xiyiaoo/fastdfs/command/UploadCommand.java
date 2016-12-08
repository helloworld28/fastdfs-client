/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.FileInfo;
import orz.xiyiaoo.fastdfs.vo.Result;
import orz.xiyiaoo.fastdfs.vo.Store;
import orz.xiyiaoo.fastdfs.vo.UploadAble;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-02 11:46:17
 * 上传文件
 *  请求协议包格式
 *      header[
 *          8字节: body length,
 *          1字节: cmd=Constants.STORAGE_PROTO_CMD_UPLOAD_FILE,
 *          1字节: status=0
 *      ]
 *      body[
 *          1字节: store path index
 *          8字节: file size,
 *          6字节: file ext name without dot(.),
 *          file size字节: file content
 *      ]
 *  响应协议包格式
 *      header[
 *          8字节: body length=FDFS_GROUP_NAME_MAX_LEN + filename length
 *          1字节: cmd=Constants.FDFS_PROTO_CMD_RESP,
 *          1字节: status
 *      ]
 *      body[
 *          FDFS_GROUP_NAME_MAX_LEN字节: group name,
 *          剩余字节: file name
 *      ]
 */
public class UploadCommand extends AbstractCommand<FileInfo> {

    protected int bufferSize = 1024 * 256;
    protected UploadAble file;
    protected Store store;

    public UploadCommand(UploadAble file, Store store) {
        this.file = file;
        this.store = store;
    }

    @Override
    public long getBodyLength() {
        return 1 + Constants.FDFS_PROTO_PKG_LEN_SIZE
                + Constants.FDFS_FILE_EXT_NAME_MAX_LEN
                + file.length();
    }

    @Override
    protected void sendBody(StreamSender sender) throws IOException {
        sender.write(store.getPathIndex());
        sender.write(ByteUtil.toBytes(file.length()));
        sender.write(ByteUtil.toBytesForExtName(file.getName()));
        sendFile(sender);
    }

    protected void sendFile(StreamSender sender) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int readLen;
        InputStream fis = null;
        try {
            fis = file.getInputStream();
            while ((readLen = fis.read(buffer)) > 0){
                sender.write(buffer, 0, readLen);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    @Override
    public int getCmd() {
        return Constants.STORAGE_PROTO_CMD_UPLOAD_FILE;
    }

    @Override
    protected void handleBody(byte[] buffer, Result<FileInfo> result) throws IOException {
        String group = ByteUtil.toString(buffer, 0, Constants.FDFS_GROUP_NAME_MAX_LEN);
        String path = ByteUtil.toString(buffer, Constants.FDFS_GROUP_NAME_MAX_LEN);
        result.setData(new FileInfo(group, path));
    }

}
