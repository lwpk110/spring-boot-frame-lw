package cn.tendata.mdcs.data.jpa.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="cn.tendata.mdcs.data.repository")
@EnableJpaAuditing
@PropertySource("classpath:application.properties")
public class DataConfig {
	
	@Autowired Environment env;
	
	@Bean
	public DataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
		return dataSource;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setDataSource(dataSource());
		factory.setPackagesToScan("cn.tendata.mdcs.data.domain");
		factory.setJpaProperties(additionalProperties());
 
		return factory;
	}
	
	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("jadira.usertype.autoRegisterUserTypes", env.getProperty("jadira.usertype.autoRegisterUserTypes", "true"));
//		properties.setProperty("jadira.usertype.databaseZone", env.getProperty("jadira.usertype.databaseZone", "UTC"));
//		properties.setProperty("jadira.usertype.javaZone", env.getProperty("jadira.usertype.javaZone", "UTC"));
		properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
	    properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
	    properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql", "true"));
	    properties.setProperty("hibernate.id.new_generator_mappings", env.getProperty("hibernate.id.new_generator_mappings", "false"));
	    properties.setProperty("hibernate.ejb.naming_strategy", env.getProperty("hibernate.ejb.naming_strategy", CustomNamingStrategy.class.getName()));
		return properties;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}
	
	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator(){
		return new HibernateExceptionTranslator();
	}
	
//	@Bean
//	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
//		return new PersistenceExceptionTranslationPostProcessor();
//	}
}
