//package com.kannect.achieve.utils;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.google.auth.oauth2.ServiceAccountCredentials;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//
//@Component
//public class GcpStorageUploader {
//
//    @Value("${gcp.project-id}")
//    private String projectId;
//
//    @Value("${gcp.bucket-name}")
//    private String bucketName;
//
//    @Value("${gcp.credentials.path}")
//    private String credentialsPath; // Path to your service account JSON file
//
//    public String uploadFile(MultipartFile file, String fileName) throws IOException {
//
//        Storage storage = StorageOptions.newBuilder()
//                .setProjectId(projectId)
//                .setCredentials(ServiceAccountCredentials.fromStream(Files.newInputStream(Paths.get(credentialsPath))))
//                .build()
//                .getService();
//
//        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
//                .setContentType(file.getContentType())
//                .build();
//
//        storage.create(blobInfo, file.getBytes());
//
//        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
//    }
//}





