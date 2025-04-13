package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Dto.AttendanceDto;
import pfe.mandomati.academicms.Dto.AttendanceSummaryDto;
import pfe.mandomati.academicms.Service.AttendanceService;
import pfe.mandomati.academicms.Service.AttendanceSummaryService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceSummaryServiceImpl implements AttendanceSummaryService {

    @Autowired
    private AttendanceService attendanceService;

    @Override
    public List<AttendanceSummaryDto> getSummaryByStudent(Long studentId) {
        // Récupérer toutes les présences pour l'étudiant
        List<AttendanceDto> attendances = attendanceService.getAttendancesByStudentId(studentId);

        // Grouper par année académique
        Map<String, List<AttendanceDto>> groupedByYear = attendances.stream()
                .collect(Collectors.groupingBy(attendance -> attendance.getDate().getYear() + "-" + (attendance.getDate().getYear() + 1)));

        // Calculer les statistiques pour chaque année académique
        return groupedByYear.entrySet().stream().map(entry -> {
            String academicYear = entry.getKey();
            List<AttendanceDto> yearAttendances = entry.getValue();

            int totalAbsent = (int) yearAttendances.stream().filter(a -> "absent".equalsIgnoreCase(a.getStatus())).count();
            int totalLate = (int) yearAttendances.stream().filter(a -> "retard".equalsIgnoreCase(a.getStatus())).count();
            int totalExcused = (int) yearAttendances.stream().filter(AttendanceDto::isExcused).count();

            return new AttendanceSummaryDto(null, studentId.toString(), academicYear, totalAbsent, totalLate, totalExcused, null);
        }).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceSummaryDto> getSummaryByClass(Long classId) {
        // Récupérer toutes les présences pour la classe
        List<AttendanceDto> attendances = attendanceService.getAllAttendances().stream()
                .filter(a -> classId.equals(a.getClassScheduleId()))
                .collect(Collectors.toList());

        // Grouper par étudiant
        Map<Long, List<AttendanceDto>> groupedByStudent = attendances.stream()
                .collect(Collectors.groupingBy(AttendanceDto::getStudentId));

        // Calculer les statistiques pour chaque étudiant
        return groupedByStudent.entrySet().stream().map(entry -> {
            Long studentId = entry.getKey();
            List<AttendanceDto> studentAttendances = entry.getValue();

            int totalAbsent = (int) studentAttendances.stream().filter(a -> "absent".equalsIgnoreCase(a.getStatus())).count();
            int totalLate = (int) studentAttendances.stream().filter(a -> "retard".equalsIgnoreCase(a.getStatus())).count();
            int totalExcused = (int) studentAttendances.stream().filter(AttendanceDto::isExcused).count();

            return new AttendanceSummaryDto(null, studentId.toString(), null, totalAbsent, totalLate, totalExcused, classId);
        }).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceSummaryDto> getTop3ClassesWithMostAbsences() {
        // Récupérer toutes les présences
        List<AttendanceDto> attendances = attendanceService.getAllAttendances();

        // Grouper par classe et calculer le total des absences
        Map<Long, Integer> absencesByClass = attendances.stream()
                .filter(a -> "absent".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.groupingBy(AttendanceDto::getClassScheduleId, Collectors.summingInt(a -> 1)));

        // Trier les classes par nombre d'absences décroissant et prendre les 3 premières
        return absencesByClass.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(entry -> new AttendanceSummaryDto(
                        null,
                        null,
                        null,
                        entry.getValue(), // Total des absences
                        0, // Total des retards (non pertinent ici)
                        0, // Total des excusés (non pertinent ici)
                        entry.getKey() // ID de la classe
                ))
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceSummaryDto getGlobalSummaryByAcademicYear(String academicYear) {
        // Récupérer toutes les présences
        List<AttendanceDto> attendances = attendanceService.getAllAttendances();
    
        // Filtrer par année académique
        List<AttendanceDto> filteredAttendances = attendances.stream()
                .filter(a -> {
                    int year = a.getDate().getYear();
                    String yearRange = year + "-" + (year + 1);
                    return yearRange.equals(academicYear);
                })
                .collect(Collectors.toList());
    
        // Calculer les statistiques
        int totalAbsent = (int) filteredAttendances.stream().filter(a -> "absent".equalsIgnoreCase(a.getStatus())).count();
        int totalLate = (int) filteredAttendances.stream().filter(a -> "retard".equalsIgnoreCase(a.getStatus())).count();
        int totalExcused = (int) filteredAttendances.stream().filter(AttendanceDto::isExcused).count();
    
        return new AttendanceSummaryDto(null, null, academicYear, totalAbsent, totalLate, totalExcused, null);
    }

    @Override
    public List<AttendanceSummaryDto> getTopAbsentStudents() {
        // Récupérer toutes les présences
        List<AttendanceDto> attendances = attendanceService.getAllAttendances();
    
        // Grouper par étudiant et calculer le total des absences
        Map<Long, Integer> absencesByStudent = attendances.stream()
                .filter(a -> "absent".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.groupingBy(AttendanceDto::getStudentId, Collectors.summingInt(a -> 1)));
    
        // Trier les étudiants par nombre d'absences décroissant et prendre les 3 premiers
        return absencesByStudent.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(entry -> new AttendanceSummaryDto(
                        null,
                        entry.getKey().toString(),
                        null,
                        entry.getValue(), // Total des absences
                        0, // Total des retards (non pertinent ici)
                        0, // Total des excusés (non pertinent ici)
                        null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceSummaryDto> getSummaryBySubject(Long subjectId) {
        // Récupérer toutes les présences
        List<AttendanceDto> attendances = attendanceService.getAllAttendances();
    
        // Filtrer par matière
        List<AttendanceDto> filteredAttendances = attendances.stream()
                .filter(a -> subjectId.equals(a.getClassScheduleId())) // Supposons que classScheduleId représente la matière
                .collect(Collectors.toList());
    
        // Grouper par étudiant
        Map<Long, List<AttendanceDto>> groupedByStudent = filteredAttendances.stream()
                .collect(Collectors.groupingBy(AttendanceDto::getStudentId));
    
        // Calculer les statistiques pour chaque étudiant
        return groupedByStudent.entrySet().stream().map(entry -> {
            Long studentId = entry.getKey();
            List<AttendanceDto> studentAttendances = entry.getValue();
    
            int totalAbsent = (int) studentAttendances.stream().filter(a -> "absent".equalsIgnoreCase(a.getStatus())).count();
            int totalLate = (int) studentAttendances.stream().filter(a -> "retard".equalsIgnoreCase(a.getStatus())).count();
            int totalExcused = (int) studentAttendances.stream().filter(AttendanceDto::isExcused).count();
    
            return new AttendanceSummaryDto(null, studentId.toString(), null, totalAbsent, totalLate, totalExcused, null);
        }).collect(Collectors.toList());
    }


    @Override
    public List<AttendanceSummaryDto> getSummaryByTeacher(Long teacherId) {
        // Récupérer toutes les présences
        List<AttendanceDto> attendances = attendanceService.getAllAttendances();
    
        // Filtrer par enseignant (supposons que teacherId est lié à classScheduleId)
        List<AttendanceDto> filteredAttendances = attendances.stream()
                .filter(a -> teacherId.equals(a.getClassScheduleId())) // Supposons que classScheduleId représente l'enseignant
                .collect(Collectors.toList());
    
        // Grouper par classe
        Map<Long, List<AttendanceDto>> groupedByClass = filteredAttendances.stream()
                .collect(Collectors.groupingBy(AttendanceDto::getClassScheduleId));
    
        // Calculer les statistiques pour chaque classe
        return groupedByClass.entrySet().stream().map(entry -> {
            Long classId = entry.getKey();
            List<AttendanceDto> classAttendances = entry.getValue();
    
            int totalAbsent = (int) classAttendances.stream().filter(a -> "absent".equalsIgnoreCase(a.getStatus())).count();
            int totalLate = (int) classAttendances.stream().filter(a -> "retard".equalsIgnoreCase(a.getStatus())).count();
            int totalExcused = (int) classAttendances.stream().filter(AttendanceDto::isExcused).count();
    
            return new AttendanceSummaryDto(null, null, null, totalAbsent, totalLate, totalExcused, classId);
        }).collect(Collectors.toList());
    }
}