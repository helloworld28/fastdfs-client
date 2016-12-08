/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import orz.xiyiaoo.fastdfs.command.Command;
import orz.xiyiaoo.fastdfs.vo.Address;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 03:01:52
 */
public class BaseClient implements FdfsClient {
    protected Log logger = LogFactory.getLog(BaseClient.class);
    private Socket socket;
    private Address address;

    public BaseClient(String host, int port, int connectTimeout, int networkTimeout) throws IOException {
        this(new Address(host, port), connectTimeout, networkTimeout);
    }
    public BaseClient(Address address, int connectTimeout, int networkTimeout) throws IOException {
        this.socket = new Socket();
        socket.setSoTimeout(networkTimeout);
        socket.connect(new InetSocketAddress(address.getHost(), address.getPort()), connectTimeout);
        this.address = address;
    }

    public <T> Result<T> execute(Command<T> cmd) throws IOException {
        return cmd.doRequest(socket);
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public boolean isValid() {
        boolean closed = socket.isClosed();
        boolean connected = socket.isConnected();
        boolean inputShutdown = socket.isInputShutdown();
        boolean outputShutdown = socket.isOutputShutdown();
        if (closed || !connected || inputShutdown || outputShutdown) {
            return false;
        }
        boolean urgent;
        try {
            socket.sendUrgentData(0xFF);
            urgent = true;
        } catch (IOException e) {
            urgent = false;
        }
        return urgent;
    }

    public Address getAddress() {
        return address;
    }
}
