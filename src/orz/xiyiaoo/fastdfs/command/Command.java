/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.net.Socket;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 03:01:20
 * fastdfs 命令
 */
public interface Command<T> {
    /**
     * 发送请求
     * @param socket socket
     * @return result
     */
    Result<T> doRequest(Socket socket) throws IOException;
}
