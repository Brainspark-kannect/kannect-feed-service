package com.kannect.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kannect.feed.entity.Feed;
import com.kannect.feed.entity.FeedLike;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

	Optional<FeedLike> findByFeedAndUserId(Feed feed, Long userId);
	
    // count likes for a feed
    long countByFeedIdAndLikedTrue(Long feedId);

    // count dislikes for a feed
    long countByFeedIdAndLikedFalse(Long feedId);


}
