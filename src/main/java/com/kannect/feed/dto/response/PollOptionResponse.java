package com.kannect.feed.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class PollOptionResponse {
    private Long optionId;
    private String text;
    private long voteCount;   // for results view
    private List<Long> votedByUsers;  // Changed from String to Long - List of user IDs who voted for this option
}