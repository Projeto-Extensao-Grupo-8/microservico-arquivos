package lotus.flor.arquivo.application.usecase;

import lotus.flor.arquivo.application.port.FileUploadPublisherPort;
import lotus.flor.arquivo.domain.model.FileUpload;

import java.util.concurrent.CompletableFuture;

public class UploadFileUseCase {

    private final FileUploadPublisherPort publisherPort;

    public UploadFileUseCase(FileUploadPublisherPort publisherPort) {
        this.publisherPort = publisherPort;
    }

    public CompletableFuture<String> execute(FileUpload fileUpload) {

        if (fileUpload.getFileName() == null) {
            throw new IllegalArgumentException("File name required");
        }

        return publisherPort.publish(fileUpload);
    }
}