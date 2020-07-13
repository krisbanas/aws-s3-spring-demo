# AWS S3 SpringBoot Demo
A demonstration of integration between AWS Simple Storage Service and a Java SpringBoot application.

#### Description:
A Gradle project containing a SpringBoot application, 
which integrates to a local Amazon S3 instance. 
The application exposes several endpoints that allow 
to perform a set of operations on the S3.

#### Implementation:
In order to create a local S3 instance, a [MinIO container](https://min.io/) is used.
The container can be easily run using the docker-compose tool and is configured in the following way:
- Supports path-style requests
- Is available on _localhost:9000_
- Uses default credentials (_minioadmin:minioadmin_)

#### Quickstart
1. Clone the repository
    
    `git clone https://github.com/krisbanas/aws-s3-spring-demo.git`

2. Build the project
    
    `gradlew build`

3. Run the docker-compose cluster in the root folder to set up minio container

    `docker-compose up`
    
4. Interact with the SpringBoot application using endpoints

#### Endpoints
The REST API is documented using Swagger tool on the root context.
The endpoints are:

##### Buckets
- GET /buckets - list all buckets
- POST /buckets/{bucketName} - create a bucket with a given name
- DELETE /buckets/{bucketName} - delete a bucket with a given name
##### Resources
- POST /bucket/{bucketName}/file - store a file in a bucket. The UUID of the file is returned.
- POST /bucket/{bucketName}/file-partial - store a big file (over 1MB). The UUID of the file is returned.
- GET /bucket/{bucketName}/file/{resourceId} - get a stored resource from bucket using its UUID
- GET /bucket/{bucketName}/file/{resourceId} - delete a stored resource from bucket using its UUID

#### Conclusion
The project can be used to kick-start Java SpringBoot projects integrating with AWS S3.
Thank You for your time checking it out!