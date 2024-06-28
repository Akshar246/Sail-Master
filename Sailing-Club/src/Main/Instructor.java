package Main;

import java.util.ArrayList;
import java.util.List;

public class Instructor {
    private final String name; // Instructor's name
    private final List<Lesson> lessons = new ArrayList<>(); // List of lessons associated with the instructor

    // Constructor to initialize the instructor's name
    public Instructor(String name) {
        this.name = name;
    }

    // Getter method for the instructor's name
    public String getName() {
        return name;
    }

    // Getter method for the list of lessons
    public List<Lesson> getLessons() {
        return lessons;
    }

    // Method to add a lesson to the instructor's list of lessons
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }
}
