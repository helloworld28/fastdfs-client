/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.vo;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-27 04:40:17
 */
public class Address {
    private String host;
    private int port;

    public Address(String address) {
        if(address.indexOf(':') > 0){
            String[] as = address.split(":");
            this.host = as[0];
            this.port = Integer.parseInt(as[1]);
        } else {
            this.host = address;
            this.port = 80;
        }
    }

    public Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof Address){
            Address a = (Address) o;
            return this.port == a.port && this.host.equals(a.host);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.host.hashCode() + this.port * 31;
    }

    @Override
    public String toString() {
        return "Address{" + host + ':' + port + '}';
    }

}
