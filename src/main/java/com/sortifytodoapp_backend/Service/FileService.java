package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.FileDTO;
import com.sortifytodoapp_backend.Exception.FileStorageException;
import com.sortifytodoapp_backend.Model.File;
import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Repository.FileRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserService userService;

    private final Path storageLocation = Path.of("C:/Projects/Sortifytodoapp_backend/src/main/resources/static/MemoFiles");

    public FileDTO saveFile(MultipartFile file, int userId) {
        try {
            User user = userService.getUserById(userId);

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileType = file.getContentType();

            File newFile = new File();
            newFile.setUser(user);
            newFile.setFileName(fileName);
            newFile.setFileType(fileType);
            newFile.setUploadDate(String.valueOf(LocalDateTime.now()));

            // Save file information to the database
            File savedFile = fileRepository.save(newFile);

            // Save the file to your storage location (e.g., local disk, cloud storage)
            saveFileToStorage(file, savedFile.getFileName());

            return mapFileToDTO(savedFile);
        } catch (Exception e) {
            throw new FileStorageException("Error uploading file", e);
        }
    }

    private void saveFileToStorage(MultipartFile file, String fileName) {
        // Implement the logic to save the file to the specified storage location
        try {
            Path filePath = storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Error saving file to storage", e);
        }
    }

    private FileDTO mapFileToDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(file.getId());
        fileDTO.setFileName(file.getFileName());
        fileDTO.setFileType(file.getFileType());
        fileDTO.setUploadDate(file.getUploadDate());
        return fileDTO;
    }

    public List<FileDTO> getFilesByUserId(int userId) {
        try {
            User user = userService.getUserById(userId);
            List<File> userFiles = fileRepository.findByUser(user);

            return userFiles.stream()
                    .map(this::mapFileToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Handle exceptions appropriately
            return List.of();
        }
    }
    public Resource getFileContentById(int id) {
        try {
            File file = fileRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("File not found"));

            // Implement logic to load file content from storage and return it as a Resource
            Path filePath = storageLocation.resolve(file.getFileName());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new NotFoundException("File not found");
            }
        } catch (Exception e) {
            throw new FileStorageException("Error fetching file content", e);
        }
    }

}
