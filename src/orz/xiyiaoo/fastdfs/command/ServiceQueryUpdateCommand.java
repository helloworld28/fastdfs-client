/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.FileInfo;
import orz.xiyiaoo.fastdfs.vo.Result;
import orz.xiyiaoo.fastdfs.vo.Store;

import java.io.IOException;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-03 05:09:35
 * 根据fileid查询可用的存储服务器
 */
public class ServiceQueryUpdateCommand extends AbstractQueryByFileInfoCommand<Store> {
    public ServiceQueryUpdateCommand(FileInfo fileInfo) {
        super(fileInfo);
    }

    @Override
    public int getCmd() {
        return Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE;
    }

    @Override
    protected void handleBody(byte[] buffer, Result<Store> result) throws IOException {
        result.setData(ByteUtil.toStore(buffer, 0));
    }
}
