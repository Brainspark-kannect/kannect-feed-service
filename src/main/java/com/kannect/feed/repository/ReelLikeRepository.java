package com.kannect.feed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kannect.feed.entity.Reel;
import com.kannect.feed.entity.ReelLike;

public interface ReelLikeRepository extends JpaRepository<ReelLike, Long> {

	Optional<ReelLike> findByReelAndUserId(Reel reel, Long userId);

	long countByReelAndLikedTrue(Reel reel);

	long countByReelAndLikedFalse(Reel reel);

	@Query("SELECT rl.userId FROM ReelLike rl WHERE rl.reel = :reel AND rl.liked = true")
	List<Long> findUserIdsByReelAndLikedTrue(@Param("reel") Reel reel);

	@Query("SELECT rl.userId FROM ReelLike rl WHERE rl.reel = :reel AND rl.liked = false")
	List<Long> findUserIdsByReelAndLikedFalse(@Param("reel") Reel reel);

}
