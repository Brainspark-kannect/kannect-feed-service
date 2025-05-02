package com.kannect.feed.dto.response;

import org.springframework.http.HttpStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SuccessResponse {

	@NotNull
	@NotEmpty
	private Integer statusCode;

	@NotNull
	@NotEmpty
	private HttpStatus status;

	@NotNull
	@NotEmpty
	private String message;

	@NotEmpty
	private Object data;
}