package lotus.flor.arquivo.infrastructure.aws;

import lotus.flor.arquivo.application.port.S3StoragePort;
import lotus.flor.arquivo.domain.model.FileUpload;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.concurrent.CompletableFuture;

public class S3StorageAdapter implements S3StoragePort {

    private final S3AsyncClient s3Client;
    private final String bucket;

    public S3StorageAdapter(S3AsyncClient s3Client, String bucket) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    @Override
    public CompletableFuture<String> upload(FileUpload file) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(file.getFileName())
                .contentType(file.getContentType())
                .build();

        return s3Client.putObject(
                request,
                AsyncRequestBody.fromBytes(file.getContent())
        ).thenApply(resp -> file.getFileName());

    }
}
