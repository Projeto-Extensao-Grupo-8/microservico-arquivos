package lotus.flor.arquivo.application.usecase;

import lotus.flor.arquivo.application.port.S3StoragePort;
import lotus.flor.arquivo.domain.model.FileUpload;

import java.util.concurrent.CompletableFuture;

public class UploadFileUseCase {

    private final S3StoragePort storagePort;

    public UploadFileUseCase(S3StoragePort storagePort) {
        this.storagePort = storagePort;
    }

    public CompletableFuture<String> execute(FileUpload fileUpload) {

        if (fileUpload.getFileName() == null) {
            throw new IllegalArgumentException("File name required");
        }

        return storagePort.upload(fileUpload);
    }
}
