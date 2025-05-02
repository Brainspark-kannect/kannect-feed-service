package com.kannect.feed.service;

import java.util.List;

import com.kannect.feed.dto.request.PollCreateRequest;
import com.kannect.feed.dto.request.PollVoteRequest;
import com.kannect.feed.dto.response.PollResponse;

/**
 * Service interface for Poll management.
 */
public interface IPollService {

    /**
     * Create a new poll with options.
     * @param request DTO containing question and options.
     * @return PollResponse DTO with created poll details.
     */
    PollResponse createPoll(PollCreateRequest request);

    /**
     * Cast a vote for a poll option.
     * @param pollId Poll ID.
     * @param request DTO containing optionId and voterId.
     */
    void vote(Long pollId, PollVoteRequest request);

    /**
     * Retrieve poll results with vote counts.
     * @param id Poll ID.
     * @return PollResponse DTO with vote counts.
     */
    PollResponse getPollWithResults(Long id);

    /**
     * Retrieve all polls (without results).
     * @return List of PollResponse DTOs.
     */
    List<PollResponse> getAllPolls();
}
