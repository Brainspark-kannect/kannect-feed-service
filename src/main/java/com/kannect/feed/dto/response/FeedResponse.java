package com.kannect.feed.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class FeedResponse {
    private Long id;
    private String title;
    private String content;
    private String type;
    private boolean isFunFriday;
    private String mediaUrl;     // URL to the uploaded media (if any)
    private Long createdBy;      // ID of the user who created the feed
    private String creatorName;  // Name of the user who created the feed
    private Instant createdAt;
    private Instant updatedAt;
    private long likeCount;
    private long dislikeCount;
    private List<Long> likedByUsers;    // List of user IDs who liked the feed
    private List<Long> dislikedByUsers; // List of user IDs who disliked the feed
}
