package com.kannect.feed.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kannect.feed.dto.request.ReactionRequest;
import com.kannect.feed.dto.request.ReelRequest;
import com.kannect.feed.dto.response.ReelResponse;
import com.kannect.feed.dto.response.SuccessResponse;
import com.kannect.feed.interfaces.IReelController;
import com.kannect.feed.service.impl.ReelServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
class ReelController implements IReelController {

	private final ReelServiceImpl reelService;
	public static final Logger LOGGER = LoggerFactory.getLogger(ReelController.class);

	@Override
	@GetMapping
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> getAllReels() {
		List<ReelResponse> reels = reelService.getAllReels();
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("All reels fetched successfully").data(reels).build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> getReelById(@PathVariable Long id) {
		ReelResponse reel = reelService.getReel(id);
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Reel fetched successfully").data(reel).build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@PostMapping
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> createReel(@RequestPart("reel")  String requestStr,
			@RequestPart("file") MultipartFile videoFile) throws IOException {
		ObjectMapper objectmapper = new ObjectMapper();
		ReelRequest request = new ReelRequest();
		try {
			request = objectmapper.readValue(requestStr, ReelRequest.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error mapping json String to FeedRequest while adding");
		}
		ReelResponse created = reelService.createReel(request, videoFile);
		SuccessResponse resp = SuccessResponse.builder().statusCode(201).status(HttpStatus.CREATED)
				.message("Reel created successfully").data(created).build();
		return new ResponseEntity<>(resp, HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> updateReel(@PathVariable Long id, @RequestBody ReelRequest request) {
		ReelResponse updated = reelService.updateReel(id, request);
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Reel updated successfully").data(updated).build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> deleteReel(@PathVariable Long id) {
		reelService.deleteReel(id);
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Reel deleted successfully").build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@PostMapping("/{id}/reaction")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> reactToReel(
			@PathVariable Long id, 
			@org.springframework.web.bind.annotation.RequestBody ReactionRequest reaction) {
		LOGGER.info("Received reel reaction request: reelId={}, request={}", id, reaction);
		if (reaction.getUserId() == null) {
			LOGGER.error("userId is null in reaction request");
			throw new IllegalArgumentException("userId is required");
		}
		reelService.reactToReel(id, reaction);
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Reel reaction recorded successfully").build();
		return ResponseEntity.ok(resp);
	}
}
