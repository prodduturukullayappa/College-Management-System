package com.college.repository;

import com.college.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
    Optional<Student> findByStudentId(String studentId);
    List<Student> findByNameContainingIgnoreCaseOrStudentIdContainingIgnoreCase(String name, String studentId);
    boolean existsByDepartmentId(Long departmentId);
}
