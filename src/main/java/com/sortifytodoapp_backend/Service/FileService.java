package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.FileDTO;
import com.sortifytodoapp_backend.Exception.FileStorageException;
import com.sortifytodoapp_backend.Model.File;
import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Path;


@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserService userService;

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
            // Change the file storage path according to your setup
            Path filePath = Path.of("C:/Projects/Sortifytodoapp_backend/src/main/resources/static/MemoFiles", fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Error saving file to storage", e);
        }
    }

    private FileDTO mapFileToDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(file.getFileName());
        fileDTO.setFileType(file.getFileType());
        fileDTO.setUploadDate(file.getUploadDate());
        return fileDTO;
    }

    public List<FileDTO> getFilesByUserId(int userId) throws ChangeSetPersister.NotFoundException {
        try {
            User user = userService.getUserById(userId);
            List<File> userFiles = fileRepository.findByUser(user);

            return userFiles.stream()
                    .map(this::mapFileToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ChangeSetPersister.NotFoundException();
        }
    }
}
