package com.kannect.feed.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kannect.feed.dto.request.FeedRequest;
import com.kannect.feed.dto.request.ReactionRequest;
import com.kannect.feed.dto.response.FeedResponse;

/**
 * Service interface for Feed management.
 */
public interface IFeedService {

    /**
     * Create a new feed post with optional media file.
     * @param request DTO containing feed data.
     * @param file Optional media file.
     * @return FeedResponse DTO of created feed.
     * @throws Exception on upload or validation errors.
     */
    FeedResponse createFeed(FeedRequest request, MultipartFile file) throws Exception;

    /**
     * Retrieve all feed posts.
     * @return List of FeedResponse DTOs.
     */
    List<FeedResponse> getAllFeeds();

    /**
     * Retrieve a feed post by its ID.
     * @param id Feed ID.
     * @return FeedResponse DTO.
     */
    FeedResponse getFeedById(Long id);

    /**
     * Retrieve feed posts tagged as Fun Friday.
     * @return List of FeedResponse DTOs.
     */
    List<FeedResponse> getFunFridayFeeds();

    /**
     * Update an existing feed post.
     * @param id Feed ID.
     * @param request DTO containing updated data.
     * @return Updated FeedResponse DTO.
     */
    FeedResponse updateFeed(Long id, FeedRequest request);

    /**
     * Delete a feed post by its ID.
     * @param id Feed ID.
     */
    void deleteFeed(Long id);

    /**
     * Like or dislike a feed post uniquely per user.
     * @param id Feed ID.
     * @param request DTO containing userId and liked flag.
     */
    void reactToFeed(Long id, ReactionRequest request);
}

