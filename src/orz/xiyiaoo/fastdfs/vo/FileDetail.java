/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

import java.util.Date;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-10 03:00:50
 * 文件详细信息
 */
public class FileDetail {
    /**
     * 上传的服务器ip
     */
    private String ip;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 上传的时间
     */
    private Date timestamp;
    /**
     * crc32只
     */
    private int crc32;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getCrc32() {
        return crc32;
    }

    public void setCrc32(int crc32) {
        this.crc32 = crc32;
    }

    @Override
    public String toString() {
        return "FileDetail{" +
                "ip='" + ip + '\'' +
                ", size=" + size +
                ", timestamp=" + timestamp +
                ", crc32=" + crc32 +
                '}';
    }
}
