package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.TrashDTO;
import com.sortifytodoapp_backend.Exception.FileStorageException;

import com.sortifytodoapp_backend.Model.File;
import com.sortifytodoapp_backend.Model.Trash;
import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Repository.FileRepository;
import com.sortifytodoapp_backend.Repository.TrashRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrashService {

    @Autowired
    private UserService userService;
    @Autowired
    private TrashRepository trashRepository;

    @Autowired
    private FileRepository fileRepository;

    private final Path storageLocation = Path.of("C:/Projects/Sortifytodoapp_backend/src/main/resources/static/MemoFiles");
    private final Path trashLocation = Path.of("C:/Projects/Sortifytodoapp_backend/src/main/resources/static/Trash");
    private TrashDTO mapFileToDTO(Trash trash) {
        TrashDTO trashDTO = new TrashDTO();
        trashDTO.setId(trash.getId());
        trashDTO.setFileName(trash.getFileName());
        trashDTO.setFileType(trash.getFileType());
        trashDTO.setUploadDate(trash.getUploadDate());
        return trashDTO;
    }

    //this logic get trash file data from backend
    public List<TrashDTO> getFilesByUserId(int userId) {
        try {
            User user = userService.getUserById(userId);
            List<Trash> userFiles = trashRepository.findByUser(user);

            return userFiles.stream()
                    .map(this::mapFileToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Handle exceptions appropriately
            return List.of();
        }
    }

    //this logic use for get file content using trashLocation saved files
    public Resource getFileContentById(int id) {
        try {
            Trash trash = trashRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("File not found"));

            // Implement logic to load file content from storage and return it as a Resource
            Path filePath = trashLocation.resolve(trash.getFileName());
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

    //this logic use for if user want to restore deleted file
    //this logic pass file data to file table and file pass to storageLocation again
    public void restoredeletedfile(int trashId) {
        try {
            Trash trash = trashRepository.findById(trashId)
                    .orElseThrow(() -> new NotFoundException("File not found"));

            // Save the file data to the Trash table
            com.sortifytodoapp_backend.Model.File file = new com.sortifytodoapp_backend.Model.File();
            file.setUser(trash.getUser());
            file.setFileName(trash.getFileName());
            file.setFileType(trash.getFileType());
            file.setUploadDate(trash.getUploadDate());
            fileRepository.save(file);

            // Move the file to the Trash directory
            Path sourcePath = trashLocation.resolve(trash.getFileName());
            Path targetPath = storageLocation.resolve(trash.getFileName());
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Delete the file from the original table
            trashRepository.delete(trash);
        } catch (Exception e) {
            throw new FileStorageException("Error moving file to trash", e);
        }
    }
}
