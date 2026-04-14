package lotus.flor.arquivo.application.port;

import lotus.flor.arquivo.domain.model.FileUpload;

import java.util.concurrent.CompletableFuture;

public interface FileUploadPublisherPort {

    CompletableFuture<String> publish(FileUpload fileUpload);

}