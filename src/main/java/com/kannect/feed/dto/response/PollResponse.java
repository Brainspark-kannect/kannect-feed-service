package com.kannect.feed.dto.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class PollResponse {
    private Long pollId;
    private String question;
    private Long createdBy;      // ID of the user who created the poll
    private String creatorName;  // Name of the user who created the poll
    private Instant createdAt;
    private List<PollOptionResponse> options;
    @Builder.Default
    private List<Long> votedByUsers = new ArrayList<>();  // List of user IDs who voted in this poll

    // Add constructor that matches the error signature
    public PollResponse(Long pollId, String question, Long createdBy, String creatorName, 
                       Instant createdAt, List<PollOptionResponse> options) {
        this.pollId = pollId;
        this.question = question;
        this.createdBy = createdBy;
        this.creatorName = creatorName;
        this.createdAt = createdAt;
        this.options = options;
        this.votedByUsers = new ArrayList<>();
    }
}
