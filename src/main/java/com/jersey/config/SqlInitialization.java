package com.jersey.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.SpringSessionContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Configuration
@EnableJpaRepositories(basePackages = "com.jersey.persistence")
public class SqlInitialization{

    private static final Logger log = LogManager.getLogger(SqlInitialization.class);

    @Bean
    public DataSource dataSource() {

        URI dbUri = null;
        String PALSPLATE_DB_URL = "postgres://vrwuksarnhwksl:94a6d7413fcd44094beab4b6b2d4ef0ee2dfe4b5caecf9a3d35e17b6bf6eab5b@ec2-79-125-2-69.eu-west-1.compute.amazonaws.com:5432/dbnane6c19acb5";
//        String PALSPLATE_DB_URL = System.getenv().get("PALSPLATE_DB_URL");
        try {
            dbUri = new URI(PALSPLATE_DB_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            log.debug("PALSPLATE_DB_URL environment variable does not exist");
        }
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        log.info(username);
        log.info(password);

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        try {
            log.info(dataSource.getConnection().getCatalog());
        } catch(Exception e){
            log.info(e.getMessage());
        }
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws URISyntaxException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("com.jersey.representations");
        entityManagerFactoryBean.setJpaProperties(buildHibernateProperties());
        entityManagerFactoryBean.setJpaProperties(new Properties() {{
            put("hibernate.current_session_context_class", SpringSessionContext.class.getName());
        }});
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter() {{
            setDatabase(Database.POSTGRESQL);
        }});
        return entityManagerFactoryBean;
    }

    protected Properties buildHibernateProperties()
    {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.use_sql_comments", "false");
        hibernateProperties.setProperty("hibernate.format_sql", "false");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "false");
        hibernateProperties.setProperty("hibernate.generate_statistics", "false");
        hibernateProperties.setProperty("javax.persistence.validation.mode", "none");

        //Audit History flags
        hibernateProperties.setProperty("org.hibernate.envers.store_data_at_delete", "true");
        hibernateProperties.setProperty("org.hibernate.envers.global_with_modified_flag", "true");

        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans","true");

        return hibernateProperties;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager());
    }
}