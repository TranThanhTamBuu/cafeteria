package source.Management;

public class Unit {
    protected Integer id;
    protected String name;

    public Unit(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Unit modifyName(String name) {
        this.name = name;
        return this;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
