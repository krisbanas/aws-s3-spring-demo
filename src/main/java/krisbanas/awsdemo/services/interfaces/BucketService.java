package krisbanas.awsdemo.services.interfaces;

import com.amazonaws.services.s3.model.Bucket;

import java.util.List;

public interface BucketService {
    List<Bucket> getAllBuckets();

    Bucket createBucket(String bucketName);

    boolean deleteBucket(String bucketName);
}
