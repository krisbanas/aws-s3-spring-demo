package krisbanas.awsdemo.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmazonS3Service {

    private final AmazonS3 amazonS3;

    public AmazonS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }
}
