package lotus.flor.arquivo.infrastructure.aws;

import lotus.flor.arquivo.application.port.S3DownloadPort;
import lotus.flor.arquivo.domain.model.FileDownload;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class S3DownloadAdapter implements S3DownloadPort {

    private final S3AsyncClient s3Client;
    private final String bucket;

    public S3DownloadAdapter(S3AsyncClient s3Client, String bucket) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    @Override
    public CompletableFuture<FileDownload> download(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3Client.getObject(request, AsyncResponseTransformer.toBytes())
                .thenApply(response -> {
                    String contentType = response.response().contentType();
                    byte[] bytes = response.asByteArray();
                    return new FileDownload(key, bytes, contentType);
                });
    }

    @Override
    public CompletableFuture<List<String>> listByName(String term) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .build();

        String termLower = term.toLowerCase(Locale.ROOT);

        return s3Client.listObjectsV2(request)
                .thenApply(response ->
                        response.contents().stream()
                                .map(obj -> obj.key())
                                .filter(key -> key.toLowerCase(Locale.ROOT).contains(termLower))
                                .collect(Collectors.toList())
                );
    }
}
