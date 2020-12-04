package source.Food;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Combo extends Food {
	ArrayList<Integer> array_dishID;
	ArrayList<Integer> quantity;

	public Combo(Integer id, String name, String type, Float discount, Integer price) {
		super(id, name, type, discount, price);
		this.array_dishID = new ArrayList<>();
		this.quantity = new ArrayList<>();
	}

	public Combo addDish(int dishID, int amount) {
		this.array_dishID.add(dishID);
		this.quantity.add(amount);
		return this;
	}

	public Combo removeDish(int index) {
		this.array_dishID.remove(index);
		this.quantity.get(index);
		return this;
	}

	public Combo modifyAmount(int index, int amount) {
		this.quantity.set(index, amount);
		return this;
	}

	public String toString() {
		return "Combo ID: " + this.id + "\n" + Arrays.toString(this.array_dishID.toArray());
	}

	public ArrayList<Integer> getArrayDishID() {
		return this.array_dishID;
	}

	public ArrayList<Integer> getArrayQuantity() {
		return this.quantity;
	}
}
