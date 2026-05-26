/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.billing.internal.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Cansei2
 */
@Configuration
public class FlywayBillingConfig {
    @Bean
    public Flyway flywayBilling(DataSource data) {
        Flyway flyway = Flyway.configure()
                .dataSource(data)
                .locations("classpath:db/migration/billing")
                .schemas("billing")
                .table("flyway_schema_history")
                .load();
        flyway.migrate();
        return flyway;
    }
}