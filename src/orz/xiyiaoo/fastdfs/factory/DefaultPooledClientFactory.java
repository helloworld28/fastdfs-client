/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.factory;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import orz.xiyiaoo.fastdfs.FdfsClientPool;
import orz.xiyiaoo.fastdfs.client.DefaultPooledClient;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-26 09:17:22
 */
public class DefaultPooledClientFactory {
    private static final String DEFAULT_CONF = "fdfs.properties";
    private static final String TRACKER_SERVER = "tracker_server";
    private static final String CONNECT_TIMEOUT = "connect_timeout";//单位秒
    private static final String NETWORK_TIMEOUT = "network_timeout";//单位秒
    private static final String CHECK_DELAY = "check.delay";//单位秒,首次检测tracker server列表的延时时间
    private static final String CHECK_PERIOD = "check.period";//单位秒,每次检测tracker server列表的时间间隔
    
    private static final String MAX_TOTAL= "pool.max_total";//池最多缓存对象
    private static final String MAX_TOTAL_PER_KEY = "pool.max_total_per_key";//每个key可以缓存数
    private static final String MAX_IDLE_PER_KEY = "pool.max_idle_per_key";//每个key可以最大空闲数
    
    

    private static Configuration configuration;
    private static DefaultPooledClient client;

    public static DefaultPooledClient getClient() throws IOException {
        if(client == null){
            synchronized (DefaultPooledClient.class){
                if(client == null){
                    if(configuration == null){
                        PropertiesConfiguration conf = new PropertiesConfiguration();
                        conf.setFileName(DEFAULT_CONF);
                        try {
                            conf.load();
                        } catch (ConfigurationException e) {
                            throw new IOException(e);
                        }
                        configuration = conf;
                    }
                    FdfsClientPool pool = newFdfsClientPool(configuration);
                    try {
                        int period = configuration.getInt(CHECK_PERIOD);
                        int delay = configuration.getInt(CHECK_DELAY);
                        client = new DefaultPooledClient(pool, configuration.getStringArray(TRACKER_SERVER), delay, period);
                    } catch (NoSuchElementException e) {
                        client = new DefaultPooledClient(pool, configuration.getStringArray(TRACKER_SERVER));
                    }
                }
            }
        }
        return client;
    }

    public static FdfsClientPool newFdfsClientPool(Configuration configuration) {
        int connectTimeout = configuration.getInt(CONNECT_TIMEOUT);
        int networkTimeout = configuration.getInt(NETWORK_TIMEOUT);
        FdfsClientFactory clientFactory = new FdfsClientFactory(connectTimeout, networkTimeout);
        
        int maxTotal = configuration.getInt(MAX_TOTAL);
        int maxTotalPerKey = configuration.getInt(MAX_TOTAL_PER_KEY);
        int maxIdlePerKey = configuration.getInt(MAX_IDLE_PER_KEY);
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxTotalPerKey(maxTotalPerKey);
        poolConfig.setMaxIdlePerKey(maxIdlePerKey);
        
        return new FdfsClientPool(clientFactory, poolConfig);
    }

    public static void setConfiguration(Configuration configuration) {
        DefaultPooledClientFactory.configuration = configuration;
    }

}
