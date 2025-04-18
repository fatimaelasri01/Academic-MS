package pfe.mandomati.academicms.Controller.LessonController;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pfe.mandomati.academicms.Dto.LessonDto.FileInfoDto;
import pfe.mandomati.academicms.Model.Lesson.LessonFile;
import pfe.mandomati.academicms.Service.LessonService.LessonFileService;

import java.io.IOException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons/{lessonId}/files")
public class LessonFileController {

    private final LessonFileService lessonFileService;

    @GetMapping
    public ResponseEntity<Set<FileInfoDto>> getLessonFiles(@PathVariable Long lessonId) {
        Set<FileInfoDto> files = lessonFileService.getFilesByLessonId(lessonId);
        return ResponseEntity.ok(files);
    }

    @PostMapping
    public ResponseEntity<FileInfoDto> addFileToLesson(
            @PathVariable Long lessonId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") LessonFile.FileType type) {
        FileInfoDto fileInfoDto = lessonFileService.addFileToLesson(lessonId, file, type);
        return ResponseEntity.ok(fileInfoDto);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> removeFileFromLesson(
            @PathVariable Long lessonId,
            @PathVariable Long fileId) {
        lessonFileService.removeFileFromLesson(lessonId, fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long lessonId,
            @PathVariable Long fileId) throws IOException {
        
        Resource resource = lessonFileService.downloadFile(lessonId, fileId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
