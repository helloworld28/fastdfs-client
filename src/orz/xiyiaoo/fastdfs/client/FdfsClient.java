/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.client;

import orz.xiyiaoo.fastdfs.command.Command;
import orz.xiyiaoo.fastdfs.vo.Address;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 02:38:54
 */
public interface FdfsClient {
    Address getAddress();
    public <T> Result<T> execute(Command<T> cmd) throws IOException;
    void close();
    boolean isValid();
}
