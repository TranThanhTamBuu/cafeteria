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

	public Menu createFood(String name, String type, Float discount, Integer price) {
		this.menu.add(new Food(name, type, discount, price));
		return this;
	}

	public Menu createCombo() {
		// TODO: implement this function after design database
		return this;
	}

	public Menu modifyFood(int index) {
		// TODO: implement this function after design database
		return this;
	}

	public Menu modifyCombo(int index) {
		// TODO: implement this function after design database
		return this;
	}

	public Menu modifyPrice(int index, Integer price) {
		menu.get(index).modifyPrice(price);
		return this;
	}

	public Menu modifyDiscount(int index, Float discount) {
		menu.get(index).modifyDiscount(discount);
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
		return menu.get(index);
	}
}
