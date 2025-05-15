package com.kannect.feed.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kannect.feed.dto.request.ReactionRequest;
import com.kannect.feed.dto.request.ReelRequest;
import com.kannect.feed.dto.response.ReelResponse;
import com.kannect.feed.entity.Reel;
import com.kannect.feed.entity.ReelLike;
import com.kannect.feed.exception.ResourceNotFoundException;
import com.kannect.feed.repository.ReelLikeRepository;
import com.kannect.feed.repository.ReelRepository;
import com.kannect.feed.service.IReelService;
import com.kannect.feed.utils.CloudinaryUploader;
import com.kannect.user.auth.repository.UserRepository;

@Service
public class ReelServiceImpl implements IReelService {
    private static final Logger logger = LoggerFactory.getLogger(ReelServiceImpl.class);
    
    @Autowired private ReelRepository reelRepo;
    @Autowired private ReelLikeRepository reelLikeRepo;
    @Autowired private CloudinaryUploader cloudinaryUploader;
    @Autowired private UserRepository userRepo;

    public ReelResponse createReel(ReelRequest req, MultipartFile videoFile) throws IOException {
        // Save Reel entity
        Reel reel = new Reel();
        reel.setCaption(req.getCaption());
        reel.setUserId(req.getUserId());
        reel.setCreatedAt(Instant.now());
        reel.setUpdatedAt(Instant.now());
        reel = reelRepo.save(reel);

        // Upload video to GCP and set URL
        if (videoFile != null && !videoFile.isEmpty()) {
        	String fileName = "reel-media/" + UUID.randomUUID() + "-" + videoFile.getOriginalFilename();
            String url = cloudinaryUploader.uploadFile(videoFile,fileName);
            reel.setVideoUrl(url);
            reel = reelRepo.save(reel);
        }

        return toDto(reel);
    }

    public ReelResponse getReel(Long id) {
        Reel reel = reelRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reel not found with id: " + id));
        return toDto(reel);
    }
    
    public List<ReelResponse> getAllReels() {
        return reelRepo.findAll().stream()
            .map(this::toDto)
            .toList();
    }

    public ReelResponse updateReel(Long id, ReelRequest req) {
        Reel reel = reelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reel not found"));
        reel.setCaption(req.getCaption());
        reel.setUpdatedAt(Instant.now());
        reel = reelRepo.save(reel);
        return toDto(reel);
    }

    public void deleteReel(Long id) {
        if (!reelRepo.existsById(id)) throw new ResourceNotFoundException("Reel not found");
        reelRepo.deleteById(id);
    }

    // ... updateReel, deleteReel similar to FeedService ...

    @Override
    public void reactToReel(Long id, ReactionRequest request) {
        logger.debug("Received reaction request for reel {}: userId={}, liked={}", id, request.getUserId(), request.isLiked());
        reactToReel(id, request.getUserId(), request.isLiked());
    }

    protected void reactToReel(Long id, Long userId, boolean liked) {
        logger.debug("Processing reaction: reelId={}, userId={}, liked={}", id, userId, liked);
        
        if (userId == null) {
            logger.error("userId is null for reel reaction");
            throw new IllegalArgumentException("userId cannot be null");
        }
        
        Reel reel = reelRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reel not found with id: " + id));
            
        logger.debug("Found reel: {}", reel);
        
        Optional<ReelLike> existing = reelLikeRepo.findByReelAndUserId(reel, userId);
        if (existing.isPresent()) {
            logger.debug("Updating existing reaction for userId={}", userId);
            ReelLike reaction = existing.get();
            reaction.setLiked(liked);
            reaction.setReactedAt(Instant.now());
            reelLikeRepo.save(reaction);
        } else {
            logger.debug("Creating new reaction for userId={}", userId);
            ReelLike reaction = new ReelLike();
            reaction.setReel(reel);
            reaction.setUserId(userId);
            reaction.setLiked(liked);
            reaction.setReactedAt(Instant.now());
            logger.debug("About to save new reaction: {}", reaction);
            ReelLike saved = reelLikeRepo.save(reaction);
            logger.debug("Saved reaction: {}", saved);
        }
    }

    private ReelResponse toDto(Reel reel) {
        // Get like/dislike counts and user lists
        long likes = reelLikeRepo.countByReelAndLikedTrue(reel);
        long dislikes = reelLikeRepo.countByReelAndLikedFalse(reel);
        List<Long> likedByUsers = reelLikeRepo.findUserIdsByReelAndLikedTrue(reel);
        List<Long> dislikedByUsers = reelLikeRepo.findUserIdsByReelAndLikedFalse(reel);

        return new ReelResponse(
            reel.getId(),
            reel.getCaption(),
            reel.getUserId(),
            getUserName(reel.getUserId()),
            reel.getVideoUrl(),
            reel.getCreatedAt(),
            reel.getUpdatedAt(),
            likes,
            dislikes,
            likedByUsers,
            dislikedByUsers
        );
    }

    private String getUserName(Long userId) {
        return userRepo.findById(userId)
            .map(user -> user.getFirstName() + " " + user.getLastName())
            .orElse("Unknown User");
    }
}

