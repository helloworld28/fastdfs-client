/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.exception;

import orz.xiyiaoo.fastdfs.Constants;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 01:50:41
 */
public class FdfsException extends RuntimeException {
    private int code;

    public FdfsException() {
        this.code = Constants.FAIL_CODE;
    }

    public FdfsException(String s) {
        super(s);
        this.code = Constants.FAIL_CODE;
    }

    public FdfsException(String s, int code) {
        super(s);
        this.code = code;
    }

    public FdfsException(String s, Throwable throwable, int code) {
        super(s, throwable);
        this.code = code;
    }

    public FdfsException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
