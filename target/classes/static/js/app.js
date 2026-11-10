const API_BASE = '/api';

document.addEventListener('DOMContentLoaded', () => {
    // Navigation handling
    document.querySelectorAll('#sidebar ul li a').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const targetPage = link.getAttribute('data-page');
            showPage(targetPage);
        });
    });

    // Initial page load
    showPage('dashboard');
});

function showPage(pageId) {
    document.querySelectorAll('.page-section').forEach(sec => sec.classList.remove('active'));
    document.querySelectorAll('#sidebar ul li').forEach(li => li.classList.remove('active'));

    const activeSection = document.getElementById(pageId + '-section');
    if (activeSection) {
        activeSection.classList.add('active');
    }

    const activeNav = document.querySelector(`#sidebar ul li a[data-page="${pageId}"]`);
    if (activeNav) {
        activeNav.parentElement.classList.add('active');
    }

    // Load data based on page
    switch (pageId) {
        case 'dashboard': loadDashboard(); break;
        case 'students': loadStudents(); loadDepartmentDropdowns(); break;
        case 'faculty': loadFaculty(); loadDepartmentDropdowns(); break;
        case 'departments': loadDepartments(); break;
        case 'courses': loadCourses(); loadDepartmentDropdowns(); loadFacultyDropdowns(); break;
        case 'attendance': loadAttendance(); loadStudentDropdowns(); loadCourseDropdowns(); break;
        case 'examinations': loadExaminations(); loadCourseDropdowns(); break;
        case 'marks': loadMarks(); loadExamDropdowns(); loadStudentDropdowns(); break;
        case 'fees': loadFees(); loadStudentDropdowns(); break;
        case 'reports': loadReports(); break;
    }
}

// Utility: Alerts
function showAlert(message, type = 'success') {
    const alertBox = document.getElementById('alert-box');
    alertBox.className = `alert alert-${type} alert-dismissible fade show`;
    alertBox.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    alertBox.classList.remove('d-none');
    setTimeout(() => alertBox.classList.add('d-none'), 4000);
}

// --- Dashboard ---
async function loadDashboard() {
    try {
        const res = await fetch(`${API_BASE}/reports/dashboard`);
        const data = await res.json();
        document.getElementById('dash-total-students').innerText = data.totalStudents;
        document.getElementById('dash-total-faculty').innerText = data.totalFaculty;
        document.getElementById('dash-total-courses').innerText = data.totalCourses;
        document.getElementById('dash-total-departments').innerText = data.totalDepartments;
        document.getElementById('dash-today-attendance').innerText = data.todayAttendance;
        document.getElementById('dash-pending-fees').innerText = '₹' + data.pendingFees.toFixed(2);
    } catch (err) {
        console.error('Error loading dashboard:', err);
    }
}

// --- Departments ---
async function loadDepartments() {
    const res = await fetch(`${API_BASE}/departments`);
    const data = await res.json();
    const tbody = document.getElementById('departments-tbody');
    tbody.innerHTML = '';
    data.forEach(d => {
        tbody.innerHTML += `
            <tr>
                <td>${d.id}</td>
                <td>${d.code}</td>
                <td>${d.name}</td>
                <td>${d.description || ''}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editDepartment(${d.id}, '${d.code}', '${d.name}', '${d.description || ''}')">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteDepartment(${d.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

async function saveDepartment(e) {
    e.preventDefault();
    const id = document.getElementById('dept-id').value;
    const dept = {
        code: document.getElementById('dept-code').value,
        name: document.getElementById('dept-name').value,
        description: document.getElementById('dept-desc').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/departments/${id}` : `${API_BASE}/departments`;

    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dept)
    });

    if (res.ok) {
        showAlert(`Department ${id ? 'updated' : 'added'} successfully!`);
        resetDeptForm();
        loadDepartments();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Operation failed', 'danger');
    }
}

function editDepartment(id, code, name, desc) {
    document.getElementById('dept-id').value = id;
    document.getElementById('dept-code').value = code;
    document.getElementById('dept-name').value = name;
    document.getElementById('dept-desc').value = desc;
}

function resetDeptForm() {
    document.getElementById('dept-id').value = '';
    document.getElementById('dept-form').reset();
}

async function deleteDepartment(id) {
    if (!confirm('Are you sure you want to delete this department?')) return;
    const res = await fetch(`${API_BASE}/departments/${id}`, { method: 'DELETE' });
    if (res.ok) {
        showAlert('Department deleted successfully');
        loadDepartments();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Failed to delete department', 'danger');
    }
}

// --- Faculty ---
async function loadFaculty() {
    const query = document.getElementById('faculty-search').value;
    const url = query ? `${API_BASE}/faculty?search=${encodeURIComponent(query)}` : `${API_BASE}/faculty`;
    const res = await fetch(url);
    const data = await res.json();
    const tbody = document.getElementById('faculty-tbody');
    tbody.innerHTML = '';
    data.forEach(f => {
        tbody.innerHTML += `
            <tr>
                <td>${f.facultyId}</td>
                <td>${f.name}</td>
                <td>${f.email}</td>
                <td>${f.phone}</td>
                <td>${f.designation}</td>
                <td>${f.department ? f.department.name : 'N/A'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editFaculty(${f.id})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteFaculty(${f.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

async function saveFaculty(e) {
    e.preventDefault();
    const id = document.getElementById('faculty-pk').value;
    const faculty = {
        facultyId: document.getElementById('faculty-id').value,
        name: document.getElementById('faculty-name').value,
        email: document.getElementById('faculty-email').value,
        phone: document.getElementById('faculty-phone').value,
        designation: document.getElementById('faculty-designation').value,
        department: { id: document.getElementById('faculty-dept').value }
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/faculty/${id}` : `${API_BASE}/faculty`;

    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(faculty)
    });

    if (res.ok) {
        showAlert(`Faculty ${id ? 'updated' : 'added'} successfully!`);
        resetFacultyForm();
        loadFaculty();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Operation failed', 'danger');
    }
}

async function editFaculty(id) {
    const res = await fetch(`${API_BASE}/faculty/${id}`);
    const f = await res.json();
    document.getElementById('faculty-pk').value = f.id;
    document.getElementById('faculty-id').value = f.facultyId;
    document.getElementById('faculty-name').value = f.name;
    document.getElementById('faculty-email').value = f.email;
    document.getElementById('faculty-phone').value = f.phone;
    document.getElementById('faculty-designation').value = f.designation;
    if (f.department) document.getElementById('faculty-dept').value = f.department.id;
}

function resetFacultyForm() {
    document.getElementById('faculty-pk').value = '';
    document.getElementById('faculty-form').reset();
}

async function deleteFaculty(id) {
    if (!confirm('Are you sure you want to delete this faculty member?')) return;
    const res = await fetch(`${API_BASE}/faculty/${id}`, { method: 'DELETE' });
    if (res.ok) {
        showAlert('Faculty deleted successfully');
        loadFaculty();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Failed to delete faculty', 'danger');
    }
}

// --- Students ---
async function loadStudents() {
    const query = document.getElementById('student-search').value;
    const url = query ? `${API_BASE}/students?search=${encodeURIComponent(query)}` : `${API_BASE}/students`;
    const res = await fetch(url);
    const data = await res.json();
    const tbody = document.getElementById('students-tbody');
    tbody.innerHTML = '';
    data.forEach(s => {
        tbody.innerHTML += `
            <tr>
                <td>${s.studentId}</td>
                <td>${s.name}</td>
                <td>${s.email}</td>
                <td>${s.phone}</td>
                <td>${s.year}</td>
                <td>${s.department ? s.department.name : 'N/A'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editStudent(${s.id})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteStudent(${s.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

async function saveStudent(e) {
    e.preventDefault();
    const id = document.getElementById('student-pk').value;
    const student = {
        studentId: document.getElementById('student-id').value,
        name: document.getElementById('student-name').value,
        email: document.getElementById('student-email').value,
        phone: document.getElementById('student-phone').value,
        year: document.getElementById('student-year').value,
        address: document.getElementById('student-address').value,
        department: { id: document.getElementById('student-dept').value }
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/students/${id}` : `${API_BASE}/students`;

    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(student)
    });

    if (res.ok) {
        showAlert(`Student ${id ? 'updated' : 'added'} successfully!`);
        resetStudentForm();
        loadStudents();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Operation failed', 'danger');
    }
}

async function editStudent(id) {
    const res = await fetch(`${API_BASE}/students/${id}`);
    const s = await res.json();
    document.getElementById('student-pk').value = s.id;
    document.getElementById('student-id').value = s.studentId;
    document.getElementById('student-name').value = s.name;
    document.getElementById('student-email').value = s.email;
    document.getElementById('student-phone').value = s.phone;
    document.getElementById('student-year').value = s.year;
    document.getElementById('student-address').value = s.address;
    if (s.department) document.getElementById('student-dept').value = s.department.id;
}

function resetStudentForm() {
    document.getElementById('student-pk').value = '';
    document.getElementById('student-form').reset();
}

async function deleteStudent(id) {
    if (!confirm('Are you sure you want to delete this student record?')) return;
    const res = await fetch(`${API_BASE}/students/${id}`, { method: 'DELETE' });
    if (res.ok) {
        showAlert('Student deleted successfully');
        loadStudents();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Failed to delete student', 'danger');
    }
}

// --- Courses ---
async function loadCourses() {
    const res = await fetch(`${API_BASE}/courses`);
    const data = await res.json();
    const tbody = document.getElementById('courses-tbody');
    tbody.innerHTML = '';
    data.forEach(c => {
        tbody.innerHTML += `
            <tr>
                <td>${c.code}</td>
                <td>${c.name}</td>
                <td>${c.credits}</td>
                <td>${c.department ? c.department.name : 'N/A'}</td>
                <td>${c.faculty ? c.faculty.name : 'Unassigned'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editCourse(${c.id})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteCourse(${c.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

async function saveCourse(e) {
    e.preventDefault();
    const id = document.getElementById('course-pk').value;
    const course = {
        code: document.getElementById('course-code').value,
        name: document.getElementById('course-name').value,
        credits: parseInt(document.getElementById('course-credits').value),
        department: { id: document.getElementById('course-dept').value },
        faculty: document.getElementById('course-faculty').value ? { id: document.getElementById('course-faculty').value } : null
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/courses/${id}` : `${API_BASE}/courses`;

    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(course)
    });

    if (res.ok) {
        showAlert(`Course ${id ? 'updated' : 'added'} successfully!`);
        resetCourseForm();
        loadCourses();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Operation failed', 'danger');
    }
}

async function editCourse(id) {
    const res = await fetch(`${API_BASE}/courses/${id}`);
    const c = await res.json();
    document.getElementById('course-pk').value = c.id;
    document.getElementById('course-code').value = c.code;
    document.getElementById('course-name').value = c.name;
    document.getElementById('course-credits').value = c.credits;
    if (c.department) document.getElementById('course-dept').value = c.department.id;
    if (c.faculty) document.getElementById('course-faculty').value = c.faculty.id;
}

function resetCourseForm() {
    document.getElementById('course-pk').value = '';
    document.getElementById('course-form').reset();
}

async function deleteCourse(id) {
    if (!confirm('Are you sure you want to delete this course?')) return;
    const res = await fetch(`${API_BASE}/courses/${id}`, { method: 'DELETE' });
    if (res.ok) {
        showAlert('Course deleted successfully');
        loadCourses();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Failed to delete course', 'danger');
    }
}

// --- Attendance ---
async function loadAttendance() {
    const date = document.getElementById('attendance-filter-date').value;
    const url = date ? `${API_BASE}/attendance?date=${date}` : `${API_BASE}/attendance`;
    const res = await fetch(url);
    const data = await res.json();
    const tbody = document.getElementById('attendance-tbody');
    tbody.innerHTML = '';
    data.forEach(a => {
        tbody.innerHTML += `
            <tr>
                <td>${a.date}</td>
                <td>${a.student.studentId} - ${a.student.name}</td>
                <td>${a.course.name}</td>
                <td><span class="badge bg-${a.status === 'Present' ? 'success' : 'danger'}">${a.status}</span></td>
            </tr>
        `;
    });
}

async function saveAttendance(e) {
    e.preventDefault();
    const attendance = {
        student: { id: document.getElementById('att-student').value },
        course: { id: document.getElementById('att-course').value },
        date: document.getElementById('att-date').value,
        status: document.getElementById('att-status').value
    };

    const res = await fetch(`${API_BASE}/attendance`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(attendance)
    });

    if (res.ok) {
        showAlert('Attendance marked successfully!');
        document.getElementById('attendance-form').reset();
        loadAttendance();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Failed to mark attendance', 'danger');
    }
}

async function calculateStudentAttendance() {
    const studentId = document.getElementById('att-student-calc').value;
    if (!studentId) return;
    const res = await fetch(`${API_BASE}/attendance/student/${studentId}/percentage`);
    const data = await res.json();
    document.getElementById('att-calc-result').innerHTML = `
        <strong>Total Classes:</strong> ${data.totalClasses} | 
        <strong>Present:</strong> ${data.presentClasses} | 
        <strong>Attendance Percentage:</strong> <span class="badge bg-info text-dark">${data.percentage}%</span>
    `;
}

// --- Examinations & Marks ---
async function loadExaminations() {
    const res = await fetch(`${API_BASE}/exams`);
    const data = await res.json();
    const tbody = document.getElementById('exams-tbody');
    tbody.innerHTML = '';
    data.forEach(e => {
        tbody.innerHTML += `
            <tr>
                <td>${e.id}</td>
                <td>${e.name}</td>
                <td>${e.course.name}</td>
                <td>${e.examDate}</td>
                <td>${e.maxMarks}</td>
                <td>${e.passMarks}</td>
            </tr>
        `;
    });
}

async function saveExam(e) {
    e.preventDefault();
    const exam = {
        name: document.getElementById('exam-name').value,
        course: { id: document.getElementById('exam-course').value },
        examDate: document.getElementById('exam-date').value,
        maxMarks: parseInt(document.getElementById('exam-max').value),
        passMarks: parseInt(document.getElementById('exam-pass').value)
    };

    const res = await fetch(`${API_BASE}/exams`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(exam)
    });

    if (res.ok) {
        showAlert('Exam created successfully!');
        document.getElementById('exam-form').reset();
        loadExaminations();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Failed to create exam', 'danger');
    }
}

async function loadMarks() {
    const studentId = document.getElementById('marks-search-student').value;
    const url = studentId ? `${API_BASE}/marks?studentId=${studentId}` : `${API_BASE}/marks`;
    const res = await fetch(url);
    const data = await res.json();
    const tbody = document.getElementById('marks-tbody');
    tbody.innerHTML = '';
    data.forEach(m => {
        tbody.innerHTML += `
            <tr>
                <td>${m.examination.name}</td>
                <td>${m.student.studentId} - ${m.student.name}</td>
                <td>${m.marksObtained} / ${m.examination.maxMarks}</td>
                <td>${m.percentage}%</td>
                <td><span class="badge bg-${m.status === 'Pass' ? 'success' : 'danger'}">${m.status}</span></td>
            </tr>
        `;
    });
}

async function saveMarks(e) {
    e.preventDefault();
    const marks = {
        examination: { id: document.getElementById('mark-exam').value },
        student: { id: document.getElementById('mark-student').value },
        marksObtained: parseInt(document.getElementById('mark-obtained').value)
    };

    const res = await fetch(`${API_BASE}/marks`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(marks)
    });

    if (res.ok) {
        showAlert('Marks entered successfully!');
        document.getElementById('marks-form').reset();
        loadMarks();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Failed to record marks', 'danger');
    }
}

// --- Fees ---
async function loadFees() {
    const studentId = document.getElementById('fee-search-student').value;
    const url = studentId ? `${API_BASE}/fees?studentId=${encodeURIComponent(studentId)}` : `${API_BASE}/fees`;
    const res = await fetch(url);
    const data = await res.json();
    const tbody = document.getElementById('fees-tbody');
    tbody.innerHTML = '';
    data.forEach(f => {
        tbody.innerHTML += `
            <tr>
                <td>${f.student.studentId} - ${f.student.name}</td>
                <td>₹${f.totalAmount.toFixed(2)}</td>
                <td>₹${f.paidAmount.toFixed(2)}</td>
                <td>₹${f.pendingAmount.toFixed(2)}</td>
                <td><span class="badge bg-${f.status === 'Paid' ? 'success' : (f.status === 'Partial' ? 'warning' : 'danger')}">${f.status}</span></td>
                <td>
                    <button class="btn btn-sm btn-success" onclick="openPaymentModal(${f.id}, ${f.pendingAmount})">Record Payment</button>
                </td>
            </tr>
        `;
    });
}
async function saveFee(e) {
    e.preventDefault();
    const fee = {
        student: { id: document.getElementById('fee-student').value },
        totalAmount: parseFloat(document.getElementById('fee-amount').value)
    };

    const res = await fetch(`${API_BASE}/fees`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(fee)
    });

    if (res.ok) {
        showAlert('Fee record created successfully!');
        document.getElementById('fee-form').reset();
        loadFees();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Failed to create fee record', 'danger');
    }
}

function openPaymentModal(feeId, pendingAmount) {
    document.getElementById('pay-fee-id').value = feeId;
    document.getElementById('pay-amount').value = pendingAmount;
    const modal = new bootstrap.Modal(document.getElementById('paymentModal'));
    modal.show();
}

async function submitPayment(e) {
    e.preventDefault();
    const payload = {
        feeId: document.getElementById('pay-fee-id').value,
        amount: parseFloat(document.getElementById('pay-amount').value),
        paymentMethod: document.getElementById('pay-method').value,
        remarks: document.getElementById('pay-remarks').value
    };

    const res = await fetch(`${API_BASE}/fees/payments`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        showAlert('Payment recorded successfully!');
        bootstrap.Modal.getInstance(document.getElementById('paymentModal')).hide();
        loadFees();
    } else {
        const err = await res.json();
        showAlert(err.error || 'Payment failed', 'danger');
    }
}

// --- Helper Dropdowns ---
async function loadDepartmentDropdowns() {
    const res = await fetch(`${API_BASE}/departments`);
    const depts = await res.json();
    ['student-dept', 'faculty-dept', 'course-dept'].forEach(id => {
        const el = document.getElementById(id);
        if (el) {
            el.innerHTML = '<option value="">Select Department</option>';
            depts.forEach(d => el.innerHTML += `<option value="${d.id}">${d.name}</option>`);
        }
    });
}

async function loadFacultyDropdowns() {
    const res = await fetch(`${API_BASE}/faculty`);
    const faculty = await res.json();
    const el = document.getElementById('course-faculty');
    if (el) {
        el.innerHTML = '<option value="">Select Faculty</option>';
        faculty.forEach(f => el.innerHTML += `<option value="${f.id}">${f.name}</option>`);
    }
}

async function loadStudentDropdowns() {
    const res = await fetch(`${API_BASE}/students`);
    const students = await res.json();
    ['att-student', 'att-student-calc', 'mark-student', 'fee-student'].forEach(id => {
        const el = document.getElementById(id);
        if (el) {
            el.innerHTML = '<option value="">Select Student</option>';
            students.forEach(s => el.innerHTML += `<option value="${s.id}">${s.studentId} - ${s.name}</option>`);
        }
    });
}

async function loadCourseDropdowns() {
    const res = await fetch(`${API_BASE}/courses`);
    const courses = await res.json();
    ['att-course', 'exam-course'].forEach(id => {
        const el = document.getElementById(id);
        if (el) {
            el.innerHTML = '<option value="">Select Course</option>';
            courses.forEach(c => el.innerHTML += `<option value="${c.id}">${c.code} - ${c.name}</option>`);
        }
    });
}

async function loadExamDropdowns() {
    const res = await fetch(`${API_BASE}/exams`);
    const exams = await res.json();
    const el = document.getElementById('mark-exam');
    if (el) {
        el.innerHTML = '<option value="">Select Exam</option>';
        exams.forEach(e => el.innerHTML += `<option value="${e.id}">${e.name}</option>`);
    }
}

function loadReports() {
    // Reports section handlers loaded statically in HTML
}
