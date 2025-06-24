package com.fivedragons.jpa.practice.service;


import com.fivedragons.jpa.practice.domain.Member;
import com.fivedragons.jpa.practice.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() {
        //given
        Member member = new Member();
        member.setName("fad");

        //when
        Long savedId = memberService.join(member);

        //then
        Member findMember = memberRepository.findOne(savedId);
        Assertions.assertEquals(member, findMember);
    }
    
    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("five");

        Member member2 = new Member();
        member2.setName("five");
        
        // when
        memberService.join(member1);

        // then
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2); // 예외 발생
        });
    }
}