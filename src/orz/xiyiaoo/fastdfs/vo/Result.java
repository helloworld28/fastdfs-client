/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

import orz.xiyiaoo.fastdfs.Constants;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 01:52:14
 */
public class Result<T> {

    private int code;
    private String message;
    private T data;
    private long bodyLength;

    public Result() {}

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public boolean isSuccess(){
        return this.code == Constants.SUCCESS_CODE;
    }

    public Result<T> fail(){
        this.code = Constants.FAIL_CODE;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(long bodyLength) {
        this.bodyLength = bodyLength;
    }
}
