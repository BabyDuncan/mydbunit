package com.babyduncan.mydbunit.example;

import com.babyduncan.mydbunit.dbunit.annotation.DataBaseFile;
import com.babyduncan.mydbunit.dbunit.testcase.mydbunitTestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-22 15:41
 */
@DataBaseFile({"test-dependents.xml", "mysql-dependents.xml"})
public class DaoHelperTest extends mydbunitTestCase {

    private static final Logger logger = Logger.getLogger(DaoHelperTest.class);

    @DataBaseFile("mysql-dependents.xml")
    @Test
    public void testGetAllBooks() throws Exception {
        logger.info(DaoHelper.getAllBooks());
    }
}
