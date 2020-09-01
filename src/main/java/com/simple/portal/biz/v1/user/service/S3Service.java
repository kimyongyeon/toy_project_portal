package com.simple.portal.biz.v1.user.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.simple.portal.biz.v1.user.exception.DeleteProfileImgFailedException;
import com.simple.portal.biz.v1.user.exception.UploadProfileImgFailedException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@NoArgsConstructor
@Slf4j
public class S3Service {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private String endPoint = "ewr1.vultrobjects.com";

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, this.region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion(this.region)
                .build();
    }

    public String upload(String userId, MultipartFile file) throws IOException {
        String fileName = userId + "-profile.jpg";

        try {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return s3Client.getUrl(bucket, fileName).toString();
        } catch (AmazonS3Exception e) {
            log.error("[S3Service] AmazonS3Exception upload Error : " + e.getMessage());
            throw new UploadProfileImgFailedException();
        } catch (SdkClientException e) {
            log.error("[S3Service] SdkClientException upload Error : " + e.getMessage());
            throw new UploadProfileImgFailedException();
        }
    }

    public void deleteImg(String objectName) {
        try {
            s3Client.deleteObject(bucket, objectName);
        } catch (AmazonS3Exception e) {
            log.error("[S3Service] AmazonS3Exception delete Error : " + e.getMessage());
            throw new DeleteProfileImgFailedException();
        } catch (SdkClientException e) {
            log.error("[S3Service] SdkClientException delete Error : " + e.getMessage());
            throw new DeleteProfileImgFailedException();
        }
    }
}
