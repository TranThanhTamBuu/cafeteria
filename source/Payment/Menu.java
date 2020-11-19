package source.Payment;

import java.util.ArrayList;

import source.Food.*;

public class Menu {
	private static final Menu instance = new Menu();

	private ArrayList<Food> menu;

	private Menu() {
		this.menu = new ArrayList<>();
	}

	public static Menu getInstance() {
		return instance;
	}

	public Menu createDish(String name, String type, Float discount, Integer price) {
		this.menu.add(new Dish(name, type, discount, price));
		return this;
	}

	public Menu createCombo(String name, String type, Float discount, Integer price) {
		this.menu.add(new Combo(name, type, discount, price));
		return this;
	}

	public Menu modifyDiscount(String type, Float discount) {
		for (Food f : menu) {
			if (f.getType() == type)
				f.modifyDiscount(discount);
		}

		return this;
	}

	public Food getFood(int index) {
		return getFood(index);
	}

}
