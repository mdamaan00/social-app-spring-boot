package com.social.app.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbConfig {
  private final Environment env;

  public DbConfig(Environment env) {
    this.env = env;
  }

  @Bean
  public DataSource dataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
    dataSource.setUrl(env.getProperty("spring.datasource.url"));
    dataSource.setUsername(env.getProperty("user"));
    dataSource.setPassword(env.getProperty("password"));
    return dataSource;
  }
}
