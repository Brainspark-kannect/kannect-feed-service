package com.kannect.feed.dto.response;

import java.time.Instant;

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
    private String videoUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
