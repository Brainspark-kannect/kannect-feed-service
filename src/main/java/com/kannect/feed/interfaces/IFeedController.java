package com.kannect.feed.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.kannect.feed.dto.request.FeedRequest;
import com.kannect.feed.dto.request.ReactionRequest;
import com.kannect.feed.dto.response.ErrorResponse;
import com.kannect.feed.dto.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feed", description = "Feed Management API")
public interface IFeedController {

    @Operation(summary = "Get all feeds", description = "Retrieve all feed posts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feeds retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getAllFeeds();

    @Operation(summary = "Get feed by ID", description = "Retrieve a feed post by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feed retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Feed not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getFeedById(
        @Parameter(description = "Feed ID") Long id
    );

    @Operation(summary = "Get Fun Friday feeds", description = "Retrieve feed posts tagged as Fun Friday")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fun Friday feeds retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getFunFridayFeeds();

    @Operation(summary = "Create feed", description = "Create a new feed post with optional media")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Feed created successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> createFeed(
        @Parameter(description = "Feed data") String request,
        @Parameter(description = "Feed media file") MultipartFile file
    ) throws Exception;

    @Operation(summary = "Update feed", description = "Update an existing feed post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feed updated successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Feed not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> updateFeed(
        @Parameter(description = "Feed ID to update") Long id,
        @Parameter(description = "Updated feed data") FeedRequest request
    );

    @Operation(summary = "Delete feed", description = "Delete a feed post by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feed deleted successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Feed not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> deleteFeed(
        @Parameter(description = "Feed ID to delete") Long id
    );

    @Operation(summary = "React to feed", description = "Like or dislike a feed post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reaction recorded successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Feed not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> reactToFeed(
        @Parameter(description = "Feed ID to react to") Long id,
        @Parameter(description = "Reaction data") ReactionRequest request
    );
}