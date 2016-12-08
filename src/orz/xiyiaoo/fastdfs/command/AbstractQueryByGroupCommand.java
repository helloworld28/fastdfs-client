/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;

import java.io.IOException;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-27 02:08:58
 */
public abstract class AbstractQueryByGroupCommand<T> extends AbstractCommand<T> {
    /**
     * 组名
     */
    private String groupName;

    public AbstractQueryByGroupCommand() {}

    public AbstractQueryByGroupCommand(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public long getBodyLength() {
        return this.groupName == null ? 0 : Constants.FDFS_GROUP_NAME_MAX_LEN;
    }

    @Override
    protected void sendBody(StreamSender sender) throws IOException {
        if(getBodyLength() > 0){
            byte[] group = ByteUtil.toBytes(groupName);
            if(group.length > Constants.FDFS_GROUP_NAME_MAX_LEN){
                throw new IllegalArgumentException("the group name '" + groupName + "' is to long, more than:" + Constants.FDFS_GROUP_NAME_MAX_LEN);
            }
            sender.write(group);
            //当位数不足FDFS_GROUP_NAME_MAX_LEN, 写0
            for (int i = group.length; i < Constants.FDFS_GROUP_NAME_MAX_LEN; i++) {
                sender.write(0);
            }
        }
    }

    public String getGroupName() {
        return groupName;
    }
}
