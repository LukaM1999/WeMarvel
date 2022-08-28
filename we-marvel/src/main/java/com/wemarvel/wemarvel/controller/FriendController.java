package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.FriendRequest;
import com.wemarvel.wemarvel.model.dto.FriendRequestDTO;
import com.wemarvel.wemarvel.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/{username}/accepted")
    public List<FriendRequestDTO> getAcceptedFriendRequests(@PathVariable String username) {
        return friendService.getAcceptedFriendRequests(username);
    }

    @GetMapping("/pending")
    public List<FriendRequestDTO> getPendingFriendRequests() {
        return friendService.getPendingFriendRequests();
    }

    @GetMapping("/{username}")
    public FriendRequest getFriendRequest(@PathVariable String username) {
        return friendService.getFriendRequest(username);
    }

    @PostMapping("/{username}")
    public void sendFriendRequest(@PathVariable String username) {
        friendService.sendFriendRequest(username);
    }

    @PatchMapping("/{requestId}")
    public void acceptFriendRequest(@PathVariable Long requestId) {
        friendService.acceptFriendRequest(requestId);
    }

    @DeleteMapping("/{requestId}")
    public void removeFriend(@PathVariable Long requestId) {
        friendService.removeFriend(requestId);
    }
}
