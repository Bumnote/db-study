package com.laboratory.basic.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.laboratory.basic.domain.Member;
import com.laboratory.basic.repository.MemberRepositoryV2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 트랜잭션 - 파라미터 연동, Pool을 고려한 종료
 * */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

	private final DataSource dataSource;
	private final MemberRepositoryV2 memberRepository;

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {

		Connection conn = dataSource.getConnection();
		try {
			conn.setAutoCommit(false); // 트랜잭션 시작
			bizLogic(conn, fromId, toId, money); // 비즈니스 로직
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw new IllegalStateException(e);
		} finally {
			release(conn);
		}
	}

	private void bizLogic(Connection conn, String fromId, String toId, int money) throws SQLException {
		// 비즈니스 로직
		Member fromMember = memberRepository.findById(conn, fromId);
		Member toMember = memberRepository.findById(conn, toId);

		memberRepository.update(conn, fromId, fromMember.getMoney() - money); // 보내는 사람의 계좌 금액 차감
		validation(toMember);
		memberRepository.update(conn, toId, toMember.getMoney() + money); // 받는 사람의 계좌 금액 증가
	}

	private void validation(Member toMember) {
		if (toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체중 예외 발생");
		}
	}

	private static void release(Connection conn) {
		if (conn != null) {
			try {
				conn.setAutoCommit(true); // 커넥션 풀 기본값 고려 (true가 기본값)
				conn.close();
			} catch (Exception e) {
				log.info("error", e);
			}
		}
	}
}
