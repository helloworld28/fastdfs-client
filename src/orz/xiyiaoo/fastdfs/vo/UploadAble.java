/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-10 04:47:46
 */
public interface UploadAble {
    long length();
    String getName();
    InputStream getInputStream() throws IOException;
}
