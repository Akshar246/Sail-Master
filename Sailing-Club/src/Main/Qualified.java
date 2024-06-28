package Main;

import java.util.ArrayList;
import java.util.List;

// Qualified class extends Member and represents a qualified member
public class Qualified extends Member {
    private final List<DinghyHire> hires = new ArrayList<>(); // List to store dinghy hires for the qualified member

    // Constructor to initialize the qualified member's name
    public Qualified(String name) {
        super(name);
    }

    // Getter method for the list of dinghy hires
    public List<DinghyHire> getHires() {
        return hires;
    }

    // Method to add a dinghy hire to the list
    public void addHire(DinghyHire hire) {
        hires.add(hire);
    }

    // Override method to check if the qualified member can book a lesson
    @Override
    public boolean canBookLesson() {
        return getLessons().size() < 3; // Qualified member can book a lesson if they have less than 3 lessons
    }

    @Override
    public boolean canHireDinghy() {
        return hires.size() < 2;
    }
}
