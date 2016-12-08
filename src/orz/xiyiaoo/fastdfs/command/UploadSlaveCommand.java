/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.FileInfo;
import orz.xiyiaoo.fastdfs.vo.UploadAble;

import java.io.IOException;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-04 11:22:44
 * 上传从文件
 *  请求协议包格式
 *      header[
 *          8字节: body length,
 *          1字节: cmd=Constants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP,
 *          1字节: status=0
 *      ]
 *      body[
 *          8字节: master file path length,
 *          8字节: file size,
 *          16字节: filename prefix,
 *          6字节: file ext name without dot(.),
 *          ?字节: master file path,
 *          file size字节: file content
 *      ]
 *  响应协议包格式
 *      header[
 *          8字节: body length=FDFS_GROUP_NAME_MAX_LEN + filename length
 *          1字节: cmd=Constants.FDFS_PROTO_CMD_RESP,
 *          1字节: status
 *      ]
 *      body[
 *          FDFS_GROUP_NAME_MAX_LEN字节: group name,
 *          剩余字节: file name
 *      ]
 */
public class UploadSlaveCommand extends UploadCommand {
    protected byte[] masterPath;
    protected String prefix;
    public UploadSlaveCommand(UploadAble file, FileInfo master, String prefix) {
        super(file, null);
        String fileName = master.getFilePath().substring(master.getFilePath().lastIndexOf(FileInfo.separator) + 1);
        if (fileName.indexOf(".") > 34) {
            throw new IllegalArgumentException("master file name[" + fileName + "] is to long, more than 34");
        }
        masterPath = ByteUtil.toBytes(master.getFilePath());
        this.prefix = prefix;
    }

    @Override
    public long getBodyLength() {
        return Constants.FDFS_PROTO_PKG_LEN_SIZE * 2
                + Constants.FDFS_FILE_PREFIX_MAX_LEN
                + Constants.FDFS_FILE_EXT_NAME_MAX_LEN
                + masterPath.length
                + file.length();
    }

    @Override
    public int getCmd() {
        return Constants.STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE;
    }

    @Override
    protected void sendBody(StreamSender sender) throws IOException {
        sender.write(ByteUtil.toBytes(masterPath.length));//master file path length
        sender.write(ByteUtil.toBytes(file.length()));//file size
        byte[] pre = ByteUtil.toBytes(prefix);
        if(pre.length > Constants.FDFS_FILE_PREFIX_MAX_LEN){
            throw new IllegalArgumentException("the prefix name '" + prefix + "' is to long, more than:" + Constants.FDFS_FILE_PREFIX_MAX_LEN);
        }
        sender.write(pre);//preifx name
        //当位数不足FDFS_GROUP_NAME_MAX_LEN, 写0
        for (int i = pre.length; i < Constants.FDFS_FILE_PREFIX_MAX_LEN; i++) {
            sender.write(0);//preifx name
        }
        sender.write(ByteUtil.toBytesForExtName(file.getName()));//file ext name
        sender.write(masterPath);//master file
        sendFile(sender);
    }
}
