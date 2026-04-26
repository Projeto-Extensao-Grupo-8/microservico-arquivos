package lotus.flor.arquivo.application.port;

import lotus.flor.arquivo.domain.model.FileDownload;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface S3DownloadPort {

    /** Baixa um arquivo pela chave exata no bucket. */
    CompletableFuture<FileDownload> download(String key);

    /** Lista todas as chaves cujo nome contém o termo informado (case-insensitive). */
    CompletableFuture<List<String>> listByName(String term);
}
