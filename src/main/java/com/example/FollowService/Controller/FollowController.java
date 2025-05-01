package com.example.FollowService.Controller;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Service.FollowService;
import com.example.FollowService.commands.FollowCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    @Autowired
    private FollowService followService;
    public FollowController(FollowService followService){
        this.followService=followService;
    }
    @PostMapping("/{follower}/{followee}")
    public ResponseEntity<Follow> follow(@PathVariable String follower, @PathVariable String followee) {
        FollowCommand followCommand = new FollowCommand(follower, followee);
        Follow createdFollow=followService.handleFollowCommand(followCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFollow);
//        return ResponseEntity.ok().build();
        //
    }

}
