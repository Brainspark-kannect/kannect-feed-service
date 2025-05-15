package com.kannect.feed.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kannect.feed.dto.request.PollCreateRequest;
import com.kannect.feed.dto.request.PollVoteRequest;
import com.kannect.feed.dto.response.PollOptionResponse;
import com.kannect.feed.dto.response.PollResponse;
import com.kannect.feed.entity.Poll;
import com.kannect.feed.entity.PollOption;
import com.kannect.feed.entity.PollVote;
import com.kannect.feed.exception.ResourceNotFoundException;
import com.kannect.feed.repository.PollOptionRepository;
import com.kannect.feed.repository.PollRepository;
import com.kannect.feed.repository.PollVoteRepository;
import com.kannect.user.auth.repository.UserRepository;

@Service
public class PollServiceImpl {
    @Autowired private PollRepository pollRepo;
    @Autowired private PollOptionRepository optionRepo;
    @Autowired private PollVoteRepository voteRepo;
    @Autowired private UserRepository userRepo;

    public PollResponse createPoll(PollCreateRequest req) {
        Poll poll = new Poll();
        poll.setQuestion(req.getQuestion());
        poll.setCreatedBy(req.getCreatedBy());
        poll.setCreatedAt(Instant.now());
        poll = pollRepo.save(poll);

        // Save options
        List<PollOptionResponse> optionResponses = new ArrayList<>();
        for (String optText : req.getOptions()) {
            PollOption opt = new PollOption();
            opt.setText(optText);
            opt.setPoll(poll);
            opt = optionRepo.save(opt);
            optionResponses.add(new PollOptionResponse(opt.getId(), opt.getText(), 0, new ArrayList<>()));
        }

        return PollResponse.builder()
            .pollId(poll.getId())
            .question(poll.getQuestion())
            .createdBy(poll.getCreatedBy())
            .creatorName(getUserName(poll.getCreatedBy()))
            .createdAt(poll.getCreatedAt())
            .options(optionResponses)
            .votedByUsers(new ArrayList<>())
            .build();
    }

    public void vote(Long pollId, PollVoteRequest req) {
        // Find option
        PollOption option = optionRepo.findById(req.getOptionId())
            .orElseThrow(() -> new ResourceNotFoundException("Poll option not found with id: " + req.getOptionId()));
            
        // Check if user has already voted
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
        Poll poll = pollRepo.findById(pollId)
            .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + pollId));
            
        List<PollOptionResponse> optionResponses = new ArrayList<>();
        for (PollOption opt : poll.getOptions()) {
            long count = voteRepo.countByOption(opt);
            List<Long> voters = voteRepo.findVoterIdsByOption(opt);
            optionResponses.add(new PollOptionResponse(opt.getId(), opt.getText(), count, voters));
        }
        
        // Get all voters for this poll
        List<Long> allVoters = voteRepo.findVoterIdsByPollId(pollId);
        
        return PollResponse.builder()
            .pollId(poll.getId())
            .question(poll.getQuestion())
            .createdBy(poll.getCreatedBy())
            .creatorName(getUserName(poll.getCreatedBy()))
            .createdAt(poll.getCreatedAt())
            .options(optionResponses)
            .votedByUsers(allVoters)
            .build();
    }

    public List<PollResponse> getAllPolls() {
        return pollRepo.findAll().stream()
            .map(poll -> {
                List<PollOptionResponse> optionResponses = new ArrayList<>();
                for (PollOption opt : poll.getOptions()) {
                    long count = voteRepo.countByOption(opt);
                    List<Long> voters = voteRepo.findVoterIdsByOption(opt);
                    optionResponses.add(new PollOptionResponse(opt.getId(), opt.getText(), count, voters));
                }
                
                // Get all voters for this poll
                List<Long> allVoters = voteRepo.findVoterIdsByPollId(poll.getId());
                
                return PollResponse.builder()
                    .pollId(poll.getId())
                    .question(poll.getQuestion())
                    .createdBy(poll.getCreatedBy())
                    .creatorName(getUserName(poll.getCreatedBy()))
                    .createdAt(poll.getCreatedAt())
                    .options(optionResponses)
                    .votedByUsers(allVoters)
                    .build();
            })
            .collect(Collectors.toList());
    }

    private String getUserName(Long userId) {
        return userRepo.findById(userId)
            .map(user -> user.getFirstName() + " " + user.getLastName())
            .orElse("Unknown User");
    }
}

