package com.example.FollowService.Service;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Repository.FollowRepository;
import com.example.FollowService.commands.FollowCommand;
import com.example.FollowService.commands.UnFollowCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FollowService {
    @Autowired
    FollowRepository followRepository;
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public List<Follow> getAll(){
        return followRepository.findAll();
    }
    public List<Follow> getFollowers(String followeeId){
        return followRepository.findByFolloweeId(followeeId);
    }
    public Follow handleFollowCommand(FollowCommand command){
        Follow new_follow=new Follow(command.getFollowerId(),command.getFolloweeId(),new Date());
        return followRepository.save(new_follow);
    }
    public Follow handleUnFollowCommand(UnFollowCommand command){
        Follow follow=followRepository.findByFolloweeIdAndFollowerId(command.getFolloweeId(), command.getFollowerId());
        if(follow!=null){
            followRepository.delete(follow);
        }else{
            System.out.println("Follow Not found");
        }
        return follow;
    }
}
