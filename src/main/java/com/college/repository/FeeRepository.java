package com.college.repository;

import com.college.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    Optional<Fee> findByStudentId(Long studentId);
    Optional<Fee> findByStudentStudentId(String studentId);
    
    @Query("SELECT SUM(f.pendingAmount) FROM Fee f")
    Double sumTotalPendingAmount();
}
