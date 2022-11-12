package me.taling.live.global.configurations;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.chime.AmazonChimeAsync;
import com.amazonaws.services.chime.AmazonChimeAsyncClientBuilder;
import com.amazonaws.services.ecs.AmazonECSAsync;
import com.amazonaws.services.ecs.AmazonECSAsyncClientBuilder;
import com.amazonaws.services.ivs.AmazonIVSAsync;
import com.amazonaws.services.ivs.AmazonIVSAsyncClientBuilder;
import me.taling.live.infra.aws.ivs.IvsConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AmazonConfiguration {
    private final Logger log = LoggerFactory.getLogger(AmazonConfiguration.class);

    @Value("${AWS_ACCESS_KEY}")
    private String accessKey;
    @Value("${AWS_SECRET_KEY}")
    private String secretKey;

    private AWSCredentials awsCredentials;

    @PostConstruct
    public void init() {
        awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    @Bean
    public AmazonChimeAsync amazonChime() {
        return AmazonChimeAsyncClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(awsCredentialsProvider())
                .build();
    }

    @Bean
    public AmazonECSAsync amazonECS() {
        return AmazonECSAsyncClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(awsCredentialsProvider())
                .build();
    }

    @Bean
    public AmazonIVSAsync amazonIVS() {
        return AmazonIVSAsyncClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(awsCredentialsProvider())
                .build();
    }

    @ConfigurationProperties(prefix = "aws.ivs")
    @Bean
    public IvsConfigurationProperties ivsConfigurationProperties() {
        return new IvsConfigurationProperties();
    }
}