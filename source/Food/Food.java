package source.Food;

public abstract class Food {
	protected Integer id;
	protected String name, category;
	protected Float discount;
	protected Integer price;

	public Food(Integer id, String name, String category, Float discount, Integer price) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.discount = discount;
		this.price = price;
	}

	public Food modifyName(String name) {
		this.name = name;
		return this;
	}

	public Food modifyCategory(String category) {
		this.category = category;
		return this;
	}

	public Food modifyDiscount(Float discount) {
		this.discount = discount;
		return this;
	}

	public Food modifyPrice(Integer price) {
		this.price = price;
		return this;
	}

	public String getName() {
		return name;
	}

	public Float getDiscount() {
		return discount;
	}

	public Integer getPrice() {
		return price;
	}

	public String getCategory() {
		return category;
	}

	public Long cost() {
		return (long) ((long) price - (price * discount));
	}

	public int GetID() {
		return this.id;
	}
}
