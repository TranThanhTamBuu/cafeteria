package source.Food;

import java.util.ArrayList;

public class Combo extends Food {
	ArrayList<Food> array_dish;
	ArrayList<Integer> amount;

	public Combo(String name, String type, Float discount, Integer price) {
		super(name, type, discount, price);
		this.array_dish = new ArrayList<>();
		this.amount = new ArrayList<>();
	}

	public Food addFood(Food food, int amount) {
		this.array_dish.add(food);
		this.amount.add(amount);
		return this;
	}

	public Food removeDish(int index) {
		this.array_dish.remove(index);
		this.amount.remove(index);
		return this;
	}

	public Food modifyAmount(int index, int amount) {
		this.amount.set(index, amount);
		return this;
	}
}
