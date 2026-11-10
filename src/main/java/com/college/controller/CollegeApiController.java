package com.college.controller;

import com.college.model.*;
import com.college.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CollegeApiController {

    @Autowired
    private CollegeService collegeService;

    // --- Dashboard ---
    @GetMapping("/reports/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(collegeService.getDashboardStats());
    }

    // --- Department API ---
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(collegeService.getAllDepartments());
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(collegeService.getDepartmentById(id));
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department dept) {
        return ResponseEntity.ok(collegeService.saveDepartment(dept));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department dept) {
        dept.setId(id);
        return ResponseEntity.ok(collegeService.saveDepartment(dept));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Map<String, String>> deleteDepartment(@PathVariable Long id) {
        collegeService.deleteDepartment(id);
        return ResponseEntity.ok(Map.of("message", "Department deleted successfully"));
    }

    // --- Faculty API ---
    @GetMapping("/faculty")
    public ResponseEntity<List<Faculty>> getAllFaculty(@RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(collegeService.searchFaculty(search.trim()));
        }
        return ResponseEntity.ok(collegeService.getAllFaculty());
    }

    @GetMapping("/faculty/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        return ResponseEntity.ok(collegeService.getFacultyById(id));
    }

    @PostMapping("/faculty")
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(collegeService.saveFaculty(faculty));
    }

    @PutMapping("/faculty/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        faculty.setId(id);
        return ResponseEntity.ok(collegeService.saveFaculty(faculty));
    }

    @DeleteMapping("/faculty/{id}")
    public ResponseEntity<Map<String, String>> deleteFaculty(@PathVariable Long id) {
        collegeService.deleteFaculty(id);
        return ResponseEntity.ok(Map.of("message", "Faculty deleted successfully"));
    }

    // --- Student API ---
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents(@RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(collegeService.searchStudents(search.trim()));
        }
        return ResponseEntity.ok(collegeService.getAllStudents());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(collegeService.getStudentById(id));
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(collegeService.saveStudent(student));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        return ResponseEntity.ok(collegeService.saveStudent(student));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        collegeService.deleteStudent(id);
        return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));
    }

    // --- Course API ---
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(collegeService.getAllCourses());
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(collegeService.getCourseById(id));
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(collegeService.saveCourse(course));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        return ResponseEntity.ok(collegeService.saveCourse(course));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Map<String, String>> deleteCourse(@PathVariable Long id) {
        collegeService.deleteCourse(id);
        return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));
    }

    // --- Attendance API ---
    @GetMapping("/attendance")
    public ResponseEntity<List<Attendance>> getAllAttendance(@RequestParam(required = false) String date) {
        if (date != null && !date.isEmpty()) {
            return ResponseEntity.ok(collegeService.getAttendanceByDate(LocalDate.parse(date)));
        }
        return ResponseEntity.ok(collegeService.getAllAttendance());
    }

    @PostMapping("/attendance")
    public ResponseEntity<Attendance> markAttendance(@RequestBody Attendance attendance) {
        return ResponseEntity.ok(collegeService.saveAttendance(attendance));
    }

    @PutMapping("/attendance/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        attendance.setId(id);
        return ResponseEntity.ok(collegeService.saveAttendance(attendance));
    }

    @GetMapping("/attendance/student/{studentId}/percentage")
    public ResponseEntity<Map<String, Object>> getAttendancePercentage(@PathVariable Long studentId) {
        return ResponseEntity.ok(collegeService.getStudentAttendancePercentage(studentId));
    }

    // --- Examination & Marks API ---
    @GetMapping("/exams")
    public ResponseEntity<List<Examination>> getAllExams() {
        return ResponseEntity.ok(collegeService.getAllExams());
    }

    @PostMapping("/exams")
    public ResponseEntity<Examination> createExam(@RequestBody Examination exam) {
        return ResponseEntity.ok(collegeService.saveExam(exam));
    }

    @GetMapping("/marks")
    public ResponseEntity<List<Marks>> getAllMarks(@RequestParam(required = false) Long studentId) {
        if (studentId != null) {
            return ResponseEntity.ok(collegeService.getMarksByStudentId(studentId));
        }
        return ResponseEntity.ok(collegeService.getAllMarks());
    }

    @PostMapping("/marks")
    public ResponseEntity<Marks> enterMarks(@RequestBody Marks marks) {
        return ResponseEntity.ok(collegeService.saveMarks(marks));
    }

    // --- Fee API ---
    @GetMapping("/fees")
    public ResponseEntity<List<Fee>> getAllFees(@RequestParam(required = false) String studentId) {
        if (studentId != null && !studentId.trim().isEmpty()) {
            return ResponseEntity.ok(List.of(collegeService.getFeeByStudentCustomId(studentId.trim())));
        }
        return ResponseEntity.ok(collegeService.getAllFees());
    }

    @PostMapping("/fees")
    public ResponseEntity<Fee> createFee(@RequestBody Fee fee) {
        return ResponseEntity.ok(collegeService.saveFee(fee));
    }

    @PostMapping("/fees/payments")
    public ResponseEntity<FeePayment> recordPayment(@RequestBody Map<String, Object> payload) {
        Long feeId = Long.parseLong(payload.get("feeId").toString());
        Double amount = Double.parseDouble(payload.get("amount").toString());
        String method = payload.get("paymentMethod").toString();
        String remarks = payload.getOrDefault("remarks", "").toString();
        return ResponseEntity.ok(collegeService.recordPayment(feeId, amount, method, remarks));
    }

    @GetMapping("/fees/{feeId}/payments")
    public ResponseEntity<List<FeePayment>> getPayments(@PathVariable Long feeId) {
        return ResponseEntity.ok(collegeService.getPaymentsByFeeId(feeId));
    }

    // --- Reports CSV Export ---
    @GetMapping("/reports/export/{type}")
    public ResponseEntity<byte[]> exportCSV(@PathVariable String type) {
        StringBuilder csv = new StringBuilder();
        String filename = type + "_report.csv";

        if ("students".equalsIgnoreCase(type)) {
            csv.append("ID,Student Code,Name,Email,Phone,Year,Department\n");
            for (Student s : collegeService.getAllStudents()) {
                csv.append(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                        s.getId(), s.getStudentId(), s.getName(), s.getEmail(), s.getPhone(), s.getYear(),
                        s.getDepartment() != null ? s.getDepartment().getName() : "N/A"));
            }
        } else if ("faculty".equalsIgnoreCase(type)) {
            csv.append("ID,Faculty Code,Name,Email,Phone,Designation,Department\n");
            for (Faculty f : collegeService.getAllFaculty()) {
                csv.append(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                        f.getId(), f.getFacultyId(), f.getName(), f.getEmail(), f.getPhone(), f.getDesignation(),
                        f.getDepartment() != null ? f.getDepartment().getName() : "N/A"));
            }
        } else if ("courses".equalsIgnoreCase(type)) {
            csv.append("ID,Course Code,Course Name,Credits,Department,Faculty\n");
            for (Course c : collegeService.getAllCourses()) {
                csv.append(String.format("%d,%s,%s,%d,%s,%s\n",
                        c.getId(), c.getCode(), c.getName(), c.getCredits(),
                        c.getDepartment() != null ? c.getDepartment().getName() : "N/A",
                        c.getFaculty() != null ? c.getFaculty().getName() : "N/A"));
            }
        } else if ("fees".equalsIgnoreCase(type)) {
            csv.append("ID,Student ID,Student Name,Total Amount,Paid Amount,Pending Amount,Status\n");
            for (Fee f : collegeService.getAllFees()) {
                csv.append(String.format("%d,%s,%s,%.2f,%.2f,%.2f,%s\n",
                        f.getId(), f.getStudent().getStudentId(), f.getStudent().getName(),
                        f.getTotalAmount(), f.getPaidAmount(), f.getPendingAmount(), f.getStatus()));
            }
        } else {
            csv.append("Report Type,Generated Date\n");
            csv.append(type.toUpperCase()).append(",").append(LocalDate.now()).append("\n");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.toString().getBytes());
    }
}
