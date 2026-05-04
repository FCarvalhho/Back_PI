/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.catalog.internal.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Cansei2
 */
@Configuration
public class FlywayCatalogConfig {
    @Bean
    public Flyway flywayCatalog(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/catalog")
            .schemas("catalog") 
            .table("flyway_schema_history") 
            .load();
        flyway.migrate();
        return flyway;
    }
}
