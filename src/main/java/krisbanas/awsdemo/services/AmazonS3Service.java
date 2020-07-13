package krisbanas.awsdemo.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.util.IOUtils;
import krisbanas.awsdemo.exceptions.ResourceExceptionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class AmazonS3Service {

    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    @Autowired
    public AmazonS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
    }

    public String storeResourceToBucket(MultipartFile file, String bucketName) throws IOException {
        validateFile(file, bucketName);
        var request = preparePutObjectRequest(file, bucketName);
        amazonS3.putObject(request);
        log.info("Stored new file with id {} in bucket {}", request.getKey(), bucketName);
        return request.getKey();
    }

    public String storeResourceStreamed(MultipartFile file, String bucketName) throws IOException, InterruptedException {
        validateFile(file, bucketName);
        String key = storeFileAndWait(file, bucketName);
        log.info("Stored new file with id {} in bucket {}", key, bucketName);
        return key;
    }

    private String storeFileAndWait(MultipartFile file, String bucketName) throws IOException, InterruptedException {
        var key = generateResourceKey();
        File newFile = convertMultipartFileToFile(file);
        var upload = transferManager.upload(bucketName, key, newFile);
        upload.waitForCompletion();
        return key;
    }

    public Resource getResource(String bucketName, String resourceId) throws IOException {
        var getObjectRequest = new GetObjectRequest(bucketName, resourceId);
        var fullObject = tryFetchingResource(getObjectRequest);
        var byteArray = IOUtils.toByteArray(fullObject.getObjectContent());
        log.info("Fetched a file with id {} from bucket {}", resourceId, bucketName);
        return new ByteArrayResource(byteArray);
    }

    public boolean deleteResource(String bucketName, String resourceId) {
        try {
            amazonS3.getObject(bucketName, resourceId);
        } catch (AmazonS3Exception e) {
            return false;
        }
        amazonS3.deleteObject(bucketName, resourceId);
        return true;
    }

    private S3Object tryFetchingResource(GetObjectRequest getObjectRequest) {
        try {
            return amazonS3.getObject(getObjectRequest);
        } catch (AmazonS3Exception ex) {
            throw new ResourceExceptionException("No resource found");
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        var newFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        file.getInputStream().transferTo(new FileOutputStream(newFile));
        return newFile;
    }

    private PutObjectRequest preparePutObjectRequest(MultipartFile multipartFile, String bucketName) throws IOException {
        var inputStream = multipartFile.getInputStream();
        var key = generateResourceKey();
        ObjectMetadata metadata = prepareMetadata();
        return new PutObjectRequest(bucketName, key, inputStream, metadata);
    }

    private void validateFile(MultipartFile multipartFile, String bucketName) {
        if (multipartFile == null) {
            log.warn("No file to store in bucket {}", bucketName);
            throw new ResourceExceptionException("No file to store sent");
        }
        if (multipartFile.isEmpty()) {
            log.warn("The file to store is empty. Ignoring...");
            throw new ResourceExceptionException("Empty file sent");
        }
    }

    private ObjectMetadata prepareMetadata() {
        var metadata = new ObjectMetadata();
        metadata.setContentType("plain/text");
        metadata.addUserMetadata("metadataKey", "metadataValue");
        return metadata;
    }

    private String generateResourceKey() {
        return UUID.randomUUID().toString();
    }
}
