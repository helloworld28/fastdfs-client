/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.FileInfo;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-03 02:36:27
 * 删除文件
 *  请求协议包格式
 *      header[
 *          8字节: body length = 16+file path length,
 *          1字节: cmd=Constants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP,
 *          1字节: status=0
 *      ]
 *      body[
 *          16字节: group name,
 *          其余字节: file path
 *      ]
 *  响应协议包格式
 *      header[
 *          8字节: body length=0
 *          1字节: cmd=Constants.FDFS_PROTO_CMD_RESP,
 *          1字节: status
 *      ]
 *      body[
 *          none
 *      ]
 */
public class DeleteCommand extends AbstractQueryByFileInfoCommand<Boolean> {

    public DeleteCommand(FileInfo fileInfo) {
        super(fileInfo);
    }

    @Override
    public int getCmd() {
        return Constants.STORAGE_PROTO_CMD_DELETE_FILE;
    }

    @Override
    protected void handleHeader(InputStream in, Result<Boolean> result) throws IOException {
        super.handleHeader(in, result);
        result.setData(result.isSuccess());
    }

    @Override
    protected void handleBody(byte[] buffer, Result<Boolean> result) throws IOException {

    }
}
