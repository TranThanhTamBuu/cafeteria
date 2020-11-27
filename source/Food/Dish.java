package source.Food;

public class Dish extends Food {
    private String specific_type;

    public Dish(Integer id, String name, String specific_type, Float discount, Integer price) {
        super(id, name, specific_type, discount, price);
        this.specific_type = "";
    }

    public Dish modifySpecification(String specific_type) {
        this.specific_type = specific_type;
        return this;
    }

    public String toString() {
        return String.format("%s | %s\n", this.id, this.name);
    }

}
