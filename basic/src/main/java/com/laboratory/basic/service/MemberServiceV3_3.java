package com.laboratory.basic.service;

import java.sql.SQLException;

import org.springframework.transaction.annotation.Transactional;

import com.laboratory.basic.domain.Member;
import com.laboratory.basic.repository.MemberRepositoryV3;

import lombok.extern.slf4j.Slf4j;

/*
 * 트랜잭션 - @Transactional AOP
 * */
@Slf4j
public class MemberServiceV3_3 {

	private final MemberRepositoryV3 memberRepository;

	public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Transactional
	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		bizLogic(fromId, toId, money);
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
}
