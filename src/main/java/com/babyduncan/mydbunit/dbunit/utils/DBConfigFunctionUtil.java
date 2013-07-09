package com.babyduncan.mydbunit.dbunit.utils;

import com.google.common.base.Function;
import com.google.protobuf.InvalidProtocolBufferException;
import model.DBUnit;
import org.apache.log4j.Logger;

/**
 * DB的config 与 byte 数组之间的相互转换
 *
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-22 13:46
 */
public class DBConfigFunctionUtil {

    private static final Logger logger = Logger.getLogger(DBConfigFunctionUtil.class);

    /**
     * Config -> byte[] 的转换函数
     */
    public static class ConfigToByteArray implements Function<DBUnit.Config, byte[]> {
        @Override
        public byte[] apply(DBUnit.Config input) {
            if (input == null) {
                return null;
            }
            return input.toByteArray();
        }
    }

    /**
     * byte[] -> Config 的转换函数
     */
    public static class ByteArrayToConfig implements Function<byte[], DBUnit.Config> {
        @Override
        public DBUnit.Config apply(byte[] input) {
            if (input == null) {
                return null;
            }
            try {
                return DBUnit.Config.parseFrom(input);
            } catch (InvalidProtocolBufferException e) {
                logger.error(e.toString(), e);
                return null;
            }
        }
    }

    public static void main(String... args) throws InvalidProtocolBufferException {
//        DOMConfigurator.configure("src/main/resources/log4j.xml");
        DBUnit.Config config = DBUnit.Config.newBuilder().setConnectionUrl("c").setPassword("p").setUsername("u").build();
        byte[] bytes = config.toByteArray();
        DBUnit.Config clone = DBUnit.Config.parseFrom(bytes);
        logger.info(clone.toString());
    }
}
