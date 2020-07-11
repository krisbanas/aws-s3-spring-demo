package krisbanas.awsdemo.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Configuration {

    private final AmazonS3ConfigurationProperties s3Config;

    @Autowired
    public AmazonS3Configuration(AmazonS3ConfigurationProperties s3Config) {
        this.s3Config = s3Config;
    }

    @Bean
    public AWSCredentialsProvider credentialsProvider() {
        var accessKey = s3Config.getAccessKey();
        var secretKey = s3Config.getSecretKey();
        var credentials = new BasicAWSCredentials(accessKey, secretKey);

        return new AWSStaticCredentialsProvider(credentials);
    }

    @Bean
    public EndpointConfiguration endpointConfiguration() {
        var region = s3Config.getRegion();
        var endpoint = s3Config.getEndpoint();

        return new EndpointConfiguration(endpoint, region);
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider())
                .withEndpointConfiguration(endpointConfiguration())
                .build();
    }
}
