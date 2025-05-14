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
import com.kannect.feed.dto.request.FeedRequest;
import com.kannect.feed.dto.request.ReactionRequest;
import com.kannect.feed.dto.response.FeedResponse;
import com.kannect.feed.dto.response.SuccessResponse;
import com.kannect.feed.interfaces.IFeedController;
import com.kannect.feed.service.impl.FeedServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
public class FeedController implements IFeedController{

	private final FeedServiceImpl feedServiceImpl;
	public static final Logger LOGGER = LoggerFactory.getLogger(FeedController.class);

	@Override
	@GetMapping
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> getAllFeeds() {
		List<FeedResponse> feeds = feedServiceImpl.getAllFeeds();
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("All feeds fetched successfully").data(feeds).build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> getFeedById(@PathVariable Long id) {
		FeedResponse feed = feedServiceImpl.getFeedById(id);
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Feed fetched successfully").data(feed).build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@GetMapping("/funfriday")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> getFunFridayFeeds() {
		List<FeedResponse> feeds = feedServiceImpl.getFunFridayFeeds();
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Fun Friday feeds fetched successfully").data(feeds).build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@PostMapping
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> createFeed(@RequestPart("feed") String requestStr,
			@RequestPart(value = "file", required = false) MultipartFile file) throws IOException{
		ObjectMapper objectmapper = new ObjectMapper();
		FeedRequest request = new FeedRequest();
		try {
			request = objectmapper.readValue(requestStr, FeedRequest.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error mapping json String to FeedRequest while adding");
		}
		FeedResponse created = feedServiceImpl.createFeed(request, file);
		SuccessResponse resp = SuccessResponse.builder().statusCode(201).status(HttpStatus.CREATED)
				.message("Feed created successfully").data(created).build();
		return new ResponseEntity<>(resp, HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> updateFeed(@PathVariable Long id, @RequestBody FeedRequest request) {
		FeedResponse updated = feedServiceImpl.updateFeed(id, request);
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Feed updated successfully").data(updated).build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> deleteFeed(@PathVariable Long id) {
		feedServiceImpl.deleteFeed(id);
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Feed deleted successfully").build();
		return ResponseEntity.ok(resp);
	}

	@Override
	@PostMapping("/{id}/reaction")
	@PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
	public ResponseEntity<SuccessResponse> reactToFeed(@PathVariable Long id, @RequestBody ReactionRequest reaction) {
		feedServiceImpl.reactToFeed(id, reaction.getUserId(), reaction.isLiked());
		SuccessResponse resp = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Feed reaction recorded successfully").build();
		return ResponseEntity.ok(resp);
	}
}
