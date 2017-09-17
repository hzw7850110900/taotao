package com.taotao.order.datasource;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
public class DataSourceConfiguration {

	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "boneCP")
	public DataSource getDataSource() {
		return DataSourceBuilder.create().type(BoneCPDataSource.class).build();
	}
}
