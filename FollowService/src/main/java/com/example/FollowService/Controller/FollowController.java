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

import java.util.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;
    public FollowController(FollowService followService){
        this.followService=followService;
    }
    @GetMapping
    public ResponseEntity<?> getAllRecords(){
        try {


            List<Follow> follows = followService.getAll();
            if (follows.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(follows);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @GetMapping("/followers/{followee}")
    public ResponseEntity<?> getFollowers(@PathVariable String followee){
       try {


           List<Follow> followers = followService.getFollowers(followee);
           if (followers.isEmpty()) {
               return ResponseEntity.noContent().build();
           }
           return ResponseEntity.ok(followers);
       } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @GetMapping("/mutualfollowers/{user1}/{user2}")
    public ResponseEntity<?> getMutualFollowers(@PathVariable String user1,@PathVariable String user2){
        try {


            List<String> mutualFollowers = followService.getMutualFollowers(user1, user2);
            if (mutualFollowers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(mutualFollowers);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/{follower}/{followee}")
    public ResponseEntity<?> follow(@PathVariable String follower, @PathVariable String followee) {
       try {


           FollowCommand command = new FollowCommand(follower, followee, followService);
           Follow newFollow = command.execute();
           if (newFollow == null) {
               return ResponseEntity.status(HttpStatus.CONFLICT)
                       .body(null);
           }
           return ResponseEntity.status(HttpStatus.CREATED).body(newFollow);
       }
       catch (IllegalStateException e) {
           return ResponseEntity.status(HttpStatus.CONFLICT)
                   .body(Collections.singletonMap("error", e.getMessage()));

       } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @DeleteMapping("/{follower}/{followee}")
    public ResponseEntity<?> unFollow(@PathVariable String follower, @PathVariable String followee) {
        try {
            UnFollowCommand command = new UnFollowCommand(follower, followee, followService);
            Follow deletedFollow = command.execute();
            if (deletedFollow == null) {
                return ResponseEntity.notFound().build();

            }
            return ResponseEntity.status(HttpStatus.OK).body(deletedFollow);
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Follow relationship not found"));
        }


    }
    @PatchMapping("/mute/{follower}/{followee}")
    public ResponseEntity<?> mute(@PathVariable String follower, @PathVariable String followee) {
        try {


            MuteCommand muteCommand = new MuteCommand(follower, followee, followService);
            Follow updatedFollow = muteCommand.execute();
            if (updatedFollow == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedFollow);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @PatchMapping("/block/{follower}/{followee}")
    public ResponseEntity<?> block(@PathVariable String follower, @PathVariable String followee) {
       try {


           BlockCommand blockCommand = new BlockCommand(follower, followee, followService);
           Follow updatedFollow = blockCommand.execute();
           if (updatedFollow == null) {
               return ResponseEntity.notFound().build();
           }
           return ResponseEntity.ok(updatedFollow);
       }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @PatchMapping("/unmute/{follower}/{followee}")
    public ResponseEntity<?> unMute(@PathVariable String follower, @PathVariable String followee) {
        try{
        UnMuteCommand unMuteCommand = new UnMuteCommand(follower, followee, followService);
        Follow updatedFollow = unMuteCommand.execute();
        if (updatedFollow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFollow);
    } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @PatchMapping("/unblock/{follower}/{followee}")
    public ResponseEntity<?> unBlock(@PathVariable String follower, @PathVariable String followee) {
       try {


           UnBlockCommand unBlockCommand = new UnBlockCommand(follower, followee, followService);
           Follow updatedFollow = unBlockCommand.execute();
           if (updatedFollow == null) {
               return ResponseEntity.notFound().build();
           }
           return ResponseEntity.ok(updatedFollow);
       }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @GetMapping("/followers/{followee}/filter-by-date")
    public ResponseEntity<?> getFollowersFilteredByDate(  @PathVariable String followee,@RequestParam(name = "after")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date){
        try {
            List<String> filteredFollowers = followService.getFollowersFilteredByDate(followee, date);
            if (filteredFollowers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(filteredFollowers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @GetMapping("/followers/{followee}/filter-by-mutuals")
    public ResponseEntity<?> getFollowersFilteredByMutuals(@PathVariable String followee,
                                                                   @RequestParam(name = "minMutuals") Integer minMutuals){
        try{
        List<String> filteredFollowers = followService.getFollowersFilteredByNumOfMutuals(followee,minMutuals);
        if (filteredFollowers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filteredFollowers);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteAll(){
        try {


            String result = followService.deleteAll();
            if (result == null || result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to delete records");
            }
            return ResponseEntity.ok(result);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @PatchMapping("/{follower}/{followee}")
    public ResponseEntity<?> editDate(@PathVariable String follower, @PathVariable String followee,@RequestBody Map<String, Date> body ) {
      try {


        Date newDate=body.get("newDate");
       Follow updatedFollow=followService.updateDate(follower,followee,newDate);
        if (updatedFollow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFollow);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @PostMapping("/seed")
    public ResponseEntity<String> follow() {
        try{
        followService.seed(); // assuming this method returns void
        return ResponseEntity.ok("Database seeded successfully");
    }   catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()).toString());
        }
    }



}

