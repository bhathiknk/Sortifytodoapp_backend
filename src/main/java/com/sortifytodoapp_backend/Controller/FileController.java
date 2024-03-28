package com.sortifytodoapp_backend.Controller;

import com.sortifytodoapp_backend.DTO.FileDTO;
import com.sortifytodoapp_backend.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

// FileController.java
@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") int userId) {
        try {
            FileDTO uploadedFile = fileService.saveFile(file, userId);
            return ResponseEntity.ok(uploadedFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileDTO>> getFilesByUserId(@PathVariable int userId) throws ChangeSetPersister.NotFoundException {
        List<FileDTO> userFiles = fileService.getFilesByUserId(userId);
        return new ResponseEntity<>(userFiles, HttpStatus.OK);
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<Resource> getFileContentById(@PathVariable int id,
                                                       HttpServletResponse response) {
        try {
            Resource fileContent = fileService.getFileContentById(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + fileContent.getFilename())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileContent.contentLength())
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable int id) {
        try {
            fileService.moveFileToTrash(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
