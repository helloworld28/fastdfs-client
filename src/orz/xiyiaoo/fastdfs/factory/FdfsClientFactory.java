/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.factory;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import orz.xiyiaoo.fastdfs.client.BaseClient;
import orz.xiyiaoo.fastdfs.client.FdfsClient;
import orz.xiyiaoo.fastdfs.vo.Address;

import java.io.IOException;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 07:29:56
 */
public class FdfsClientFactory extends BaseKeyedPooledObjectFactory<Address, FdfsClient> {

    private int connectTimeout;
    private int networkTimeout;

    public FdfsClientFactory(int connectTimeout, int networkTimeout) {
        this.connectTimeout = connectTimeout * 1000;
        this.networkTimeout = networkTimeout * 1000;
    }

    @Override
    public FdfsClient create(Address key) throws IOException {
        //System.out.println("DefaultClientFactory.create:" + key);
        return new BaseClient(key, connectTimeout, networkTimeout);
    }

    @Override
    public PooledObject<FdfsClient> wrap(FdfsClient client) {
        return new DefaultPooledObject<FdfsClient>(client);
    }

    @Override
    public void destroyObject(Address key, PooledObject<FdfsClient> pooledObject) throws Exception {
        pooledObject.getObject().close();
    }

    @Override
    public boolean validateObject(Address key, PooledObject<FdfsClient> p) {
        return p.getObject().isValid();
    }
}
