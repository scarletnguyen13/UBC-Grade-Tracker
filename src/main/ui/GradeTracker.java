package ui;

import model.*;
import persistance.FileHandler;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradeTracker {
    private static final String MAIN_CMD = "tracker";
    private static final String NAME_CMD = "--name";
    private static final String ID_CMD = "--studentId";
    private static final String COURSE_CMD = "--course";
    private static final String COURSES_CMD = "--courses";
    private static final String COMPONENTS_CMD = "--components";
    private static final String SAVE_CMD = "--save";

    Student student;
    Scanner scanner;
    FileHandler fileHandler;

    // Initialize the Grade Tracker console application
    public GradeTracker() {
        scanner = new Scanner(System.in);
        fileHandler = new FileHandler();
        try {
            this.student = this.fileHandler.read();
            handleCommand();
        } catch (IOException | ClassNotFoundException e) {
            takeStudentInfo();
        }
    }

    // Get student's basic information
    // MODIFIES: this
    // EFFECTS: create a new Student instance using user's input data
    public void takeStudentInfo() {
        System.out.print("\nName (*): ");
        String name = scanner.nextLine();

        System.out.print("\nStudent Number (*): ");
        String studentId = scanner.nextLine();

        System.out.print("\nCurrent session year(eg. 2019-W): ");
        String session = scanner.nextLine();

        HashSet<Session> sessions = new HashSet<>();
        Session currentSession = convertSessionInput(session);

        if (currentSession.getType().equals(SessionType.WINTER_SESSION)) {
            currentSession.addTerm(convertTermInput("Term 1"));
            currentSession.addTerm(convertTermInput("Term 2"));
        } else {
            currentSession.addTerm(convertTermInput("Summer Term"));
        }

        sessions.add(currentSession);
        this.student = new Student(name, studentId, sessions);

        handleCommand();
    }

    // Convert user's session input (ie. 2019-W) to Session object
    private Session convertSessionInput(String session) {
        String[] sessionInfo = session.split("-");
        int year = Integer.parseInt(sessionInfo[0]);

        Session currentSession = new Session(
                year,
                sessionInfo[1].equalsIgnoreCase("S")
                        ? SessionType.SUMMER_SESSION
                        : SessionType.WINTER_SESSION
        );


        return currentSession;
    }

    // Convert user's term input (ie. list of courses) to Term object
    private Term convertTermInput(String termName) {
        Term term = new Term(termName);

        System.out.println("\n" + termName + " Courses (type \"exit\" to quit): ");
        String course = scanner.nextLine();

        while (!course.equals("exit")) {
            term.addCourse(new Course(course));
            course = scanner.nextLine();
        }

        return term;
    }

    // Split user's command input (ie. tracker --name "Scarlet Nguyen") to list of commands
    private List<String> convertCommandInput(String cmd) {
        List<String> list = new ArrayList<>();
        // Split between white space, except for between double quotes
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(cmd);
        while (m.find()) {
            list.add(m.group(1).replace("\"", "")); // add and remove surrounding quotes
        }
        return list;
    }

    // Handle user's command
    private void handleCommand() {
        System.out.println("\nHi " + this.student.getName() + ", what do you want to do next?");

        System.out.print("\n> ");
        while (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();

            if (!cmd.equals("")) {
                printCommandResponse(cmd);
                System.out.print("> ");
            }
        }
    }

    // Print response based on the given command
    private void printCommandResponse(String cmd) {
        List<String> cmdList = convertCommandInput(cmd);
        if (cmdList.get(0).equals(MAIN_CMD)) {
            switch (cmdList.size()) {
                case 1: {
                    printWelcome();
                    break;
                }
                case 2: {
                    handleOneCommand(cmdList.get(1));
                    break;
                }
                case 3: {
                    handleTwoCommand(cmdList.get(1), cmdList.get(2));
                    break;
                }
                default: {
                    printWarning();
                    break;
                }
            }
        } else {
            printWarning();
        }
    }

    private void printWelcome() {
        System.out.println("Welcome to UBC Grade Tracker!");
    }

    private void printWarning() {
        System.out.println("Wrong command!");
    }

    // Print the student's requested info
    private void handleOneCommand(String cmd) {
        switch (cmd) {
            case NAME_CMD: {
                System.out.println(student.getName());
                break;
            }
            case ID_CMD: {
                System.out.println(student.getStudentId());
                break;
            }
            case COURSES_CMD: {
                System.out.println(student.getAllCourses());
                break;
            }
            case SAVE_CMD: {
                saveData();
                break;
            }
            default: {
                System.out.println("Wrong command!");
                break;
            }
        }
    }

    private void saveData() {
        try {
            this.fileHandler.write(this.student);
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    // MODIFIES: this
    // EFFECTS: updates and modifies the student's info based on the requested information
    private void handleTwoCommand(String cmd1, String cmd2) {
        switch (cmd1) {
            case NAME_CMD:
                this.student.setName(cmd2);
                System.out.println(student.getName());
                break;
            case ID_CMD:
                this.student.setStudentId(cmd2);
                System.out.println(student.getStudentId());
                break;
            case COURSE_CMD:
                Course course = student.findCourseByName(cmd2);
                if (course == null) {
                    System.out.println("Course not found!");
                    break;
                }
                handleCourseCommand(course);
                break;
            default:
                System.out.println("Wrong command!");
                break;
        }
    }

    // Handle course-specific command
    private void handleCourseCommand(Course course) {
        System.out.print("\n> " + course.getName() + " ");

        String cmd = scanner.nextLine();
        while (!cmd.equals("exit")) {
            if (!cmd.equals("")) {
                printCourseCommandResponse(course, cmd);
                System.out.print("> " + course.getName() + " ");
                cmd = scanner.nextLine();
            }
        }

        handleCommand();
    }

    // Print response based on the given course-specific command
    private void printCourseCommandResponse(Course course, String cmd) {
        List<String> cmdList = convertCommandInput(cmd);
        switch (cmdList.size()) {
            case 1: {
                handleCourseOneCommand(course, cmdList.get(0));
                break;
            }
            case 2: {
                handleCourseTwoCommand(course, cmdList.get(0), cmdList.get(1));
                break;
            }
            default: {
                System.out.println("Wrong command!");
                break;
            }
        }
    }

    // Print the course's requested info
    private void handleCourseOneCommand(Course course, String cmd1) {
        switch (cmd1) {
            case COMPONENTS_CMD: {
                System.out.println(course.getComponents());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates and modifies the course's info based on the requested information
    private void handleCourseTwoCommand(Course course, String cmd1, String cmd2) {
        switch (cmd1) {
            case COMPONENTS_CMD: {
                String[] componentsSplit = cmd2.split(", ");
                Set<CourseComponent> courseComponents = new HashSet<>();

                for (String c: componentsSplit) {
                    String[] split = c.split(" ");
                    String componentName = split[0];
                    int componentPercentage = Integer.parseInt(split[1]);
                    courseComponents.add(
                            new CourseComponent(componentName, componentPercentage)
                    );
                }

                course.setComponents(courseComponents);
                this.student.editCourse(course);
                System.out.println(course.getComponents());
            }
        }
    }
}
