package com.college.repository;

import com.college.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    boolean existsByFacultyId(String facultyId);
    boolean existsByEmail(String email);
    List<Faculty> findByNameContainingIgnoreCaseOrFacultyIdContainingIgnoreCase(String name, String facultyId);
    boolean existsByDepartmentId(Long departmentId);
}
