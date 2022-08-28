package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.FriendRequest;
import com.wemarvel.wemarvel.model.dto.FriendRequestDTO;

import java.util.List;

public interface FriendService {
    List<FriendRequestDTO> getAcceptedFriendRequests(String username);
    List<FriendRequestDTO> getPendingFriendRequests();
    FriendRequest getFriendRequest(String username);
    void sendFriendRequest(String username);
    void acceptFriendRequest(Long requestId);
    void removeFriend(Long requestId);
}
