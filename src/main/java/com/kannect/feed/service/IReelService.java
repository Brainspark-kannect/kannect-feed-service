package com.kannect.feed.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kannect.feed.dto.request.ReactionRequest;
import com.kannect.feed.dto.request.ReelRequest;
import com.kannect.feed.dto.response.ReelResponse;

public interface IReelService {

    /**
     * Create a new reel post with video file.
     * @param request DTO containing reel data.
     * @param file Video file.
     * @return ReelResponse DTO of created reel.
     * @throws Exception on upload errors.
     */
    ReelResponse createReel(ReelRequest request, MultipartFile file) throws Exception;

    /**
     * Retrieve all reels.
     * @return List of ReelResponse DTOs.
     */
    List<ReelResponse> getAllReels();

    /**
     * Retrieve a reel by its ID.
     * @param id Reel ID.
     * @return ReelResponse DTO.
     */
    ReelResponse getReel(Long id);

    /**
     * Update an existing reel post.
     * @param id Reel ID.
     * @param request DTO containing updated data.
     * @return Updated ReelResponse DTO.
     */
    ReelResponse updateReel(Long id, ReelRequest request);

    /**
     * Delete a reel post by its ID.
     * @param id Reel ID.
     */
    void deleteReel(Long id);

    /**
     * Like or dislike a reel post uniquely per user.
     * @param id Reel ID.
     * @param request DTO containing userId and liked flag.
     */
    void reactToReel(Long id, ReactionRequest request);
}