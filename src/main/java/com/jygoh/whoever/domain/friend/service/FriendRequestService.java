package com.jygoh.whoever.domain.friend.service;


import com.jygoh.whoever.domain.friend.model.Friendship;

import java.util.List;

public interface FriendRequestService {

    void sendFriendRequest(String token, Long receiverId);

    void acceptFriendRequest(String token, Long requestId);

    void rejectFriendRequest(String token, Long requestId);

    void cancelFriendRequest(String token, Long requestId);

    List<Friendship> getFriendsList(String token);
}
