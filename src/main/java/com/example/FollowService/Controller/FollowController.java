package com.example.FollowService.Controller;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Service.FollowService;
import com.example.FollowService.commands.FollowCommand;
import com.example.FollowService.commands.MuteCommand;
import com.example.FollowService.commands.UnFollowCommand;
import com.example.FollowService.commands.UnMuteCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @GetMapping("/mutualfollowers/{user1}/{user2}")
    public ResponseEntity<List<String>> getMutualFollowers(@PathVariable String user1,@PathVariable String user2){
        List<String> followers = followService.getMutualFollowers(user1,user2);
        return ResponseEntity.ok(followers);
    }
    //update date

    @PostMapping("/{follower}/{followee}")
    public ResponseEntity<Follow> follow(@PathVariable String follower, @PathVariable String followee) {
        FollowCommand followCommand = new FollowCommand(follower, followee,followService);
        followCommand.execute();
//        Follow createdFollow=followCommand.execute();
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdFollow);
        return ResponseEntity.ok().build();
        //
    }
    @DeleteMapping("/{follower}/{followee}")
    public ResponseEntity<Follow> unFollow(@PathVariable String follower, @PathVariable String followee) {
        UnFollowCommand unFollowCommand = new UnFollowCommand(follower, followee,followService);
//        Follow deletedFollow=unFollowCommand.execute();
//        return ResponseEntity.status(HttpStatus.OK).body(deletedFollow);
        unFollowCommand.execute();
        return ResponseEntity.ok().build();

    }
    @PatchMapping("/mute/{follower}/{followee}")
    public ResponseEntity<Follow> mute(@PathVariable String follower, @PathVariable String followee) {
        MuteCommand muteCommand=new MuteCommand(follower,followee,followService);
        muteCommand.execute();
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/unmute/{follower}/{followee}")
    public ResponseEntity<Follow> unMute(@PathVariable String follower, @PathVariable String followee) {
        UnMuteCommand unMuteCommand=new UnMuteCommand(follower,followee,followService);
        unMuteCommand.execute();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/followers/{followee}/filter-by-date")
    public ResponseEntity<List<String>> getFollowersFilteredByDate(  @PathVariable String followee,@RequestParam(name = "after")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date){
        List<String> followers = followService.getFollowersFilteredByDate(followee,date);
        return ResponseEntity.ok(followers);
    }
    @GetMapping("/followers/{followee}/filter-by-mutuals")
    public ResponseEntity<List<String>> getFollowersFilteredByDate(@PathVariable String followee,
                                                                   @RequestParam(name = "minMutuals") Integer minMutuals){
        List<String> followers = followService.getFollowersFilteredByNumOfMutuals(followee,minMutuals);
        return ResponseEntity.ok(followers);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteAll(){
       return ResponseEntity.ok(followService.deleteAll());
    }



}
