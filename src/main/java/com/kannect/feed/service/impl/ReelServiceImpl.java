package com.kannect.feed.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kannect.feed.dto.request.ReelRequest;
import com.kannect.feed.dto.response.ReelResponse;
import com.kannect.feed.entity.Reel;
import com.kannect.feed.entity.ReelLike;
import com.kannect.feed.exception.ResourceNotFoundException;
import com.kannect.feed.repository.ReelLikeRepository;
import com.kannect.feed.repository.ReelRepository;

@Service
public class ReelServiceImpl {
    @Autowired private ReelRepository reelRepo;
    @Autowired private ReelLikeRepository reelLikeRepo;
    @Autowired private GcpStorageService gcpStorage;

    public ReelResponse createReel(ReelRequest req, MultipartFile videoFile) {
        // Save Reel entity
        Reel reel = new Reel();
        reel.setCaption(req.getCaption());
        reel.setUserId(req.getUserId());
        reel = reelRepo.save(reel);

        // Upload video to GCP and set URL
        if (videoFile != null && !videoFile.isEmpty()) {
            String url = gcpStorage.upload(videoFile);
            reel.setVideoUrl(url);
            reel = reelRepo.save(reel);
        }

        return new ReelResponse(reel.getId(), reel.getCaption(), reel.getUserId(),
                                reel.getVideoUrl(), reel.getCreatedAt(), reel.getUpdatedAt());
    }

    public ReelResponse getReel(Long id) {
        Reel reel = reelRepo.findById(id).orElseThrow(/*NotFound*/);
        return new ReelResponse(reel.getId(), reel.getCaption(), reel.getUserId(),
                                reel.getVideoUrl(), reel.getCreatedAt(), reel.getUpdatedAt());
    }
    
    public List<ReelResponse> getAllReels() {
        return reelRepo.findAll().stream()
            .map(r -> new ReelResponse(r.getId(), r.getCaption(), r.getUserId(), r.getVideoUrl(), r.getCreatedAt(), r.getUpdatedAt()))
            .toList();
    }

    public ReelResponse updateReel(Long id, ReelRequest req) {
        Reel reel = reelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reel not found"));
        reel.setCaption(req.getCaption());
        reel = reelRepo.save(reel);
        return new ReelResponse(reel.getId(), reel.getCaption(), reel.getUserId(), reel.getVideoUrl(), reel.getCreatedAt(), reel.getUpdatedAt());
    }

    public void deleteReel(Long id) {
        if (!reelRepo.existsById(id)) throw new ResourceNotFoundException("Reel not found");
        reelRepo.deleteById(id);
    }

    // ... updateReel, deleteReel similar to FeedService ...

    public void reactToReel(Long id, Long userId, boolean liked) {
        Reel reel = reelRepo.findById(id).orElseThrow(/*NotFound*/);
        Optional<ReelLike> existing = reelLikeRepo.findByReelAndUserId(reel, userId);
        if (existing.isPresent()) {
            ReelLike reaction = existing.get();
            reaction.setLiked(liked);
            reelLikeRepo.save(reaction);
        } else {
            ReelLike reaction = new ReelLike();
            reaction.setReel(reel);
            reaction.setUserId(userId);
            reaction.setLiked(liked);
            reelLikeRepo.save(reaction);
        }
    }
}

