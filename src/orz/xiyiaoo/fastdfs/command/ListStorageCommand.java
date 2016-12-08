/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.Result;
import orz.xiyiaoo.fastdfs.vo.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-27 09:10:24
 * 查询某组的storage
 *  请求协议包格式
 *      header[
 *          8字节: body length=Constants.FDFS_GROUP_NAME_MAX_LEN,
 *          1字节: cmd=Constants.TRACKER_PROTO_CMD_SERVER_LIST_STORAGE,
 *          1字节: status=0
 *      ]
 *      body[
 *          Constants.FDFS_GROUP_NAME_MAX_LEN字节: groupName位数不足则补0
 *      ]
 *  响应协议包格式
 *      header[
 *          8字节: body length=n*Constants.STORAGE_BYTE_LENGTH,
 *          1字节: cmd=Constants.FDFS_PROTO_CMD_RESP,
 *          1字节: status
 *      ]
 *      body[
 *          n*Constants.STORAGE_BYTE_LENGTH: 每Constants.STORAGE_BYTE_LENGTH字节是一个storage信息
 *      ]
 */
public class ListStorageCommand extends AbstractQueryByGroupCommand<List<Storage>> {

    public ListStorageCommand(String groupName) {
        super(groupName);
    }

    @Override
    public int getCmd() {
        return Constants.TRACKER_PROTO_CMD_SERVER_LIST_STORAGE;
    }

    @Override
    public int getBufferSize() {
        return Constants.STORAGE_BYTE_LENGTH;
    }

    @Override
    protected void handleBody(byte[] buffer, Result<List<Storage>> result) throws IOException {
        if(result.getData() == null){
            List<Storage> storages = new ArrayList<Storage>();
            result.setData(storages);
        }
        result.getData().add(ByteUtil.toStorage(buffer, 0));
    }

}
