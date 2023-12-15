package ba.sum.fpmoz.pmaapp.models;


public class Subject {
    public String name;

    public String classes;
    public String exercises;
    public String seminars;
    public String semester;
    public String practical;
    public String ects;
    public String userId;
    public String studies;
    public String department;

    public Subject() {
    }

    public Subject(String name, String classes, String exercises, String seminars, String semester, String practical, String ects, String userId, String studies, String department) {
        this.name = name;
        this.classes = classes;
        this.exercises = exercises;
        this.seminars = seminars;
        this.semester = semester;
        this.practical = practical;
        this.ects = ects;
        this.userId = userId;
        this.studies = studies;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public String getSeminars() {
        return seminars;
    }

    public void setSeminars(String seminars) {
        this.seminars = seminars;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPractical() {
        return practical;
    }

    public void setPractical(String practical) {
        this.practical = practical;
    }

    public String getEcts() {
        return ects;
    }

    public void setects(String ects) {
        this.ects = ects;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStudies() {
        return studies;
    }

    public void setStudies(String studies) {
        this.studies = studies;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}


