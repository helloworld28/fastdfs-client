/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-27 11:42:33
 */
public class Store {
    private String groupName;
    private String host;
    private int port;
    private int pathIndex;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    @Override
    public String toString() {
        return "Store{" +
                "groupName='" + groupName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", pathIndex=" + pathIndex +
                '}';
    }
}
