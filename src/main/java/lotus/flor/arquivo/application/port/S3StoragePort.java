package lotus.flor.arquivo.application.port;

import lotus.flor.arquivo.domain.model.FileUpload;

import java.util.concurrent.CompletableFuture;

public interface S3StoragePort {

    CompletableFuture<String> upload(FileUpload file);

}
