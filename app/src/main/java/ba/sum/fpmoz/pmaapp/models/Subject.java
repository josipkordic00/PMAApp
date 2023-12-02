package ba.sum.fpmoz.pmaapp.models;

public class Subject {
    public String name;
    public int classes;
    public int exercises;
    public int seminars;
    public int semester;
    public int practical;
    public int ECTS;
    public String studies;
    public String department;

    public Subject(String name, int classes, int exercises, int seminars, int semester, int practical, int ECTS, String studies, String department) {
        this.name = name;
        this.classes = classes;
        this.exercises = exercises;
        this.seminars = seminars;
        this.semester = semester;
        this.practical = practical;
        this.ECTS = ECTS;
        this.studies = studies;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClasses() {
        return classes;
    }

    public void setClasses(int classes) {
        this.classes = classes;
    }

    public int getExercises() {
        return exercises;
    }

    public void setExercises(int exercises) {
        this.exercises = exercises;
    }

    public int getSeminars() {
        return seminars;
    }

    public void setSeminars(int seminars) {
        this.seminars = seminars;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getPractical() {
        return practical;
    }

    public void setPractical(int practical) {
        this.practical = practical;
    }

    public int getECTS() {
        return ECTS;
    }

    public void setECTS(int ECTS) {
        this.ECTS = ECTS;
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
