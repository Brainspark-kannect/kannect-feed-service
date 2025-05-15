package com.kannect.feed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kannect.feed.entity.PollOption;
import com.kannect.feed.entity.PollVote;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {

	boolean existsByOptionAndVoterId(PollOption option, Long voterId);

	long countByOption(PollOption opt);

	@Query("SELECT v.voterId FROM PollVote v WHERE v.option = :option")
	List<Long> findVoterIdsByOption(@Param("option") PollOption option);

	@Query("SELECT v.voterId FROM PollVote v WHERE v.option.poll.id = :pollId")
	List<Long> findVoterIdsByPollId(@Param("pollId") Long pollId);

}
