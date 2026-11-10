package com.college.repository;

import com.college.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByStudentIdAndCourseIdAndDate(Long studentId, Long courseId, LocalDate date);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByCourseIdAndDate(Long courseId, LocalDate date);
    long countByStudentIdAndStatus(Long studentId, String status);
    long countByStudentId(Long studentId);
    long countByDateAndStatus(LocalDate date, String status);
}
