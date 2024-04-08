package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.FileDTO;
import com.sortifytodoapp_backend.Exception.FileStorageException;
import com.sortifytodoapp_backend.Model.File;
import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Repository.FileRepository;
import com.sortifytodoapp_backend.Repository.TrashRepository;
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

    @Autowired
    private TrashRepository trashRepository;

    //this path use for save and retrieve memofiles
    private final Path storageLocation = Path.of("C:/Projects/Sortifytodoapp_backend/src/main/resources/static/MemoFiles");

    //this file path use for save and retrive deleted files
    private final Path trashLocation = Path.of("C:/Projects/Sortifytodoapp_backend/src/main/resources/static/Trash");


    //this logic make using api get file and file details save to database
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

    //this logic make to save file to assigened file path
    private void saveFileToStorage(MultipartFile file, String fileName) {
        try {
            Path filePath = storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Error saving file to storage", e);
        }
    }

    //this use for get file data as parameter
    private FileDTO mapFileToDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(file.getId());
        fileDTO.setFileName(file.getFileName());
        fileDTO.setFileType(file.getFileType());
        fileDTO.setUploadDate(file.getUploadDate());
        return fileDTO;
    }

    //this logic get all file details from database with using mapFileToDTO logic and pass to frontend
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

    //this logic get specific file using storageLocation path and using the file Id and pass that data to frontend to open file
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

    //this logic use for if user delete file that file data pass to trash table and also pass save file to trashLocation
    public void moveFileToTrash(int fileId) {
        try {
            File file = fileRepository.findById(fileId)
                    .orElseThrow(() -> new NotFoundException("File not found"));

            // Save the file data to the Trash table
            com.sortifytodoapp_backend.Model.Trash trash = new com.sortifytodoapp_backend.Model.Trash();
            trash.setUser(file.getUser());
            trash.setFileName(file.getFileName());
            trash.setFileType(file.getFileType());
            trash.setUploadDate(file.getUploadDate());
            trashRepository.save(trash);

            // Move the file to the Trash directory
            Path sourcePath = storageLocation.resolve(file.getFileName());
            Path targetPath = trashLocation.resolve(file.getFileName());
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Delete the file from the original table
            fileRepository.delete(file);
        } catch (Exception e) {
            throw new FileStorageException("Error moving file to trash", e);
        }
    }

}
