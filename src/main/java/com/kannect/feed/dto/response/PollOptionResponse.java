package com.kannect.feed.dto.response;

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
}