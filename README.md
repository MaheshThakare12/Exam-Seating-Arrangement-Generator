🏫 Exam-Seating-Arrangement-Generator

A Java-based automation system for generating exam hall seating arrangements.
The project reads student and subject data from CSV files, automatically allocates seats for both common subjects and branch-only subjects, and exports the final seating plan as PDF reports using the iTextPDF library.

Built with Apache Maven, this project demonstrates practical Object-Oriented Programming (OOP) principles applied to a real-world academic management task.

✨ Features

📂 CSV Input Integration — Reads student and subject details from CSV files

🧮 Automated Seat Allocation — Handles both common and branch-specific subject arrangements

🏫 Multi-Room Management — Allocates students across multiple exam halls with invigilator assignment

📄 PDF Report Generation — Creates professional PDFs using iTextPDF, including student details, room lists, and subject info

⚙️ Maven Build Automation — Easily compile and run via Apache Maven

🧠 Strong OOP Design — Modular, extensible, and easy to maintain

💡 Object-Oriented Concepts Used

Classes & Objects — Representing Students, Subjects, Halls, and Allocators

Inheritance — For handling common vs branch-specific subjects

Encapsulation — Controlled access to student data

Abstraction — Simplified seat assignment logic through service classes

Polymorphism — Different allocation rules for different subject types

Exception Handling — Gracefully manages invalid data and file errors

File Handling & I/O — Reads CSV input and generates PDF outputs

Multithreading (optional) — Can be extended to parallelize allocation generation

📁 Project Structure
- `pom.xml`: Maven configuration (includes iTextPDF dependency)
  
- `data/`: Contains input data files
  - `students.csv`: Input student data
  - `subjects.csv`: Input subject data
  
- `output/`: Directory for generated files
  - `ExamAllocation-Common-Subjects.pdf`
  - `ExamAllocation-Branch-Only-Subjects.pdf`

- `src/`: Source code
  - `main/java/`: Main application code
    - `model/`: Contains model classes
      - `Student.java`
      - `Subject.java`
      - `ExamHall.java`
      - `Invigilator.java`
    - `service/`: Contains service classes
      - `CSVReader.java`
      - `SeatAllocator.java`
      - `PDFGenerator.java`
    - `main/`: Main entry point of the application
      - `ExamSystemExcelPDF.java`
      
  - `test/java/`: Contains test classes
    - ...
    
- `README.md`: Project documentation

⚙️ Requirements

Java JDK 8+

Apache Maven 3.6+

iTextPDF 5.5+ (added via Maven dependency)

🚀 Build & Run (Maven)
1️⃣ Compile the project
mvn clean compile

2️⃣ Package it
mvn package

3️⃣ Run the main program
java -cp target/exam-seating-generator-1.0-SNAPSHOT.jar main.ExamSystemExcelPDF


The program reads CSV data, allocates seats, and creates two PDF reports:

ExamAllocation-Common-Subjects.pdf

ExamAllocation-Branch-Only-Subjects.pdf

🧩 Output Overview
🧠 Common Subjects Allocation

Generated File: ExamAllocation-Common-Subjects.pdf
Includes shared subjects like Mathematics, Physics, Chemistry, Engineering Mechanics, etc.

Each page contains:

Subject name, duration, and exam date/time

Hall number and invigilator

Student list (PRN, Name, Branch)

🧑‍🏫 Branch-Only Subjects Allocation

Generated File: ExamAllocation-Branch-Only-Subjects.pdf
Covers department-specific subjects such as:

Civil Engineering — Structural Mechanics, Surveying

Mechanical Engineering — Thermodynamics, Machine Design

IT — Data Structures, Web Technologies

Each section displays:

Branch name

Subject info

Invigilator name

Student list with PRN and branch details

🧠 Example Snippet (Generated via iTextPDF)
Exam Hall Allocation — Branch Only Subjects

Civil Engineering:
Structural Mechanics — 3 hours — 28-06-2025 09:00 AM
Surveying — 3 hours — 30-06-2025 09:00 AM

Room 501 | Invigilator: Prof. Kirti Nair
--------------------------------------------------
Name          PRN         Branch
Kunal Sethi   CI250401    Civil
Kyra Sharma   CI250402    Civil
Lakshya Raut  CI250403    Civil
...

🧱 Technologies Used

Java (OOP Concepts)

Apache Maven

iTextPDF Library

CSV File Handling

Console-based Execution

