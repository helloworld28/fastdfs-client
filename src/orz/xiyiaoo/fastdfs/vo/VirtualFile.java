/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-10 04:37:02
 */
public class VirtualFile implements UploadAble {
    private byte[] content;
    private String name;

    public VirtualFile(byte[] content, String name) {
        this.content = content;
        this.name = name;
    }

    @Override
    public long length() {
        return content.length;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }
}
