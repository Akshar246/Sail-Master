package Main;

// Learner class extends Member and represents a learner member
public class Learner extends Member {
    private final Instructor instructor; // The instructor assigned to the learner

    // Constructor to initialize the learner's name and assigned instructor
    public Learner(String name, Instructor instructor) {
        super(name); // Call the superclass constructor to set the name
        this.instructor = instructor;
    }

    // Getter method for the assigned instructor
    public Instructor getInstructor() {
        return instructor;
    }

    // Override method to check if the learner can book a lesson
    @Override
    public boolean canBookLesson() {
        return getLessons().size() < 3; // Learner can book a lesson if they have less than 3 lessons
    }

    // Override method to check if the learner can hire a dinghy (always false for learners)
    @Override
    public boolean canHireDinghy() {
        return false; // Learners cannot hire dinghies
    }
}
