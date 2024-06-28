package Main;

import java.util.*;

public class Main {
    private static final List<Member> members = new ArrayList<>();
    private static final List<Instructor> instructors = new ArrayList<>();
    private static final List<SailingDinghy> dinghies = new ArrayList<>();
    private static final Set<String> validDays = new HashSet<>(Arrays.asList("Wed", "Sat", "Sun"));

    public static void main(String[] args) {
        // Initialize dummy data
        initializeData();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = getValidIntInput(scanner);

            switch (choice) {
                case 1:
                    bookLesson(scanner);
                    break;
                case 2:
                    listMemberLessons(scanner);
                    break;
                case 3:
                    listInstructorLessons(scanner);
                    break;
                case 4:
                    hireDinghy(scanner);
                    break;
                case 5:
                    displayDinghyBookings(scanner);
                    break;
                case 0:
                    System.out.println("Exiting program.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Get a valid integer input from the user.
     */
    private static int getValidIntInput(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    /**
     * Initialize the data for members, instructors, and dinghies.
     */
    private static void initializeData() {
        // Initialize dinghies
        for (int i = 1; i <= 10; i++) {
            dinghies.add(new SailingDinghy("Dinghy " + i));
        }

        // Initialize instructors
        Instructor instructor1 = new Instructor("Instructor 1");
        Instructor instructor2 = new Instructor("Instructor 2");
        instructors.add(instructor1);
        instructors.add(instructor2);

        // Initialize members
        members.add(new Learner("Learner 1", instructor1));
        members.add(new Learner("Learner 2", instructor2));
        members.add(new Qualified("Qualified 1"));
        members.add(new Qualified("Qualified 2"));

        // Add dummy lessons for learners
        Lesson lesson1 = new Lesson(members.get(0), dinghies.get(0), instructor1, "Wed", "09:00");
        Lesson lesson2 = new Lesson(members.get(1), dinghies.get(1), instructor2, "Sat", "10:00");
        members.get(0).addLesson(lesson1);
        instructor1.addLesson(lesson1);
        dinghies.get(0).addBooking(lesson1);

        members.get(1).addLesson(lesson2);
        instructor2.addLesson(lesson2);
        dinghies.get(1).addBooking(lesson2);

        // Add dummy dinghy hires for qualified members
        DinghyHire hire1 = new DinghyHire(members.get(2), dinghies.get(2), "Wed", "11:00");
        DinghyHire hire2 = new DinghyHire(members.get(3), dinghies.get(3), "Sat", "12:00");
        ((Qualified) members.get(2)).addHire(hire1);
        dinghies.get(2).addBooking(hire1);

        ((Qualified) members.get(3)).addHire(hire2);
        dinghies.get(3).addBooking(hire2);

        // Additional lessons to test limits
        Lesson lesson3 = new Lesson(members.get(0), dinghies.get(4), instructor1, "Sun", "09:00");
        Lesson lesson4 = new Lesson(members.get(0), dinghies.get(5), instructor1, "Sun", "10:00");
        members.get(0).addLesson(lesson3);
        instructor1.addLesson(lesson3);
        dinghies.get(4).addBooking(lesson3);

        members.get(0).addLesson(lesson4);
        instructor1.addLesson(lesson4);
        dinghies.get(5).addBooking(lesson4);
    }

    //Print the main menu.
    private static void printMenu() {
        System.out.println("-------------------------");
        System.out.println("Sailing Club Menu");
        System.out.println("-------------------------");
        System.out.println("Book lesson for member ..........1");
        System.out.println("List member lessons .............2");
        System.out.println("List instructor lessons .........3");
        System.out.println("Hire dinghy for qualified member 4");
        System.out.println("Display dinghy bookings .........5");
        System.out.println("Exit ............................0");
        System.out.println("-------------------------");
        System.out.print("Enter choice:> ");
    }

    // Book a lesson for a member.
    private static void bookLesson(Scanner scanner) {
        System.out.println("Select a member:");
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i).getName());
        }

        int memberIndex = getValidIndex(scanner, members.size());
        if (memberIndex == -1) return;

        Member member = members.get(memberIndex);

        if (!member.canBookLesson()) {
            System.out.println("This member has reached the limit of lessons for the week.");
            return;
        }

        Instructor instructor = null;
        if (member instanceof Learner) {
            instructor = ((Learner) member).getInstructor();
        } else if (member instanceof Qualified) {
            System.out.println("Select an instructor:");
            for (int i = 0; i < instructors.size(); i++) {
                System.out.println((i + 1) + ". " + instructors.get(i).getName());
            }

            int instructorIndex = getValidIndex(scanner, instructors.size());
            if (instructorIndex == -1) return;

            instructor = instructors.get(instructorIndex);
        }

        assert instructor != null;
        System.out.println("Weekly schedule for " + instructor.getName() + ":");
        displayWeeklySchedule(instructor);

        System.out.print("Enter day (Wed, Sat, Sun): ");
        String day = scanner.nextLine().trim().toLowerCase();
        if (!validDays.contains(day.substring(0, 1).toUpperCase() + day.substring(1))) {
            System.out.println("Invalid day selected.");
            return;
        }

        System.out.print("Enter time (09:00 - 18:00): ");
        String time = scanner.nextLine();

        if (isValidTime(time)) {
            System.out.println("Invalid time selected.");
            return;
        }

        if (isMemberOrInstructorBusy(member, instructor, day, time) || isDinghyBooked(day, time)) {
            System.out.println("The member or instructor is already booked for this time.");
            return;
        }

        System.out.println("Available dinghies:");
        List<SailingDinghy> availableDinghies = getAvailableDinghies(day, time);
        for (int i = 0; i < availableDinghies.size(); i++) {
            System.out.println((i + 1) + ". " + availableDinghies.get(i).getName());
        }

        int dinghyIndex = getValidIndex(scanner, availableDinghies.size());
        if (dinghyIndex == -1) return;

        SailingDinghy dinghy = availableDinghies.get(dinghyIndex);
        Lesson lesson = new Lesson(member, dinghy, instructor, day, time);
        member.addLesson(lesson);
        instructor.addLesson(lesson);
        dinghy.addBooking(lesson);

        System.out.println("Lesson booked successfully.");
        System.out.println("Member: " + member.getName() + ", Instructor: " + instructor.getName() + ", Dinghy: " + dinghy.getName() + ", Day: " + day + ", Time: " + time);
    }

    // Get a valid index input from the user.
    private static int getValidIndex(Scanner scanner, int size) {
        int index = -1;
        while (index < 0 || index >= size) {
            System.out.print("Enter number: ");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt() - 1;
                if (index < 0 || index >= size) {
                    System.out.println("Invalid selection. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                return -1;
            }
        }
        scanner.nextLine();
        return index;
    }

    // List lessons for a selected member.
    private static void listMemberLessons(Scanner scanner) {
        System.out.println("Select a member:");
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i).getName());
        }
        int memberIndex = getValidIndex(scanner, members.size());
        if (memberIndex == -1) return;

        Member member = members.get(memberIndex);
        List<Lesson> lessons = member.getLessons();

        if (lessons.isEmpty()) {
            System.out.println("This member has no lessons booked for the week.");
            return;
        }

        System.out.println("Lessons for " + member.getName() + ":");
        lessons.sort(Comparator.comparing(Lesson::getDay).thenComparing(Lesson::getTime));
        printTableHeader();
        for (Lesson lesson : lessons) {
            printLessonDetails(lesson);
        }
        printTableFooter();
    }

    // List lessons for a selected instructor.
    private static void listInstructorLessons(Scanner scanner) {
        System.out.println("Select an instructor:");
        for (int i = 0; i < instructors.size(); i++) {
            System.out.println((i + 1) + ". " + instructors.get(i).getName());
        }
        int instructorIndex = getValidIndex(scanner, instructors.size());
        if (instructorIndex == -1) return;

        Instructor instructor = instructors.get(instructorIndex);
        List<Lesson> lessons = instructor.getLessons();

        if (lessons.isEmpty()) {
            System.out.println("This instructor has no lessons booked for the week.");
            return;
        }

        System.out.println("Lessons for " + instructor.getName() + ":");
        lessons.sort(Comparator.comparing(Lesson::getDay).thenComparing(Lesson::getTime));
        printInstructorLessonsTableHeader();
        for (Lesson lesson : lessons) {
            printInstructorLessonDetails(lesson);
        }
        printTableFooter();
    }

    // Hire a dinghy for a qualified member.
    private static void hireDinghy(Scanner scanner) {
        System.out.println("Select a qualified member:");
        List<Qualified> qualifiedMembers = new ArrayList<>();
        for (Member member : members) {
            if (member instanceof Qualified) {
                qualifiedMembers.add((Qualified) member);
            }
        }
        for (int i = 0; i < qualifiedMembers.size(); i++) {
            System.out.println((i + 1) + ". " + qualifiedMembers.get(i).getName());
        }
        int memberIndex = getValidIndex(scanner, qualifiedMembers.size());
        if (memberIndex == -1) return;

        Qualified member = qualifiedMembers.get(memberIndex);

        if (!member.canHireDinghy()) {
            System.out.println("This member has reached the limit of dinghy hires for the week.");
            return;
        }

        System.out.print("Enter day (Wed, Sat, Sun): ");
        String day = scanner.nextLine().trim().toLowerCase();
        if (!validDays.contains(day.substring(0, 1).toUpperCase() + day.substring(1))) {
            System.out.println("Invalid day selected.");
            return;
        }

        System.out.print("Enter time (09:00 - 18:00): ");
        String time = scanner.nextLine();

        if (isValidTime(time)) {
            System.out.println("Invalid time selected.");
            return;
        }

        if (isMemberBusy(member, day, time) || isDinghyBooked(day, time)) {
            System.out.println("The member is already booked for this time.");
            return;
        }

        System.out.println("Available dinghies:");
        List<SailingDinghy> availableDinghies = getAvailableDinghies(day, time);
        for (int i = 0; i < availableDinghies.size(); i++) {
            System.out.println((i + 1) + ". " + availableDinghies.get(i).getName());
        }
        int dinghyIndex = getValidIndex(scanner, availableDinghies.size());
        if (dinghyIndex == -1) return;

        SailingDinghy dinghy = availableDinghies.get(dinghyIndex);
        DinghyHire hire = new DinghyHire(member, dinghy, day, time);
        member.addHire(hire);
        dinghy.addBooking(hire);

        System.out.println("Dinghy hire booked successfully.");
        System.out.printf("Member: %s, Dinghy: %s, Day: %s, Time: %s%n", member.getName(), dinghy.getName(), day, time);
    }

    // Display bookings for a selected dinghy.
    private static void displayDinghyBookings(Scanner scanner) {
        System.out.println("Select a dinghy:");
        for (int i = 0; i < dinghies.size(); i++) {
            System.out.println((i + 1) + ". " + dinghies.get(i).getName());
        }
        int dinghyIndex = getValidIndex(scanner, dinghies.size());
        if (dinghyIndex == -1) return;

        SailingDinghy dinghy = dinghies.get(dinghyIndex);
        List<Booking> bookings = dinghy.getBookings();

        if (bookings.isEmpty()) {
            System.out.println("This dinghy has no bookings for the week.");
            return;
        }

        System.out.println("Bookings for " + dinghy.getName() + ":");
        bookings.sort(Comparator.comparing(Booking::getDay).thenComparing(Booking::getTime));
        printDinghyBookingTableHeader();
        for (Booking booking : bookings) {
            printDinghyBookingDetails(booking);
        }
        printTableFooter();
    }

    // Display the weekly schedule for an instructor.
    private static void displayWeeklySchedule(Instructor instructor) {
        List<Lesson> lessons = instructor.getLessons();
        for (String day : Arrays.asList("Wed", "Sat", "Sun")) {
            System.out.println(day + ":");
            for (int hour = 9; hour <= 18; hour++) {
                String time = hour + ":00";
                boolean isBooked = lessons.stream().anyMatch(l -> l.getDay().equals(day) && l.getTime().equals(time));
                System.out.println("  " + time + " - " + (isBooked ? "Booked" : "Available"));
            }
        }
    }


    // Validate the time format and range.
    private static boolean isValidTime(String time) {
        return !time.matches("^\\d{2}:00$") || Integer.parseInt(time.split(":")[0]) < 9 || Integer.parseInt(time.split(":")[0]) > 18;
    }

    // Check if a member or instructor is busy at a given time.
    private static boolean isMemberOrInstructorBusy(Member member, Instructor instructor, String day, String time) {
        boolean memberBusy = member.getLessons().stream().anyMatch(l -> l.getDay().equals(day) && l.getTime().equals(time));
        boolean instructorBusy = instructor.getLessons().stream().anyMatch(l -> l.getDay().equals(day) && l.getTime().equals(time));
        return memberBusy || instructorBusy;
    }

    // Check if any dinghy is booked at a given time.
    private static boolean isDinghyBooked(String day, String time) {
        return dinghies.stream().flatMap(d -> d.getBookings().stream()).anyMatch(b -> b.getDay().equals(day) && b.getTime().equals(time));
    }

    // Check if a member is busy at a given time.
    private static boolean isMemberBusy(Member member, String day, String time) {
        boolean lessonBusy = member.getLessons().stream().anyMatch(l -> l.getDay().equals(day) && l.getTime().equals(time));
        boolean hireBusy = member instanceof Qualified && ((Qualified) member).getHires().stream().anyMatch(h -> h.getDay().equals(day) && h.getTime().equals(time));
        return lessonBusy || hireBusy;
    }

    // Get a list of available dinghies at a given time
    private static List<SailingDinghy> getAvailableDinghies(String day, String time) {
        List<SailingDinghy> availableDinghies = new ArrayList<>();
        for (SailingDinghy dinghy : dinghies) {
            boolean isBooked = dinghy.getBookings().stream().anyMatch(b -> b.getDay().equals(day) && b.getTime().equals(time));
            if (!isBooked) {
                availableDinghies.add(dinghy);
            }
        }
        return availableDinghies;
    }

    // Print the table header for lessons.
    private static void printTableHeader() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-15s | %-10s | %-5s | %-17s |%n", "Instructor", "Member", "Day", "Time", "Dinghy");
        System.out.println("---------------------------------------------------------------------------");
    }

    // Print the table header for instructor lessons.
    private static void printInstructorLessonsTableHeader() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("| %-15s | %-10s | %-5s | %-31s |%n", "Member", "Day", "Time", "Dinghy");
        System.out.println("---------------------------------------------------------------------------");
    }

    // Print the table header for dinghy bookings.
    private static void printDinghyBookingTableHeader() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-10s | %-5s | %-20s | %-12s |%n", "Type", "Day", "Time", "Member", "Instructor");
        System.out.println("---------------------------------------------------------------------------");
    }

    //Print the details of a lesson.
    private static void printLessonDetails(Lesson lesson) {
        System.out.printf("| %-12s | %-15s | %-10s | %-5s | %-17s |%n", lesson.getInstructor().getName(), lesson.getMember().getName(), lesson.getDay(), lesson.getTime(), lesson.getDinghy().getName());
    }

    //Print the details of an instructor's lesson.
    private static void printInstructorLessonDetails(Lesson lesson) {
        System.out.printf("| %-15s | %-10s | %-5s | %-31s |%n", lesson.getMember().getName(), lesson.getDay(), lesson.getTime(), lesson.getDinghy().getName());
    }

    //Print the details of a dinghy booking.
    private static void printDinghyBookingDetails(Booking booking) {
        String bookingType = booking instanceof Lesson ? "Lesson" : "Hire";
        String instructorName = booking instanceof Lesson ? ((Lesson) booking).getInstructor().getName() : "N/A";
        System.out.printf("| %-12s | %-10s | %-5s | %-20s | %-12s |%n", bookingType, booking.getDay(), booking.getTime(), booking.getMember().getName(), instructorName);
    }

    // print the table for the footer
    private static void printTableFooter() {
        System.out.println("---------------------------------------------------------------------------");
    }
}