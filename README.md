# College Management System

A simple, complete, and fully functional **College Management System** built with **Java 21**, **Spring Boot**, **MySQL**, **Bootstrap 5**, and **JavaScript**.

---

## 🚀 Features & Modules
1. **Dashboard:** Live statistics on students, faculty, courses, departments, daily attendance, pending fees.
2. **Student Management:** Full CRUD operations, department mapping, search.
3. **Faculty Management:** Full CRUD operations, designation, department mapping, search.
4. **Department Management:** Full CRUD operations, constraint validation (prevents delete when referenced).
5. **Course Management:** Credit hours, department & faculty mapping.
6. **Attendance Management:** Mark attendance, date filters, duplicate check, attendance percentage calculator.
7. **Examination & Marks:** Create exams, record student marks, automated percentage & Pass/Fail status calculation.
8. **Fee Management:** Assign total fee, record payments, track pending balance & payment history.
9. **Reports:** CSV Export for Students, Faculty, Courses, and Fees.

---
## Technologies Used
Backend: Java 21, Spring Boot 3.2, Spring Data JPA, RESTful APIs, Apache Maven
Database: MySQL Server with relational mapping, foreign key protection, and composite unique constraints
Frontend: Bootstrap 5, HTML5, JavaScript (Fetch API) for a responsive single-page experience

## 📁 Project Structure

```text
CollegeManagementSystem/
│
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.jar
│
├── .gitignore
├── pom.xml
├── README.md
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── college/
    │   │           └── management/
    │   │               ├── CollegeManagementApplication.java
    │   │               ├── controller/
    │   │               ├── model/
    │   │               ├── repository/
    │   │               └── service/
    │   │
    │   └── resources/
    │       ├── static/
    │       │   ├── css/
    │       │   │   └── style.css
    │       │   ├── js/
    │       │   │   └── app.js
    │       │   └── index.html
    │       │
    │       └── application.properties
    │
    └── test/
        └── java/
            └── com/
                └── college/
                    └── management/
                        └── CollegeManagementApplicationTests.java
```
## 🚀 Project Features

### 1. Department Management
- **Add a Department:** Create academic departments with department name, code, and head of department details.
- **View Departments:** Display all active departments with faculty counts.
- **Update Department Details:** Modify department names, codes, and administrative heads.
- **Delete a Department:** Remove department records while maintaining foreign key integrity for associated courses.

### 2. Student Management
- **Register a Student:** Record student profiles, including name, roll number, department, and contact information.
- **View Students:** List enrolled students with search and filters by department or batch.
- **Update Student Details:** Modify contact details, active status, and academic year.
- **Delete a Student:** Remove student profiles and handle associated academic records safely.

### 3. Course & Faculty Management
- **Add Courses / Assign Faculty:** Register courses with credit units and assign instructors.
- **View Courses:** Display active courses, syllabus outlines, and assigned faculty.
- **Update Course Information:** Modify course codes, credit allocations, and faculty assignments.
- **Delete Courses:** Remove courses from the curriculum while maintaining data integrity.

### 4. Attendance & Examination Management
- **Record Attendance:** Mark daily attendance for each subject with duplicate prevention using student, subject, and date constraints.
- **Schedule Exams:** Schedule mid-term and final examinations with subject details and maximum marks.
- **Process Grades:** Record marks, calculate percentages, and automatically evaluate Pass/Fail results.
- **View Academic Performance:** Generate individual student grade cards and department-wise pass metrics.

### 5. Fee & Billing Management
- **Generate Student Fee Invoices:** Create semester-wise invoices covering:
  - Tuition fees (₹)
  - Examination and laboratory charges
  - Late payment penalties
- **Log Payments:** Record partial or full payments with automatic balance tracking.
- **Track Fee Balances:** Monitor paid amounts, pending balances, and payment status.
- **Export CSV Reports:** Download financial and attendance summaries for administrative auditing.
## clone the respiratory


## 🛠️ Prerequisites & Setup (Windows 11)
- **Java Development Kit (JDK 21)** installed and added to `PATH`.
- **Apache Maven 3.8+** installed.
- **MySQL Server 8.0+** running locally.

### 1. Database Setup
1. Open MySQL Workbench or Command Prompt.
2. Run the SQL script located in `database/college.sql`:
   ```bash
   mysql -u root -p < database/college.sql
   ```
3. Update `src/main/resources/application.properties` with your MySQL `username` and `password` if different from `root`/`root`.

---

## 🏃 Running the Application

Open Command Prompt / PowerShell in the project directory:

```bash
# 1. Clean and Build project
mvn clean install

# 2. Run Spring Boot application
mvn spring-boot:run
```

---

## 🌐 Accessing the Application
Open your browser and navigate to:
[http://localhost:8080](http://localhost:8080)

---

## 📮 Postman Collection
Import `College_Management_API.postman_collection.json` into Postman to test all REST API endpoints.

## Future Enhancements
Role-Based Access Control: Secure portal access using Spring Security and JWT authentication for administrators, faculty, and students.
Automated Document Export: PDF generation for official fee receipts, student transcripts, and semester report cards.
Notification System: Automated email and SMS alerts for fee dues, attendance warnings, and exam schedules.
Interactive Data Visualization: Chart.js integration on the admin dashboard for visual trends in student enrollments and fee collections.
 ## Why use this Project
✅ Simple and Effective: Console-based system for quick access.
✅ Easy to Modify: Readable Java code structure.
✅ Persistent Data Storage: Keeps data across sessions.




