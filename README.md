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
