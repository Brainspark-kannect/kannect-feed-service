//package com.kannect.achieve.utils;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//@Component
//public class S3Uploader {
//
//	@Value("${aws.s3.bucket-name}")
//	private String bucketName;
//
//	@Value("${aws.access-key}")
//	private String accessKey;
//
//	@Value("${aws.secret-key}")
//	private String secretKey;
//
//	@Value("${aws.region}")
//	private String region;
//
//	public String uploadFile(MultipartFile file,String fileName) throws IOException {
//
//		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
//
//		S3Client s3Client = S3Client.builder()
//		    .region(Region.of(region))
//		    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//		    .build();
//
//
//		s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName).acl(ObjectCannedACL.PUBLIC_READ)
//				.contentType(file.getContentType()).build(), RequestBody.fromBytes(file.getBytes()));
//
//		return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
//	}
//
//}





