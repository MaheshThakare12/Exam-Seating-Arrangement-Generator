package seatarrangement.project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

// ------------------- 1. OOP Classes -------------------

class Student {
    private final String name;
    private final String PRN;
    private final String branch;
    private final int year;

    public Student(String name, String PRN, String branch, int year) {
        this.name = name;
        this.PRN = PRN;
        this.branch = branch;
        this.year = year;
    }

    public String getName() { return name; }
    public String getPRN() { return PRN; }
    public String getBranch() { return branch; }
    public int getYear() { return year; }
}

class Room {
    private int roomNumber;
    private int benches;
    private ArrayList<Student> students;

    public Room(int roomNumber, int benches) {
        this.roomNumber = roomNumber;
        this.benches = benches;
        this.students = new ArrayList<>();
    }

    public int getRoomNumber() { return roomNumber; }
    public int getBenches() { return benches; }
    public ArrayList<Student> getStudents() { return students; }
    public void addStudent(Student s) { students.add(s); }
}

class Building {
    private final int buildingNumber;
    private final ArrayList<Room> rooms;

    public Building(int buildingNumber) {
        this.buildingNumber = buildingNumber;
        this.rooms = new ArrayList<>();
    }

    public int getBuildingNumber() { return buildingNumber; }
    public ArrayList<Room> getRooms() { return rooms; }
    public void addRoom(Room r) { rooms.add(r); }
}

class Subject {
    private final String name;
    private final String branchType;  // Common/Branch Name
    private final String examDate;
    private final int year;
    private final String duration;

    public Subject(String name, String branchType, String examDate, int year, String duration) {
        this.name = name;
        this.branchType = branchType;
        this.examDate = examDate;
        this.year = year;
        this.duration = duration;
    }

    public String getName() { return name; }
    public String getBranchType() { return branchType; }
    public String getExamDate() { return examDate; }
    public int getYear() { return year; }
    public String getDuration() { return duration; }
}


class Invigilator {
    private final String name;
    private final String department;

    public Invigilator(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public String getName() { return name; }
    public String getDepartment() { return department; }
}

class ExamHall {
    private final Room room;
    private final Invigilator invigilator;

    public ExamHall(Room room, Invigilator invigilator) {
        this.room = room;
        this.invigilator = invigilator;
    }

    public Room getRoom() { return room; }
    public Invigilator getInvigilator() { return invigilator; }
}

// ------------------- 2. Main Program -------------------

public class ExamSystemExcelPDF {

    // Common subjects across all branches
    private static Set<String> commonSubjectNames() {
        return new HashSet<>(Arrays.asList(
                "Mathematics", "Physics", "Chemistry",
                "Engineering Mechanics", "Basic Electrical Engineering",
                "Computer Programming", "Engineering Graphics",
                "Environmental Studies", "Workshop Practice"
        ));
    }


    // Branch-specific subject mapping
    private static Map<String, List<String>> getBranchSpecificSubjects() {
        Map<String, List<String>> map = new HashMap<>();

        map.put("Civil Engineering", Arrays.asList("Structural Mechanics", "Surveying"));
        map.put("Mechanical Engineering", Arrays.asList("Thermodynamics", "Machine Design"));
        map.put("Electrical Engineering", Arrays.asList("Circuit Theory", "Power Systems"));
        map.put("Information Technology", Arrays.asList("Data Structures", "Web Technologies"));
        map.put("Computer Science", Arrays.asList("Operating Systems", "Database Management Systems"));

        return map;
    }

    // Filter subjects by name set
    private static List<Subject> filterSubjectsByNames(List<Subject> all, Set<String> names, boolean include) {
        List<Subject> out = new ArrayList<>();
        for (Subject s : all) {
            boolean has = names.contains(s.getName());
            if ((include && has) || (!include && !has)) {
                out.add(s);
            }
        }
        return out;
    }

    public static void main(String[] args) {

        ArrayList<Student> students = readStudentsCSV("C:\\Users\\ADMIN\\OneDrive\\Coding Language\\java project\\exam-allocation\\examhall-allocation\\students.csv");
        ArrayList<Building> buildings = readBuildingsCSV("C:\\Users\\ADMIN\\OneDrive\\Coding Language\\java project\\exam-allocation\\examhall-allocation\\buildings.csv");
        ArrayList<Subject> allSubjects = readSubjectsCSV("C:\\Users\\ADMIN\\OneDrive\\Coding Language\\java project\\exam-allocation\\examhall-allocation\\subjects.csv");
        ArrayList<Invigilator> invigilators = readInvigilatorsCSV("C:\\Users\\ADMIN\\OneDrive\\Coding Language\\java project\\exam-allocation\\examhall-allocation\\invigilators.csv");

        // Debug: Print loaded data
        System.out.println("Loaded " + students.size() + " students:");
        for (int i = 0; i < Math.min(5, students.size()); i++) {
            Student s = students.get(i);
            System.out.println(s.getName() + " | " + s.getPRN() + " | " + s.getBranch());
        }
        for (Building b : buildings) {
            for (Room r : b.getRooms()) {
                System.out.println("Room " + r.getRoomNumber() + " benches=" + r.getBenches());
            }
        }

        // Build subject groups
        Set<String> common = commonSubjectNames();
        List<Subject> commonSubjects = filterSubjectsByNames(allSubjects, common, true);
        List<Subject> branchOnlySubjects = filterSubjectsByNames(allSubjects, common, false);

        // Allocate students to rooms
        ArrayList<ExamHall> examHalls = allocateStudentsAndInvigilators(buildings, students, invigilators);

        // Generate two PDFs
        generatePDFWithSubjects("ExamAllocation-Common-Subjects.pdf",
                examHalls,
                commonSubjects,
                "Exam Hall Allocation — Common Subjects",
                false);

        generatePDFWithSubjects("ExamAllocation-Branch-Only-Subjects.pdf",
                examHalls,
                branchOnlySubjects,
                "Exam Hall Allocation — Branch Only Subjects",
                true);
    }

    // ------------------- CSV Utilities -------------------

    private static List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        if (line == null) return fields;

        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                fields.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        fields.add(sb.toString());
        return fields;
    }

    private static BufferedReader openCsv(String filePath) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
    }

    private static String safeGet(List<String> row, int idx) {
        if (idx >= row.size()) return "";
        String v = row.get(idx);
        if (v == null) return "";
        v = v.trim();
        if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
            v = v.substring(1, v.length()-1);
        }
        return v.trim();
    }

    // ------------------- 3. Read CSV Methods -------------------

    public static ArrayList<Student> readStudentsCSV(String filePath) {
        ArrayList<Student> students = new ArrayList<>();
        try (BufferedReader br = openCsv(filePath)) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = parseCsvLine(line);
                String PRN = safeGet(cols, 0);
                String name = safeGet(cols, 1);
                String branch = safeGet(cols, 2);
                String yearStr = safeGet(cols, 3);

                if (name.isEmpty() || PRN.isEmpty()) continue;

                // Map FE/SE/TE/BE strings to numeric years
                int year = 0;
                String y = yearStr == null ? "" : yearStr.trim().toUpperCase(Locale.ROOT);
                if (y.startsWith("F")) year = 1;
                else if (y.startsWith("S") && y.contains("E")) year = 2;
                else if (y.startsWith("T")) year = 3;
                else if (y.startsWith("B")) year = 4;
                try {
                    year = Integer.parseInt(yearStr.trim());
                } catch (Exception ignore) {
                    // keep mapped value
                }

                students.add(new Student(name, PRN, branch, year));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return students;
    }

    public static ArrayList<Building> readBuildingsCSV(String filePath) {
        ArrayList<Building> buildings = new ArrayList<>();
        try (BufferedReader br = openCsv(filePath)) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = parseCsvLine(line);
                String buildingNoStr = safeGet(cols, 0);
                String roomNoStr = safeGet(cols, 1);
                String benchesStr = safeGet(cols, 2);
                if (buildingNoStr.isEmpty() || roomNoStr.isEmpty()) continue;

                int buildingNo = Integer.parseInt(buildingNoStr);
                int roomNo = Integer.parseInt(roomNoStr);
                int benches = benchesStr.isEmpty() ? 0 : Integer.parseInt(benchesStr);

                Room room = new Room(roomNo, benches);

                Building b = null;
                for (Building bd : buildings) {
                    if (bd.getBuildingNumber() == buildingNo) {
                        b = bd;
                        break;
                    }
                }
                if (b == null) {
                    b = new Building(buildingNo);
                    buildings.add(b);
                }
                b.addRoom(room);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return buildings;
    }

    public static ArrayList<Subject> readSubjectsCSV(String filePath) {
        ArrayList<Subject> subjects = new ArrayList<>();
        try (BufferedReader br = openCsv(filePath)) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = parseCsvLine(line);

                String name = safeGet(cols, 0);
                String branchType = safeGet(cols, 1);
                String date = safeGet(cols, 2);
                String yearStr = safeGet(cols, 3);
                String duration = safeGet(cols, 4);

                if (name.isEmpty()) continue;

                // ------------------- YEH FIX HAI -------------------
                // Puraani line ko delete karein:
                // int year = yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr);

                // Aur yeh naya logic add karein (aapke readStudentsCSV se):
                int year = 0;
                String y = yearStr == null ? "" : yearStr.trim().toUpperCase(Locale.ROOT);
                if (y.startsWith("F")) year = 1;
                else if (y.startsWith("S") && y.contains("E")) year = 2;
                else if (y.startsWith("T")) year = 3;
                else if (y.startsWith("B")) year = 4;
                try {
                    // Agar "1", "2" jaisa number hai, toh use parse karega
                    year = Integer.parseInt(yearStr.trim());
                } catch (Exception ignore) {
                    // Agar "First Year" jaisa text hai, toh upar map ki hui value (1) use karega
                }
                // ----------------------------------------------------

                subjects.add(new Subject(name, branchType, date, year, duration));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return subjects;
    }

    private static List<Subject> filterSubjectsByBranchType(List<Subject> all, String type) {
        List<Subject> out = new ArrayList<>();
        for (Subject s : all) {
            if (s.getBranchType().equalsIgnoreCase(type)) {
                out.add(s);
            }
        }
        return out;
    }


    public static ArrayList<Invigilator> readInvigilatorsCSV(String filePath) {
        ArrayList<Invigilator> invigilators = new ArrayList<>();
        try (BufferedReader br = openCsv(filePath)) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = parseCsvLine(line);
                String name = safeGet(cols, 0);
                String dept = safeGet(cols, 1);
                if (name.isEmpty()) continue;
                invigilators.add(new Invigilator(name, dept));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return invigilators;
    }

    // ------------------- 4. Allocation Logic -------------------

    public static ArrayList<ExamHall> allocateStudentsAndInvigilators(ArrayList<Building> buildings,
                                                                      ArrayList<Student> students,
                                                                      ArrayList<Invigilator> invigilators) {
        ArrayList<ExamHall> examHalls = new ArrayList<>();
        int studentIndex = 0;
        int invIndex = 0;

        for (Building b : buildings) {
            for (Room r : b.getRooms()) {
                for (int i = 0; i < r.getBenches() && studentIndex < students.size(); i++) {
                    r.addStudent(students.get(studentIndex));
                    studentIndex++;
                }
                Invigilator inv = null;
                if (invIndex < invigilators.size()) {
                    inv = invigilators.get(invIndex);
                    invIndex++;
                }
                examHalls.add(new ExamHall(r, inv));
            }
        }
        return examHalls;
    }

    // ------------------- 5. PDF Generation -------------------

    public static void generatePDFWithSubjects(String outputName,
                                               ArrayList<ExamHall> examHalls,
                                               List<Subject> subjects,
                                               String titleText,
                                               boolean isBranchSpecific) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputName));
            document.open();

            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph(titleText, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);

            // Branch-specific vs Common subjects display
            if (isBranchSpecific) {
                Map<String, List<String>> branchMap = getBranchSpecificSubjects();

                for (Map.Entry<String, List<String>> entry : branchMap.entrySet()) {
                    String branchName = entry.getKey();
                    List<String> subjNames = entry.getValue();

                    Paragraph branchHeader = new Paragraph(branchName + ":", subTitleFont);
                    document.add(branchHeader);

                    com.itextpdf.text.List subList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);

                    for (String subjName : subjNames) {
                        for (Subject s : subjects) {
                            // Match subject name AND branchType
                            if (s.getName().equals(subjName) && s.getBranchType().equalsIgnoreCase(branchName)) {
                                // Use formatted date here
                                String prettyDate = s.getExamDate().replace(" 0.00", " 09:00 AM");
                                subList.add(new ListItem(s.getName() + " — " + s.getDuration() + " — " + prettyDate));
                            }
                        }
                    }

                    document.add(subList);
                    document.add(new Paragraph(" "));

                    // Branch-specific students seating
                    for (ExamHall eh : examHalls) {
                        List<Student> roomBranchStudents = new ArrayList<>();
                        for (Student st : eh.getRoom().getStudents()) {
                            if (st.getBranch() != null &&
                                    st.getBranch().toLowerCase(Locale.ROOT).contains(branchName.toLowerCase(Locale.ROOT).split(" ")[0])) {
                                roomBranchStudents.add(st);
                            }
                        }
                        if (roomBranchStudents.isEmpty()) continue;

                        Paragraph roomPara = new Paragraph(
                                "Room " + eh.getRoom().getRoomNumber() +
                                        " | Invigilator: " + (eh.getInvigilator() != null ? eh.getInvigilator().getName() : "N/A"),
                                subTitleFont
                        );
                        document.add(roomPara);

                        com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(3);
                        table.addCell("Name");
                        table.addCell("PRN");
                        table.addCell("Branch");
                        for (Student st : roomBranchStudents) {
                            table.addCell(st.getName());
                            table.addCell(st.getPRN());
                            table.addCell(st.getBranch());
                        }
                        document.add(table);
                        document.add(new Paragraph("--------------------------------------------------"));
                    }

                    document.add(new Paragraph(" "));
                }
            } else {
                // Common subjects simple ordered list
                Paragraph subHeader = new Paragraph("Subjects Included:", subTitleFont);
                document.add(subHeader);
                com.itextpdf.text.List subjList = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED);
                for (Subject s : subjects) {
                    String prettyDate = s.getExamDate().replace(" 0.00", " 09:00 AM");
                    subjList.add(new ListItem(s.getName() + " — " + s.getDuration() + " — " + prettyDate));
                }
                document.add(subjList);
                document.add(new Paragraph(" "));

                // Unified seating for common subjects
                for (ExamHall eh : examHalls) {
                    Paragraph roomPara = new Paragraph(
                            "Room " + eh.getRoom().getRoomNumber() +
                                    " | Invigilator: " + (eh.getInvigilator() != null ? eh.getInvigilator().getName() : "N/A"),
                            subTitleFont
                    );
                    document.add(roomPara);

                    com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(3);
                    table.addCell("Name");
                    table.addCell("PRN");
                    table.addCell("Branch");
                    for (Student st : eh.getRoom().getStudents()) {
                        table.addCell(st.getName());
                        table.addCell(st.getPRN());
                        table.addCell(st.getBranch());
                    }
                    document.add(table);
                    document.add(new Paragraph("--------------------------------------------------"));
                }
            }

            document.close();
            System.out.println("PDF generated: " + outputName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
