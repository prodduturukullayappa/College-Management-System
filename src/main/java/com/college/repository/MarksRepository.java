package com.college.repository;

import com.college.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    boolean existsByExaminationIdAndStudentId(Long examId, Long studentId);
    List<Marks> findByStudentId(Long studentId);
    List<Marks> findByExaminationId(Long examId);
    Optional<Marks> findByExaminationIdAndStudentId(Long examId, Long studentId);
}
