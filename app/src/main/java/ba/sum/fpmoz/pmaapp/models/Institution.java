package ba.sum.fpmoz.pmaapp.models;

public class Institution {
    public String name;
    public String address;

    public String university;

    public Institution() {}
    public Institution(String name, String address, String university) {
        this.name = name;
        this.address = address;
        this.university = university;
    }


    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
