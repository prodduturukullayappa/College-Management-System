package com.college.repository;

import com.college.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);
    boolean existsByDepartmentId(Long departmentId);
    boolean existsByFacultyId(Long facultyId);
}
