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

🧠 Strong OOP Design — Modular code structure, easy to extend

💡 Object-Oriented Concepts Used

Classes & Objects — Representing Students, Subjects, Halls, and Allocators

Inheritance — For handling common vs branch-specific subjects

Encapsulation — Controlled access to student data

Abstraction — Simplified seat assignment logic via separate services

Polymorphism — Different allocation rules for different subject types

Exception Handling — Managing invalid data and I/O errors gracefully

File Handling & I/O — Reading CSV inputs and generating PDF outputs

Multithreading (optional) — Can be extended to parallelize allocation generation

📁 Project Structure
Exam-Seating-Arrangement-Generator/
│
├── pom.xml                          # Maven configuration (includes iTextPDF dependency)
├── data/
│   ├── students.csv                 # Input student data
│   ├── subjects.csv                 # Input subject data
│
├── output/
│   ├── ExamAllocation-Common-Subjects.pdf
│   └── ExamAllocation-Branch-Only-Subjects.pdf
│
├── src/
│   ├── main/java/
│   │   ├── model/
│   │   │   ├── Student.java
│   │   │   ├── Subject.java
│   │   │   ├── ExamHall.java
│   │   │   └── Invigilator.java
│   │   ├── service/
│   │   │   ├── CSVReader.java
│   │   │   ├── SeatAllocator.java
│   │   │   └── PDFGenerator.java
│   │   └── main/
│   │       └── ExamSystemExcelPDF.java   # Main entry point
│   └── test/java/
│       └── ...
└── README.md

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


The program will read CSV data, allocate seats, and create two PDF reports:

ExamAllocation-Common-Subjects.pdf

ExamAllocation-Branch-Only-Subjects.pdf

🧩 Output Overview
🧠 Common Subjects Allocation

Generated file: ExamAllocation-Common-Subjects.pdf
Covers shared subjects like Mathematics, Physics, Chemistry, Engineering Mechanics, etc.
Each page lists:

Subject name, duration, and date/time

Hall number and invigilator

Allocated student list (with PRN, name, and branch)

ExamAllocation-Common-Subjects

🧑‍🏫 Branch-Only Subjects Allocation

Generated file: ExamAllocation-Branch-Only-Subjects.pdf
Includes department-specific subjects such as:

Civil Engineering: Structural Mechanics, Surveying

Mechanical Engineering: Thermodynamics, Machine Design

IT: Data Structures, Web Technologies
Each hall section displays the branch, subject, invigilator, and student details

ExamAllocation-Branch-Only-Subj…

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

