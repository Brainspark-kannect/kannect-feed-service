package com.kannect.feed.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class PollResponse {
    private Long pollId;
    private String question;
    private List<PollOptionResponse> options;
}
