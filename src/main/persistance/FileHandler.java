package persistance;

import model.Student;

import java.io.*;

public class FileHandler {
    private static final String PATH = "./data/student.txt";

    private FileOutputStream file;
    private ObjectOutputStream obj;

    public FileHandler() {}

    public void write(Student student) {
        try {
            file = new FileOutputStream(new File(PATH));
            obj = new ObjectOutputStream(file);

            obj.writeObject(student);

            obj.close();
            file.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    public Student read() throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(PATH));
        ObjectInputStream oi = new ObjectInputStream(fi);

        // Read objects
        Student student = (Student) oi.readObject();
        System.out.println(student.toString());

        oi.close();
        fi.close();

        return student;
    }
}
