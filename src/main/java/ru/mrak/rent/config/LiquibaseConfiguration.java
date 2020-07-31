package ru.mrak.rent.config;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Настройки миграции БД
 */
@Configuration
public class LiquibaseConfiguration {
    
    @Bean
    public SpringLiquibase liquibase(HikariDataSource hikariDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/master.xml");
        liquibase.setDataSource(hikariDataSource);
        return liquibase;
    }
    
}
