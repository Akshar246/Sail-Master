package Main;

import java.util.ArrayList;
import java.util.List;

abstract class Member {
    private final String name; // Member's name
    private final List<Lesson> lessons = new ArrayList<>(); // List of lessons associated with the member

    // Constructor to initialize the member's name
    public Member(String name) {
        this.name = name;
    }

    // Getter method for the member's name
    public String getName() {
        return name;
    }

    // Getter method for the list of lessons
    public List<Lesson> getLessons() {
        return lessons;
    }

    // Method to add a lesson to the member's list of lessons
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    // Abstract method to check if the member can book a lesson
    public abstract boolean canBookLesson();

    // Abstract method to check if the member can hire a dinghy
    public abstract boolean canHireDinghy();
}
