public class waw {
    private String name;
    private int age;
    private int idnum;

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int newAge) {
        if (newAge > 0) {
            age = newAge;
        } else {
            System.out.println("Age must be positive.");
        }
    }

    public int getIdnum() {
        return idnum;
    }

    public void setIdnum(int newIdnum) {
        idnum = newIdnum;
    }

    public static void main(String[] args) {
        waw p = new waw();
        p.setName("karan");
        p.setAge(21);
        p.setIdnum(241300005);

        System.out.println("Name: " + p.getName());
        System.out.println("Age: " + p.getAge());
        System.out.println("Idnum: " + p.getIdnum());
    }
}

