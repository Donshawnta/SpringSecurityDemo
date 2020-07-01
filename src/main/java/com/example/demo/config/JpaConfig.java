package com.example.demo.config;

import com.example.demo.db.DemoDalAutoConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(basePackageClasses = DemoDalAutoConfig.class)
@EntityScan(basePackageClasses = DemoDalAutoConfig.class)
public class JpaConfig {
}
