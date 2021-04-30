package com.dictionary.demo.service;

import com.dictionary.demo.domain.Member;
import com.dictionary.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setEmail("spring1@gmail.com");
        member.setPassword("spring1@gmail.com");
        member.setName("spring1");
        member.setGender("female");
        member.setBirthday("19950101");

        //When
        Long saveId = memberService.join(member);

        //Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getEmail(), findMember.getEmail());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setEmail("spring1@gmail.com");
        member1.setPassword("spring1@gmail.com");
        member1.setName("spring1");
        member1.setGender("female");
        member1.setBirthday("19950101");

        Member member2 = new Member();
        member2.setEmail("spring1@gmail.com");
        member2.setPassword("spring1@gmail.com");
        member2.setName("spring1");
        member2.setGender("female");
        member2.setBirthday("19950101");

        //When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}