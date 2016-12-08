/**
 * Copyright © Foresee Science & Technology Ltd. 
 */
package client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import orz.xiyiaoo.fastdfs.client.DefaultPooledClient;
import orz.xiyiaoo.fastdfs.factory.DefaultPooledClientFactory;
import orz.xiyiaoo.fastdfs.vo.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: xiyiaoo@gmail.com
 * Date: 2015-11-25 07:44:47
 */
public class TestDefaultClient {
    private DefaultPooledClient client;

    @Before
    public void setUp() throws Exception {
        client = DefaultPooledClientFactory.getClient();
    }

    @Test
    public void testActiveCheck() throws Exception {
        TimeUnit.SECONDS.sleep(360);

    }

    @Test
    public void testListGroup() throws Exception {
        System.out.println(client.listGroup());
        System.out.println(client.listGroup());
        System.out.println(client.listGroup());
        System.out.println(client.listGroup());
        System.out.println(client.listGroup());
        System.out.println(client.listGroup());
        System.out.println(client.listGroup());
    }

    @Test
    public void testLoop() throws Exception {
        for (int i = 0; i < 100; i++) {
            //System.out.println(client.listGroup());
            //System.out.println(client.activeTest());
            System.out.println(client.queryStore());
        }

        for(Group group : client.listGroup()){
            System.out.println(group);
            for (int i = 0; i < 50; i++) {
                System.out.println(client.listStorage(group.getGroupName()));
            }
        }

    }

    @Test
    public void testListStorage() throws Exception {
        for(Group group : client.listGroup()){
            System.out.println(group);
            System.out.println(client.listStorage(group.getGroupName()));
            System.out.println(client.listStorage(group.getGroupName()));
        }
    }

    @Test
    public void testActive() throws Exception {
        System.out.println(client.activeTest());
    }

    @Test
    public void testQueryStore() throws Exception {
        //System.out.println(client.queryStore("group1"));
        System.out.println(client.queryStore());
    }

    @Test
    public void testUpload() throws Exception {
        File file = new File("E:/t.log");
        List<Meta> metas = new ArrayList<Meta>();
        //metas.add(new Meta("a", "1"));
        FileInfo info = client.uploadFile(file, metas);
        System.out.println(info);
    }

    @Test
    public void testUpload2() throws Exception {
        UploadAble file = new VirtualFile("Hello World!!".getBytes(),"a.txt");
        List<Meta> metas = new ArrayList<Meta>();
        metas.add(new Meta("a", "1"));
        FileInfo info = client.uploadFile(file, metas);
        System.out.println(info);
    }

    @Test
    public void testDownload() throws Exception {
        byte[] info = client.downloadFile(new FileInfo("group2", "M00/11/D6/lhQRXlZpTgGAS6-OAAAADZPv568515.txt"));
        System.out.println(new String(info));
    }

    @Test
    public void testDownload2() throws Exception {
        FileOutputStream out = new FileOutputStream("D:/t.gif");
        client.downloadFile(new FileInfo("group2", "M00/10/E1/lhQRXVaOLOSANWaaAF3hGDzVy4g938.gif"), /*System.*/out);
    }

    @Test
    public void testUploadSlave() throws Exception {
        File file = new File("E:/t.log");
        FileInfo info = client.uploadFile(file, new FileInfo("group2", "M01/11/D6/lhQRXlZhGkOAEvMzAAAB0JKkfTo038.log"), "_1");
        System.out.println(info);
    }

    @Test
    public void testSetMeta() throws Exception {
        FileInfo fileInfo = new FileInfo("group2/M01/10/E1/lhQRXVZgGdGADRuSAAAB0JKkfTo710.log");
        List<Meta> metas = new ArrayList<Meta>();
        metas.add(new Meta("a", "1"));
        metas.add(new Meta("b", "1"));
        System.out.println(client.setMeta(fileInfo, metas));
    }

    @Test
    public void testGetMeta() throws Exception {
        FileInfo fileInfo = new FileInfo("group2/M01/10/E1/lhQRXVZgGdGADRuSAAAB0JKkfTo710.log");
        System.out.println(client.getMeta(new FileInfo("group2", "M01/11/D6/lhQRXlZpQfWAGiadAAAB0JKkfTo302.log")));
    }

    @Test
    public void testServiceQueryUpdate() throws Exception {
        String file = "group2/M00/10/E1/lhQRXVZf5FmAMZksAAAB0JKkfTo579.log";
        System.out.println(client.queryStore(new FileInfo(file)));
    }

    @Test
    public void testDelete() throws Exception {
        String file = "group2/M00/10/E1/lhQRXVZgEaiAKNWOAAAB0JKkfTo525.log";
        System.out.println(client.deleteFile(new FileInfo(file)));
    }

    @After
    public void tearDown() throws Exception {
        if (client != null) {
            client.getPool().clear();
        }
    }
}
