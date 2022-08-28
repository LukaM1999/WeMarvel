package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.FriendRequest;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.FriendRequestDTO;
import com.wemarvel.wemarvel.repository.FriendRequestRepository;
import com.wemarvel.wemarvel.service.FriendService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private RegisteredUserService registeredUserService;


    @Override
    public List<FriendRequestDTO> getAcceptedFriendRequests(String username) {
        RegisteredUser user = registeredUserService.getUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found");
        List<FriendRequestDTO> friends = friendRequestRepository.getAcceptedFriendRequests(user.getId());
        for(FriendRequestDTO friend : friends) {
            Long friendId = Objects.equals(friend.getReceiverId(), user.getId()) ? friend.getSenderId() : friend.getReceiverId();
            RegisteredUser friendUser = registeredUserService.getUserById(friendId);
            friend.setUsername(friendUser.getUsername());
            friend.setImageUrl(friendUser.getImageUrl());
            friend.setGender(friendUser.getGender());
            friend.setLocation(friendUser.getLocation());
            friend.setBirthday(friendUser.getBirthday());
        }
        return friends;
    }

    @Override
    public List<FriendRequestDTO> getPendingFriendRequests() {
        RegisteredUser signedInUser = getSignedInUser();
        if(signedInUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<FriendRequestDTO> friendRequests = friendRequestRepository.getPendingFriendRequests(signedInUser.getId());
        for(FriendRequestDTO friendRequest : friendRequests) {
            Long receiverId = Objects.equals(friendRequest.getReceiverId(), signedInUser.getId()) ?
                    friendRequest.getSenderId() : friendRequest.getReceiverId();
            RegisteredUser receiver = registeredUserService.getUserById(receiverId);
            friendRequest.setUsername(receiver.getUsername());
            friendRequest.setImageUrl(receiver.getImageUrl());
            friendRequest.setGender(receiver.getGender());
            friendRequest.setLocation(receiver.getLocation());
            friendRequest.setBirthday(receiver.getBirthday());
        }
        return friendRequests;
    }

    @Override
    public FriendRequest getFriendRequest(String username) {
        RegisteredUser signedInUser = getSignedInUser();
        if(signedInUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        RegisteredUser user = registeredUserService.getUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return friendRequestRepository.findBySenderAndReceiver(signedInUser.getId(), user.getId());
    }

    @Override
    public void sendFriendRequest(String username) {
        RegisteredUser sender = getSignedInUser();
        if(sender == null) throw new UsernameNotFoundException("Sender not found");
        RegisteredUser receiver = registeredUserService.getUserByUsername(username);
        if(receiver == null) throw new UsernameNotFoundException("Receiver not found");
        if(sender.getId().equals(receiver.getId()))
            throw new IllegalArgumentException("You can't send friend request to yourself");
        if(friendRequestRepository.findBySenderAndReceiver(sender.getId(), receiver.getId()) != null)
            throw new IllegalArgumentException("Friend request already exists");
        FriendRequest friendRequest = new FriendRequest(sender.getId(), receiver.getId(), LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
    }

    @Override
    public void acceptFriendRequest(Long requestId) {
        RegisteredUser receiver = getSignedInUser();
        if(receiver == null) throw new UsernameNotFoundException("Receiver not found");
        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElse(null);
        if(friendRequest == null) throw new IllegalArgumentException("Friend request not found");
        if(!friendRequest.getReceiverId().equals(receiver.getId()))
            throw new IllegalArgumentException("You can't accept friend request from other users");
        friendRequest.setAccepted(true);
        friendRequest.setFriendsSince(LocalDate.now());
        friendRequestRepository.save(friendRequest);
    }

    @Override
    public void removeFriend(Long requestId) {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not found");
        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElse(null);
        if(friendRequest == null) throw new IllegalArgumentException("Friend request not found");
        if(!friendRequest.getReceiverId().equals(user.getId()) && !friendRequest.getSenderId().equals(user.getId()))
            throw new IllegalArgumentException("You can't remove friends from other users");
        friendRequestRepository.delete(friendRequest);
    }
}
