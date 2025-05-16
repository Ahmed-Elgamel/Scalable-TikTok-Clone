package com.example.FollowService.Controller;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Service.FollowService;
import com.example.FollowService.commands.*;
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
    private final FollowService followService;
    public FollowController(FollowService followService){
        this.followService=followService;
    }
    @GetMapping
    public ResponseEntity<List<Follow>> getAllRecords(){
        List<Follow> follows = followService.getAll();
        if (follows.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(follows);
    }
    @GetMapping("/followers/{followee}")
    public ResponseEntity<List<Follow>> getFollowers(@PathVariable String followee){
        List<Follow> followers = followService.getFollowers(followee);
        if (followers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(followers);
    }
    @GetMapping("/mutualfollowers/{user1}/{user2}")
    public ResponseEntity<List<String>> getMutualFollowers(@PathVariable String user1,@PathVariable String user2){
        List<String> mutualFollowers = followService.getMutualFollowers(user1,user2);
        if (mutualFollowers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mutualFollowers);
    }
    //update date

    @PostMapping("/{follower}/{followee}")
    public ResponseEntity<Follow> follow(@PathVariable String follower, @PathVariable String followee) {
        FollowCommand command = new FollowCommand(follower, followee,followService);
        Follow newFollow = command.execute();
        if (newFollow == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);  // or a meaningful error DTO/message
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newFollow);
    }
    @DeleteMapping("/{follower}/{followee}")
    public ResponseEntity<Follow> unFollow(@PathVariable String follower, @PathVariable String followee) {
        UnFollowCommand command = new UnFollowCommand(follower, followee,followService);
        boolean deleted = command.execute() != null;
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();

    }
    @PatchMapping("/mute/{follower}/{followee}")
    public ResponseEntity<Follow> mute(@PathVariable String follower, @PathVariable String followee) {
        MuteCommand muteCommand=new MuteCommand(follower,followee,followService);
       Follow updatedFollow= muteCommand.execute();
        if (updatedFollow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFollow);
    }
    @PatchMapping("/block/{follower}/{followee}")
    public ResponseEntity<Follow> block(@PathVariable String follower, @PathVariable String followee) {
        BlockCommand blockCommand=new BlockCommand(follower,followee,followService);
        Follow updatedFollow= blockCommand.execute();
        if (updatedFollow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFollow);
    }
    @PatchMapping("/unmute/{follower}/{followee}")
    public ResponseEntity<Follow> unMute(@PathVariable String follower, @PathVariable String followee) {
        UnMuteCommand unMuteCommand=new UnMuteCommand(follower,followee,followService);
        Follow updatedFollow= unMuteCommand.execute();
        if (updatedFollow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFollow);
    }
    @PatchMapping("/unblock/{follower}/{followee}")
    public ResponseEntity<Follow> unBlock(@PathVariable String follower, @PathVariable String followee) {
        UnBlockCommand unBlockCommand=new UnBlockCommand(follower,followee,followService);
        Follow updatedFollow= unBlockCommand.execute();
        if (updatedFollow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFollow);
    }
    @GetMapping("/followers/{followee}/filter-by-date")
    public ResponseEntity<List<String>> getFollowersFilteredByDate(  @PathVariable String followee,@RequestParam(name = "after")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date){
        List<String> filteredFollowers = followService.getFollowersFilteredByDate(followee,date);
        if (filteredFollowers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filteredFollowers);
    }
    @GetMapping("/followers/{followee}/filter-by-mutuals")
    public ResponseEntity<List<String>> getFollowersFilteredByMutuals(@PathVariable String followee,
                                                                   @RequestParam(name = "minMutuals") Integer minMutuals){
        List<String> filteredFollowers = followService.getFollowersFilteredByNumOfMutuals(followee,minMutuals);
        if (filteredFollowers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filteredFollowers);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteAll(){
        String result = followService.deleteAll();
        if (result == null || result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete records");
        }
        return ResponseEntity.ok(result);
    }



}


//        Follow createdFollow=followCommand.execute();
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdFollow);
