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

	public Menu createDish(Integer id, String name, String type, Float discount, Integer price) {
		this.menu.add(new Dish(id, name, type, discount, price));
		return this;
	}

	public Menu createCombo(Integer id, String name, String type, Float discount, Integer price) {
		this.menu.add(new Combo(id, name, type, discount, price));
		return this;
	}

	public Menu modifyDiscount(String category, Float discount) {
		for (Food f : menu) {
			if (f.getCategory() == category)
				f.modifyDiscount(discount);
		}

		return this;
	}

	public Food getFood(int index) {
		return getFood(index);
	}
}
