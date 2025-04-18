package pfe.mandomati.academicms.Service.Impl.AttendanceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Dto.AttendanceDto.ClassSessionDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Attendance.ClassSession;
import pfe.mandomati.academicms.Repository.AttendanceRepo.ClassSessionRepository;
import pfe.mandomati.academicms.Service.AttendanceService.ClassSessionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassSessionServiceImpl implements ClassSessionService {

    private final ClassSessionRepository classSessionRepository;

    @Override
    public ClassSessionDto createClassSession(ClassSessionDto classSessionDto) {
        ClassSession classSession = new ClassSession();
        classSession.setClassId(classSessionDto.getClassId());
        classSession.setTeacherId(classSessionDto.getTeacherId());
        classSession.setSubjectId(classSessionDto.getSubjectId());
        classSession.setSessionDate(classSessionDto.getSessionDate());
        classSession.setStartTime(classSessionDto.getStartTime());
        classSession.setEndTime(classSessionDto.getEndTime());

        classSession = classSessionRepository.save(classSession);
        return entityToDto(classSession);
    }

    @Override
    public ClassSessionDto updateClassSession(Long id, ClassSessionDto classSessionDto) {
        ClassSession classSession = classSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession not found with id " + id));

        classSession.setClassId(classSessionDto.getClassId());
        classSession.setTeacherId(classSessionDto.getTeacherId());
        classSession.setSubjectId(classSessionDto.getSubjectId());
        classSession.setSessionDate(classSessionDto.getSessionDate());
        classSession.setStartTime(classSessionDto.getStartTime());
        classSession.setEndTime(classSessionDto.getEndTime());

        classSession = classSessionRepository.save(classSession);
        return entityToDto(classSession);
    }

    @Override
    public void deleteClassSession(Long id) {
        ClassSession classSession = classSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession not found with id " + id));
        classSessionRepository.delete(classSession);
    }

    @Override
    public ClassSessionDto getClassSessionById(Long id) {
        ClassSession classSession = classSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession not found with id " + id));
        return entityToDto(classSession);
    }

    @Override
    public List<ClassSessionDto> getAllClassSessions() {
        List<ClassSession> classSessions = classSessionRepository.findAll();
        return classSessions.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private ClassSessionDto entityToDto(ClassSession classSession) {
        return new ClassSessionDto(
                classSession.getId(),
                classSession.getClassId(),
                classSession.getTeacherId(),
                classSession.getSubjectId(),
                classSession.getSessionDate(),
                classSession.getStartTime(),
                classSession.getEndTime()
        );
    }
}