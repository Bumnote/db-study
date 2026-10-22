package com.laboratory.basic.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.laboratory.basic.domain.Member;
import com.laboratory.basic.repository.MemberRepositoryV3;

import lombok.extern.slf4j.Slf4j;

/*
 * 트랜잭션 - 트랜잭션 매니저
 * */
@Slf4j
public class MemberServiceV3_2 {

	// private final PlatformTransactionManager transactionManager;
	private final TransactionTemplate txTemplate;
	private final MemberRepositoryV3 memberRepository;

	public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
		this.txTemplate = new TransactionTemplate(transactionManager);
		this.memberRepository = memberRepository;
	}

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {

		txTemplate.executeWithoutResult((status) -> {
			try {
				bizLogic(fromId, toId, money); // 비즈니스 로직
			} catch (SQLException e) {
				throw new IllegalStateException(e);
			}
		});
	}

	private void bizLogic(String fromId, String toId, int money) throws SQLException {
		// 비즈니스 로직
		Member fromMember = memberRepository.findById(fromId);
		Member toMember = memberRepository.findById(toId);

		memberRepository.update(fromId, fromMember.getMoney() - money); // 보내는 사람의 계좌 금액 차감
		validation(toMember);
		memberRepository.update(toId, toMember.getMoney() + money); // 받는 사람의 계좌 금액 증가
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
