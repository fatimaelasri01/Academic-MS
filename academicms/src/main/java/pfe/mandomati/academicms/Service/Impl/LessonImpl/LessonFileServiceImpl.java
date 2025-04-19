package pfe.mandomati.academicms.Service.Impl.LessonImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import pfe.mandomati.academicms.Service.Impl.Utils.FileUtil;
import pfe.mandomati.academicms.Service.LessonService.LessonFileService;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LessonFileServiceImpl implements LessonFileService {


    private final LessonRepository lessonRepository;

    private final LessonFileRepository lessonFileRepository;

    @Value("${file.upload.lesson-directory}")
    private String uploadDirectory;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/jpeg",
            "image/png"
    );

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDirectory));
        } catch (IOException e) {
            log.error("Erreur lors de la création du répertoire de stockage", e);
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
        log.info("Upload de fichier pour la leçon {} - Type: {}", lessonId, type);

        // Validation du type MIME
        if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
            log.warn("Type de fichier non autorisé: {}", file.getContentType());
            throw new IllegalArgumentException("Type de fichier non supporté");
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
        
        try {
            String subDir = switch (type) {
                case LESSON_CONTENT -> "content";
                case TD_CONTENT -> "td";
                case ADDITIONAL_RESOURCE -> "resource";
            };
            String filePath = FileUtil.saveFile(uploadDirectory, file, subDir);
            
            LessonFile lessonFile = new LessonFile();
            lessonFile.setName(file.getOriginalFilename());
            lessonFile.setFilePath(filePath);
            lessonFile.setType(type);
            lessonFile.setLesson(lesson);

            log.info("Fichier enregistré: {}", filePath);
            
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
            FileUtil.deleteFile(lessonFile.getFilePath());
            log.info("Fichier supprimé: {}", lessonFile.getFilePath());
            lessonFileRepository.delete(lessonFile);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource downloadFile(Long lessonId, Long fileId) {
        LessonFile lessonFile = lessonFileRepository.findByIdAndLessonId(fileId, lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "File not found with id: " + fileId + " for lesson id: " + lessonId));
        
        try {
            Path filePath = Paths.get(lessonFile.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                log.error("Fichier inaccessible: {}", filePath);
                throw new RuntimeException("Fichier inaccessible");
            }
            return resource;
        } catch (Exception e) {
            log.error("Erreur lors du téléchargement", e);
            throw new RuntimeException("Erreur de téléchargement", e);
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