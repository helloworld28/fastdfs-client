/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-10 04:51:00
 */
public class NormalFile implements UploadAble {
    private File file;

    public NormalFile(File file) {
        if(!file.exists()){
            throw new IllegalArgumentException("file is not exists:" + file.getAbsolutePath());
        }
        this.file = file;
    }

    public NormalFile(String path) {
        this(new File(path));
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }
}
