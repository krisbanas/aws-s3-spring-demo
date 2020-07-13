package krisbanas.awsdemo.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "s3")
public class AmazonS3ConfigurationProperties {

    @NonNull
    private String endpoint;

    @NonNull
    private String region;

    @NonNull
    private String accessKey;

    @NonNull
    private String secretKey;
}
