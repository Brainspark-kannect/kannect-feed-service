package com.kannect.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequest {
    private String title;
    private String content;
    private String type;
    private boolean isFunFriday;
    // (File upload can be handled via a separate endpoint or multipart request.)
}
