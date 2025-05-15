package com.kannect.feed.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReactionRequest {
    @NotNull(message = "userId is required")
    private Long userId;         // ID of user reacting
    private boolean liked;       // true=like, false=dislike
}