/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import orz.xiyiaoo.fastdfs.client.FdfsClient;
import orz.xiyiaoo.fastdfs.vo.Address;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-27 04:21:01
 */
public class FdfsClientPool extends GenericKeyedObjectPool<Address, FdfsClient> {
    public FdfsClientPool(KeyedPooledObjectFactory<Address, FdfsClient> factory) {
        super(factory);
    }

    public FdfsClientPool(KeyedPooledObjectFactory<Address, FdfsClient> factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
    }

    public FdfsClient borrowObject(String host, int port) throws Exception {
        return borrowObject(new Address(host, port));
    }

    public FdfsClient borrowObject(String host, int port, long borrowMaxWaitMillis) throws Exception {
        return super.borrowObject(new Address(host, port), borrowMaxWaitMillis);
    }
    public void returnObject(FdfsClient client) {
        if (client != null) {
            super.returnObject(client.getAddress(), client);
        }
    }
}
