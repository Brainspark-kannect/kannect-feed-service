package com.kannect.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kannect.feed.entity.PollOption;
import com.kannect.feed.entity.PollVote;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {

	boolean existsByOptionAndVoterId(PollOption option, String voterId);

	long countByOption(PollOption opt);

}
