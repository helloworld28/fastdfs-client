/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.FileInfo;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-03 04:46:18
 * 根据FileInfo查询
 */
public abstract class AbstractQueryByFileInfoCommand<T> extends AbstractCommand<T> {
    public AbstractQueryByFileInfoCommand(FileInfo fileInfo) {
        this.body = new byte[Constants.FDFS_GROUP_NAME_MAX_LEN + fileInfo.getFilePath().length()];
        byte[] group = ByteUtil.toBytes(fileInfo.getGroupName());
        System.arraycopy(group, 0, body, 0, group.length);
        byte[] path = ByteUtil.toBytes(fileInfo.getFilePath());
        System.arraycopy(path, 0, body, Constants.FDFS_GROUP_NAME_MAX_LEN, path.length);
    }
}
