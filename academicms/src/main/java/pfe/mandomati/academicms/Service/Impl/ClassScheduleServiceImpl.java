package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Model.ClassSchedule;
import pfe.mandomati.academicms.Repository.ClassScheduleRepository;
import pfe.mandomati.academicms.Service.ClassScheduleService;

import java.util.List;
import java.util.Optional;

@Service
public class ClassScheduleServiceImpl implements ClassScheduleService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Override
    public ClassSchedule saveClassSchedule(ClassSchedule classSchedule) {
        return classScheduleRepository.save(classSchedule);
    }

    @Override
    public List<ClassSchedule> getAllClassSchedules() {
        return classScheduleRepository.findAll();
    }

    @Override
    public ClassSchedule getClassScheduleById(Long id) {
        Optional<ClassSchedule> optionalClassSchedule = classScheduleRepository.findById(id);
        return optionalClassSchedule.orElse(null);
    }

    @Override
    public ClassSchedule updateClassSchedule(ClassSchedule classSchedule) {
        return classScheduleRepository.save(classSchedule);
    }

    @Override
    public void deleteClassSchedule(Long id) {
        classScheduleRepository.deleteById(id);
    }
}