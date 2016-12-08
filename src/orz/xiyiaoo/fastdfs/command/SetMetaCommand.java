/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.FileInfo;
import orz.xiyiaoo.fastdfs.vo.Meta;
import orz.xiyiaoo.fastdfs.vo.MetaOperation;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-03 11:30:12
 * 设置meta
 *  请求协议包格式
 *      header[
 *          8字节: body length,
 *          1字节: cmd=Constants.STORAGE_PROTO_CMD_SET_METADATA,
 *          1字节: status=0
 *      ]
 *      body[
 *          8字节: filename size,
 *          8字节: meta length,
 *          1字节: operation flag 'O' overwrite | 'M' merge,
 *          16字节: group name,
 *          filename length字节: filename
 *          meta length字节: meta = name\x02value\x01name\x02value],
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
public class SetMetaCommand extends AbstractCommand<Boolean> {

    public SetMetaCommand(FileInfo fileInfo, List<Meta> metas) {
        this(fileInfo, metas, MetaOperation.MERAGE);
    }

    public SetMetaCommand(FileInfo fileInfo, List<Meta> metas, MetaOperation operation) {
        byte[] path = ByteUtil.toBytes(fileInfo.getFilePath());
        byte[] meta = ByteUtil.toBytes(metas);
        body = new byte[Constants.FDFS_PROTO_PKG_LEN_SIZE*2 + 1 + Constants.FDFS_GROUP_NAME_MAX_LEN + path.length + meta.length];
        int offset = 0;
        System.arraycopy(ByteUtil.toBytes(path.length), 0, body, offset, Constants.FDFS_PROTO_PKG_LEN_SIZE);
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        System.arraycopy(ByteUtil.toBytes(meta.length), 0, body, offset, Constants.FDFS_PROTO_PKG_LEN_SIZE);
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        body[offset] = (byte) operation.getValue();
        offset ++;
        byte[] group = ByteUtil.toBytes(fileInfo.getGroupName());
        System.arraycopy(group, 0, body, offset, group.length);
        offset += Constants.FDFS_GROUP_NAME_MAX_LEN;
        System.arraycopy(path, 0, body, offset, path.length);
        offset += path.length;
        System.arraycopy(meta, 0, body, offset, meta.length);
    }

    @Override
    public int getCmd() {
        return Constants.STORAGE_PROTO_CMD_SET_METADATA;
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
