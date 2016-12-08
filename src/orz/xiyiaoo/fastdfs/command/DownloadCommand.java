/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.FileInfo;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-10 05:14:33
 * 下载文件
 *  请求协议包格式
 *      header[
 *          8字节: body length,
 *          1字节: cmd=Constants.STORAGE_PROTO_CMD_DOWNLOAD_FILE,
 *          1字节: status=0
 *      ]
 *      body[
 *          8字节: offset,
 *          8字节: download length,
 *          16字节: group name,
 *          ?字节: file path
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
public class DownloadCommand extends AbstractCommand<byte[]> {
    public DownloadCommand(FileInfo fileInfo, int offset, int downloadLength) {
        byte[] path = ByteUtil.toBytes(fileInfo.getFilePath());
        this.body = new byte[Constants.FDFS_PROTO_PKG_LEN_SIZE*2 + Constants.FDFS_GROUP_NAME_MAX_LEN + path.length];
        byte[] group = ByteUtil.toBytes(fileInfo.getGroupName());
        byte[] b8 = ByteUtil.toBytes(0);
        System.arraycopy(b8, 0, body, offset, b8.length);
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        if(downloadLength != 0){
            b8 = ByteUtil.toBytes(downloadLength);
        }
        System.arraycopy(b8, 0, body, offset, b8.length);
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        System.arraycopy(group, 0, body, offset, group.length);
        offset += Constants.FDFS_GROUP_NAME_MAX_LEN;
        System.arraycopy(path, 0, body, offset, path.length);
    }

    public DownloadCommand(FileInfo fileInfo) {
        this(fileInfo, 0, 0);
    }

    @Override
    public int getCmd() {
        return Constants.STORAGE_PROTO_CMD_DOWNLOAD_FILE;
    }

    @Override
    protected void handleBody(byte[] buffer, Result<byte[]> result) throws IOException {
        result.setData(buffer);
    }
}
