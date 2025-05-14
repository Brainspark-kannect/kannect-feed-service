package com.kannect.feed.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kannect.feed.dto.request.PollCreateRequest;
import com.kannect.feed.dto.request.PollVoteRequest;
import com.kannect.feed.dto.response.PollOptionResponse;
import com.kannect.feed.dto.response.PollResponse;
import com.kannect.feed.entity.Poll;
import com.kannect.feed.entity.PollOption;
import com.kannect.feed.entity.PollVote;
import com.kannect.feed.repository.PollOptionRepository;
import com.kannect.feed.repository.PollRepository;
import com.kannect.feed.repository.PollVoteRepository;

@Service
public class PollServiceImpl {
    @Autowired private PollRepository pollRepo;
    @Autowired private PollOptionRepository optionRepo;
    @Autowired private PollVoteRepository voteRepo;

    public PollResponse createPoll(PollCreateRequest req) {
        Poll poll = new Poll();
        poll.setQuestion(req.getQuestion());
        poll.setCreatedAt(Instant.now());
        poll = pollRepo.save(poll);

        // Save options
        List<PollOptionResponse> optionResponses = new ArrayList<>();
        for (String optText : req.getOptions()) {
            PollOption opt = new PollOption();
            opt.setText(optText);
            opt.setPoll(poll);
            opt = optionRepo.save(opt);
            optionResponses.add(new PollOptionResponse(opt.getId(), opt.getText(), 0));
        }

        return new PollResponse(poll.getId(), poll.getQuestion(), optionResponses);
    }

    public void vote(Long pollId, PollVoteRequest req) {
        // Find option
        PollOption option = optionRepo.findById(req.getOptionId())
            .orElseThrow(/*NotFound*/);
        // Prevent duplicate vote for this option by this voter
        if (voteRepo.existsByOptionAndVoterId(option, req.getVoterId())) {
            throw new IllegalStateException("User has already voted for this option");
        }
        PollVote vote = new PollVote();
        vote.setOption(option);
        vote.setVoterId(req.getVoterId());
        vote.setVotedAt(Instant.now());
        voteRepo.save(vote);
    }

    public PollResponse getPollWithResults(Long pollId) {
        Poll poll = pollRepo.findById(pollId).orElseThrow(/*NotFound*/);
        List<PollOptionResponse> optionResponses = new ArrayList<>();
        for (PollOption opt : poll.getOptions()) {
            long count = voteRepo.countByOption(opt);
            optionResponses.add(new PollOptionResponse(opt.getId(), opt.getText(), count));
        }
        return new PollResponse(poll.getId(), poll.getQuestion(), optionResponses);
    }
}

