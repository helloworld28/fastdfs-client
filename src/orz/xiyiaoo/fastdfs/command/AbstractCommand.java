/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.command;

import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.exception.FdfsException;
import orz.xiyiaoo.fastdfs.utils.ByteUtil;
import orz.xiyiaoo.fastdfs.vo.Result;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 03:13:14
 */
public abstract class AbstractCommand<T> implements Command<T> {

    protected byte[] body;

    /**
     * 获取命令代码
     * @see orz.xiyiaoo.fastdfs.Constants
     * @return int
     */
    public abstract int getCmd();

    /**
     * 获取请求体长度
     * @return long
     */
    public long getBodyLength(){
        return body == null ? 0 : body.length;
    }

    /**
     * 获取body缓重区大小
     *  默认0[表示大小等于整个body流的总长度]
     *  此值必须能被总长度除尽,即为总长度的 1/n
     * @return long
     */
    public int getBufferSize(){
        return 0;
    }

    public int getStatus(){
        return Constants.SUCCESS_CODE;
    }

    public int getResponseCmd(){
        return Constants.FDFS_PROTO_CMD_RESP;
    }

    @Override
    public Result<T> doRequest(Socket socket) throws IOException {
        //发送
        StreamSender sender = new StreamSender(socket.getOutputStream());
        sendHeader(sender);
        sendBody(sender);

        //接收
        InputStream in = socket.getInputStream();
        Result<T> result = new Result<T>();
        handleHeader(in, result);
        if(result.isSuccess()){
            if(result.getBodyLength() > 0){
                //为0或大于body长度时时缓冲区大小设置为body的总长度
                int size = (getBufferSize() == 0 || getBufferSize() > result.getBodyLength()) ? (int) result.getBodyLength() : getBufferSize();

                byte[] buffer = new byte[size];
                int read, left = (int) result.getBodyLength();
                while (left > 0){
                    //剩余字节小于buffer则截取
                    if(left < buffer.length){
                        buffer = new byte[left];
                    }
                    read = in.read(buffer);
//                    System.out.println(in.available() + "-" + read + ":" + left);
                    if(read < 0){
                        throw new FdfsException("读取socket失败");
                    }
                    left -= read;
                    if (read < buffer.length) {
                        handleBody(Arrays.copyOf(buffer, read), result);
                    } else {
                        handleBody(buffer, result);
                    }
                }
            }
        }/* else {
            byte[] buffer = new byte[(int) result.getBodyLength()];
            in.read(buffer);
            //handleBody(buffer, result);
        }*/
        return result;
    }

    /**
     * 发送请求头
     * @param sender socket输出流
     * @throws IOException
     */
    protected void sendHeader(StreamSender sender) throws IOException {
        /*
         *  request header 格式 [
         *      8字节:package length,
         *      1字节:command
         *      1字节:status
         *  ]
         */
        sender.write(ByteUtil.toBytes(getBodyLength()));
        sender.write(getCmd());
        sender.write(getStatus());
    }

    /**
     * 发送请求体,当body不为空时输出body
     * @param sender socket输出流
     * @throws IOException
     */
    protected void sendBody(StreamSender sender) throws IOException {
        /*
         *  request body 格式 [
         *      根据command有不同的格式
         *  ]
         */
        if (body != null) {
            sender.write(body);
        }
    }

    /**
     * 处理响应头, 不要关闭此流
     * @param in socket响应的流
     * @throws IOException
     */
    protected void handleHeader(InputStream in, Result<T> result) throws IOException {
        byte[] header = new byte[Constants.FDFS_PROTO_PKG_LEN_SIZE + 2];

        int len = in.read(header);

        if (len != header.length) {
            result.fail().setMessage("receive package size " + len + " < " + header.length);
            return;
        }

        if (header[Constants.PROTO_HEADER_CMD_INDEX] != getResponseCmd()) {
            result.fail().setMessage("receive cmd: " + header[Constants.PROTO_HEADER_CMD_INDEX] + " is not correct, expect cmd: " + getResponseCmd());
            return;
        }
        long bodyLength = ByteUtil.toLong(header, 0);
        if (bodyLength < 0) {
            result.fail().setMessage("receive body length: " + bodyLength + " < 0!");
        }

        result.setCode(header[Constants.PROTO_HEADER_STATUS_INDEX]);
        result.setBodyLength(bodyLength);

    }

    /**
     * 处理响应体,此方法可能会调用0次或多次,默认是将整个body的流(如果有)传入只调用一次
     * @param buffer body流,默认是全部,如果指定了bufferSize则每次传输bufferSize长度的流
     * @param result result
     */
    protected abstract void handleBody(byte[] buffer, Result<T> result) throws IOException;

}
