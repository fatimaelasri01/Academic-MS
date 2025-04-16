package pfe.mandomati.academicms.Controller.LessonController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pfe.mandomati.academicms.Dto.LessonDto.LessonDto;
import pfe.mandomati.academicms.Service.LessonService.LessonService;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private  LessonService lessonService;

    @PostMapping("/create")
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto) {
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.ok(createdLesson);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        LessonDto lessonDto = lessonService.getLessonById(id);
        return ResponseEntity.ok(lessonDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        List<LessonDto> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        LessonDto updatedLesson = lessonService.updateLesson(id, lessonDto);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}