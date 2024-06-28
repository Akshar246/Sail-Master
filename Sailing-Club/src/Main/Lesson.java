package Main;

public class Lesson extends Booking {
    private final Instructor instructor; // The instructor for the lesson

    // Constructor to initialize the lesson details
    public Lesson(Member member, SailingDinghy dinghy, Instructor instructor, String day, String time) {
        super(member, dinghy, day, time); // Call the superclass constructor
        this.instructor = instructor;
    }

    // Getter method for the instructor
    public Instructor getInstructor() {
        return instructor;
    }
}
