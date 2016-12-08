/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import orz.xiyiaoo.fastdfs.FdfsClientPool;
import orz.xiyiaoo.fastdfs.command.*;
import orz.xiyiaoo.fastdfs.exception.FdfsException;
import orz.xiyiaoo.fastdfs.vo.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 07:28:50
 *
 */
public class DefaultPooledClient{

    protected Log logger = LogFactory.getLog(DefaultPooledClient.class);

    /**
     * client连接池
     */
    private FdfsClientPool pool;
    /**
     * 跟踪服务器地址列表
     */
    private Address[] trackServers;
    /**
     * 存活的跟踪服务器数量
     */
    private int aliveNum;
    /**
     * 随机数生成器,用于从跟踪服务器地址列表随机取一个
     */
    private Random random;

    public DefaultPooledClient(FdfsClientPool pool, final String[] trackServers) {
        this(pool, trackServers, 1, 300);
    }

    public DefaultPooledClient(FdfsClientPool pool, final String[] trackServers, int delay, int period) {
        if(trackServers.length == 0){
            throw new IllegalArgumentException("trackServers can not be empty");
        }
        this.pool = pool;
        this.aliveNum = trackServers.length;
        this.trackServers = new Address[trackServers.length];
        for (int i = 0; i < trackServers.length; i++) {
            this.trackServers[i] = new Address(trackServers[i]);
        }
        this.random = new Random(47);
        initActiveCheck(delay * 1000, period * 1000);
    }

    protected void initActiveCheck(long delay, long period) {
        final DefaultPooledClient client = this;
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                int index = 0, end = client.trackServers.length;
                while (index < end){
                    try {
                        client.execute(new ActiveTestCommand(), client.trackServers[index]);
                        index ++;
                    } catch (Exception e) {
                        //System.out.println("invalid address:" + client.trackServers[index]);
                        Address address = client.trackServers[--end];
                        client.trackServers[end] = client.trackServers[index];
                        client.trackServers[index] = address;
                    }
                }
                client.aliveNum = index;
            }
        }, delay, period);
    }

    /**
     * 执行操作命令
     * @param command 操作
     * @param <T> 结果类型
     * @return 结果
     */
    public <T> T execute(Command<T> command){
        return execute(command, null);
    }

    /**
     * 执行操作命令
     * @param command 操作
     * @param key 服务器地址,为空时则查找tracker server
     * @param <T> 结果类型
     * @return 结果
     */
    public <T> T execute(Command<T> command, Address key){
        FdfsClient client = null;
        Result<T> result = null;
        try {
            if (key == null) {
                key = getTrackServer();
            }
            client = pool.borrowObject(key);
            result = client.execute(command);
        } catch (Exception e) {
            if (e instanceof IOException) {
                //pool.clear(key);
                try {
                    pool.invalidateObject(key, client);
                    client = null;
                } catch (Exception e1) {
                    logger.error(e);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                pool.returnObject(client.getAddress(), client);
            }
        }
        if (result.isSuccess()) {
            return result.getData();
        } else {
            throw new FdfsException(result.getMessage(), result.getCode());
        }
    }

    /**
     * 列出fdfs的分组信息
     * @return list
     */
    public List<Group> listGroup() {
        return execute(new ListGroupCommand());
    }

    /**
     * 列出fdfs的某组的存储服务器信息
     * @param groupName 组名
     * @return list
     */
    public List<Storage> listStorage(String groupName) {
        return execute(new ListStorageCommand(groupName));
    }

    public boolean activeTest() {
        return execute(new ActiveTestCommand());
    }

    /**
     * 查出可用的存储,tracker server会根据storage server的情况返回一个
     * @return store
     */
    public Store queryStore(){
        return queryStore((String)null);
    }

    /**
     * 查出某组下可用的存储,tracker server会根据storage server的情况返回一个
     * @param groupName 组名
     * @return store
     */
    public Store queryStore(String groupName){
        ServiceQueryStoreCommand command;
        if (groupName == null) {
            command = new ServiceQueryStoreCommand();
        } else {
            command = new ServiceQueryStoreCommand(groupName);
        }
        return execute(command);
    }

    /**
     * 查出该文件所用的存储
     * @param fileInfo 文件信息
     * @return store
     */
    public Store queryStore(FileInfo fileInfo){
        return execute(new ServiceQueryUpdateCommand(fileInfo));
    }

    /**
     * 上传文件
     * @param file 文件
     * @param metas meta
     * @return 文件信息
     */
    public FileInfo uploadFile(File file, List<Meta> metas){
        return uploadFile(new NormalFile(file), metas);
    }

    /**
     * 上传文件
     * @param file 可上传的文件
     * @param metas meta
     * @return 文件信息
     */
    public FileInfo uploadFile(UploadAble file, List<Meta> metas){
        Store store = queryStore();//查询可用服务器
        Address address = new Address(store.getHost(), store.getPort());
        FileInfo fileInfo = execute(new UploadCommand(file, store), address);
        //FIXME 在一个socket里面操作
        if (metas != null && !metas.isEmpty()){
            try {
                execute(new SetMetaCommand(fileInfo, metas), address);
            } catch (Exception e) {
                execute(new DeleteCommand(fileInfo), address);
            }
        }
        return fileInfo;
    }
    /**
     * 上传从文件
     * @param file 文件 如: xxx.yyy
     * @param master 主文件信息 如: group0/M00/00/4F/lhIfZ1eydf2ANAEFAAKSBcmo2q8242.jpg
     * @param prefix 从文件后缀 如: _300X200
     * @return 文件信息 如: group0/M00/00/4F/lhIfZ1eydf2ANAEFAAKSBcmo2q8242_300X200.yyy
     */
    public FileInfo uploadFile(File file, FileInfo master, String prefix){
        return uploadFile(new NormalFile(file), master, prefix);
    }

    /**
     * 上传从文件
     * @param file 可上传的文件
     * @param master 主文件信息
     * @param prefix 从文件后缀
     * @return 文件信息
     */
    public FileInfo uploadFile(UploadAble file, FileInfo master, String prefix){
        Store store = queryStore(master);//查询可用服务器
        return execute(new UploadSlaveCommand(file, master, prefix), new Address(store.getHost(), store.getPort()));
    }

    /**
     * 下载文件
     * @param fileInfo 文件信息
     * @return 文件内容
     */
    public byte[] downloadFile(FileInfo fileInfo){
        Store store = queryStore(fileInfo);//查询可用服务器
        return execute(new DownloadCommand(fileInfo), new Address(store.getHost(), store.getPort()));
    }

    /**
     * 下载文件
     * @param fileInfo 文件信息
     * @param stream 输出流,用来写文件内容
     */
    public void downloadFile(FileInfo fileInfo, OutputStream stream){
        Store store = queryStore(fileInfo);//查询可用服务器
        execute(new Download2StreamCommand(fileInfo, stream), new Address(store.getHost(), store.getPort()));
    }

    /**
     * 删除文件
     * @param fileInfo 文件信息
     * @return bool
     */
    public boolean deleteFile(FileInfo fileInfo){
        Store store = queryStore(fileInfo);//查询所在服务器
        return execute(new DeleteCommand(fileInfo), new Address(store.getHost(), store.getPort()));
    }

    public boolean setMeta(FileInfo fileInfo, List<Meta> metas){
        Store store = queryStore(fileInfo);//查询所在服务器
        return execute(new SetMetaCommand(fileInfo, metas), new Address(store.getHost(), store.getPort()));
    }

    public List<Meta> getMeta(FileInfo fileInfo){
        Store store = queryStore(fileInfo);//查询所在服务器
        return execute(new GetMetaCommand(fileInfo), new Address(store.getHost(), store.getPort()));
    }

    public Address getTrackServer() {
        if (aliveNum == 1) {
            return trackServers[0];
        } else if (aliveNum == 0) {
            throw new FdfsException("tracker server is not active");
        }
        return trackServers[random.nextInt(aliveNum)];
    }

    public FdfsClientPool getPool() {
        return pool;
    }

}
