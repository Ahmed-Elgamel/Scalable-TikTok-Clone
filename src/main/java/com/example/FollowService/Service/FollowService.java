package com.example.FollowService.Service;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Repository.FollowRepository;
import com.example.FollowService.commands.FollowCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FollowService {
    @Autowired
    FollowRepository followRepository;
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public Follow handleFollowCommand(FollowCommand command){
        Follow new_follow=new Follow(command.getFollowerId(),command.getFolloweeId(),new Date());
       return followRepository.save(new_follow);

    }
}
