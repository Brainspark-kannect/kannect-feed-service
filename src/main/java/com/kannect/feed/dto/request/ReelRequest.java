package com.kannect.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class ReelRequest {
    private String caption;
    private Long userId;
    // (Video file upload via multipart or separate endpoint.)
}
