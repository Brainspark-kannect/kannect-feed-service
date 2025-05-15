package com.kannect.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @NotBlank(message = "Type is required")
    private String type;
    
    private boolean isFunFriday;
    
    @NotNull(message = "Creator ID is required")
    private Long createdBy;  // ID of the user creating the feed
    // (File upload can be handled via a separate endpoint or multipart request.)
}
