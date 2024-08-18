package com.jygoh.whoever.domain.friend.service;

import com.jygoh.whoever.domain.friend.model.FriendRequest;
import com.jygoh.whoever.domain.friend.model.FriendRequestStatus;
import com.jygoh.whoever.domain.friend.model.Friendship;
import com.jygoh.whoever.domain.friend.repository.FriendRequestRepository;
import com.jygoh.whoever.domain.friend.repository.FriendshipRepository;
import com.jygoh.whoever.domain.member.entity.Member;
import com.jygoh.whoever.domain.member.repository.MemberRepository;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {

    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public FriendRequestServiceImpl(MemberRepository memberRepository,
                                    FriendRequestRepository friendRequestRepository,
                                    FriendshipRepository friendshipRepository,
                                    JwtTokenProvider jwtTokenProvider) {

        this.memberRepository = memberRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void sendFriendRequest(String token, Long receiverId) {

        Long senderId = jwtTokenProvider.getUserIdFromToken(token);

        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        if (friendRequestRepository.existsBySenderAndReceiver(sender, receiver)) {
            throw new IllegalStateException("Friend request already sent");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        friendRequestRepository.save(friendRequest);
    }

    @Override
    public void acceptFriendRequest(String token, Long requestId) {

        Long receiverId = jwtTokenProvider.getUserIdFromToken(token);

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!request.getReceiver().getId().equals(receiverId)) {
            throw new IllegalStateException("You cannot accept this friend request");
        }

        request.accept(); // 상태를 ACCEPTED로 변경

        Friendship friendship = Friendship.builder()
                .member1(request.getSender())
                .member2(request.getReceiver())
                .createdAt(LocalDateTime.now())
                .build();

        friendshipRepository.save(friendship);
        friendRequestRepository.save(request);
    }

    @Override
    public void rejectFriendRequest(String token, Long requestId) {

        Long receiverId = jwtTokenProvider.getUserIdFromToken(token);

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!request.getReceiver().getId().equals(receiverId)) {
            throw new IllegalStateException("You cannot reject this friend request");
        }

        request.reject(); // 상태를 REJECTED로 변경
        friendRequestRepository.save(request);
    }

    @Override
    public void cancelFriendRequest(String token, Long requestId) {

        Long senderId = jwtTokenProvider.getUserIdFromToken(token);

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!request.getSender().getId().equals(senderId)) {
            throw new IllegalStateException("You cannot cancel this friend request");
        }

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be canceled");
        }

        friendRequestRepository.delete(request);
    }

    @Override
    public List<Friendship> getFriendsList(String token) {

        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return friendshipRepository.findAllByMember1OrMember2(member, member);
    }
}
