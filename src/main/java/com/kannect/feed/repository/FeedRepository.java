package com.kannect.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kannect.feed.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

	Optional<Feed> findByIsFunFridayTrue();

}
