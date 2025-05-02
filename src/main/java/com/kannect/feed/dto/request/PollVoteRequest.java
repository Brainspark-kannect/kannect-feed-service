package com.kannect.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class PollVoteRequest {
    private Long optionId;
    private String voterId;   // anonymous user identifier
}
