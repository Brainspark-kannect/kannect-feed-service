package com.kannect.feed.service.impl;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kannect.feed.dto.request.FeedRequest;
import com.kannect.feed.dto.response.FeedResponse;
import com.kannect.feed.entity.Feed;
import com.kannect.feed.entity.FeedLike;
import com.kannect.feed.entity.FeedMedia;
import com.kannect.feed.repository.FeedLikeRepository;
import com.kannect.feed.repository.FeedMediaRepository;
import com.kannect.feed.repository.FeedRepository;
import com.kannect.feed.utils.CloudinaryUploader;

@Service
public class FeedServiceImpl {
    @Autowired private FeedRepository feedRepo;
    @Autowired private FeedMediaRepository mediaRepo;
    @Autowired private FeedLikeRepository feedLikeRepo;
    @Autowired private CloudinaryUploader cloudinaryUploader;

    public FeedResponse createFeed(FeedRequest req, MultipartFile file) throws IOException {
        // 1. Save Feed entity
        Feed feed = new Feed();
        feed.setTitle(req.getTitle());
        feed.setContent(req.getContent());
        feed.setType(req.getType());
        feed.setFunFriday(req.isFunFriday());
        feed = feedRepo.save(feed);

        // 2. If file provided, upload to GCP and save FeedMedia
        if (file != null && !file.isEmpty()) {
            // Use Google Cloud Storage client to upload file
        	String fileName = "feed-media/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            String url = cloudinaryUploader.uploadFile(file,fileName); // returns public URL or path
            FeedMedia media = new FeedMedia();
            media.setFeed(feed);
            media.setGcpUrl(url);
            mediaRepo.save(media);
            feed.setMedia(media);
        }

        // 3. Build and return response DTO
        FeedResponse resp = new FeedResponse(feed.getId(), feed.getTitle(),
            feed.getContent(), feed.getType(), feed.isFunFriday(),
            (feed.getMedia()!=null ? feed.getMedia().getGcpUrl() : null),
            feed.getCreatedAt(), feed.getUpdatedAt());
        return resp;
    }

    public FeedResponse getFeedById(Long id) {
        Feed feed = feedRepo.findById(id).orElseThrow(/*NotFound*/);
        return toDto(feed);
    }

    public List<FeedResponse> getAllFeeds() {
        return feedRepo.findAll().stream()
                       .map(this::toDto).collect(Collectors.toList());
    }

    public List<FeedResponse> getFunFridayFeeds() {
        // Only return Friday posts if today is Friday (business logic example)
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY) {
            return feedRepo.findByIsFunFridayTrue().stream()
                           .map(this::toDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public FeedResponse updateFeed(Long id, FeedRequest req) {
        Feed feed = feedRepo.findById(id).orElseThrow(/*NotFound*/);
        feed.setTitle(req.getTitle());
        feed.setContent(req.getContent());
        feed.setType(req.getType());
        feed.setFunFriday(req.isFunFriday());
        feed = feedRepo.save(feed);
        return toDto(feed);
    }

    public void deleteFeed(Long id) {
        feedRepo.deleteById(id);
    }

    public void reactToFeed(Long id, Long userId, boolean liked) {
        Feed feed = feedRepo.findById(id).orElseThrow(/*NotFound*/);
        // Check existing reaction
        Optional<FeedLike> existing = feedLikeRepo.findByFeedAndUserId(feed, userId);
        if (existing.isPresent()) {
            // Update existing reaction
            FeedLike reaction = existing.get();
            reaction.setLiked(liked);
            feedLikeRepo.save(reaction);
        } else {
            // Create new reaction
            FeedLike reaction = new FeedLike();
            reaction.setFeed(feed);
            reaction.setUserId(userId);
            reaction.setLiked(liked);
            feedLikeRepo.save(reaction);
        }
    }

    private FeedResponse toDto(Feed feed) {
        String mediaUrl = (feed.getMedia()!=null ? feed.getMedia().getGcpUrl() : null);
        return new FeedResponse(feed.getId(), feed.getTitle(), feed.getContent(),
            feed.getType(), feed.isFunFriday(), mediaUrl,
            feed.getCreatedAt(), feed.getUpdatedAt());
    }
}

