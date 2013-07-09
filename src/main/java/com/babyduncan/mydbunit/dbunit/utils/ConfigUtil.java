package com.babyduncan.mydbunit.dbunit.utils;

import com.babyduncan.mydbunit.zkclient.ZkClient;
import com.babyduncan.mydbunit.zkclient.ZkMap;
import com.babyduncan.mydbunit.zkclient.ZkMapSetter;
import model.DBUnit;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 把DB的连接信息保存到配置中心
 * 从配置中心获取DB的连接
 *
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-22 13:44
 */
public final class ConfigUtil {

    private static final Logger logger = Logger.getLogger(ConfigUtil.class);

    private static final String ZkConnect = "zk1.in.i.babyduncan.com:2181,zk2.in.i.babyduncan.com:2181,zk3.in.i.babyduncan.com:2181,zk4.in.i.babyduncan.com:2181,zk5.in.i.babyduncan.com:2181";

    private static final String path = "/talent/mydbunit/dbunit";

    private static ZkMap<DBUnit.Config> zkMap = null;

    private static Lock lock = new ReentrantLock();

    public static Map<String, DBUnit.Config> getConfigMap() {
        if (zkMap != null) {
            return zkMap.delegate();
        } else {
            lock.lock();
            try {
                if (zkMap != null) {
                    return zkMap.delegate();
                }
                DBConfigFunctionUtil.ByteArrayToConfig byteArrayToConfig = new DBConfigFunctionUtil.ByteArrayToConfig();
                ZkClient zkClient = new ZkClient(ZkConnect, null, null);
                zkMap = ZkMap.createZkMap(zkClient, path, byteArrayToConfig);
                return zkMap.delegate();
            } finally {
                lock.unlock();
            }
        }

    }


    public static void main(String... args) throws InterruptedException {

        DBConfigFunctionUtil.ByteArrayToConfig byteArrayToConfig = new DBConfigFunctionUtil.ByteArrayToConfig();
        DBConfigFunctionUtil.ConfigToByteArray configToByteArray = new DBConfigFunctionUtil.ConfigToByteArray();

        ZkClient zkClient = new ZkClient(ZkConnect, null, null);
        ZkMap<DBUnit.Config> zkMap = ZkMap.createZkMap(zkClient, path, byteArrayToConfig);
        ZkMapSetter zkMapSetter = new ZkMapSetter(zkClient, path, configToByteArray, true);

//        DBUnit.Config config = DBUnit.Config.newBuilder().setConnectionUrl(
//                "jdbc:mysql://localhost:3307/test?characterEncoding=gbk"
//        ).setUsername("root").setPassword("mysql").build();
//
//        DBUnit.Config config2 = DBUnit.Config.newBuilder().setConnectionUrl(
//                "jdbc:mysql://localhost:3307/mysql?characterEncoding=gbk"
//        ).setUsername("root").setPassword("mysql").build();
//
////        zkMapSetter.remove("test");
//        zkMapSetter.put("test", config);
//        zkMapSetter.put("mysql", config2);
//        DBUnit.Config config = DBUnit.Config.newBuilder().setConnectionUrl(
//                "jdbc:mysql://localhost:3307/profile01?characterEncoding=gbk"
//        ).setUsername("root").setPassword("mysql").build();
//        zkMapSetter.remove("profile01");
//        zkMapSetter.put("profile1", config);
        for (int i = 0; i < 10; i++) {
            logger.info(zkMap.delegate());
            Thread.sleep(500);
        }
    }


}
