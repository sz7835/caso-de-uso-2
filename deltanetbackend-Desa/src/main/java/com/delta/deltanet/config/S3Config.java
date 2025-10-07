package com.delta.deltanet.config;

import com.delta.deltanet.models.entity.SisParam;
import com.delta.deltanet.models.service.SisParamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Autowired
    private SisParamServiceImpl sisParamService;

    @Bean
    public S3Client s3Client() {
        Region region = Region.US_EAST_1;

        // 1) Try DB params
        String accessKeyId = null;
        String secretAccessKey = null;

        SisParam p1 = sisParamService.buscaEtiqueta("S3_ACCESS_KEY_ID");
        if (p1 != null && p1.getValor() != null && !p1.getValor().isEmpty()) {
            accessKeyId = p1.getValor();
        }

        SisParam p2 = sisParamService.buscaEtiqueta("S3_SECRET_ACCESS_KEY");
        if (p2 != null && p2.getValor() != null && !p2.getValor().isEmpty()) {
            secretAccessKey = p2.getValor();
        }

        // 2) Fallback to env vars
        if (isEmpty(accessKeyId))  accessKeyId  = System.getenv("AWS_ACCESS_KEY_ID");
        if (isEmpty(secretAccessKey)) secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        // 3) Choose creds provider
        AwsCredentialsProvider provider;
        if (!isEmpty(accessKeyId) && !isEmpty(secretAccessKey)) {
            AwsCredentials creds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            provider = StaticCredentialsProvider.create(creds);
        } else {
            // Last-resort: default chain (profile, instance role, etc.)
            provider = DefaultCredentialsProvider.create();
        }

        return S3Client.builder()
                .region(region)
                .credentialsProvider(provider)
                .build();
    }

    private static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
