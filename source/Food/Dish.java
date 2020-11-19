package source.Food;

public class Dish extends Food {
    String specification;

    public Dish(String name, String type, Float discount, Integer price) {
        super(name, type, discount, price);
        this.specification = "";
    }

    public Dish modifySpecification(String specification) {
        this.specification = specification;
        return this;
    }

}
