package com.babyduncan.mydbunit.dbunit.utils;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 测试一下 能不能正确的取到zookeeper的配置
 *
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-22 14:51
 */
public class ConfigUtilTest {

    private static final Logger logger = Logger.getLogger(ConfigUtilTest.class);

    @Test
    public void testGetConfigMap() throws Exception {
        logger.info(ConfigUtil.getConfigMap());
        logger.info(ConfigUtil.getConfigMap());
        logger.info(ConfigUtil.getConfigMap());
        logger.info(ConfigUtil.getConfigMap());
        logger.info(ConfigUtil.getConfigMap());
    }
}
