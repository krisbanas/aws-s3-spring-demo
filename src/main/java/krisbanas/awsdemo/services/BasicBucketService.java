package krisbanas.awsdemo.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import krisbanas.awsdemo.exceptions.BucketException;
import krisbanas.awsdemo.services.interfaces.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BasicBucketService implements BucketService {

    private final AmazonS3 amazonS3;

    public BasicBucketService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    @Override
    public Bucket createBucket(String bucketName) {
        throwExceptionIfBucketDoesNotExist(bucketName);
        Bucket newBucket = amazonS3.createBucket(bucketName);
        log.info("Created bucket: {}", newBucket);
        return newBucket;
    }

    @Override
    public boolean deleteBucket(String bucketName) {
        if (amazonS3.doesBucketExistV2(bucketName)) return deleteBucketWithContents(bucketName);
        log.warn("Cannot delete bucket {}. Bucket does not exists.", bucketName);
        return false;
    }

    private boolean deleteBucketWithContents(String bucketName) {
        deleteAllResourcesFromBucket(bucketName);
        amazonS3.deleteBucket(bucketName);
        log.info("Deleted bucket: {}", bucketName);
        return true;
    }

    private void deleteAllResourcesFromBucket(String bucketName) {
        var resourcesInBucket = amazonS3.listObjectsV2(bucketName);
        for (var objectSummary : resourcesInBucket.getObjectSummaries())
            amazonS3.deleteObject(bucketName, objectSummary.getKey());
    }

    private void throwExceptionIfBucketDoesNotExist(String bucketName) {
        if (amazonS3.doesBucketExistV2(bucketName)) {
            log.warn("Cannot create bucket {}. Bucket already exists.", bucketName);
            throw new BucketException("Bucket already exists");
        }
    }
}
