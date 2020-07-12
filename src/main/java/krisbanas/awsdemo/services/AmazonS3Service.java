package krisbanas.awsdemo.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import krisbanas.awsdemo.exceptions.CreateBucketException;
import krisbanas.awsdemo.exceptions.ResourceExceptionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AmazonS3Service {

    private final AmazonS3 amazonS3;

    public AmazonS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    public Bucket createBucket(String bucketName) {
        if (amazonS3.doesBucketExistV2(bucketName)) {
            log.warn("Cannot create bucket {}. Bucket already exists.", bucketName);
            throw new CreateBucketException("Bucket already exists");
        }
        Bucket newBucket = amazonS3.createBucket(bucketName);
        log.info("Created bucket: {}", newBucket);
        return newBucket;
    }

    public boolean deleteBucket(String bucketName) {
        if (amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.deleteBucket(bucketName);
            log.info("Deleted bucket: {}", bucketName);
            return true;
        }
        log.warn("Cannot delete bucket {}. Bucket does not exists.", bucketName);
        return false;
    }

    public String storeResourceToBucket(MultipartFile multipartFile, String bucketName) throws IOException {
        validateFile(multipartFile, bucketName);
        var request = preparePutObjectRequest(multipartFile, bucketName);
        amazonS3.putObject(request);
        log.info("Stored new file with id {} in bucket {}", request.getKey(), bucketName);
        return request.getKey();
    }

    public Resource getResource(String resourceId, String bucketName) throws IOException {
        var getObjectRequest = new GetObjectRequest(bucketName, resourceId);
        var fullObject = amazonS3.getObject(getObjectRequest);
        var byteArray = IOUtils.toByteArray(fullObject.getObjectContent());
        log.info("Fetched a file with id {} from bucket {}", resourceId, bucketName);
        return new ByteArrayResource(byteArray);
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
