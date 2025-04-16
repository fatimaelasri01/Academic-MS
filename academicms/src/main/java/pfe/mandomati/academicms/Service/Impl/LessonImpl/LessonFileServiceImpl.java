package pfe.mandomati.academicms.Service.Impl.LessonImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import pfe.mandomati.academicms.Dto.LessonDto.FileInfoDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Lesson.Lesson;
import pfe.mandomati.academicms.Model.Lesson.LessonFile;
import pfe.mandomati.academicms.Repository.LessonRepo.LessonFileRepository;
import pfe.mandomati.academicms.Repository.LessonRepo.LessonRepository;
import pfe.mandomati.academicms.Service.LessonService.LessonFileService;

@Service
@Transactional
public class LessonFileServiceImpl implements LessonFileService {


    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonFileRepository lessonFileRepository;

    @Value("${file.upload.lesson-directory}")
    private String uploadDirectory;

    { 
        // Create upload directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(uploadDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Set<FileInfoDto> getFilesByLessonId(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
        return lesson.getFiles().stream()
                .map(this::mapFileEntityToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public FileInfoDto addFileToLesson(Long lessonId, MultipartFile file, LessonFile.FileType type) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
        
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);
            Files.copy(file.getInputStream(), filePath);
            
            LessonFile lessonFile = new LessonFile();
            lessonFile.setName(file.getOriginalFilename());
            lessonFile.setFilePath(filePath.toString());
            lessonFile.setType(type);
            lessonFile.setLesson(lesson);
            
            LessonFile savedFile = lessonFileRepository.save(lessonFile);
            return mapFileEntityToDto(savedFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void removeFileFromLesson(Long lessonId, Long fileId) {
        LessonFile lessonFile = lessonFileRepository.findByIdAndLessonId(fileId, lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "File not found with id: " + fileId + " for lesson id: " + lessonId));
        
        try {
            Files.deleteIfExists(Paths.get(lessonFile.getFilePath()));
            lessonFileRepository.delete(lessonFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource downloadFile(Long lessonId, Long fileId) {
        LessonFile lessonFile = lessonFileRepository.findByIdAndLessonId(fileId, lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "File not found with id: " + fileId + " for lesson id: " + lessonId));
        
        try {
            Path filePath = Paths.get(lessonFile.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private FileInfoDto mapFileEntityToDto(LessonFile lessonFile) {
        FileInfoDto fileInfoDto = new FileInfoDto();
        fileInfoDto.setId(lessonFile.getId());
        fileInfoDto.setName(lessonFile.getName());
        fileInfoDto.setType(lessonFile.getType());
        fileInfoDto.setDownloadUrl("/api/lessons/" + lessonFile.getLesson().getId() + 
                                 "/files/" + lessonFile.getId() + "/download");
        return fileInfoDto;
    }
}