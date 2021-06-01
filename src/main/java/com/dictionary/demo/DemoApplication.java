package com.dictionary.demo;

import com.dictionary.demo.domain.Member;
import com.dictionary.demo.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    private final MemberService memberService;
    public DemoApplication(MemberService memberService) {
        this.memberService = memberService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        for(int i=1;i<=10;i++) {
            Member member = new Member();
            member.setEmail("a" + Integer.toString(i) + "@gmail.com");
            member.setPassword("a");
            member.setName("멤버" + Integer.toString(i));
            member.setBirthday("2000.10.1" + Integer.toString(i-1));
            member.setGender("남");
            StringBuffer sb = new StringBuffer();
            for(int j = 0; j < 100; j++) {
                int a = (int) (Math.random()*10000) % 5;
                sb.append(Integer.toString(a));
            }
            int cnt = 0;
            for(int j = 99; j >= 0; j--) {
                cnt++;
                if(cnt==3) {
                    sb.insert(j, ",");
                    cnt = 0;
                }
            }
            member.setScore(sb.toString());
            memberService.join(member);
        }
    }
}
