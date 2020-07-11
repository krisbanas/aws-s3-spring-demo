# aws-s3-spring-demo
A demonstration of integration between AWS S3 and SpringBoot.

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

3. Run the docker-compose cluster in the root folder

    `docker-compose up`
    
4. Interact with the SpringBoot application using endpoints

#### Endpoints

TODO