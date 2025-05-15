package com.kannect.feed.service.impl;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
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
import com.kannect.user.auth.repository.UserRepository;

@Service
public class FeedServiceImpl {
    @Autowired private FeedRepository feedRepo;
    @Autowired private FeedMediaRepository mediaRepo;
    @Autowired private FeedLikeRepository feedLikeRepo;
    @Autowired private CloudinaryUploader cloudinaryUploader;
    @Autowired private UserRepository userRepo;

    public FeedResponse createFeed(FeedRequest req, MultipartFile file) throws IOException {
        // 1. Save Feed entity
        Feed feed = new Feed();
        feed.setTitle(req.getTitle());
        feed.setContent(req.getContent());
        feed.setType(req.getType());
        feed.setFunFriday(req.isFunFriday());
        feed.setCreatedBy(req.getCreatedBy());
        feed.setCreatedAt(Instant.now());
        feed.setUpdatedAt(Instant.now());
        feed = feedRepo.save(feed);

        // 2. If file provided, upload to GCP and save FeedMedia
        if (file != null && !file.isEmpty()) {
            String fileName = "feed-media/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            String url = cloudinaryUploader.uploadFile(file,fileName);
            FeedMedia media = new FeedMedia();
            media.setFeed(feed);
            media.setGcpUrl(url);
            mediaRepo.save(media);
            feed.setMedia(media);
        }

        return toDto(feed);
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
        feed.setUpdatedAt(Instant.now());
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
            reaction.setReactedAt(Instant.now());
            feedLikeRepo.save(reaction);
        }
    }

    private FeedResponse toDto(Feed feed) {
        String mediaUrl = (feed.getMedia() != null)
            ? feed.getMedia().getGcpUrl()
            : null;

        // Fetch like/dislike counts
        long likes = feedLikeRepo.countByFeedIdAndLikedTrue(feed.getId());
        long dislikes = feedLikeRepo.countByFeedIdAndLikedFalse(feed.getId());
        List<Long> likedByUsers = feedLikeRepo.findUserIdsByFeedIdAndLikedTrue(feed.getId());
        List<Long> dislikedByUsers = feedLikeRepo.findUserIdsByFeedIdAndLikedFalse(feed.getId());

        return new FeedResponse(
            feed.getId(),
            feed.getTitle(),
            feed.getContent(),
            feed.getType(),
            feed.isFunFriday(),
            mediaUrl,
            feed.getCreatedBy(),
            getUserName(feed.getCreatedBy()),
            feed.getCreatedAt(),
            feed.getUpdatedAt(),
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

