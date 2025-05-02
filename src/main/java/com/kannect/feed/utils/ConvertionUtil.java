package com.kannect.feed.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConvertionUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static List<String> convertObjectToListOfStrings(Object obj) {
		return objectMapper.convertValue(obj, new TypeReference<List<String>>() {
		});
	}
}
