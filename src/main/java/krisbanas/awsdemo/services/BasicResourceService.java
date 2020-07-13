package krisbanas.awsdemo.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.util.IOUtils;
import krisbanas.awsdemo.exceptions.BucketException;
import krisbanas.awsdemo.exceptions.ResourceExceptionException;
import krisbanas.awsdemo.services.interfaces.ResourceService;
import krisbanas.awsdemo.services.utils.FileUtils;
import krisbanas.awsdemo.services.utils.S3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class BasicResourceService implements ResourceService {

    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    @Autowired
    public BasicResourceService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
    }

    @Override
    public String storeResourceToBucket(MultipartFile file, String bucketName) throws IOException {
        if (!amazonS3.doesBucketExistV2(bucketName)) throw new BucketException("Bucket does not exist");
        FileUtils.validateFile(file, bucketName);
        var request = S3Utils.preparePutObjectRequest(file, bucketName);
        amazonS3.putObject(request);
        log.info("Stored new file with id {} in bucket {}", request.getKey(), bucketName);
        return request.getKey();
    }

    @Override
    public String storeResourceStreamed(MultipartFile file, String bucketName) throws IOException, InterruptedException {
        if (!amazonS3.doesBucketExistV2(bucketName)) throw new BucketException("Bucket does not exist");
        FileUtils.validateFile(file, bucketName);
        String key = storeFileAndWait(file, bucketName);
        log.info("Stored new file with id {} in bucket {}", key, bucketName);
        return key;
    }

    @Override
    public Resource getResource(String bucketName, String resourceId) throws IOException {
        var getObjectRequest = new GetObjectRequest(bucketName, resourceId);
        var fullObject = tryFetchingResource(getObjectRequest);
        var byteArray = IOUtils.toByteArray(fullObject.getObjectContent());
        log.info("Fetched a file with id {} from bucket {}", resourceId, bucketName);
        return new ByteArrayResource(byteArray);
    }

    @Override
    public boolean deleteResource(String bucketName, String resourceId) {
        try {
            amazonS3.getObject(bucketName, resourceId);
        } catch (AmazonS3Exception e) {
            return false;
        }
        amazonS3.deleteObject(bucketName, resourceId);
        return true;
    }

    private String storeFileAndWait(MultipartFile file, String bucketName) throws IOException, InterruptedException {
        var key = S3Utils.generateResourceKey();
        File newFile = FileUtils.convertMultipartFileToFile(file);
        var upload = transferManager.upload(bucketName, key, newFile);
        upload.waitForCompletion();
        newFile.delete();
        return key;
    }

    private S3Object tryFetchingResource(GetObjectRequest getObjectRequest) {
        try {
            return amazonS3.getObject(getObjectRequest);
        } catch (AmazonS3Exception ex) {
            throw new ResourceExceptionException("No resource found");
        }
    }
}
