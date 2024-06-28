package Main;

abstract class Booking {
    private final Member member;          // The member involved in the booking
    private final SailingDinghy dinghy;   // The dinghy used for the booking
    private final String day;             // The day of the booking
    private final String time;            // The time of the booking

    // Constructor to initialize the booking details
    public Booking(Member member, SailingDinghy dinghy, String day, String time) {
        this.member = member;
        this.dinghy = dinghy;
        this.day = day;
        this.time = time;
    }

    // Getter method for the member
    public Member getMember() {
        return member;
    }

    // Getter method for the dinghy
    public SailingDinghy getDinghy() {
        return dinghy;
    }

    // Getter method for the day
    public String getDay() {
        return day;
    }

    // Getter method for the time
    public String getTime() {
        return time;
    }
}
