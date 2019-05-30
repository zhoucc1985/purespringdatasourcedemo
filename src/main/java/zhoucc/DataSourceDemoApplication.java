package zhoucc;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

/**
 * @ClassName: DataSourceDemoApplication
 * @Auther: zhoucc
 * @Date: 2019/5/30 17:09
 * @Description:
 */

@Configuration
@EnableTransactionManagement
public class DataSourceDemoApplication {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) throws SQLException {
         //通过XML构建Spring beans
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext*.xml");
//        // 通过扫描注解类构建Spring beans
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DataSourceDemoApplication.class);
//        // 通过扫描含有注解（Configuration）的包构建Spring beans
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("zhoucc");
        // 扫描带有Configuration的注解来
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class);
        showBeans(applicationContext);
        dataSourceDemo(applicationContext);
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("driverClassName", "org.h2.Driver");
        properties.setProperty("url", "jdbc:h2:mem:testdb");
        properties.setProperty("username", "sa");
        return (DataSource) BasicDataSourceFactory.createDataSource(properties);
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }

    private static void showBeans(ApplicationContext applicationContext) {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
    }

    private static void dataSourceDemo(ApplicationContext applicationContext) throws SQLException {
        DataSourceDemoApplication demo = applicationContext.getBean("dataSourceDemoApplication", DataSourceDemoApplication.class);
        demo.showDataSource();
    }

    public void showDataSource() throws SQLException {
        System.out.println(dataSource.toString());
        Connection conn = dataSource.getConnection();
        System.out.println(conn.toString());
        conn.close();
    }
}
