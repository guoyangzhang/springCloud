package com.zhang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DataSourceProperties.DS, ignoreUnknownFields = false)
@Data
public class DataSourceProperties {
    //对应配置文件里的配置键
    final static String DS="mysqldb.datasource";

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int initialSize = 10;
    private int minIdle;
    private int maxIdle;
    private int maxActive;
    private int maxWait;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private int maxOpenPreparedStatements;
    private String filters;

    private String mapperLocations;
    private String typeAliasPackage;

   }
