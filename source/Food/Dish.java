package source.Food;

public class Dish extends Food {
    public Dish(Integer id, String name, String specific_type, Float discount, Integer price) {
        super(id, name, specific_type, discount, price);
        this.specific_type = "";
    }

    public String toString() {
        return String.format("%s | %s\n", this.id, this.name);
    }
}
