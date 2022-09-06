package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.FriendRequest;
import com.wemarvel.wemarvel.model.dto.FriendRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("SELECT f FROM FriendRequest f " +
            "WHERE (f.senderId = ?1 AND f.receiverId = ?2) " +
            "OR (f.receiverId = ?1 AND f.senderId = ?2)")
    FriendRequest findBySenderAndReceiver(Long senderId, Long receiverId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.FriendRequestDTO(" +
            "f.id, f.senderId, u.username, u.imageUrl, f.receiverId, u2.username, u2.imageUrl, f.accepted, f.sentAt, f.friendsSince) " +
            "FROM FriendRequest f " +
            "JOIN RegisteredUser u ON f.senderId = u.id " +
            "JOIN RegisteredUser u2 ON f.receiverId = u2.id " +
            "WHERE (f.receiverId = ?1 OR f.senderId = ?1) AND f.accepted = true")
    List<FriendRequestDTO> getAcceptedFriendRequests(Long id);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.FriendRequestDTO(" +
            "f.id, f.senderId, u.username, u.imageUrl, f.receiverId, u2.username, u2.imageUrl, f.accepted, f.sentAt, f.friendsSince) " +
            "FROM FriendRequest f " +
            "JOIN RegisteredUser u ON f.senderId = u.id " +
            "JOIN RegisteredUser u2 ON f.receiverId = u2.id " +
            "WHERE (f.receiverId = ?1 OR f.senderId = ?1) AND f.accepted = false")
    List<FriendRequestDTO> getPendingFriendRequests(Long id);

    @Query("SELECT f FROM FriendRequest f " +
            "WHERE (f.senderId = ?1 AND f.receiverId = ?2) " +
            "OR (f.receiverId = ?1 AND f.senderId = ?2) AND f.accepted = true")
    FriendRequest getAcceptedFriendRequest(Long excludedUserId, Long recipientId);
}
