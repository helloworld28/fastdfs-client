/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.FileInfo;
import orz.xiyiaoo.fastdfs.vo.Meta;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.util.List;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-03 05:51:49
 * 查询meta
 */
public class GetMetaCommand extends AbstractQueryByFileInfoCommand<List<Meta>> {
    public GetMetaCommand(FileInfo fileInfo) {
        super(fileInfo);
    }

    @Override
    public int getCmd() {
        return Constants.STORAGE_PROTO_CMD_GET_METADATA;
    }

    @Override
    protected void handleBody(byte[] buffer, Result<List<Meta>> result) throws IOException {
        result.setData(ByteUtil.toMetas(buffer, 0));
    }
}
