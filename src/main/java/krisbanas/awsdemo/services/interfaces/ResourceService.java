package krisbanas.awsdemo.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ResourceService {
    String storeResourceToBucket(MultipartFile file, String bucketName) throws IOException;

    String storeResourceStreamed(MultipartFile file, String bucketName) throws IOException, InterruptedException;

    Resource getResource(String bucketName, String resourceId) throws IOException;

    boolean deleteResource(String bucketName, String resourceId);
}
