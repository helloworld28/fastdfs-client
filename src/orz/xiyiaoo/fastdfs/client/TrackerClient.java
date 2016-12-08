/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.client;

import orz.xiyiaoo.fastdfs.command.ListGroupCommand;
import orz.xiyiaoo.fastdfs.vo.Group;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.util.List;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 06:02:25
 */
public class TrackerClient extends BaseClient {
    public TrackerClient(String host, int port, int connectTimeout, int networkTimeout) throws IOException {
        super(host, port, connectTimeout, networkTimeout);
    }

    public List<Group> listGroup() throws IOException {
        Result<List<Group>> result = execute(new ListGroupCommand());
        if(result.isSuccess()){
            return result.getData();
        }
        return null;
    }
}
