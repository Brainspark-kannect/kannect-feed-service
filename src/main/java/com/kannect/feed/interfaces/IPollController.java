package com.kannect.feed.interfaces;

import org.springframework.http.ResponseEntity;

import com.kannect.feed.dto.request.PollCreateRequest;
import com.kannect.feed.dto.request.PollVoteRequest;
import com.kannect.feed.dto.response.ErrorResponse;
import com.kannect.feed.dto.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Poll", description = "Poll Management API")
public interface IPollController {

    @Operation(summary = "Create poll", description = "Create a new poll with options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Poll created successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> createPoll(
        @Parameter(description = "Poll data") PollCreateRequest request
    );

    @Operation(summary = "Vote in poll", description = "Cast a vote for a poll option")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vote recorded successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vote request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> vote(
        @Parameter(description = "Poll ID") Long pollId,
        @Parameter(description = "Vote data") PollVoteRequest request
    );

    @Operation(summary = "Get poll results", description = "Retrieve poll question and results")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Poll fetched successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Poll not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getPoll(
        @Parameter(description = "Poll ID") Long id
    );
    
    @Operation(summary = "Get all polls", description = "Retrieve all polls")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Polls retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getAllPolls();
}

