package ba.sum.fpmoz.pmaapp.models;

import java.util.Date;
import java.util.List;

public class Evidention {
    public Date dateTime;
    public User professor;
    public List<User> studentList;
    public Institution institution;
    public Subject subject;
    public String academicYear;
    public Evidention() {}

    public Evidention(Date dateTime, User professor, List<User> studentList, Institution institution, Subject subject, String academicYear) {
        this.dateTime = dateTime;
        this.professor = professor;
        this.studentList = studentList;
        this.institution = institution;
        this.subject = subject;
        this.academicYear = academicYear;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }

    public List<User> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<User> studentList) {
        this.studentList = studentList;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
}
