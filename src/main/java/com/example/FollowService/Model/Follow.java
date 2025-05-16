package com.example.FollowService.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "follows")
public class Follow {
    @Id
    private String id;
    private String followeeId;
    private String followerId;

    private Date followedAt;
    private boolean muted;
    private boolean blocked;

    public Follow() {
    }

    public Follow( String followerId,String followeeId, Date followedAt) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.followedAt = followedAt;
        muted=false;
        blocked=false;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(String followeeId) {
        this.followeeId = followeeId;
    }

    public Date getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(Date followedAt) {
        this.followedAt = followedAt;
    }
}
