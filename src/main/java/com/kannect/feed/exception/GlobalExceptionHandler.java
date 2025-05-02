package com.kannect.feed.exception;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.kannect.feed.dto.response.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
@ResponseStatus
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ex.printStackTrace();
		StringBuilder errorString = new StringBuilder();
		List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

		validationErrorList.forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String validationMsg = error.getDefaultMessage();
			errorString.append(fieldName).append("=").append(validationMsg).append("; ");
		});
		ErrorResponse message = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
				errorString.toString());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException exception,
			HttpHeaders headers, HttpStatusCode status, WebRequest webRequest) {
		exception.printStackTrace();
		ErrorResponse errorResponseDTO = new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
				exception.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception,
			WebRequest request) {
		exception.printStackTrace();
		ErrorResponse errorResponseDTO = new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,
				exception.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception,
			WebRequest request) {
		exception.printStackTrace();
		Map<NodeImpl, String> errorMessage = new HashMap<>();
		for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
			errorMessage.put(((PathImpl) violation.getPropertyPath()).getLeafNode(), violation.getMessage());
		}
		ErrorResponse errorResponseDTO = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
				errorMessage.toString());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException exception, WebRequest request) {
		exception.printStackTrace();
		Map<String, String> errorMessage = new HashMap<>();
		errorMessage.put(exception.getPropertyName(), exception.getMessage());
		ErrorResponse errorResponseDTO = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
				errorMessage.toString());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception,
			WebRequest request) {
		exception.printStackTrace();
		ErrorResponse errorResponseDTO = new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT,
				exception.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest webRequest) {
		exception.printStackTrace();
		ErrorResponse errorResponseDTO = new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
				exception.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception, WebRequest request) {
		exception.printStackTrace();
		ErrorResponse message = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
	}

	@ExceptionHandler(LoginFailedException.class)
	public ResponseEntity<ErrorResponse> handleLoginFailedException(LoginFailedException exception,
			WebRequest webRequest) {
		exception.printStackTrace();
		ErrorResponse errorResponseDTO = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED,
				exception.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(RequestValidationFailedException.class)
	public ResponseEntity<ErrorResponse> handleRequestValidationFailedException(
			RequestValidationFailedException exception, WebRequest webRequest) {
		ErrorResponse errorResponseDTO = new ErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT,
				exception.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
	}


}
