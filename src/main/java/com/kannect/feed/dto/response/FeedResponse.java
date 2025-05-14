package com.kannect.feed.dto.response;

import java.time.Instant;

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
    private Instant createdAt;
    private Instant updatedAt;
    private long likeCount;
    private long dislikeCount;
}
