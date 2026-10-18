package com.laboratory.basic.connection.h2;

import static com.laboratory.basic.connection.h2.ConnectionConst.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionUtil {

	public static Connection getConnection() {
		try {
			Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			log.info("get connection = {}, class = {}", conn, conn.getClass());
			return conn;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
}
