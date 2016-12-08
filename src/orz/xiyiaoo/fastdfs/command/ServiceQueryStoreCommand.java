/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.Result;
import orz.xiyiaoo.fastdfs.vo.Store;

import java.io.IOException;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-27 11:41:42
 * 查询一个可用的存储节点
 */
public class ServiceQueryStoreCommand extends AbstractQueryByGroupCommand<Store> {
    private int cmd;
    public ServiceQueryStoreCommand() {
        cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
    }

    public ServiceQueryStoreCommand(String groupName) {
        super(groupName);
        cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
    }

    @Override
    public int getCmd() {
        return cmd;
    }

    @Override
    protected void handleBody(byte[] buffer, Result<Store> result) throws IOException {
        result.setData(ByteUtil.toStore(buffer, 0));
    }
}
