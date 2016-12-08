/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-03 02:38:31
 */
public class FileInfo {
    /**
     * 所属group
     */
    private String groupName;
    /**
     * 文件路径
     */
    private String filePath;

    public static final char separator = '/';

    public FileInfo() {
    }

    public FileInfo(String fileId) {
        int i = fileId.indexOf(separator);
        this.groupName = fileId.substring(0, i);
        this.filePath = fileId.substring(i+1);
    }

    public FileInfo(String groupName, String filePath) {
        this.groupName = groupName;
        this.filePath = filePath;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "groupName='" + groupName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    public String fileId(){
        return groupName + '/' + filePath;
    }
}
