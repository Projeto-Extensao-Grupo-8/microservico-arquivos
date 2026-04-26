package lotus.flor.arquivo.controller;

import lotus.flor.arquivo.application.usecase.GetFileByNameUseCase;
import lotus.flor.arquivo.application.usecase.GetProfilePhotoUseCase;
import lotus.flor.arquivo.domain.model.FileDownload;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/arquivos")
public class DownloadController {

    private final GetProfilePhotoUseCase getProfilePhotoUseCase;
    private final GetFileByNameUseCase getFileByNameUseCase;

    public DownloadController(
            GetProfilePhotoUseCase getProfilePhotoUseCase,
            GetFileByNameUseCase getFileByNameUseCase) {
        this.getProfilePhotoUseCase = getProfilePhotoUseCase;
        this.getFileByNameUseCase = getFileByNameUseCase;
    }

    /**
     * GET /arquivos/perfil/{userId}
     * Retorna a foto de perfil do usuário como download direto.
     * Espera um arquivo no bucket com a chave: perfil-{userId}.{ext}
     */
    @GetMapping("/perfil/{userId}")
    public CompletableFuture<ResponseEntity<byte[]>> getProfilePhoto(
            @PathVariable String userId) {

        return getProfilePhotoUseCase.execute(userId)
                .thenApply(this::toResponse)
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    /**
     * GET /arquivos?nome=documento.pdf
     * Busca e retorna um arquivo do bucket pelo nome exato.
     */
    @GetMapping
    public CompletableFuture<ResponseEntity<byte[]>> getFileByName(
            @RequestParam("nome") String nome) {

        return getFileByNameUseCase.execute(nome)
                .thenApply(this::toResponse)
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    // ── helper ───────────────────────────────────────────────────────────────

    private ResponseEntity<byte[]> toResponse(FileDownload file) {
        MediaType mediaType = parseMediaType(file.getContentType());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(mediaType)
                .contentLength(file.getContent().length)
                .body(file.getContent());
    }

    private MediaType parseMediaType(String contentType) {
        try {
            return contentType != null
                    ? MediaType.parseMediaType(contentType)
                    : MediaType.APPLICATION_OCTET_STREAM;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
