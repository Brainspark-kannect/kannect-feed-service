package com.kannect.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionRequest {
    private Long userId;         // ID of user reacting
    private boolean liked;       // true=like, false=dislike
}