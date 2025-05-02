package com.kannect.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kannect.feed.entity.Reel;
import com.kannect.feed.entity.ReelLike;

public interface ReelLikeRepository extends JpaRepository<ReelLike, Long> {

	Optional<ReelLike> findByReelAndUserId(Reel reel, Long userId);

}
