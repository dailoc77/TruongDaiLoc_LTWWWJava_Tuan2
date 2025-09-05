package iuh.fit.se.bai5;

import java.util.Date;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private Date dob;

    public User() {}

    public User(int id, String firstName, String lastName, Date dob) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }
}

