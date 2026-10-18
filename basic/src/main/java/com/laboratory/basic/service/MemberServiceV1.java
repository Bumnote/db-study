package com.laboratory.basic.service;

import java.sql.SQLException;

import com.laboratory.basic.domain.Member;
import com.laboratory.basic.repository.MemberRepositoryV1;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceV1 {

	private final MemberRepositoryV1 memberRepository;

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
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
