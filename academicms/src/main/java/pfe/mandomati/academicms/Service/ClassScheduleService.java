package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Model.ClassSchedule;
import java.util.List;

public interface ClassScheduleService {
    ClassSchedule saveClassSchedule(ClassSchedule classSchedule);
    List<ClassSchedule> getAllClassSchedules();
    ClassSchedule getClassScheduleById(Long id);
    ClassSchedule updateClassSchedule(ClassSchedule classSchedule);
    void deleteClassSchedule(Long id);
}