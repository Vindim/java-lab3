package webapp2;

/**
 * Class for abiturient
 * id - abiturient number
 * lastName - last name of abiturient
 * firstName - first name of abiturient
 * middle name - middle name of abiturient
 * faculty - faculty
 */
public class Abiturient {
    private Integer id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String faculty;

    public Abiturient(Integer id, String lastName, String firstName, String middleName, String faculty) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.faculty = faculty;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getFaculty() {
        return faculty;
    }

    public Integer getId() {
        return id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    /**method generate full name of abiturient
     *
     * @return String
     */
    public String getFio() {
        return lastName + " " + firstName + " " + middleName;
    }
}
