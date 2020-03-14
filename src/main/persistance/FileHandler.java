package persistance;

import model.Student;

import java.io.*;

/**
 * Handle reading and writing storage files
 */
public class FileHandler {
    private static final String FILE_PATH = "./data/student.txt";
    private String path;

    public FileHandler(String path) {
        this.path = path;
    }

    public FileHandler() {
        this(FILE_PATH);
    }

    // EFFECTS: creates/overwrites a file in data folder that stores the student object
    public void write(Student student) throws IOException {
        FileOutputStream file = new FileOutputStream(new File(this.path));
        ObjectOutputStream obj = new ObjectOutputStream(file);

        // Write objects
        obj.writeObject(student);

        obj.close();
        file.close();
    }

    // EFFECTS: returns the student object from the file in data folder
    public Student read() throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(this.path));
        ObjectInputStream oi = new ObjectInputStream(fi);

        // Read objects
        Student student = (Student) oi.readObject();

        oi.close();
        fi.close();

        return student;
    }

    public String getPath() {
        return this.path;
    }
}
