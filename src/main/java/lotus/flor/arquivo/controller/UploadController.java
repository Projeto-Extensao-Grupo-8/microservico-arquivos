package lotus.flor.arquivo.controller;

import lotus.flor.arquivo.application.usecase.UploadFileUseCase;
import lotus.flor.arquivo.domain.model.FileUpload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final UploadFileUseCase uploadFileUseCase;

    public UploadController(UploadFileUseCase uploadFileUseCase) {
        this.uploadFileUseCase = uploadFileUseCase;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<String>> upload(
            @RequestParam("file") MultipartFile file) throws Exception {

        FileUpload fileUpload = new FileUpload(
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType()
        );

        return uploadFileUseCase.execute(fileUpload)
                .thenApply(ResponseEntity::ok);
    }
}
