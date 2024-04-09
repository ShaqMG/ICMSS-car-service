//package org.icmss.icmsscarservice.service;
//
//import com.levani.storageservice.exceptions.MyCustomException;
//import com.levani.storageservice.model.enums.MyStatusCodes;
//import io.minio.MinioClient;
//import io.minio.PostPolicy;
//import io.minio.PutObjectArgs;
//import io.minio.errors.*;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import okhttp3.*;
//import org.apache.http.entity.ContentType;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.security.InvalidKeyException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.time.ZonedDateTime;
//import java.util.*;
//
//import static org.apache.http.entity.ContentType.*;
//
//@Service
//@RequiredArgsConstructor
//public class MinioService {
//
//    private final Environment env;
//    private final MinioClient minioClient;
//    private final OkHttpClient client;
//
//    @SneakyThrows
//    public String uploadAndGetPath(String fileName) {
//        ContentType contentType = getContentType(fileName);
//        if (!List.of(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_SVG.getMimeType()).contains(contentType.getMimeType()))
//        {
//            throw new MyCustomException(MyStatusCodes.BAD_REQUEST, "Content Type not allowed : " + contentType.getMimeType());
//        }
//        String bucketName = env.getProperty("minio.storage.bucket");
//        String name = hashInput(fileName);
//        if (bucketName == null) {
//            throw new MyCustomException(MyStatusCodes.INTERNAL_SERVER_ERROR);
//        }
//
//        Request request =
//                new Request.Builder()
//                        .url(String.format("%s:%s/%s",env.getProperty("minio.host.url"),env.getProperty("minio.host.port"),bucketName))
//                        .post(createMultiPartBody(contentType,name,bucketName,fileName))
//                        .build();
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) {
//            throw new MyCustomException(MyStatusCodes.BAD_REQUEST, response.message());
//        }
//        return String.format("%s/%s", bucketName, name);
//    }
//
//    private MultipartBody createMultiPartBody(ContentType contentType,String name,String bucketName,String file) throws Exception{
//        PostPolicy policy = new PostPolicy(bucketName, ZonedDateTime.now().plusSeconds(7));
//        policy.addEqualsCondition("key", name);
//        policy.addStartsWithCondition("Content-Type", contentType.getMimeType());
//        policy.addContentLengthRangeCondition( 1024, 20 * 1024 * 1024);
//        Map<String, String> formData = minioClient.getPresignedPostFormData(policy);
//        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
//        multipartBuilder.setType(MultipartBody.FORM);
//        for (Map.Entry<String, String> entry : formData.entrySet()) {
//            multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
//        }
//        multipartBuilder.addFormDataPart("key", name);
//        multipartBuilder.addFormDataPart("Content-Type", contentType.getMimeType());
//
////
//        byte[] decode = extractByteArr(file);
//        multipartBuilder.addFormDataPart(
//                "file", name, RequestBody.create(decode, MediaType.get(contentType.getMimeType())));
//
//        return multipartBuilder.build();
//    }
//
//    private ContentType getContentType(String fileName) {
//        String mimeType = fileName.substring(fileName.indexOf(':') + 1, fileName.indexOf(';'));
//        return ContentType.create(mimeType);
//    }
//
//    private byte[] extractByteArr(String fileName) {
//
//        String imageString = fileName.split(",")[1];
//        ;
//        return Base64.getDecoder().decode(imageString);
//    }
//
//    private String hashInput(String fileName) throws NoSuchAlgorithmException {
//        String extension = fileName.substring(fileName.indexOf("/") + 1, fileName.indexOf(";"));
//        SecureRandom secureRandom = new SecureRandom();
//        int i = secureRandom.nextInt(99999, 999999);
//        MessageDigest md = MessageDigest.getInstance("SHA-1");
//        byte[] digest = md.digest(String.valueOf(i).getBytes());
//        BigInteger no = new BigInteger(1, digest);
//
//        // Convert message digest into hex value
//        StringBuilder hashtext = new StringBuilder(no.toString(16));
//
//        while (hashtext.length() < 32) {
//            hashtext.insert(0, "0");
//        }
//        return hashtext + "." + extension;
//    }
//}
