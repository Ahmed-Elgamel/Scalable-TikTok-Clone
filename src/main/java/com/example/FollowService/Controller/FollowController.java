package com.example.FollowService.Controller;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Service.FollowService;
import com.example.FollowService.commands.FollowCommand;
import com.example.FollowService.commands.UnFollowCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    @Autowired
    private final FollowService followService;
    public FollowController(FollowService followService){
        this.followService=followService;
    }
    @GetMapping
    public ResponseEntity<List<Follow>> getAllRecord(){
        List<Follow> follows = followService.getAll();
        return ResponseEntity.ok(follows);
    }
    @GetMapping("/followers/{followee}")
    public ResponseEntity<List<Follow>> getFollowers(@PathVariable String followee){
        List<Follow> followers = followService.getFollowers(followee);
        return ResponseEntity.ok(followers);
    }
    //update date



    @PostMapping("/{follower}/{followee}")
    public ResponseEntity<Follow> follow(@PathVariable String follower, @PathVariable String followee) {
        FollowCommand followCommand = new FollowCommand(follower, followee);
        Follow createdFollow=followService.handleFollowCommand(followCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFollow);
//        return ResponseEntity.ok().build();
        //
    }
    @DeleteMapping("/{follower}/{followee}")
    public ResponseEntity<Follow> unFollow(@PathVariable String follower, @PathVariable String followee) {
        UnFollowCommand unFollowCommand = new UnFollowCommand(follower, followee);
        Follow deletedFollow=followService.handleUnFollowCommand(unFollowCommand);
        return ResponseEntity.status(HttpStatus.OK).body(deletedFollow);
    }


}
