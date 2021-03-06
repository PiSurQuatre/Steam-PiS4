package config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by pic on 29/04/2017.
 */
@Configuration
@EnableJpaRepositories(basePackages = "dao")
public class DBConfig {

    final private String HEROKU_DATABASE_URL = "DATABASE_URL";
    final private String HEROKU_DATABASE_USR = "JDBC_DATABASE_USERNAME";
    final private String HEROKU_DATABASE_PWD = "JDBC_DATABASE_PASSWORD";

    @Bean
    public DataSource dataSource(Properties dbProperties) throws URISyntaxException {
        String username = dbProperties.getProperty("username");

        String password = dbProperties.getProperty("password");

        String jdbcUrl = dbProperties.getProperty("jdbcUrl");


        if (null != System.getenv("DATABASE_URL")) {
            //Si on est sur le serveur on récupère user et mdp
            URI dbUri = new URI(System.getenv("DATABASE_URL"));
            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
        }

        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(dbProperties.getProperty("driverClass"));
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setIdleConnectionTestPeriodInMinutes(60);
        dataSource.setIdleMaxAgeInMinutes(240);
        dataSource.setMaxConnectionsPerPartition(10);
        dataSource.setMinConnectionsPerPartition(1);
        dataSource.setPartitionCount(2);
        dataSource.setAcquireIncrement(5);
        dataSource.setStatementsCacheSize(500);
        return dataSource;
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory,
                                                         DataSource dataSource) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, Properties dbProperties)
            throws SQLException {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        String dialect = dbProperties.getProperty("hibernateDialect");
        factory.getJpaPropertyMap().put("hibernate.dialect", dialect);
        factory.setPackagesToScan("entity");
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

}
