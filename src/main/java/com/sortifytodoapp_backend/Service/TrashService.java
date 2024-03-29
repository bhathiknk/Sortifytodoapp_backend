package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.TrashDTO;
import com.sortifytodoapp_backend.Exception.FileStorageException;

import com.sortifytodoapp_backend.Model.Trash;
import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Repository.TrashRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrashService {

    @Autowired
    private UserService userService;
    @Autowired
    private TrashRepository trashRepository;
    private final Path trashLocation = Path.of("C:/Projects/Sortifytodoapp_backend/src/main/resources/static/Trash");
    private TrashDTO mapFileToDTO(Trash trash) {
        TrashDTO trashDTO = new TrashDTO();
        trashDTO.setId(trash.getId());
        trashDTO.setFileName(trash.getFileName());
        trashDTO.setFileType(trash.getFileType());
        trashDTO.setUploadDate(trash.getUploadDate());
        return trashDTO;
    }

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
}
