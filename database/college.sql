-- Create Database
CREATE DATABASE IF NOT EXISTS college_management;
USE college_management;

-- Temporarily disable foreign key checks to allow clean dropping of tables with dependencies
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS fee_payments;
DROP TABLE IF EXISTS fees;
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS results;        -- Drops the leftover dependent table causing the error
DROP TABLE IF EXISTS examinations;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS faculty;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS departments;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. Departments Table
CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(20) NOT NULL UNIQUE,
    description TEXT
);

-- 2. Faculty Table
CREATE TABLE faculty (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    faculty_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    designation VARCHAR(50) NOT NULL,
    department_id BIGINT,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- 3. Students Table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    year VARCHAR(20) NOT NULL,
    address TEXT,
    department_id BIGINT,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- 4. Courses Table
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    department_id BIGINT,
    faculty_id BIGINT,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculty(id) ON DELETE SET NULL
);

-- 5. Attendance Table
CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(10) NOT NULL,
    UNIQUE KEY unique_attendance (student_id, course_id, date),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- 6. Examinations Table
CREATE TABLE examinations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    course_id BIGINT NOT NULL,
    exam_date DATE NOT NULL,
    max_marks INT NOT NULL,
    pass_marks INT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- 7. Marks Table
CREATE TABLE marks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    marks_obtained INT NOT NULL,
    percentage DOUBLE,
    status VARCHAR(10),
    UNIQUE KEY unique_student_exam (exam_id, student_id),
    FOREIGN KEY (exam_id) REFERENCES examinations(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- 8. Fees Table
CREATE TABLE fees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL UNIQUE,
    total_amount DOUBLE NOT NULL,
    paid_amount DOUBLE DEFAULT 0.0,
    pending_amount DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- 9. Fee Payments Table
CREATE TABLE fee_payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fee_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    remarks VARCHAR(255),
    FOREIGN KEY (fee_id) REFERENCES fees(id) ON DELETE CASCADE
);

-- SAMPLE DATA

INSERT INTO departments (id, name, code, description) VALUES
(1, 'Computer Science & Engineering', 'CSE', 'Department of Computer Science and Information Technology'),
(2, 'Electronics & Communication', 'ECE', 'Department of Electronics and Communication Engineering'),
(3, 'Mechanical Engineering', 'MECH', 'Department of Mechanical Engineering');

INSERT INTO faculty (id, faculty_id, name, email, phone, designation, department_id) VALUES
(1, 'FAC101', 'Dr. Robert Smith', 'robert.smith@college.edu', '9876543210', 'Professor & HOD', 1),
(2, 'FAC102', 'Prof. Alice Johnson', 'alice.johnson@college.edu', '9876543211', 'Associate Professor', 1),
(3, 'FAC103', 'Dr. Michael Brown', 'michael.brown@college.edu', '9876543212', 'Professor', 2);

INSERT INTO students (id, student_id, name, email, phone, year, address, department_id) VALUES
(1, 'STU1001', 'John Doe', 'john.doe@student.edu', '9123456780', 'Year 3', '123 University Ave, Cityville', 1),
(2, 'STU1002', 'Jane Smith', 'jane.smith@student.edu', '9123456781', 'Year 3', '456 College Road, Townsville', 1),
(3, 'STU1003', 'David Lee', 'david.lee@student.edu', '9123456782', 'Year 2', '789 Academy St, Metrocity', 2);

INSERT INTO courses (id, code, name, credits, department_id, faculty_id) VALUES
(1, 'CS301', 'Data Structures & Algorithms', 4, 1, 1),
(2, 'CS302', 'Database Management Systems', 4, 1, 2),
(3, 'EC201', 'Digital Signal Processing', 3, 2, 3);

INSERT INTO attendance (id, student_id, course_id, date, status) VALUES
(1, 1, 1, CURRENT_DATE(), 'Present'),
(2, 2, 1, CURRENT_DATE(), 'Present'),
(3, 3, 3, CURRENT_DATE(), 'Absent');

INSERT INTO examinations (id, name, course_id, exam_date, max_marks, pass_marks) VALUES
(1, 'Mid-Semester CS301', 1, CURRENT_DATE(), 100, 40),
(2, 'Mid-Semester CS302', 2, CURRENT_DATE(), 100, 40);

INSERT INTO marks (id, exam_id, student_id, marks_obtained, percentage, status) VALUES
(1, 1, 1, 85, 85.0, 'Pass'),
(2, 1, 2, 78, 78.0, 'Pass');

INSERT INTO fees (id, student_id, total_amount, paid_amount, pending_amount, status) VALUES
(1, 1, 5000.0, 3000.0, 2000.0, 'Partial'),
(2, 2, 5000.0, 5000.0, 0.0, 'Paid'),
(3, 3, 5000.0, 0.0, 5000.0, 'Pending');

INSERT INTO fee_payments (id, fee_id, amount, payment_date, payment_method, remarks) VALUES
(1, 1, 3000.0, CURRENT_DATE(), 'Credit Card', 'First Installment'),
(2, 2, 5000.0, CURRENT_DATE(), 'Bank Transfer', 'Full Payment');