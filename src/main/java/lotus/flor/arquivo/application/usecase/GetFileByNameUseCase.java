package lotus.flor.arquivo.application.usecase;

import lotus.flor.arquivo.application.port.S3DownloadPort;
import lotus.flor.arquivo.domain.model.FileDownload;

import java.util.concurrent.CompletableFuture;

/**
 * Busca um arquivo no bucket pelo nome exato.
 */
public class GetFileByNameUseCase {

    private final S3DownloadPort downloadPort;

    public GetFileByNameUseCase(S3DownloadPort downloadPort) {
        this.downloadPort = downloadPort;
    }

    public CompletableFuture<FileDownload> execute(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName é obrigatório");
        }

        return downloadPort.download(fileName)
                .exceptionally(ex -> {
                    throw new IllegalArgumentException(
                            "Arquivo não encontrado: " + fileName, ex);
                });
    }
}
