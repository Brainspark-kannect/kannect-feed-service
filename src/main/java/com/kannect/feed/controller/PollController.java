package com.kannect.feed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kannect.feed.dto.request.PollCreateRequest;
import com.kannect.feed.dto.request.PollVoteRequest;
import com.kannect.feed.dto.response.PollResponse;
import com.kannect.feed.dto.response.SuccessResponse;
import com.kannect.feed.interfaces.IPollController;
import com.kannect.feed.service.impl.PollServiceImpl;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
class PollController implements IPollController {

    private final PollServiceImpl pollService;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
    public ResponseEntity<SuccessResponse> createPoll(@RequestBody PollCreateRequest request) {
        PollResponse created = pollService.createPoll(request);
        SuccessResponse resp = SuccessResponse.builder()
            .statusCode(201)
            .status(HttpStatus.CREATED)
            .message("Poll created successfully")
            .data(created)
            .build();
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/{id}/vote")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SuccessResponse> vote(@PathVariable Long id, @RequestBody PollVoteRequest request) {
        pollService.vote(id, request);
        SuccessResponse resp = SuccessResponse.builder()
            .statusCode(200)
            .status(HttpStatus.OK)
            .message("Vote recorded successfully")
            .build();
        return ResponseEntity.ok(resp);
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SuccessResponse> getPoll(@PathVariable Long id) {
        PollResponse poll = pollService.getPollWithResults(id);
        SuccessResponse resp = SuccessResponse.builder()
            .statusCode(200)
            .status(HttpStatus.OK)
            .message("Poll fetched successfully")
            .data(poll)
            .build();
        return ResponseEntity.ok(resp);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
    public ResponseEntity<SuccessResponse> getAllPolls() {
        // Optional: implement listing if needed
        SuccessResponse resp = SuccessResponse.builder()
            .statusCode(200)
            .status(HttpStatus.OK)
            .message("All polls fetched successfully")
            .build();
        return ResponseEntity.ok(resp);
    }
}

