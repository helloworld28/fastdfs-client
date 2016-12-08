/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-12-03 08:58:42
 */
public class Meta {
    private String name;
    private String value;

    public Meta() {
    }

    public Meta(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
