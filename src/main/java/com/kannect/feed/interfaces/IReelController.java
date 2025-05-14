package com.kannect.feed.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.kannect.feed.dto.request.ReactionRequest;
import com.kannect.feed.dto.request.ReelRequest;
import com.kannect.feed.dto.response.ErrorResponse;
import com.kannect.feed.dto.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reel", description = "Reels Management API")
public interface IReelController {

    @Operation(summary = "Get all reels", description = "Retrieve all reel posts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reels retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getAllReels();

    @Operation(summary = "Get reel by ID", description = "Retrieve a reel post by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reel retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reel not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getReelById(
        @Parameter(description = "Reel ID") Long id
    );

    @Operation(summary = "Create reel", description = "Create a new reel post with media")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reel created successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> createReel(
        @Parameter(description = "Reel data") String request,
        @Parameter(description = "Reel video file") MultipartFile file
    ) throws Exception;

    @Operation(summary = "Update reel", description = "Update an existing reel post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reel updated successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reel not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> updateReel(
        @Parameter(description = "Reel ID to update") Long id,
        @Parameter(description = "Updated reel data") ReelRequest request
    );

    @Operation(summary = "Delete reel", description = "Delete a reel post by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reel deleted successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reel not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> deleteReel(
        @Parameter(description = "Reel ID to delete") Long id
    );

    @Operation(summary = "React to reel", description = "Like or dislike a reel post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reaction recorded successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reel not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> reactToReel(
        @Parameter(description = "Reel ID to react to") Long id,
        @Parameter(description = "Reaction data") ReactionRequest request
    );
}
