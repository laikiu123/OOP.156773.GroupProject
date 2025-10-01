# 🎓 Student Management System

## 📌 Introduction
**Student Management System (SMS)** is a Java application designed to support modern student management in educational institutions.  
The system provides functions for managing student information, course registration, grading, and graduation validation. It supports multiple program types:
- **Regular Students**
- **Credit-Based Students**
- **Part-Time Students**

This project was developed as part of the **Object-Oriented Programming (OOP)** course to practice object-oriented analysis, design, and Java programming.

---

## ✨ Features
- **Student Management**: add, update, delete, search students by ID or name.  
- **Course Management**: create courses, assign final exam weights (`finalWeight`), view course lists.  
- **Course Registration**: students can register or cancel course enrollment.  
- **Grading System**: enter midterm/final grades, calculate final grades based on weight.  
- **Graduation Validation**: check graduation conditions (credits earned, GPA, program type).  
- **Program Management**: manage study programs with `ProgramType` (Regular, Credit-Based, Part-Time).  

---

## 🏗️ Architecture & Project Structure
### Main packages:
```
SVBK/
 └── model/
      ├── Student (abstract)
      ├── CreditBasedStudent
      ├── PartTimeStudent
      ├── Course
      ├── Program
      ├── Enrollment
      ├── StudentManager
      ├── CourseManager
      ├── GradingSystem
      └── GraduationValidator
 └── main/
      └── StudentManagementSystem.java
```

### Class Diagram
- PlantUML source: [`docs/class-diagram.puml`](docs/class-diagram.puml)  
- Exported image (put your generated file here): `images/class-diagram.png`  
![Class Diagram](images/class-diagram.png)

> To export the diagram locally, install PlantUML and run (from the repo root):
> ```bash
> java -jar plantuml.jar docs/class-diagram.puml -o images
> ```

### UI Demo
- **Program Management Screen**  
  Put a screenshot at `images/ui-program-management.png`  
  ![Program Management UI](images/ui-program-management.png)

- **Student Management Screen**  
  Put a screenshot at `images/ui-student-management.png`  
  ![Student Management UI](images/ui-student-management.png)

---

## ⚙️ Technologies
- **Language**: Java 17  
- **UI**: JavaFX (FXML)  
- **Build Tool**: Maven/Gradle (optional)  
- **Recommended IDE**: IntelliJ IDEA / Eclipse  

---

## 🚀 How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/student-management-system.git
   ```
2. Open the project in IntelliJ IDEA or Eclipse.  
3. Build the project and run `StudentManagementSystem.java`.  
4. The management interface will appear, and you can start interacting with the system.  

---

## 👨‍💻 Team Members
- Trọng Lê Minh (Team Leader)  
- [Member 2]  
- [Member 3]  

---

## 🤝 Contribution
We welcome contributions and improvements to this project:  
- Fork the repository  
- Create a new branch (`feature/your-feature-name`)  
- Commit your changes and open a Pull Request  

---

## 📜 License
This project is developed for educational purposes.  
You are free to use and modify it for non-commercial purposes.  
