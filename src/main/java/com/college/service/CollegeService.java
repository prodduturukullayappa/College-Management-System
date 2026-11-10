package com.college.service;

import com.college.model.*;
import com.college.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class CollegeService {

    @Autowired private StudentRepository studentRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private ExaminationRepository examinationRepository;
    @Autowired private MarksRepository marksRepository;
    @Autowired private FeeRepository feeRepository;
    @Autowired private FeePaymentRepository feePaymentRepository;

    // --- Dashboard ---
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalFaculty", facultyRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalDepartments", departmentRepository.count());
        stats.put("todayAttendance", attendanceRepository.countByDateAndStatus(LocalDate.now(), "Present"));
        Double pendingFees = feeRepository.sumTotalPendingAmount();
        stats.put("pendingFees", pendingFees != null ? pendingFees : 0.0);
        return stats;
    }

    // --- Department ---
    public List<Department> getAllDepartments() { return departmentRepository.findAll(); }
    public Department getDepartmentById(Long id) { return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found")); }
    public Department saveDepartment(Department dept) {
        if (dept.getId() == null) {
            if (departmentRepository.existsByName(dept.getName())) throw new RuntimeException("Department name already exists");
            if (departmentRepository.existsByCode(dept.getCode())) throw new RuntimeException("Department code already exists");
        }
        return departmentRepository.save(dept);
    }
    public void deleteDepartment(Long id) {
        if (studentRepository.existsByDepartmentId(id) || facultyRepository.existsByDepartmentId(id) || courseRepository.existsByDepartmentId(id)) {
            throw new RuntimeException("Cannot delete department because it is referenced by students, faculty, or courses.");
        }
        departmentRepository.deleteById(id);
    }

    // --- Faculty ---
    public List<Faculty> getAllFaculty() { return facultyRepository.findAll(); }
    public Faculty getFacultyById(Long id) { return facultyRepository.findById(id).orElseThrow(() -> new RuntimeException("Faculty not found")); }
    public Faculty saveFaculty(Faculty faculty) {
        if (faculty.getId() == null) {
            if (facultyRepository.existsByFacultyId(faculty.getFacultyId())) throw new RuntimeException("Faculty ID already exists");
            if (facultyRepository.existsByEmail(faculty.getEmail())) throw new RuntimeException("Email already exists");
        }
        return facultyRepository.save(faculty);
    }
    public void deleteFaculty(Long id) {
        if (courseRepository.existsByFacultyId(id)) {
            throw new RuntimeException("Cannot delete faculty assigned to courses. Unassign them first.");
        }
        facultyRepository.deleteById(id);
    }
    public List<Faculty> searchFaculty(String query) {
        return facultyRepository.findByNameContainingIgnoreCaseOrFacultyIdContainingIgnoreCase(query, query);
    }

    // --- Student ---
    public List<Student> getAllStudents() { return studentRepository.findAll(); }
    public Student getStudentById(Long id) { return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found")); }
    public Student saveStudent(Student student) {
        if (student.getId() == null) {
            if (studentRepository.existsByStudentId(student.getStudentId())) throw new RuntimeException("Student ID already exists");
            if (studentRepository.existsByEmail(student.getEmail())) throw new RuntimeException("Student email already exists");
        }
        return studentRepository.save(student);
    }
    public void deleteStudent(Long id) { studentRepository.deleteById(id); }
    public List<Student> searchStudents(String query) {
        return studentRepository.findByNameContainingIgnoreCaseOrStudentIdContainingIgnoreCase(query, query);
    }

    // --- Course ---
    public List<Course> getAllCourses() { return courseRepository.findAll(); }
    public Course getCourseById(Long id) { return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found")); }
    public Course saveCourse(Course course) {
        if (course.getId() == null && courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException("Course code already exists");
        }
        return courseRepository.save(course);
    }
    public void deleteCourse(Long id) { courseRepository.deleteById(id); }

    // --- Attendance ---
    public List<Attendance> getAllAttendance() { return attendanceRepository.findAll(); }
    public Attendance saveAttendance(Attendance attendance) {
        if (attendance.getId() == null) {
            if (attendanceRepository.existsByStudentIdAndCourseIdAndDate(
                    attendance.getStudent().getId(), attendance.getCourse().getId(), attendance.getDate())) {
                throw new RuntimeException("Attendance already marked for this student, course, and date.");
            }
        }
        return attendanceRepository.save(attendance);
    }
    public List<Attendance> getAttendanceByDate(LocalDate date) { return attendanceRepository.findByDate(date); }
    public Map<String, Object> getStudentAttendancePercentage(Long studentId) {
        long total = attendanceRepository.countByStudentId(studentId);
        long present = attendanceRepository.countByStudentIdAndStatus(studentId, "Present");
        double percentage = total > 0 ? ((double) present / total) * 100.0 : 0.0;
        Map<String, Object> map = new HashMap<>();
        map.put("totalClasses", total);
        map.put("presentClasses", present);
        map.put("percentage", Math.round(percentage * 100.0) / 100.0);
        return map;
    }

    // --- Examination & Marks ---
    public List<Examination> getAllExams() { return examinationRepository.findAll(); }
    public Examination saveExam(Examination exam) { return examinationRepository.save(exam); }
    
    public List<Marks> getAllMarks() { return marksRepository.findAll(); }
    public Marks saveMarks(Marks marks) {
        Examination exam = examinationRepository.findById(marks.getExamination().getId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        
        if (marks.getId() == null && marksRepository.existsByExaminationIdAndStudentId(
                marks.getExamination().getId(), marks.getStudent().getId())) {
            throw new RuntimeException("Marks already submitted for this student in this examination.");
        }

        double pct = ((double) marks.getMarksObtained() / exam.getMaxMarks()) * 100.0;
        marks.setPercentage(Math.round(pct * 100.0) / 100.0);
        marks.setStatus(marks.getMarksObtained() >= exam.getPassMarks() ? "Pass" : "Fail");
        return marksRepository.save(marks);
    }
    public List<Marks> getMarksByStudentId(Long studentId) { return marksRepository.findByStudentId(studentId); }

    // --- Fee Management ---
    public List<Fee> getAllFees() { return feeRepository.findAll(); }
    public Fee saveFee(Fee fee) {
        if (fee.getId() == null) {
            Optional<Fee> existing = feeRepository.findByStudentId(fee.getStudent().getId());
            if (existing.isPresent()) throw new RuntimeException("Fee structure already exists for this student.");
            fee.setPaidAmount(0.0);
            fee.setPendingAmount(fee.getTotalAmount());
            fee.setStatus("Pending");
        } else {
            Fee existing = feeRepository.findById(fee.getId()).orElseThrow(() -> new RuntimeException("Fee record not found"));
            fee.setPendingAmount(fee.getTotalAmount() - existing.getPaidAmount());
            if (fee.getPendingAmount() <= 0) {
                fee.setStatus("Paid");
            } else if (existing.getPaidAmount() > 0) {
                fee.setStatus("Partial");
            } else {
                fee.setStatus("Pending");
            }
        }
        return feeRepository.save(fee);
    }

    public FeePayment recordPayment(Long feeId, Double amount, String paymentMethod, String remarks) {
        Fee fee = feeRepository.findById(feeId).orElseThrow(() -> new RuntimeException("Fee record not found"));
        if (amount > fee.getPendingAmount()) {
            throw new RuntimeException("Payment amount exceeds pending amount (" + fee.getPendingAmount() + ")");
        }

        FeePayment payment = new FeePayment();
        payment.setFee(fee);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(paymentMethod);
        payment.setRemarks(remarks);
        feePaymentRepository.save(payment);

        fee.setPaidAmount(fee.getPaidAmount() + amount);
        fee.setPendingAmount(fee.getTotalAmount() - fee.getPaidAmount());
        if (fee.getPendingAmount() <= 0) {
            fee.setStatus("Paid");
        } else {
            fee.setStatus("Partial");
        }
        feeRepository.save(fee);

        return payment;
    }

    public List<FeePayment> getPaymentsByFeeId(Long feeId) {
        return feePaymentRepository.findByFeeId(feeId);
    }

    public Fee getFeeByStudentCustomId(String studentIdStr) {
        return feeRepository.findByStudentStudentId(studentIdStr)
                .orElseThrow(() -> new RuntimeException("Fee record not found for student code: " + studentIdStr));
    }
}
