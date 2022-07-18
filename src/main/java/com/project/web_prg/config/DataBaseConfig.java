package com.project.web_prg.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

// DB 관련설정
@Configuration
// 설정한 것이랑 유사
@ComponentScan(basePackages = "com.project.web_prj")
public class DataBaseConfig {
    // DB 접속정보 설정(DataSource 설정)
    @Bean
    public DataSource dataSource(){
        HikariConfig config = new HikariConfig();
        config.setUsername("spring4");
        config.setPassword("1234");

        config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");

        return new HikariDataSource(config);
    }
}
