package com.kannect.feed.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class ReelResponse {
    private Long id;
    private String caption;
    private Long userId;
    private String userName;  // Name of the user who created the reel
    private String videoUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private long likeCount;
    private long dislikeCount;
    private List<Long> likedByUsers;    // List of user IDs who liked the reel
    private List<Long> dislikedByUsers; // List of user IDs who disliked the reel
}
