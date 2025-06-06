package com.example.FollowService.Service;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Repository.FollowRepository;
import com.example.FollowService.dto.FolloweesResponseEvent;
import com.example.FollowService.dto.RequestFolloweesEvent;
import com.example.FollowService.strategy.FilterByDateStrategy;
import com.example.FollowService.strategy.FilterByMutalFollowersStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class FollowService {
    @Autowired
    FollowRepository followRepository;
    private final KafkaTemplate<String, FolloweesResponseEvent> kafkaFetchUserFolloweesResponse;

    public FollowService(FollowRepository followRepository, KafkaTemplate<String, FolloweesResponseEvent> kafkaFetchUserFolloweesResponse) {
        this.followRepository = followRepository;
        this.kafkaFetchUserFolloweesResponse = kafkaFetchUserFolloweesResponse;
    }

    public List<Follow> getAll(){
        return followRepository.findAll();
    }
    public List<Follow> getFollowers(String followeeId){
        if (followeeId == null || followeeId.isEmpty()) {
            throw new IllegalArgumentException("Followee ID must not be null or empty");
        }
        return followRepository.findByFolloweeId(followeeId);
    }
    public Follow handleFollowCommand(String followerId,String followeeId){
        Optional<Follow> existing=followRepository.findByFollowerIdAndFolloweeId(followerId,followeeId);
        if(existing.isPresent())
        {
            throw new IllegalStateException("Already following");
        }
        Follow new_follow=new Follow(followerId,followeeId,new Date());
        return followRepository.save(new_follow);
    }
    public Follow handleUnFollowCommand(String followerId,String followeeId){
        Follow follow=followRepository.findByFollowerIdAndFolloweeId(followerId,followeeId)
                .orElseThrow(() -> new NoSuchElementException("Follow relationship not found"));;
            followRepository.delete(follow);

        return follow;
    }

    public Follow handleUnMuteCommand(String followerId, String followeeId) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new NoSuchElementException("Follow relationship not found"));

        follow.setMuted(false);
        followRepository.save(follow);
        return follow;

    }
    public Follow handleUnBlockCommand(String followerId, String followeeId) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new NoSuchElementException("Follow relationship not found"));

        follow.setBlocked(false);
        followRepository.save(follow);
        return follow;

    }

    public Follow handleMuteCommand(String followerId, String followeeId) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new NoSuchElementException("Follow relationship not found"));

        follow.setMuted(true);
        followRepository.save(follow);
        return follow;
    }
    public Follow handleBlockCommand(String followerId, String followeeId) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new NoSuchElementException("Follow relationship not found"));

        follow.setBlocked(true);
        followRepository.save(follow);
        return follow;
    }

    public List<String> getMutualFollowers(String user1, String user2) {
        if (user1.equals(user2)) {
            throw new IllegalArgumentException("Users must be different to find mutual followers");
        }

        List<Follow> followers1=getFollowers(user1);
        List<Follow> followers2=getFollowers(user2);
        Set<String> followersSet1 = new HashSet<>();
        for (Follow follow : followers1) {
            followersSet1.add(follow.getFollowerId());
        }

        Set<String> mutualUsers = new HashSet<>();
        for (Follow follow : followers2) {
            if (followersSet1.contains(follow.getFollowerId())) {
                mutualUsers.add(follow.getFollowerId());
            }
        }

        return new ArrayList<>(mutualUsers);

        }

    public List<String> getFollowersFilteredByDate(String followee,Date date){
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        FilterByDateStrategy byDateStrategy=new FilterByDateStrategy(date);
        List<Follow> followers=getFollowers(followee);
        if (followers.isEmpty()) {
            return Collections.emptyList();
        }
        return byDateStrategy.filter(followers);
    }
    public List<String> getFollowersFilteredByNumOfMutuals(String followee,Integer numOfMutuals){
        if (numOfMutuals == null || numOfMutuals < 0) {
            throw new IllegalArgumentException("Minimum mutuals must be non-negative");
        }
        FilterByMutalFollowersStrategy byMutalFollowersStrategy=new FilterByMutalFollowersStrategy(followee,numOfMutuals,this);
        List<Follow> followers=getFollowers(followee);
        if (followers.isEmpty()) {
            return Collections.emptyList();
        }
        return byMutalFollowersStrategy.filter(followers);
    }
    public String deleteAll(){
        followRepository.deleteAll();
        return "Deleted All successfully";
    }
    public Follow updateDate(String follower,String followee,Date date){
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
       Follow follow= followRepository.findByFollowerIdAndFolloweeId(follower,followee)
                .orElseThrow(() -> new NoSuchElementException("Follow relationship not found"));
       follow.setFollowedAt(date);
       followRepository.save(follow);
       return follow;
    }

    public List<String> getFollowees(String userId) {
        List<Follow> follows = followRepository.findByFollowerId(userId);

        return follows.stream()
                .map(Follow::getFolloweeId)
                .collect(Collectors.toList());
    }




    @KafkaListener( topics = "followees.fetch.request",
            groupId = "follow-ms-FolloweesRequest-consumer-group",
            containerFactory = "RequestUserFolloweesKafkaListenerFactory"
    )
    public void consumeFetchUserFollowees(RequestFolloweesEvent requestFolloweesEvent)  {
        System.out.println("****************************Received event to fetch user followees (FOLLOW SERVICE)****************************");

        // get the followee of this user :)
        String userId = requestFolloweesEvent.getUserId();
        String replyTopic = requestFolloweesEvent.getReplyTopic();
        List<String> followeeIds = getFollowees(userId);
        FolloweesResponseEvent followeesResponseEvent = new FolloweesResponseEvent(userId, followeeIds);
        kafkaFetchUserFolloweesResponse.send(replyTopic, followeesResponseEvent);



    }
    public String seed(){
        List<Follow> follows = Arrays.asList(
                new Follow("youssef", "messi", new Date()),
                new Follow("mohamed", "messi", new Date()),
                new Follow("ahmed", "messi", new Date()),
                new Follow("mohie", "messi", new Date()),
                new Follow("omar", "messi", new Date()),
                new Follow("mohie", "youssef", new Date()),
                new Follow("omar", "youssef", new Date()),
                new Follow("youssef", "omar", new Date())
        );
        followRepository.saveAll(follows);
        return "Database seeded with follows!";
    }



    public List<String> getFollowersIds(String followeeId){
        if (followeeId == null || followeeId.isEmpty()) {
            throw new IllegalArgumentException("Followee ID must not be null or empty");
        }
        return followRepository.findByFolloweeId(followeeId).stream().map(Follow::getFollowerId).collect(Collectors.toList());
    }




}


//
//        List<String> user1FollowerIds = user1Followers.stream()
//                .map(Follow::getFollowerId)
//                .collect(Collectors.toList());
//
//        List<String> user2FollowerIds = user2Followers.stream()
//                .map(Follow::getFollowerId)
//                .collect(Collectors.toList());
//
//        // Find intersection of user1's and user2's followers
//        user1FollowerIds.retainAll(user2FollowerIds);
//
//        return user1FollowerIds;



//        List<String> mutual_users=new ArrayList<>();
//        for(int i=0;i<followers1.size();i++) {
//            for(int j=0;j<followers2.size();j++){
//                if(followers1.get(i).getFollowerId().equals(followers2.get(j).getFollowerId()))
//                    mutual_users.add(followers1.get(i).getFollowerId());
//            }
//        }
//        return mutual_users;






