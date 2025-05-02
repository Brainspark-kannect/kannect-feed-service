package com.kannect.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kannect.feed.entity.Poll;

public interface PollRepository extends JpaRepository<Poll, Long>{

}
