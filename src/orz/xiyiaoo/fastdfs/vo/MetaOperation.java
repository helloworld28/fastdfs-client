package orz.xiyiaoo.fastdfs.vo;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-03 06:11:10
 */
public enum MetaOperation {
    OVERWRITE('O'), MERAGE('M');
    private char value;

    MetaOperation(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
