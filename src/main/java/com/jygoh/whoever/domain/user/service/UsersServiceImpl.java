package com.jygoh.whoever.domain.user.service;

import com.jygoh.whoever.domain.user.dto.UserCreateReqDto;
import com.jygoh.whoever.domain.user.dto.UserUpdateReqDto;
import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.user.entity.Provider;
import com.jygoh.whoever.domain.user.entity.Role;
import com.jygoh.whoever.domain.user.profile.dto.UserProfileResDto;
import com.jygoh.whoever.domain.user.profile.dto.UserProfileResDtoFactory;
import com.jygoh.whoever.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserProfileResDtoFactory userProfileResDtoFactory;

    public UsersServiceImpl(UserRepository userRepository,
        BCryptPasswordEncoder passwordEncoder,
        UserProfileResDtoFactory userProfileResDtoFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userProfileResDtoFactory = userProfileResDtoFactory;
    }

    @Override
    public Long registerUser(UserCreateReqDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        Optional<Users> existingMember = userRepository.findByEmail(requestDto.getEmail());
        if (existingMember.isPresent()) {
            Users users = existingMember.get();
            if (!users.getProviders().isEmpty()) {
                throw new IllegalArgumentException("소셜 로그인으로 이미 가입된 사용자입니다. 이메일을 사용할 수 없습니다.");
            }
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Users users = requestDto.toEntity().toBuilder().password(encodedPassword).build();
        userRepository.save(users);
        return users.getId();
    }

    private Users createNewUser(String email, String name, Provider provider,
        String profileImageUrl, String providerId) {
        // 새로운 Member 객체 생성
        Users newUsers = Users.builder().email(email).nickname(name)  // nickname을 name으로 설정
            .profileImageUrl(profileImageUrl).providers(List.of(provider))
            .providerId(providerId) // providerId 설정
            .role(Role.MEMBER) // 기본 역할 설정
            .build();
        return userRepository.save(newUsers);
    }

    @Override
    public void updateMember(Long id, UserUpdateReqDto requestDto) {
        Users users = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Member Id"));
        users.updateProfile(requestDto.getEmail(), requestDto.getNickname(),
            passwordEncoder.encode(requestDto.getPassword()), requestDto.getProfileImageUrl());
    }

    @Override
    public void deleteMember(Long id) {
        Users users = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        userRepository.delete(users);
    }

    @Override
    public UserProfileResDto getMemberProfileByNickname(String nickname) {
        Users users = userRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return userProfileResDtoFactory.createFromMember(users);
    }

    @Override
    public void setNickname(Long userId, String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("Nickname is already in use.");
        }

        Users users = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        users.updateNickname(nickname);
        users.completeSignUp();
    }
}
