/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package orz.xiyiaoo.fastdfs.utils;

import orz.xiyiaoo.fastdfs.Base64;
import orz.xiyiaoo.fastdfs.Constants;
import orz.xiyiaoo.fastdfs.vo.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 01:53:18
 * 字节工具类
 */
public class ByteUtil {
    private static Base64 base64 = new Base64('-', '_', '.', 0);
    private ByteUtil() {
    }

    /**
     * long类型数值转换成字节数组
     *
     * @param n 数值
     * @return bytes
     */
    public static byte[] toBytes(long n) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((n >> 56) & 0xFF);
        bytes[1] = (byte) ((n >> 48) & 0xFF);
        bytes[2] = (byte) ((n >> 40) & 0xFF);
        bytes[3] = (byte) ((n >> 32) & 0xFF);
        bytes[4] = (byte) ((n >> 24) & 0xFF);
        bytes[5] = (byte) ((n >> 16) & 0xFF);
        bytes[6] = (byte) ((n >> 8) & 0xFF);
        bytes[7] = (byte) (n & 0xFF);
        return bytes;
    }

    /**
     * 从字节流中某位开始获取long数值
     * @param bytes  字节流
     * @param offset 偏移
     * @return long
     */
    public static long toLong(byte[] bytes, int offset) {
        return (((long) (bytes[offset] >= 0 ? bytes[offset] : 256 + bytes[offset])) << 56)
                | (((long) (bytes[offset + 1] >= 0 ? bytes[offset + 1]
                : 256 + bytes[offset + 1])) << 48)
                | (((long) (bytes[offset + 2] >= 0 ? bytes[offset + 2]
                : 256 + bytes[offset + 2])) << 40)
                | (((long) (bytes[offset + 3] >= 0 ? bytes[offset + 3]
                : 256 + bytes[offset + 3])) << 32)
                | (((long) (bytes[offset + 4] >= 0 ? bytes[offset + 4]
                : 256 + bytes[offset + 4])) << 24)
                | (((long) (bytes[offset + 5] >= 0 ? bytes[offset + 5]
                : 256 + bytes[offset + 5])) << 16)
                | (((long) (bytes[offset + 6] >= 0 ? bytes[offset + 6]
                : 256 + bytes[offset + 6])) << 8)
                | ((long) (bytes[offset + 7] >= 0 ? bytes[offset + 7]
                : 256 + bytes[offset + 7]));
    }
    public static int toInt(byte[] bytes, int offset) {
        return ((bytes[offset] >= 0 ? bytes[offset] : 256 + bytes[offset]) << 24) |
                ((bytes[offset + 1] >= 0 ? bytes[offset + 1] : 256 + bytes[offset + 1]) << 16) |
                ((bytes[offset + 2] >= 0 ? bytes[offset + 2] : 256 + bytes[offset + 2]) << 8) |
                (bytes[offset + 3] >= 0 ? bytes[offset + 3] : 256 + bytes[offset + 3]);
    }

    /**
     * 从字节流中某位开始获取boolean值
     * @param bytes  字节流
     * @param offset 偏移
     * @return bool
     */
    public static boolean toBoolean(byte[] bytes, int offset) {
        return bytes[offset] != 0;
    }

    public static String toString(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length).trim();
    }

    public static String toString(byte[] bytes, int offset) {
        return new String(bytes, offset, bytes.length-offset).trim();
    }

    /**
     * 从字节流中某位开始获取Date
     * @param bytes  字节流
     * @param offset 偏移
     * @return date
     */
    public static Date toDate(byte[] bytes, int offset) {
        return new Date(toLong(bytes, offset) * 1000);
    }

    /**
     * 从字节流中获取group
     * @param bytes  字节流
     * @param offset 偏移
     * @return group
     */
    public static Group toGroup(byte[] bytes, int offset) {
        Group group = new Group();
        group.setGroupName(toString(bytes, offset, Constants.FDFS_GROUP_NAME_MAX_LEN + 1));
        offset += Constants.FDFS_GROUP_NAME_MAX_LEN + 1;
        group.setTotalMB(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setFreeMB(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setTrunkFreeMB(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setStorageCount((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setStoragePort((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setStorageHttpPort((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setActiveCount((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setCurrentWriteServer((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setStorePathCount((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setSubdirCountPerPath((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        group.setCurrentTrunkFileId((int) toLong(bytes, offset));
        return group;
    }

    private static void validByteLength(byte[] bytes, int offset, int expectLength) {
        if (bytes.length - offset < expectLength) {
            throw new IllegalArgumentException("the valid byte length is less than " + expectLength);
        }
    }

    /**
     * 从字节流中获取storage
     * @param bytes  字节流
     * @param offset 偏移
     * @return storage
     */
    public static Storage toStorage(byte[] bytes, int offset) {
        validByteLength(bytes, offset, 600);
        Storage storage = new Storage();
        storage.setStatus(bytes[offset]);
        offset += 1;
        storage.setId(toString(bytes, offset, Constants.FDFS_STORAGE_ID_MAX_SIZE));
        offset += Constants.FDFS_STORAGE_ID_MAX_SIZE;
        storage.setIpAddr(toString(bytes, offset, Constants.FDFS_IPADDR_SIZE));
        offset += Constants.FDFS_IPADDR_SIZE;
        storage.setDomainName(toString(bytes, offset, Constants.FDFS_DOMAIN_NAME_MAX_SIZE));
        offset += Constants.FDFS_DOMAIN_NAME_MAX_SIZE;
        storage.setSrcIpAddr(toString(bytes, offset, Constants.FDFS_IPADDR_SIZE));
        offset += Constants.FDFS_IPADDR_SIZE;
        storage.setVersion(toString(bytes, offset, Constants.FDFS_VERSION_SIZE));
        offset += Constants.FDFS_VERSION_SIZE;
        storage.setJoinTime(toDate(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setUpTime(toDate(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalMB(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setFreeMB(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setUploadPriority((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setStorePathCount((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSubdirCountPerPath((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setCurrentWritePath((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setStoragePort((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setStorageHttpPort((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalUploadCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessUploadCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalAppendCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessAppendCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalModifyCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessModifyCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalTruncateCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessTruncateCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalSetMetaCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessSetMetaCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalDeleteCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessDeleteCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalDownloadCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessDownloadCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalGetMetaCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessGetMetaCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalCreateLinkCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessCreateLinkCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalDeleteLinkCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessDeleteLinkCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalUploadBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessUploadBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalAppendBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessAppendBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalModifyBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessModifyBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalDownloadloadBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessDownloadloadBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalSyncInBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessSyncInBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalSyncOutBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessSyncOutBytes(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalFileOpenCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessFileOpenCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalFileReadCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessFileReadCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTotalFileWriteCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setSuccessFileWriteCount(toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setLastSourceUpdate(toDate(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setLastSyncUpdate(toDate(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setLastSyncedTimestamp(toDate(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setLastHeartBeatTime(toDate(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        storage.setTrunkServer(toBoolean(bytes, offset));

        return storage;
    }

    /**
     * 从字节流中获取storage
     * @param bytes  字节流
     * @param offset 偏移
     * @return storage
     */
    public static Store toStore(byte[] bytes, int offset) {
        Store store = new Store();
        store.setGroupName(toString(bytes, offset, Constants.FDFS_GROUP_NAME_MAX_LEN));
        offset += Constants.FDFS_GROUP_NAME_MAX_LEN;
        store.setHost(toString(bytes, offset, Constants.FDFS_IPADDR_SIZE - 1));
        offset += Constants.FDFS_IPADDR_SIZE - 1;
        store.setPort((int) toLong(bytes, offset));
        offset += Constants.FDFS_PROTO_PKG_LEN_SIZE;
        if(offset < bytes.length){
            store.setPathIndex(bytes[offset]);
        }
        return store;
    }

    public static byte[] toBytes(List<Meta> metas){
        if(metas == null || metas.isEmpty()){
            return new byte[0];
        }
        StringBuilder sb = new StringBuilder();
        for (Meta meta : metas) {
            sb.append(Constants.FDFS_RECORD_SEPERATOR)
                    .append(meta.getName())
                    .append(Constants.FDFS_FIELD_SEPERATOR)
                    .append(meta.getValue());
        }
        return sb.deleteCharAt(0).toString().getBytes();
    }

    public static List<Meta> toMetas(byte[] bytes, int offset){
        List<Meta> metas = new ArrayList<Meta>();
        for (String string : toString(bytes, offset).split(Constants.FDFS_RECORD_SEPERATOR)) {
            String[] ss = string.split(Constants.FDFS_FIELD_SEPERATOR);
            metas.add(new Meta(ss[0],ss[1]));
        }
        return metas;
    }

    public static byte[] toBytes(String string){
        if(string == null || string.isEmpty()){
            return new byte[0];
        }
        return string.getBytes();
    }

    public static byte[] toBytesForExtName(String filename){
        int dotIndex = filename.indexOf(".");
        if(dotIndex < 0){
            throw new IllegalArgumentException("can not find ext name");
        }
        byte[] ext = toBytes(filename.substring(dotIndex + 1));
        if(ext.length > Constants.FDFS_FILE_EXT_NAME_MAX_LEN){
            throw new IllegalArgumentException("the ext name length is too long");
        }
        byte[] bytes = new byte[Constants.FDFS_FILE_EXT_NAME_MAX_LEN];
        System.arraycopy(ext, 0, bytes, 0, ext.length);
        return bytes;
    }

    public static String getExtName(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public static String toIp(byte[] bytes, int offset){
        StringBuilder sb = new StringBuilder(16);
        for (int i = offset; i < offset + 4; i++) {
            sb.append(".").append((bytes[i] >= 0) ? bytes[i] : 256 + bytes[i]);
        }
        return sb.deleteCharAt(0).toString();
    }
    public static FileDetail toFileDetail(FileInfo fileInfo){
        return toFileDetail(fileInfo.getFilePath());
    }

    public static FileDetail toFileDetail(String filepath){
        FileDetail detail = new FileDetail();
        byte[] bytes = base64.decodeAuto(filepath.substring(Constants.FDFS_FILE_PATH_LEN,
                Constants.FDFS_FILE_PATH_LEN + Constants.FDFS_FILENAME_BASE64_LENGTH));
        detail.setIp(toIp(bytes, 0));
        detail.setTimestamp(new Date(toInt(bytes, 4) * 1000L));
        detail.setSize(toLong(bytes, 8) & 0xfffffffL);
        detail.setCrc32(toInt(bytes, 16));
        return detail;
    }

    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static String md5(byte[] source) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(source);
        byte bytes[] = digest.digest();
        char[] chars = new char[32];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            chars[k++] = hexDigits[bytes[i] >>> 4 & 0xf];
            chars[k++] = hexDigits[bytes[i] & 0xf];
        }

        return new String(chars);
    }

    public static String makeToken(String filepath, int timestamp, String secretkey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bsFilename = toBytes(filepath);
        byte[] bsKey = toBytes(secretkey);
        byte[] bsTimestamp = toBytes(Integer.toString(timestamp));

        byte[] bytes = new byte[bsFilename.length + bsKey.length + bsTimestamp.length];
        System.arraycopy(bsFilename, 0, bytes, 0, bsFilename.length);
        System.arraycopy(bsKey, 0, bytes, bsFilename.length, bsKey.length);
        System.arraycopy(bsTimestamp, 0, bytes, bsFilename.length + bsKey.length, bsTimestamp.length);

        return md5(bytes);
    }
}
