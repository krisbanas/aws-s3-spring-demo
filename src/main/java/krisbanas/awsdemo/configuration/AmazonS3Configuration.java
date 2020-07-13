package krisbanas.awsdemo.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class AmazonS3Configuration {

    private final AmazonS3ConfigurationProperties s3Config;

    @Autowired
    public AmazonS3Configuration(AmazonS3ConfigurationProperties s3Config) {
        this.s3Config = s3Config;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public AmazonS3 amazonS3() {
        var credentialsProvider = createCredentialsProvider();
        var endpointConfiguration = createEndpointConfiguration();

        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(endpointConfiguration)
                .withPathStyleAccessEnabled(true)
                .build();
    }

    private EndpointConfiguration createEndpointConfiguration() {
        var region = s3Config.getRegion();
        var endpoint = s3Config.getEndpoint();
        return new EndpointConfiguration(endpoint, region);
    }

    private AWSStaticCredentialsProvider createCredentialsProvider() {
        var accessKey = s3Config.getAccessKey();
        var secretKey = s3Config.getSecretKey();
        var credentials = new BasicAWSCredentials(accessKey, secretKey);
        return new AWSStaticCredentialsProvider(credentials);
    }
}
