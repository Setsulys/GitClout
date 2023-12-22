package fr.uge.gitclout.gitclout.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class ConfigJpa {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("fr.uge.gitclout.gitclout.jpa"); // Set your package name here
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "update");
        em.getJpaPropertyMap().put("hibernate.show_sql", true);
        em.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
        return em;
    }
}