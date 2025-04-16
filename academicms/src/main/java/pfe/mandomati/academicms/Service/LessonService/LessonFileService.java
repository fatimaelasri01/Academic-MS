package pfe.mandomati.academicms.Service.LessonService;

import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import pfe.mandomati.academicms.Dto.LessonDto.FileInfoDto;
import pfe.mandomati.academicms.Model.Lesson.LessonFile;

public interface LessonFileService {
    Set<FileInfoDto> getFilesByLessonId(Long lessonId);
    FileInfoDto addFileToLesson(Long lessonId, MultipartFile file, LessonFile.FileType type);
    void removeFileFromLesson(Long lessonId, Long fileId);
    Resource downloadFile(Long lessonId, Long fileId);
}
