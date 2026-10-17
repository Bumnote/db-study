package com.laboratory.basic.connection;

import static com.laboratory.basic.h2.ConnectionConst.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ConnectionTest {

	@Test
	void driverManager() throws SQLException {
		Connection conn1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Connection conn2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

		log.info("[conn1] connection = {} / class = {}", conn1, conn1.getClass());
		log.info("[conn2] connection = {} / class = {}", conn2, conn2.getClass());
	}

	@Test
	void dataSourceDriverManager() throws SQLException {
		// DriverManagerDataSource - 항상 새로운 커넥션 획득 / 설정과 사용을 분리
		DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD); // 설정은 여기서 한번에 진행
		useDataSource(dataSource);
	}

	private void useDataSource(DataSource dataSource) throws SQLException {
		Connection conn1 = dataSource.getConnection(); // URL, USERNAME, PASSWORD 정보가 필요없다.
		Connection conn2 = dataSource.getConnection();

		log.info("[conn1] connection = {} / class = {}", conn1, conn1.getClass());
		log.info("[conn2] connection = {} / class = {}", conn2, conn2.getClass());
	}
}
