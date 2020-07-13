package krisbanas.awsdemo.services.utils;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class S3Utils {

    private S3Utils() {
    }

    public static PutObjectRequest preparePutObjectRequest(MultipartFile multipartFile, String bucketName) throws IOException {
        var inputStream = multipartFile.getInputStream();
        var key = generateResourceKey();
        ObjectMetadata metadata = prepareMetadata();
        return new PutObjectRequest(bucketName, key, inputStream, metadata);
    }

    private static ObjectMetadata prepareMetadata() {
        var metadata = new ObjectMetadata();
        metadata.setContentType("plain/text");
        metadata.addUserMetadata("metadataKey", "metadataValue");
        return metadata;
    }

    public static String generateResourceKey() {
        return UUID.randomUUID().toString();
    }
}
