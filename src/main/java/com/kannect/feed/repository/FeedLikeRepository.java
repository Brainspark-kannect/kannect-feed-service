package com.kannect.feed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kannect.feed.entity.Feed;
import com.kannect.feed.entity.FeedLike;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

	Optional<FeedLike> findByFeedAndUserId(Feed feed, Long userId);
	
    // count likes for a feed
    long countByFeedIdAndLikedTrue(Long feedId);

    // count dislikes for a feed
    long countByFeedIdAndLikedFalse(Long feedId);

    // get users who liked a feed
    @Query("SELECT fl.userId FROM FeedLike fl WHERE fl.feed.id = :feedId AND fl.liked = true")
    List<Long> findUserIdsByFeedIdAndLikedTrue(@Param("feedId") Long feedId);
    
    // get users who disliked a feed
    @Query("SELECT fl.userId FROM FeedLike fl WHERE fl.feed.id = :feedId AND fl.liked = false")
    List<Long> findUserIdsByFeedIdAndLikedFalse(@Param("feedId") Long feedId);

}
