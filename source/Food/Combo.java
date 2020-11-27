package source.Food;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Combo extends Food {
	ArrayList<Dish> array_dish;
	ArrayList<Integer> quantity;

	public Combo(Integer id, String name, String type, Float discount, Integer price) {
		super(id, name, type, discount, price);
		this.array_dish = new ArrayList<>();
		this.quantity = new ArrayList<>();
	}

	public Combo addDish(Dish dish, int amount) {
		this.array_dish.add(dish);
		this.quantity.add(amount);
		return this;
	}

	public Combo removeDish(int index) {
		this.array_dish.remove(index);
		this.quantity.get(index);
		return this;
	}

	public Combo modifyAmount(int index, int amount) {
		this.quantity.set(index, amount);
		return this;
	}

	public String toString() {
		return "Combo ID: " + this.id + "\n" + Arrays.toString(this.array_dish.toArray());
	}
}
