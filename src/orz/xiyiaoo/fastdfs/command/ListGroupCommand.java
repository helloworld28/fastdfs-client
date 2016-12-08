/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.Group;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 04:57:52
 * 列出所有group
 *  请求协议包格式
 *      header[
 *          8字节: body length=0,
 *          1字节: cmd=Constants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP,
 *          1字节: status=0
 *      ]
 *      body[
 *          空
 *      ]
 *  响应协议包格式
 *      header[
 *          8字节: body length=n*Constants.GROUP_BYTE_LENGTH,
 *          1字节: cmd=Constants.FDFS_PROTO_CMD_RESP,
 *          1字节: status
 *      ]
 *      body[
 *          n*Constants.GROUP_BYTE_LENGTH字节: 每Constants.GROUP_BYTE_LENGTH字节是一个group信息
 *      ]
 */
public class ListGroupCommand extends AbstractCommand<List<Group>> {

    @Override
    public int getCmd() {
        return Constants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP;
    }

    @Override
    public int getBufferSize() {
        return Constants.GROUP_BYTE_LENGTH;
    }

    @Override
    protected void handleBody(byte[] buffer, Result<List<Group>> result) throws IOException {
        if(result.getData() == null){
            List<Group> groups = new ArrayList<Group>();
            result.setData(groups);
        }
        result.getData().add(ByteUtil.toGroup(buffer, 0));
    }
}
