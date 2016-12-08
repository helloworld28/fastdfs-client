/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-27 11:14:19
 * 请求协议包格式
 *      header[
 *          8字节: body length=0,
 *          1字节: cmd=Constants.FDFS_PROTO_CMD_ACTIVE_TEST,
 *          1字节: status=0
 *      ]
 *      body[
 *          空
 *      ]
 *  响应协议包格式
 *      header[
 *          8字节: body length=0,
 *          1字节: cmd=Constants.FDFS_PROTO_CMD_RESP,
 *          1字节: status
 *      ]
 *      body[
 *          空
 *      ]
 */
public class ActiveTestCommand extends AbstractCommand<Boolean> {
    @Override
    public int getCmd() {
        return Constants.FDFS_PROTO_CMD_ACTIVE_TEST;
    }

    @Override
    protected void handleHeader(InputStream in, Result<Boolean> result) throws IOException {
        super.handleHeader(in, result);
        result.setData(result.isSuccess());
    }

    @Override
    protected void handleBody(byte[] buffer, Result<Boolean> result) throws IOException {}
}
