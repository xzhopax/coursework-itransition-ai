package com.dampcave.courseworkitransitionai.amazon;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

public class S3Util {
    private static final String BUCKET = "spring-amazon-img";

    public static void uploadFile(String filename, InputStream inputStream) throws IOException {
        S3Client client = S3Client.builder().build();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(filename)
                .build();

        client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
    }
}
