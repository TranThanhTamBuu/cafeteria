//package source.Payment;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Comparator;
//
//import source.Food.*;
//
//public class Menu {
//	private static final Menu instance = new Menu();
//
//	private ArrayList<Food> menu;
//
//	private Menu() {
//		this.menu = new ArrayList<>();
//	}
//
//	public static Menu getInstance() {
//		return instance;
//	}
//
////	public Menu createDish(Integer id, String name, String type, Float discount, Integer price) {
////		this.menu.add(new Dish(id, name, type, discount, price));
////		return this;
////	}
//
//	public Menu createCombo(Integer id, String name, String type, Float discount, Integer price) {
//		this.menu.add(new Combo(id, name, type, discount, price));
//		return this;
//	}
//
//	public Menu modifyDiscount(String category, Float discount) {
//		for (Food f : menu) {
//			if (f.getCategory() == category)
//				f.modifyDiscount(discount);
//		}
//
//		return this;
//	}
//
//	public Food getFood(int index) {
//		return getFood(index);
//	}
//
//	public <T> T FindGenFood(int ID) {
//		int foundIdx = Collections.binarySearch(this.menu, new Dish(ID, "", "", 0f, 0), new Comparator<Food>() {
//
//			@Override
//			public int compare(Food o1, Food o2) {
//				// TODO Auto-generated method stub
//				if (o1.GetID() < o2.GetID()) {
//					return -1;
//				} else if (o1.GetID() > o2.GetID()) {
//					return 1;
//				}
//				return 0;
//			}
//
//		});		
//
//		if (foundIdx < 0) {
//			return null;
//		}
//		return (T) this.menu.get(foundIdx);
//	}
//
//	public <T extends Food> void AddAllGenFood(ArrayList<T> foods) {
//		this.menu.addAll(foods);
//	}
//
//	public void PrintMenu() {
//		System.out.println(Arrays.toString(this.menu.toArray()));
//	}
//
//	public void Sort() {
//		Collections.sort(this.menu);
//	}
//        
//        public ArrayList<Food> getMenu() {
//            return this.menu;
//        }
//}
