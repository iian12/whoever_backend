package com.jygoh.whoever.member;

import com.jygoh.whoever.domain.member.dto.MemberCreateRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberLoginRequestDto;
import com.jygoh.whoever.domain.member.dto.MemberUpdateRequestDto;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.entity.Role;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.domain.member.service.MemberServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceImplIntegrationTest {

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll(); // 테스트 환경을 위해 데이터베이스를 초기화
    }

    @Test
    public void testRegister_Success() {
        // Given
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto("testUsername", "testPassword", "test@example.com");

        // When
        Long memberId = memberService.register(requestDto);

        // Then
        Member savedMember = memberRepository.findById(memberId).orElse(null);
        assertNotNull(savedMember);
        assertEquals("testUsername", savedMember.getUsername());
        assertTrue(passwordEncoder.matches("testPassword", savedMember.getPassword()));
        assertEquals("test@example.com", savedMember.getEmail());
    }

    @Test
    public void testLogin_Success() {
        // Given
        Member member = Member.builder()
            .username("testUsername")
            .password(passwordEncoder.encode("testPassword"))
            .email("test@example.com")
            .role(Role.MEMBER)
            .build();
        memberRepository.save(member);

        MemberLoginRequestDto requestDto = new MemberLoginRequestDto("testUsername", "testPassword");

        // When
        String result = memberService.Login(requestDto);

        // Then
        assertEquals("Login successful", result);
    }

    @Test
    public void testUpdateMember_Success() {
        // Given
        Member member = Member.builder()
            .username("oldUsername")
            .password(passwordEncoder.encode("oldPassword"))
            .email("oldEmail@example.com")
            .role(Role.MEMBER)
            .build();
        memberRepository.save(member);

        Long memberId = member.getId();
        MemberUpdateRequestDto requestDto = new MemberUpdateRequestDto("newUsername", "newPassword", "newEmail@example.com");

        // When
        memberService.updateMember(memberId, requestDto);

        // Then
        Member updatedMember = memberRepository.findById(memberId).orElse(null);
        assertNotNull(updatedMember);
        assertEquals("newUsername", updatedMember.getUsername());
        assertEquals("newEmail@example.com", updatedMember.getEmail());
        assertTrue(passwordEncoder.matches("newPassword", updatedMember.getPassword()));
    }

    @Test
    public void testDeleteMember_Success() {
        // Given
        Member member = Member.builder()
            .username("testUsername")
            .password(passwordEncoder.encode("testPassword"))
            .email("test@example.com")
            .role(Role.MEMBER)
            .build();
        memberRepository.save(member);

        Long memberId = member.getId();

        // When
        memberService.deleteMember(memberId);

        // Then
        assertFalse(memberRepository.findById(memberId).isPresent());
    }
}
