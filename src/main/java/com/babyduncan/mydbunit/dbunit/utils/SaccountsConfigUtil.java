package com.babyduncan.mydbunit.dbunit.utils;

import com.babyduncan.mydbunit.zkclient.ZkClient;
import com.babyduncan.mydbunit.zkclient.ZkMap;
import com.babyduncan.mydbunit.zkclient.ZkMapSetter;
import model.DBUnit;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 设置saccounts的dbunit测试在配置中心的配置
 *
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-22 13:44
 */
public final class SaccountsConfigUtil {

    private static final Logger logger = Logger.getLogger(SaccountsConfigUtil.class);

    private static final String ZkConnect = "zk1.in.i.babyduncan.com:2181,zk2.in.i.babyduncan.com:2181,zk3.in.i.babyduncan.com:2181,zk4.in.i.babyduncan.com:2181,zk5.in.i.babyduncan.com:2181";

    private static final String path = "/talent/mydbunit/dbunit";

    private static ZkMap<DBUnit.Config> zkMap = null;


    public static Map<String, DBUnit.Config> getConfigMap() {
        if (zkMap == null) {
            Lock lock = new ReentrantLock();
            lock.lock();
            try {
                DBConfigFunctionUtil.ByteArrayToConfig byteArrayToConfig = new DBConfigFunctionUtil.ByteArrayToConfig();
                ZkClient zkClient = new ZkClient(ZkConnect, null, null);
                zkMap = ZkMap.createZkMap(zkClient, path, byteArrayToConfig);
            } finally {
                lock.unlock();
            }
        }
        return zkMap.delegate();
    }

    public static Map<String, DBUnit.Config> getConfigMapFromLocal() {
        Map<String, DBUnit.Config> zkMapSetter = new HashMap<String, DBUnit.Config>();
//        共有四个数据库
        DBUnit.Config config0 = DBUnit.Config.newBuilder().setConnectionUrl(
                "jdbc:mysql://10.10.68.111:3309/saccount_0?characterEncoding=gbk"
        ).setUsername("mydbunit_dbunit").setPassword("dbunit").build();

        zkMapSetter.put("saccounts_0", config0);

        DBUnit.Config config1 = DBUnit.Config.newBuilder().setConnectionUrl(
                "jdbc:mysql://10.10.68.111:3309/saccount_1?characterEncoding=gbk"
        ).setUsername("mydbunit_dbunit").setPassword("dbunit").build();

        zkMapSetter.put("saccounts_1", config1);

        DBUnit.Config config2 = DBUnit.Config.newBuilder().setConnectionUrl(
                "jdbc:mysql://10.10.68.111:3309/saccount_2?characterEncoding=gbk"
        ).setUsername("mydbunit_dbunit").setPassword("dbunit").build();

        zkMapSetter.put("saccounts_2", config2);

        DBUnit.Config config3 = DBUnit.Config.newBuilder().setConnectionUrl(
                "jdbc:mysql://10.10.68.111:3309/saccount_3?characterEncoding=gbk"
        ).setUsername("mydbunit_dbunit").setPassword("dbunit").build();

        zkMapSetter.put("saccounts_3", config3);

        return zkMapSetter;
    }


    public static void main(String... args) throws InterruptedException {

        DBConfigFunctionUtil.ByteArrayToConfig byteArrayToConfig = new DBConfigFunctionUtil.ByteArrayToConfig();
        DBConfigFunctionUtil.ConfigToByteArray configToByteArray = new DBConfigFunctionUtil.ConfigToByteArray();

        ZkClient zkClient = new ZkClient(ZkConnect, null, null);
        ZkMap<DBUnit.Config> zkMap = ZkMap.createZkMap(zkClient, path, byteArrayToConfig);
        ZkMapSetter zkMapSetter = new ZkMapSetter(zkClient, path, configToByteArray, true);

        //共有四个数据库
//        DBUnit.Config config0 = DBUnit.Config.newBuilder().setConnectionUrl(
//                "jdbc:mysql://10.10.68.111:3309/saccount_0?characterEncoding=gbk"
//        ).setUsername("mydbunit_dbunit").setPassword("dbunit").build();
//
//        zkMapSetter.put("saccounts_0", config0);
//
//        DBUnit.Config config1 = DBUnit.Config.newBuilder().setConnectionUrl(
//                "jdbc:mysql://10.10.68.111:3309/saccount_1?characterEncoding=gbk"
//        ).setUsername("mydbunit_dbunit").setPassword("dbunit").build();
//
//        zkMapSetter.put("saccounts_1", config1);
//
//        DBUnit.Config config2 = DBUnit.Config.newBuilder().setConnectionUrl(
//                "jdbc:mysql://10.10.68.111:3309/saccount_2?characterEncoding=gbk"
//        ).setUsername("mydbunit_dbunit").setPassword("dbunit").build();
//
//        zkMapSetter.put("saccounts_2", config2);
//
//        DBUnit.Config config3 = DBUnit.Config.newBuilder().setConnectionUrl(
//                "jdbc:mysql://10.10.68.111:3309/saccount_3?characterEncoding=gbk"
//        ).setUsername("mydbunit_dbunit").setPassword("dbunit").build();
//
//        zkMapSetter.put("saccounts_3", config3);

        for (int i = 0; i < 2; i++) {
            logger.info(zkMap.delegate());
            Thread.sleep(500);
        }
    }


}
