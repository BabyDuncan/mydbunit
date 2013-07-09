package com.babyduncan.mydbunit.dbunit.testcase;

import com.google.common.base.Preconditions;
import com.babyduncan.mydbunit.dbunit.annotation.DataBaseFile;
import com.babyduncan.mydbunit.dbunit.utils.ConfigUtil;
import com.babyduncan.mydbunit.platform.ddd.ds.DataSourceFactory;
import com.babyduncan.mydbunit.platform.ddd.ds.impl.MultiProxyDataSourceFactory;
import model.DBUnit;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.dbunit.DefaultOperationListener;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * BeforeClass 和 AfterClass 注解 是每个类只执行一次
 * Before 和 After 注解 是每个方法都执行一次
 *
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-21 20:33
 */
public class MydbunitTestCase {

    private static final Logger logger = Logger.getLogger(MydbunitTestCase.class);

    /**
     * 解析类的注解
     */
    @BeforeClass
    public static void beforeClass() {
        System.out.println("before class started ...");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("after class finished ...");
    }

    /**
     * 解析方法的注解
     * 读取数据文件，根据数据文件做CLEAN INSERT操作
     */
    @Before
    public void before() throws Exception {
        //读取配置文件开始
        //配置文件格式：bizname-dependents.xml metaDataFiles 的存储就是 k-bizname v-bizname-dependents.xml
        Map<String, String> metaDataFiles = new HashMap<String, String>();
        //首先读取类的注解
        getClassAnnotatedDataBaseFile(metaDataFiles);
        //再获取方法的注解
        getMethodAnnotatedDataBaseFile(metaDataFiles);
        //到此全部的数据库文件名称已经获取到
        //把数据文件转换成  FlatXmlDataSet
        Map<String, IDataSet> flatXmlDataSets = getFlatXmlDataSets(metaDataFiles);
        //获得配置中心的dbunit数据库连接
        Map<String, DBUnit.Config> configMap = ConfigUtil.getConfigMap();
        buildTestDataSource(configMap);
        Map<String, IDatabaseTester> databaseTesterMap = getDataBaseTester(configMap);
        Preconditions.checkArgument(metaDataFiles.size() > 0, "no DataBaseFile annotated!!");

        for (String key : flatXmlDataSets.keySet()) {
            final IDatabaseTester databaseTester = databaseTesterMap.get(key);
            databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
            databaseTester.setDataSet(flatXmlDataSets.get(key));
            databaseTester.setOperationListener(new DefaultOperationListener());
            databaseTester.onSetup();
        }

    }

    private Map<String, IDatabaseTester> getDataBaseTester(Map<String, DBUnit.Config> configMap) {
        Map<String, IDatabaseTester> databaseTesterMap = new HashMap<String, IDatabaseTester>();
        for (String key : configMap.keySet()) {
            DBUnit.Config config = configMap.get(key);
            try {
                JdbcDatabaseTester databaseTester = new JdbcDatabaseTester("com.mysql.jdbc.Driver", config.getConnectionUrl(), config.getUsername(), config.getPassword());
                databaseTesterMap.put(key, databaseTester);
            } catch (ClassNotFoundException e) {
                logger.error(e.toString(), e);
            }
        }
        return databaseTesterMap;
    }

    private Map<String, IDataSet> getFlatXmlDataSets(Map<String, String> metaDataFiles) {
        Map<String, IDataSet> flatXmlDataSetMap = new HashMap<String, IDataSet>();
        for (String key : metaDataFiles.keySet()) {
            try {
                FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSetBuilder().build(mydbunitTestCase.class.getClassLoader().getResourceAsStream(metaDataFiles.get(key)));
                flatXmlDataSetMap.put(key, flatXmlDataSet);
            } catch (DataSetException e) {
                logger.error(e.toString(), e);
            }
        }
        return flatXmlDataSetMap;
    }

    private void getMethodAnnotatedDataBaseFile(Map<String, String> metaDataFiles) {
        Class clazz = this.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            boolean hasAnnotation = method.isAnnotationPresent(DataBaseFile.class);
            if (hasAnnotation) {
                DataBaseFile dataBaseFile = method.getAnnotation(DataBaseFile.class);
                if (!(null == dataBaseFile) && dataBaseFile.value().length > 0) {
                    String[] data = dataBaseFile.value();
                    for (int i = 0; i < data.length; i++) {
                        String anno = data[i];
                        String bizname = getBizNameFromAnno(anno);
                        metaDataFiles.put(bizname, anno);
                    }
                }
            }
        }
    }

    private void getClassAnnotatedDataBaseFile(Map<String, String> metaDataFiles) {
        Class clazz = this.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (DataBaseFile.class.equals(annotation.annotationType())) {
                DataBaseFile dataBaseConfig = (DataBaseFile) annotation;
                if (dataBaseConfig!=null && dataBaseConfig.value().length>0) {
                    String[] data = dataBaseConfig.value();
                    for (int i = 0; i < data.length; i++) {
                        String anno = data[i];
                        String bizname = getBizNameFromAnno(anno);
                        metaDataFiles.put(bizname, anno);
                    }
                }
            }
        }
    }

    private String getBizNameFromAnno(String anno) {
        Preconditions.checkArgument(anno.contains("-"), "wrong dependents file name");
        return anno.substring(0, anno.indexOf("-"));
    }

    private void buildTestDataSource(Map<String, DBUnit.Config> configMap) {
        MultiProxyDataSourceFactory factory = new MultiProxyDataSourceFactory();
        for (String key : configMap.keySet()) {
            DBUnit.Config config = configMap.get(key);
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl(config.getConnectionUrl());
            ds.setUsername(config.getUsername());
            ds.setPassword(config.getPassword());
            ds.setDefaultAutoCommit(true);
            factory.getDataSources().put(key, ds);
        }
        DataSourceFactory.setDataSourceFactory(factory);
    }


    /**
     * 读取数据文件，做CLEAN操作
     */
    @After
    public void after() throws Exception {
        //读取配置文件开始
        //配置文件格式：bizname-dependents.xml metaDataFiles 的存储就是 k-bizname v-bizname-dependents.xml
        Map<String, String> metaDataFiles = new HashMap<String, String>();
        //首先读取类的注解
        getClassAnnotatedDataBaseFile(metaDataFiles);
        //再获取方法的注解
        getMethodAnnotatedDataBaseFile(metaDataFiles);
        //到此全部的数据库文件名称已经获取到
        //把数据文件转换成  FlatXmlDataSet
        Map<String, IDataSet> flatXmlDataSets = getFlatXmlDataSets(metaDataFiles);
        //获得配置中心的dbunit数据库连接
        Map<String, DBUnit.Config> configMap = ConfigUtil.getConfigMap();
        Map<String, IDatabaseTester> databaseTesterMap = getDataBaseTester(configMap);
        Preconditions.checkArgument(metaDataFiles.size() > 0, "no DataBaseFile annotated!!");

        for (String key : flatXmlDataSets.keySet()) {
            final IDatabaseTester databaseTester = databaseTesterMap.get(key);
            databaseTester.setSetUpOperation(DatabaseOperation.DELETE_ALL);
            databaseTester.setDataSet(flatXmlDataSets.get(key));
            databaseTester.setOperationListener(new DefaultOperationListener());
            databaseTester.onSetup();
        }

    }

}
