package com.laboratory.basic.connection.h2;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class DBConnectionUtilTest {

	@Test
	@DisplayName("DB 연결 테스트")
	void connectionTest() {
		Connection conn = DBConnectionUtil.getConnection();
		assertThat(conn).isNotNull();
	}
}