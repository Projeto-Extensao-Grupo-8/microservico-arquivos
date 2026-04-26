package lotus.flor.arquivo.application.usecase;

import lotus.flor.arquivo.application.port.S3DownloadPort;
import lotus.flor.arquivo.domain.model.FileDownload;

import java.util.concurrent.CompletableFuture;

/**
 * Busca a foto de perfil de um usuário.
 * Convenção de nome no bucket: "perfil-{userId}.{ext}"
 * O use case lista as chaves que começam com o prefixo e retorna a primeira correspondência.
 */
public class GetProfilePhotoUseCase {

    private static final String PREFIX = "perfil-";

    private final S3DownloadPort downloadPort;

    public GetProfilePhotoUseCase(S3DownloadPort downloadPort) {
        this.downloadPort = downloadPort;
    }

    public CompletableFuture<FileDownload> execute(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId é obrigatório");
        }

        String key = PREFIX + userId;

        return downloadPort.listByName(key)
                .thenCompose(keys -> {
                    if (keys.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Foto de perfil não encontrada para o usuário: " + userId);
                    }
                    // Pega a primeira correspondência (mais recente ou única)
                    return downloadPort.download(keys.get(0));
                });
    }
}
